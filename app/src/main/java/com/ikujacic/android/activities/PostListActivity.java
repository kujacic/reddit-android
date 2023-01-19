package com.ikujacic.android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.ikujacic.android.R;
import com.ikujacic.android.adapter.ClickListener;
import com.ikujacic.android.adapter.PostAdapter;
import com.ikujacic.android.api.PostApi;
import com.ikujacic.android.api.RetrofitService;
import com.ikujacic.android.model.Post;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostListActivity extends AppCompatActivity implements ClickListener {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);

        recyclerView = findViewById(R.id.postList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String communityName = getIntent().getStringExtra("communityName");
        
        loadPosts(communityName);
    }

    private void loadPosts(String community) {
        RetrofitService retrofitService = new RetrofitService();
        PostApi postApi = retrofitService.getRetrofit().create(PostApi.class);
        postApi.getByCommunity(community).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                populateListView(response.body());
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(PostListActivity.this, "Failed to load posts!", Toast.LENGTH_SHORT).show();
                Logger.getLogger(CommunityListActivity.class.getName()).log(Level.SEVERE, "ERROR LOADING POSTS", t);
            }
        });
    }

    private void populateListView(List<Post> postList) {
        PostAdapter postAdapter = new PostAdapter(postList, this);
        recyclerView.setAdapter(postAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onItemClicked(Object object) {
        Post post = (Post) object;
        Toast.makeText(PostListActivity.this, post.getTitle(), Toast.LENGTH_SHORT).show();
    }
}