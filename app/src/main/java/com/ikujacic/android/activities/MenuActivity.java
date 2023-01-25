package com.ikujacic.android.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.ikujacic.android.R;

public class MenuActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        user = getIntent().getStringExtra("user");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_log_out) {
            Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        if (id == R.id.nav_communities) {
            Intent intent = new Intent(MenuActivity.this, CommunityListActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }
        if (id == R.id.nav_posts) {
            Intent intent = new Intent(MenuActivity.this, PostListActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }
        if (id == R.id.nav_settings) {
            Intent intent = new Intent(MenuActivity.this, AccountActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }
        return false;
    }
}