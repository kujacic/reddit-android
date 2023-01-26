package com.ikujacic.android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import com.ikujacic.android.R;
import com.ikujacic.android.adapter.ClickListener;
import com.ikujacic.android.adapter.PostAdapter;
import com.ikujacic.android.api.PostApi;
import com.ikujacic.android.api.RetrofitService;
import com.ikujacic.android.model.Post;
import com.squareup.seismic.ShakeDetector;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostListActivity extends AppCompatActivity implements ClickListener, PopupMenu.OnMenuItemClickListener, ShakeDetector.Listener {

    private RecyclerView recyclerView;
    static String communityName, user;
    private boolean isMainPage;
    private boolean shakeSort = false;
    PostApi postApi = new RetrofitService().getRetrofit().create(PostApi.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_list);

        // Shake event
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        ShakeDetector sd = new ShakeDetector(this);
        sd.start(sensorManager, SensorManager.SENSOR_DELAY_GAME);

        recyclerView = findViewById(R.id.postList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(getIntent().getStringExtra("user") != null) {
            user = getIntent().getStringExtra("user");
        }

        TextView label = findViewById(R.id.toolbar_label);
        if(getIntent().getStringExtra("communityName") != null) {
            communityName = getIntent().getStringExtra("communityName");

            label.setText("/" + communityName);
            isMainPage = false;

            loadPosts(communityName);

            findViewById(R.id.postList_fab).setOnClickListener(view -> {
                Intent intent = new Intent(this, PostForm.class);
                intent.putExtra("communityName", communityName);
                intent.putExtra("user", user);
                startActivity(intent);
            });
        } else {
            label.setText("All Posts");
            isMainPage = true;
            loadAllPosts();

            findViewById(R.id.postList_fab).setVisibility(View.GONE);
//            findViewById(R.id.postList_fab).setOnClickListener(view -> {
//                Intent intent = new Intent(this, PostForm.class);
//                intent.putExtra("communityName", communityName);
//                intent.putExtra("user", user);
//                startActivity(intent);
//            });
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
                intent.putExtra("postId", post.getId().toString());
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

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.sort_menu);
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.newest: {
                loadPostsSorted("newest");
                return true;
            }
            case R.id.oldest: {
                loadPostsSorted("oldest");
                return true;
            }
            case R.id.top_rated: {
                loadPostsSorted("topRated");
                return true;
            }
            case R.id.lowest_rated:_rated: {
                loadPostsSorted("lowestRated");
                return true;
            }
            case R.id.hot: {
                loadPostsSorted("hot");
                return true;
            }
            default:
                return false;
        }
    }

    public void loadPostsSorted(String sortBy) {
        Call<List<Post>> postsCall;
        if (isMainPage) {
            postsCall = postApi.getAllSorted(sortBy);
        } else {
            postsCall = postApi.getByCommunitySorted(communityName, sortBy);
        }
        postsCall.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                populateListView(response.body(), true);
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed()
    {
        if (isMainPage) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void hearShake() {
        Toast.makeText(this, "Don't shake me, bro!", Toast.LENGTH_SHORT).show();
        if (!shakeSort) {
            shakeSort = true;
            loadPostsSorted("topRated");
        } else {
            shakeSort = false;
            loadPostsSorted("lowestRated");
        }
    }
}