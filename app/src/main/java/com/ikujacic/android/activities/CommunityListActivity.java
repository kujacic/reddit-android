package com.ikujacic.android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.ikujacic.android.R;
import com.ikujacic.android.adapter.ClickListener;
import com.ikujacic.android.adapter.CommunityAdapter;
import com.ikujacic.android.api.CommunityApi;
import com.ikujacic.android.api.RetrofitService;
import com.ikujacic.android.model.Community;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunityListActivity extends AppCompatActivity implements ClickListener {

    private RecyclerView recyclerView;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_list);

        recyclerView = findViewById(R.id.communityList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        user = getIntent().getStringExtra("user");

        loadCommunities();
    }

    private void loadCommunities() {
        RetrofitService retrofitService = new RetrofitService();
        CommunityApi communityApi = retrofitService.getRetrofit().create(CommunityApi.class);
        communityApi.getAll().enqueue(new Callback<List<Community>>() {
            @Override
            public void onResponse(Call<List<Community>> call, Response<List<Community>> response) {
                populateListView(response.body());
            }

            @Override
            public void onFailure(Call<List<Community>> call, Throwable t) {
                Toast.makeText(CommunityListActivity.this, "Failed to load communities!", Toast.LENGTH_SHORT).show();
                Logger.getLogger(CommunityListActivity.class.getName()).log(Level.SEVERE, "ERROR LOADING COMMUNITIES", t);
            }
        });
    }

    private void populateListView(List<Community> communityList) {
        CommunityAdapter communityAdapter = new CommunityAdapter(communityList, this);
        recyclerView.setAdapter(communityAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onItemClicked(Object object) {
        Community community = (Community) object;
        Intent intent = new Intent(CommunityListActivity.this, PostListActivity.class);
        intent.putExtra("communityName", community.getName());
        intent.putExtra("user", user);
        startActivity(intent);
    }

    @Override
    public void onBackPressed()
    {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}