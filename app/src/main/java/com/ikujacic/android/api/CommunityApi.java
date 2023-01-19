package com.ikujacic.android.api;

import com.ikujacic.android.model.Community;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CommunityApi {

    @GET("/community")
    Call<List<Community>> getAll();
}