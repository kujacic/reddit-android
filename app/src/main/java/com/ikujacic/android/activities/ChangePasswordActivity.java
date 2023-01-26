package com.ikujacic.android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.ikujacic.android.R;
import com.ikujacic.android.api.RetrofitService;
import com.ikujacic.android.api.UserApi;
import com.ikujacic.android.model.ChangePasswordDTO;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText oldPasswordText, newPasswordText, confirmPasswordText;
    private String user;
    private UserApi userApi = new RetrofitService().getRetrofit().create(UserApi.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        oldPasswordText = findViewById(R.id.old_password_edit);
        newPasswordText = findViewById(R.id.new_password_edit);
        confirmPasswordText = findViewById(R.id.confirm_password_edit);
        user = getIntent().getStringExtra("user");

        findViewById(R.id.change_password_api_call).setOnClickListener(view -> {
            changePassword();
        });
    }

    private void changePassword() {
        String oldPassword = String.valueOf(oldPasswordText.getText()).trim();
        String newPassword = String.valueOf(newPasswordText.getText()).trim();
        String confirmPassowrd = String.valueOf(confirmPasswordText.getText()).trim();
        if(oldPassword.isEmpty()) {
            oldPasswordText.setError("Old Password is required!");
            oldPasswordText.requestFocus();
            return;
        }
        if(newPassword.isEmpty()) {
            newPasswordText.setError("New Password is required!");
            newPasswordText.requestFocus();
            return;
        }
        if(confirmPassowrd.isEmpty()) {
            confirmPasswordText.setError("Confirm Password is required!");
            confirmPasswordText.requestFocus();
            return;
        }
        if (!newPassword.equals(confirmPassowrd)) {
            confirmPasswordText.setError("Confirm Password doesnt match New Password!");
            confirmPasswordText.requestFocus();
            return;
        }

        userApi.updatePassword(new ChangePasswordDTO(oldPassword, newPassword), user).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    Toast.makeText(ChangePasswordActivity.this, "Password updated!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ChangePasswordActivity.this, AccountActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Old Password is incorrect!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ChangePasswordActivity.this, "Password update failed!", Toast.LENGTH_SHORT).show();
                Logger.getLogger(LoginActivity.class.getName()).log(Level.SEVERE, "Password update failed!", t);
            }
        });
    }
}