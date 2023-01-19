package com.ikujacic.android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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

public class SignUpActivity extends AppCompatActivity {

    private EditText usernameText, passwordText, emailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailText = findViewById(R.id.email_edit_text);
        usernameText = findViewById(R.id.username_edit_text);
        passwordText = findViewById(R.id.password_edit_text);

        findViewById(R.id.sign_up_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser();
            }
        });
    }

    private void signUpUser() {
        String email = String.valueOf(emailText.getText()).trim();
        String username = String.valueOf(usernameText.getText()).trim();
        String password = String.valueOf(passwordText.getText()).trim();
        if(email.isEmpty()) {
            emailText.setError("Email is required!");
            emailText.requestFocus();
            return;
        }
        boolean validEmail = Patterns.EMAIL_ADDRESS.matcher(email).matches();
        if (!validEmail) {
            emailText.setError("Incorrect Email format!");
            emailText.requestFocus();
            return;
        }
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

        userApi.create(new User(username, password, email)).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.code() == 200) {
                    Toast.makeText(SignUpActivity.this, "User signed up! Please log in", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                } else if (response.code() == 400) {
                    Toast.makeText(SignUpActivity.this, "User with email/username exists! Try again!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(SignUpActivity.this, "SignUp failed!", Toast.LENGTH_SHORT).show();
                Logger.getLogger(LoginActivity.class.getName()).log(Level.SEVERE, "ERRORRRRR", t);
            }
        });
    }
}