package com.ikujacic.android.api;

import com.ikujacic.android.model.Comment;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CommentApi {

    @GET("/comment/post")
    Call<List<Comment>> getByPost(@Query("postId") String postId);
}