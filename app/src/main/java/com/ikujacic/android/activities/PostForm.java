package com.ikujacic.android.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ikujacic.android.R;
import com.ikujacic.android.api.PostApi;
import com.ikujacic.android.api.RetrofitService;
import com.ikujacic.android.model.Post;

import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostForm extends AppCompatActivity {

    private EditText titleText, textText;
    private String user, communityName;
    private PostApi postApi = new RetrofitService().getRetrofit().create(PostApi.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_form);

        titleText = findViewById(R.id.title_edit);
        textText = findViewById(R.id.text_edit);
        user = getIntent().getStringExtra("user");
        communityName = getIntent().getStringExtra("communityName");

        if (getIntent().getStringExtra("title") != null && getIntent().getStringExtra("text") != null) {
            EditText loadedTitle = (EditText) findViewById(R.id.title_edit);
            loadedTitle.setText(getIntent().getStringExtra("title"), TextView.BufferType.NORMAL);
            EditText loadedText = (EditText) findViewById(R.id.text_edit);
            loadedText.setText(getIntent().getStringExtra("text"), TextView.BufferType.NORMAL);
            Button button = findViewById(R.id.create_post);
            if (getIntent().getStringExtra("editPostId") != null) {
                // EDIT
                button.setText("Edit");
                setTitle("Edit Post");
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editPost(Integer.valueOf(getIntent().getStringExtra("editPostId")));
                    }
                });

                // DELETE
                Button deleteButton = findViewById(R.id.delete_post);
                deleteButton.setVisibility(View.VISIBLE);
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deletePost(Integer.valueOf(getIntent().getStringExtra("editPostId")));
                    }
                });

            } else {
                // VIEW
                setTitle("");
                loadedTitle.setKeyListener(null);
                loadedText.setKeyListener(null);
                button.setVisibility(View.GONE);
            }
        } else {
            // CREATE
            findViewById(R.id.create_post).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createPost();
                }
            });
        }
    }

    private void createPost() {
        String title = String.valueOf(titleText.getText()).trim();
        String text = String.valueOf(textText.getText()).trim();

        if(title.isEmpty()) {
            titleText.setError("Title cannot be empty!");
            titleText.requestFocus();
            return;
        }
        if(text.isEmpty()) {
            textText.setError("Text cannot be empty!");
            textText.requestFocus();
            return;
        }

        postApi.create(new Post(title, text, user, communityName)).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.code() == 200) {
                    Toast.makeText(PostForm.this, "Post created!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PostForm.this, PostListActivity.class);
                    intent.putExtra("user", user);
                    intent.putExtra("communityName", communityName);
                    startActivity(intent);
                } else {
                    Toast.makeText(PostForm.this, "Incorrect Credentials! Try again!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(PostForm.this, "Failed to create post!", Toast.LENGTH_SHORT).show();
                Logger.getLogger(LoginActivity.class.getName()).log(Level.SEVERE, "FAILED TO CREATE POST", t);
            }
        });
    }

    private void editPost(Integer id) {
        String title = String.valueOf(titleText.getText()).trim();
        String text = String.valueOf(textText.getText()).trim();

        if(title.isEmpty()) {
            titleText.setError("Title cannot be empty!");
            titleText.requestFocus();
            return;
        }
        if(text.isEmpty()) {
            textText.setError("Text cannot be empty!");
            textText.requestFocus();
            return;
        }

        postApi.update(new Post(id, title, text, user, communityName, null)).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (response.code() == 200) {
                    Toast.makeText(PostForm.this, "Post updated!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PostForm.this, PostListActivity.class);
                    intent.putExtra("user", user);
                    intent.putExtra("communityName", communityName);
                    startActivity(intent);
                } else {
                    Toast.makeText(PostForm.this, "Failed to update post!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                Toast.makeText(PostForm.this, "Failed to update post!", Toast.LENGTH_SHORT).show();
                Logger.getLogger(LoginActivity.class.getName()).log(Level.SEVERE, "FAILED TO UPDATE POST", t);
            }
        });
    }

    private void deletePost(Integer editPostId) {
        postApi.delete(editPostId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.code() == 200) {
                    Toast.makeText(PostForm.this, "Post deleted!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PostForm.this, PostListActivity.class);
                    intent.putExtra("user", user);
                    intent.putExtra("communityName", communityName);
                    startActivity(intent);
                } else {
                    Toast.makeText(PostForm.this, "Failed to delete post!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(PostForm.this, "Failed to delete post!", Toast.LENGTH_SHORT).show();
                Logger.getLogger(LoginActivity.class.getName()).log(Level.SEVERE, "FAILED TO DELETE POST", t);
            }
        });
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        Intent intent = new Intent(PostForm.this, PostListActivity.class);
//        intent.putExtra("user", user);
//        intent.putExtra("communityName", communityName);
//        startActivity(intent);
//    }
}