package com.ikujacic.android.api;

import com.ikujacic.android.model.Community;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface CommunityApi {

    @GET("/community")
    Call<List<Community>> getAll();

    @POST("/community")
    Call<Void> create(@Body Community community, @Query("user") String user);
}