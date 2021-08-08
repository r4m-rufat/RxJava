package com.codingwithrufat.rxjava.network;

import com.codingwithrufat.rxjava.model.Comments;
import com.codingwithrufat.rxjava.model.Posts;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IApi {

    @GET("posts")
    Observable<List<Posts>> getPosts();

    @GET("posts/{id}/comments")
    Observable<List<Comments>> getComments(
            @Path("id") int id
    );

}
