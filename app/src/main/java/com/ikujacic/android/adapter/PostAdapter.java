package com.ikujacic.android.adapter;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ikujacic.android.R;
import com.ikujacic.android.api.ReactionApi;
import com.ikujacic.android.api.RetrofitService;
import com.ikujacic.android.model.Post;
import com.ikujacic.android.model.Reaction;
import com.ikujacic.android.model.ReactionCount;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostAdapter extends RecyclerView.Adapter<PostHolder> {

    private List<Post> postList;
    private ClickListener clickListener;
    private String user;
    ColorStateList orange, gray, downvote;
    private ReactionApi reactionApi = new RetrofitService().getRetrofit().create(ReactionApi.class);

    public PostAdapter(List<Post> postList, ClickListener clickListener, String user) {
        this.postList = postList;
        this.clickListener = clickListener;
        this.user = user;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_list_item, parent, false);
        orange = ColorStateList.valueOf(parent.getResources().getColor(R.color.orange_red));
        gray = ColorStateList.valueOf(parent.getResources().getColor(R.color.dark_gray));
        downvote = ColorStateList.valueOf(parent.getResources().getColor(R.color.downvote));
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        Post post = postList.get(position);
        holder.title.setText(post.getTitle());
        holder.text.setText(post.getText());
        holder.author.setText("by: " + post.getAuthor());
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClicked(postList.get(holder.getAdapterPosition()));
            }
        });
        holder.upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upvote(holder, post);
            }
        });
        holder.downvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downvote(holder, post);
            }
        });
        calculateReactionDelta(post.getId(), holder);
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public void calculateReactionDelta(Integer id, PostHolder holder) {
        reactionApi.get(id, user).enqueue(new Callback<ReactionCount>() {
            @Override
            public void onResponse(Call<ReactionCount> call, Response<ReactionCount> response) {
                ReactionCount result = (ReactionCount) response.body();
                if (result.getCount() != null) {
                    holder.reactions.setText(String.valueOf(result.getCount()));
                } else {
                    holder.reactions.setText("0");
                }
                if (result.getType() != null) {
                    if (result.getType().equals("UPVOTE")) {
                        holder.upvote.setIconTint(orange);
                    }
                    else if (result.getType().equals("DOWNVOTE")) {
                        holder.downvote.setIconTint(downvote);
                    }
                }
            }

            @Override
            public void onFailure(Call<ReactionCount> call, Throwable t) {
                Logger.getLogger(PostAdapter.class.getName()).log(Level.SEVERE, "FAILED TO GET REACTIONS", t);
            }
        });
    }

    private void upvote(PostHolder holder, Post post) {
        if (!holder.upvote.getIconTint().equals(orange)) {
            holder.upvote.setIconTint(orange);
            holder.downvote.setIconTint(gray);
            Reaction reaction = new Reaction("UPVOTE", post.getId(), null, user);
            reactionApi.create(reaction).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    calculateReactionDelta(post.getId(), holder);
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
        }
    }

    private void downvote(PostHolder holder, Post post) {
        Reaction reaction = null;
        if (!holder.downvote.getIconTint().equals(downvote)) {
            reaction = new Reaction("DOWNVOTE", post.getId(), null, user);
        } else {
            reaction = new Reaction("DOWNVOTE", post.getId(), null, user);
        }
        holder.downvote.setIconTint(downvote);
        holder.upvote.setIconTint(gray);

        reactionApi.create(reaction).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                calculateReactionDelta(post.getId(), holder);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
}