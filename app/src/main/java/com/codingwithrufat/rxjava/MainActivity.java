package com.codingwithrufat.rxjava;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.codingwithrufat.rxjava.model.Task;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    // variables
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Task task = new Task("Learn android", true, 3);
        // final List<Task> tasks = DataSource.getTasksList();

        /**
         * this one is create method
         */
        /*
        .create(new ObservableOnSubscribe<Task>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Task> emitter) throws Throwable {
                for (Task task: tasks){
                    if (!emitter.isDisposed()){
                        emitter.onNext(task);
                    }
                }

                if (!emitter.isDisposed()){
                    emitter.onComplete();
                }

            }
        })
         */

        /**
         * just operator is limited operator(max -> 10 objects)
         */
        /*
        .just(task)
         */

        // range operator

        /*
        Observable<Task> taskObservable = Observable
                .range(1, 10)
                .subscribeOn(Schedulers.io())
                .map(new Function<Integer, Task>() {
                    @Override
                    public Task apply(Integer integer) throws Throwable {
                        return new Task("this is task " + integer, false, integer);
                    }
                })
                .takeWhile(new Predicate<Task>() {
                    @Override
                    public boolean test(Task task) throws Throwable {
                        return task.getPriority() < 10;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
         */

        Observable<Integer> taskObservable = Observable
                .range(1, 10)
                .subscribeOn(Schedulers.io())
                .repeat(2)
                .observeOn(AndroidSchedulers.mainThread());

        taskObservable.subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                Log.d(TAG, "onNext: " + integer);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        compositeDisposable.dispose();

    }
}