package com.ikujacic.android.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.ikujacic.android.R;
import com.ikujacic.android.api.ReactionApi;
import com.ikujacic.android.api.RetrofitService;
import com.ikujacic.android.api.UserApi;
import com.ikujacic.android.model.User;

import org.w3c.dom.Text;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private String user;
    private TextView usernameText, emailText;
    MenuItem karma;
    private UserApi userApi = new RetrofitService().getRetrofit().create(UserApi.class);
    private ReactionApi reactionApi = new RetrofitService().getRetrofit().create(ReactionApi.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        user = getIntent().getStringExtra("user");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }

        View headerLayout = navigationView.getHeaderView(0);
        usernameText = headerLayout.findViewById(R.id.menu_username);
        emailText = headerLayout.findViewById(R.id.menu_email);
        loadUserData(user);
        loadUserKarma();
        Menu menu = navigationView.getMenu();
        karma = menu.findItem(R.id.nav_karma);
    }

    private void loadUserKarma() {
        reactionApi.getUserKarma(user).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.code() == 200) {
                    karma.setTitle("Karma: " + response.body());
                } else {
                    karma.setTitle("Karma: 0");
                }
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
            }
        });
    }

    private void loadUserData(String user) {
        userApi.getByUsername(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                usernameText.setText(user.getUsername());
                emailText.setText(user.getEmail());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(MenuActivity.this, "Fetching username and email failed!", Toast.LENGTH_SHORT).show();
                Logger.getLogger(LoginActivity.class.getName()).log(Level.SEVERE, "Fetching username and email failed!", t);
            }
        });
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