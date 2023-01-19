package com.ikujacic.android.api;

import com.ikujacic.android.model.User;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserApi {

    @POST("/user/login")
    Call<String> login(@Body User user);

    @POST("/user")
    Call<User> create(@Body User user);
}