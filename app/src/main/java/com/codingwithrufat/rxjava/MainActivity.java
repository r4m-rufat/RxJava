package com.codingwithrufat.rxjava;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.codingwithrufat.rxjava.model.Task;
import com.codingwithrufat.rxjava.repository.DataSource;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    // ui
    private TextView text;

    // variables
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Observable<Task> taskObservable = Observable
                .fromIterable(DataSource.getTasksList())
                .subscribeOn(Schedulers.io())
                .filter(new Predicate<Task>() { // background proses
                    @Override
                    public boolean test(Task task) throws Throwable {
                        Log.d(TAG, "test: " + Thread.currentThread().getName());
                        return task.isComplete();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());

        taskObservable.subscribe(new Observer<Task>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.d(TAG, "onSubscribe: called");

                compositeDisposable.add(d);

            }

            @Override
            public void onNext(@NonNull Task task) {
                Log.d(TAG, "onNext: " + Thread.currentThread().getName());
                Log.d(TAG, "onNext: " + task.getDescription());

            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.d(TAG, "onError: " + e.getMessage());

            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: called");

            }
        });

        compositeDisposable.add(taskObservable.subscribe(new Consumer<Task>() {
            @Override
            public void accept(Task task) throws Throwable {

            }
        }));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        compositeDisposable.dispose();
    }
}