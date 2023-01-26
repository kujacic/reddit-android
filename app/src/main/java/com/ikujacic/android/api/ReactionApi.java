package com.ikujacic.android.api;

import com.ikujacic.android.model.Reaction;
import com.ikujacic.android.model.ReactionCount;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReactionApi {

    @GET("/reaction/post/{id}")
    Call<ReactionCount> getforPost(@Path("id") Integer id, @Query("user") String user);

    @GET("/reaction/comment/{id}")
    Call<ReactionCount> getForComment(@Path("id") Integer id, @Query("user") String user);

    @GET("/reaction/user")
    Call<Integer> getUserKarma(@Query("user") String user);

    @POST("/reaction")
    Call<Void> create(@Body Reaction reaction);
}