package com.ikujacic.android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
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
    static String communityName, user, communityLink;
    PostApi postApi = new RetrofitService().getRetrofit().create(PostApi.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);

        recyclerView = findViewById(R.id.postList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(getIntent().getStringExtra("user") != null) {
            user = getIntent().getStringExtra("user");
        }

        TextView label = findViewById(R.id.toolbar_label);
        if(getIntent().getStringExtra("communityName") != null) {
            communityName = getIntent().getStringExtra("communityName");

            label.setText("/" + communityName);

            loadPosts(communityName);

            findViewById(R.id.postList_fab).setOnClickListener(view -> {
                Intent intent = new Intent(this, PostForm.class);
                intent.putExtra("communityName", communityName);
                intent.putExtra("user", user);
                startActivity(intent);
            });
        } else {
            label.setText("All Posts");

            loadAllPosts();

            findViewById(R.id.postList_fab).setOnClickListener(view -> {
                Intent intent = new Intent(this, PostForm.class);
                intent.putExtra("communityName", communityName);
                intent.putExtra("user", user);
                startActivity(intent);
            });
        }

        findViewById(R.id.menu).setOnClickListener(view -> {
            Intent intent = new Intent(this, MenuActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        });
    }

    private void loadPosts(String community) {
        postApi.getByCommunity(community).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                populateListView(response.body(), false);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(PostListActivity.this, "Failed to load posts!", Toast.LENGTH_SHORT).show();
                Logger.getLogger(CommunityListActivity.class.getName()).log(Level.SEVERE, "ERROR LOADING POSTS", t);
            }
        });
    }

    private void loadAllPosts() {
        postApi.getAll().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                populateListView(response.body(), true);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(PostListActivity.this, "Failed to load posts!", Toast.LENGTH_SHORT).show();
                Logger.getLogger(CommunityListActivity.class.getName()).log(Level.SEVERE, "ERROR LOADING POSTS", t);
            }
        });
    }

    private void populateListView(List<Post> postList, boolean withLinkToCommunity) {
        PostAdapter postAdapter = new PostAdapter(postList, this, user, withLinkToCommunity, getBaseContext());
        recyclerView.setAdapter(postAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onItemClicked(Object object) {
        Post post = (Post) object;
        postApi.get(post.getId()).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                Post newPost = (Post) response.body();
                Intent intent = new Intent(PostListActivity.this, PostForm.class);
                intent.putExtra("communityName", communityName);
                intent.putExtra("user", user);
                intent.putExtra("title", newPost.getTitle());
                intent.putExtra("text", newPost.getText());
                if (user.equals(post.getAuthor())) {
                    intent.putExtra("editPostId", String.valueOf(post.getId()));
                }
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
    }

//    @Override
//    public void onBackPressed()
//    {
//        super.onBackPressed();
//        Intent intent = new Intent(PostListActivity.this, CommunityListActivity.class);
//        intent.putExtra("user", user);
//        startActivity(intent);
//    }
}