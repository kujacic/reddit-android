package com.ikujacic.android.api;

import com.ikujacic.android.model.Community;
import com.ikujacic.android.model.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PostApi {

    @GET("/post/community")
    Call<List<Post>> getByCommunity(@Query("communityName") String communityName);
}
