package com.ikujacic.android.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ikujacic.android.R;
import com.ikujacic.android.adapter.CommentAdapter;
import com.ikujacic.android.adapter.PostAdapter;
import com.ikujacic.android.api.CommentApi;
import com.ikujacic.android.api.CommunityApi;
import com.ikujacic.android.api.PostApi;
import com.ikujacic.android.api.RetrofitService;
import com.ikujacic.android.databinding.ActivityPostFormBinding;
import com.ikujacic.android.model.Comment;
import com.ikujacic.android.model.Flair;
import com.ikujacic.android.model.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostForm extends AppCompatActivity {

    ActivityPostFormBinding postFormBinding;
    private RecyclerView recyclerView;
    private EditText titleText, textText, newCommentText;
    private AutoCompleteTextView flairName;
    private String user, communityName, postId;
    private ArrayList<String> flairs;
    private PostApi postApi = new RetrofitService().getRetrofit().create(PostApi.class);
    private CommentApi commentApi = new RetrofitService().getRetrofit().create(CommentApi.class);
    private CommunityApi communityApi = new RetrofitService().getRetrofit().create(CommunityApi.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        postFormBinding = ActivityPostFormBinding.inflate(getLayoutInflater());
        setContentView(postFormBinding.getRoot());


        titleText = findViewById(R.id.title_edit);
        textText = findViewById(R.id.text_edit);
        flairName = findViewById(R.id.flairText);
        newCommentText = findViewById(R.id.comment_post_edit);
        user = getIntent().getStringExtra("user");
        postId = getIntent().getStringExtra("postId");
        communityName = getIntent().getStringExtra("communityName");

        recyclerView = findViewById(R.id.commentList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadFlairs(getBaseContext());

        if (getIntent().getStringExtra("title") != null && getIntent().getStringExtra("text") != null) {
            titleText.setText(getIntent().getStringExtra("title"), TextView.BufferType.NORMAL);
            textText.setText(getIntent().getStringExtra("text"), TextView.BufferType.NORMAL);
            flairName.setText(getIntent().getStringExtra("flair"));
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
                titleText.setKeyListener(null);
                textText.setKeyListener(null);
                button.setVisibility(View.GONE);
            }

            loadComments();

            findViewById(R.id.create_post_comment).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createComment();
                }
            });
        } else {
            findViewById(R.id.comments_label).setVisibility(View.GONE);
            findViewById(R.id.new_comment).setVisibility(View.GONE);
            findViewById(R.id.commentList).setVisibility(View.GONE);
            // CREATE
            findViewById(R.id.create_post).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createPost();
                }
            });
        }
    }

    private void loadFlairs(Context context) {
        if (communityName != null) {
            communityApi.getFlairsForCommunity(communityName).enqueue(new Callback<ArrayList<String>>() {
                @Override
                public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                    if (response.code() == 200) {
                        flairs = new ArrayList<>();
                        flairs.addAll(response.body());
                        postFormBinding.flairText.setAdapter(new ArrayAdapter(context, R.layout.flair_layout, flairs));
                    }
                }
                @Override
                public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                }
            });
        } else {
            postApi.getFlairsForPost(postId).enqueue(new Callback<ArrayList<String>>() {
                @Override
                public void onResponse(Call<ArrayList<String>> call, Response<ArrayList<String>> response) {
                    if (response.code() == 200) {
                        flairs = new ArrayList<>();
                        flairs.addAll(response.body());
                        postFormBinding.flairText.setAdapter(new ArrayAdapter(context, R.layout.flair_layout, flairs));
                    }
                }
                @Override
                public void onFailure(Call<ArrayList<String>> call, Throwable t) {
                }
            });
        }

    }

    private void createComment() {
        String newComment = String.valueOf(newCommentText.getText()).trim();
        if(newComment.isEmpty()) {
            newCommentText.setError("Comment text is required!");
            newCommentText.requestFocus();
            return;
        }

        commentApi.create(new Comment(null, newComment, null, false, user, Integer.valueOf(postId), null)).enqueue(new Callback<Comment>() {
            @Override
            public void onResponse(Call<Comment> call, Response<Comment> response) {
                if (response.code() == 200) {
                    Toast.makeText(PostForm.this, "Comment successful!", Toast.LENGTH_SHORT).show();
                    newCommentText.setText("");
                    newCommentText.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(newCommentText.getWindowToken(), 0);
                    loadComments();
                }
            }
            @Override
            public void onFailure(Call<Comment> call, Throwable t) {
                Toast.makeText(PostForm.this, "Comment failed!", Toast.LENGTH_SHORT).show();
                Logger.getLogger(LoginActivity.class.getName()).log(Level.SEVERE, "Comment failed!", t);
            }
        });
    }

    private void loadComments() {
        commentApi.getByPost(postId).enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                populateListView(response.body());
            }
            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Toast.makeText(PostForm.this, "Failed to load comments!", Toast.LENGTH_SHORT).show();
                Logger.getLogger(PostForm.class.getName()).log(Level.SEVERE, "ERROR LOADING POSTS", t);
            }
        });
    }

    private void populateListView(List<Comment> commentList) {
        CommentAdapter commentAdapter = new CommentAdapter(commentList, user);
        recyclerView.setAdapter(commentAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
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

        postApi.create(new Post(null, title, text, user, communityName, null, String.valueOf(postFormBinding.flairText.getText()))).enqueue(new Callback<Post>() {
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

        postApi.update(new Post(id, title, text, user, communityName, null, String.valueOf(postFormBinding.flairText.getText()))).enqueue(new Callback<Post>() {
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