package com.ikujacic.android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.EditText;
import android.widget.Toast;

import com.ikujacic.android.R;
import com.ikujacic.android.api.RetrofitService;
import com.ikujacic.android.api.UserApi;
import com.ikujacic.android.model.User;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountActivity extends AppCompatActivity {

    private EditText usernameText, emailText;
    private String user;
    private UserApi userApi = new RetrofitService().getRetrofit().create(UserApi.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        usernameText = findViewById(R.id.username_edit);
        emailText = findViewById(R.id.email_edit);

        user = getIntent().getStringExtra("user");

        loadUserData(user);

        findViewById(R.id.change_password).setOnClickListener(view -> {
            Intent intent = new Intent(AccountActivity.this, ChangePasswordActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        });

        findViewById(R.id.update_account).setOnClickListener(view -> {
            updateAccount();
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

            }
        });
    }

    private void updateAccount() {
        String username = String.valueOf(usernameText.getText()).trim();
        String email = String.valueOf(emailText.getText()).trim();
        if(username.isEmpty()) {
            usernameText.setError("Username is required!");
            usernameText.requestFocus();
            return;
        }
        if(email.isEmpty()) {
            emailText.setError("Email is required!");
            emailText.requestFocus();
            return;
        }
        userApi.update(new User(null, username,null, email), user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    Toast.makeText(AccountActivity.this, "User account updated!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(AccountActivity.this, MenuActivity.class);
                    intent.putExtra("user", username);
                    startActivity(intent);
                } else if (response.code() == 500){
                    Toast.makeText(AccountActivity.this, "Username already exists!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(AccountActivity.this, "Update user account failed!", Toast.LENGTH_SHORT).show();
                Logger.getLogger(LoginActivity.class.getName()).log(Level.SEVERE, "Update user account failed", t);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AccountActivity.this, MenuActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }
}