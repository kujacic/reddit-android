package com.ikujacic.android.api;

import com.ikujacic.android.model.ChangePasswordDTO;
import com.ikujacic.android.model.User;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserApi {

    @POST("/user/login")
    Call<String> login(@Body User user);

    @POST("/user")
    Call<User> create(@Body User user);

    @GET("/user/username")
    Call<User> getByUsername(@Query("username") String username);

    @POST("/user/changePassword")
    Call<Void> updatePassword(@Body ChangePasswordDTO changePasswordDTO, @Query("username") String username);

    @PUT("/user")
    Call<User> update(@Body User user, @Query("username") String username);
}