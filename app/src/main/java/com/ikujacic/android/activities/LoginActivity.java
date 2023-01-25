package com.ikujacic.android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class LoginActivity extends AppCompatActivity {

    private EditText usernameText, passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameText = findViewById(R.id.username_edit_text);
        passwordText = findViewById(R.id.password_edit_text);

        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });

        findViewById(R.id.sign_up_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
    }

    private void loginUser() {
        String username = String.valueOf(usernameText.getText()).trim();
        String password = String.valueOf(passwordText.getText()).trim();
        if(username.isEmpty()) {
            usernameText.setError("Username is required!");
            usernameText.requestFocus();
            return;
        }
        if(password.isEmpty()) {
            passwordText.setError("Password is required!");
            passwordText.requestFocus();
            return;
        }

        RetrofitService retrofitService = new RetrofitService();
        UserApi userApi = retrofitService.getRetrofit().create(UserApi.class);

        userApi.login(new User(username, password)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.code() == 200) {
                    Toast.makeText(LoginActivity.this, "User logged in!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, PostListActivity.class);
                    intent.putExtra("user", username);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Incorrect Credentials! Try again!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
                Logger.getLogger(LoginActivity.class.getName()).log(Level.SEVERE, "ERRORRRRR", t);
            }
        });
    }
}