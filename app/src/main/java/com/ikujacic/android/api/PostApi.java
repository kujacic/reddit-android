package com.ikujacic.android.api;

import com.ikujacic.android.model.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PostApi {

    @GET("/post/{id}")
    Call<Post> get(@Path("id") Integer id);

    @GET("/post/community")
    Call<List<Post>> getByCommunity(@Query("communityName") String communityName);

    @GET("/post")
    Call<List<Post>> getAll();

    @POST("/post")
    Call<Post> create(@Body Post post);

    @PUT("/post")
    Call<Post> update(@Body Post post);

    @DELETE("/post/{id}")
    Call<Void> delete(@Path("id") Integer id);
}