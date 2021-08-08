package com.codingwithrufat.rxjava;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codingwithrufat.rxjava.model.Comments;
import com.codingwithrufat.rxjava.model.Posts;
import com.codingwithrufat.rxjava.network.ApiClient;
import com.codingwithrufat.rxjava.network.IApi;

import java.util.List;
import java.util.Random;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    // ui
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    // variables
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private IApi iApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iApi = ApiClient.getRetrofit().create(IApi.class);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewAdapter = new RecyclerViewAdapter();
        recyclerView.setAdapter(recyclerViewAdapter);

        getData();

    }

    private void getData() {

        getObservablePosts()
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<Posts, ObservableSource<Posts>>() {
                    @Override
                    public ObservableSource<Posts> apply(Posts posts) throws Throwable {
                        return getObservableComments(posts);
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Posts>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Posts posts) {
                        recyclerViewAdapter.updatePost(posts);
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

    private Observable<Posts> getObservablePosts() {

        return iApi.getPosts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<List<Posts>, ObservableSource<Posts>>() {
                    @Override
                    public ObservableSource<Posts> apply(List<Posts> posts) throws Throwable {
                        Log.d(TAG, "apply: " + posts.get(0).getTitle());
                        recyclerViewAdapter.setPosts(posts);
                        return Observable.fromIterable(posts)
                                .subscribeOn(Schedulers.io());
                    }
                });

    }

    private Observable<Posts> getObservableComments(Posts posts) {

        return iApi.getComments(posts.getId())
                .map(new Function<List<Comments>, Posts>() {
                    @Override
                    public Posts apply(List<Comments> comments) throws Throwable {

                        int delay = ((new Random()).nextInt(5) + 1) * 1000; // sleep thread for x ms
                        Thread.sleep(delay);

                        posts.setComments(comments);
                        return posts;
                    }
                })
                .observeOn(Schedulers.io());

    }

}