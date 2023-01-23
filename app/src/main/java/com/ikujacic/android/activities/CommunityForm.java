package com.ikujacic.android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ikujacic.android.R;
import com.ikujacic.android.api.CommunityApi;
import com.ikujacic.android.api.RetrofitService;
import com.ikujacic.android.model.Community;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommunityForm extends AppCompatActivity {

    private EditText titleText, descriptionText;
    private String user;
    private CommunityApi communityApi = new RetrofitService().getRetrofit().create(CommunityApi.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_form);

        titleText = findViewById(R.id.title_edit);
        descriptionText = findViewById(R.id.description_edit);
        user = getIntent().getStringExtra("user");

        findViewById(R.id.create_community).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createCommunity();
            }
        });
    }

    private void createCommunity() {
        String title = String.valueOf(titleText.getText()).trim();
        String description = String.valueOf(descriptionText.getText()).trim();

        if(title.isEmpty()) {
            titleText.setError("Title cannot be empty!");
            titleText.requestFocus();
            return;
        }
        if(description.isEmpty()) {
            descriptionText.setError("Text cannot be empty!");
            descriptionText.requestFocus();
            return;
        }

        communityApi.create(new Community(title, description), user).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    Toast.makeText(CommunityForm.this, "Community created!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CommunityForm.this, CommunityListActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                } else {
                    Toast.makeText(CommunityForm.this, "Falied to create community!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CommunityForm.this, "Failed to create community!", Toast.LENGTH_SHORT).show();
                Logger.getLogger(LoginActivity.class.getName()).log(Level.SEVERE, "FAILED TO CREATE COMMUNITY", t);
            }
        });
    }
}