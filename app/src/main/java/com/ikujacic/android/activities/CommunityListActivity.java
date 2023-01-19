package com.ikujacic.android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.ikujacic.android.R;
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

public class CommunityListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_list);

        recyclerView = findViewById(R.id.communityList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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
        CommunityAdapter communityAdapter = new CommunityAdapter(communityList);
        recyclerView.setAdapter(communityAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
    }
}