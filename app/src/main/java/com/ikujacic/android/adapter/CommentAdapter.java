package com.ikujacic.android.adapter;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ikujacic.android.R;
import com.ikujacic.android.api.CommentApi;
import com.ikujacic.android.api.ReactionApi;
import com.ikujacic.android.api.RetrofitService;
import com.ikujacic.android.model.Comment;
import com.ikujacic.android.model.Reaction;
import com.ikujacic.android.model.ReactionCount;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentAdapter extends RecyclerView.Adapter<CommentHolder> {

    private String user;
    List<Comment> commentList;
    ColorStateList orange, gray, downvote;
    CommentApi commentApi = new RetrofitService().getRetrofit().create(CommentApi.class);
    ReactionApi reactionApi = new RetrofitService().getRetrofit().create(ReactionApi.class);

    public CommentAdapter(List<Comment> commentList, String user) {
        this.commentList = commentList;
        this.user = user;
    }

    @NonNull
    @Override
    public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_list_item, parent, false);
        orange = ColorStateList.valueOf(parent.getResources().getColor(R.color.reddit_orange));
        gray = ColorStateList.valueOf(parent.getResources().getColor(R.color.dark_gray));
        downvote = ColorStateList.valueOf(parent.getResources().getColor(R.color.downvote));
        return new CommentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.text.setText(comment.getText());
        holder.author.setText("by: " + comment.getAuthor());
        holder.reply.setOnClickListener(view -> {
            toggleView(holder.reply_dialog);
        });
        holder.upvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upvote(holder, comment);
            }
        });
        holder.downvote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downvote(holder, comment);
            }
        });
        calculateReactionDelta(comment.getId(), holder);
    }

    private void upvote(CommentHolder holder, Comment comment) {
        if (!holder.upvote.getIconTint().equals(orange)) {
            holder.upvote.setIconTint(orange);
            holder.downvote.setIconTint(gray);
            Reaction reaction = new Reaction("UPVOTE", null, comment.getId(), user);
            reactionApi.create(reaction).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    calculateReactionDelta(comment.getId(), holder);
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
        }
    }

    private void downvote(CommentHolder holder, Comment comment) {
        Reaction reaction = null;
        if (!holder.downvote.getIconTint().equals(downvote)) {
            reaction = new Reaction("DOWNVOTE", null, comment.getId(), user);
        } else {
            reaction = new Reaction("DOWNVOTE", null, comment.getId(), user);
        }
        holder.downvote.setIconTint(downvote);
        holder.upvote.setIconTint(gray);

        reactionApi.create(reaction).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                calculateReactionDelta(comment.getId(), holder);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    private void calculateReactionDelta(Integer id, CommentHolder holder) {
        reactionApi.getForComment(id, user).enqueue(new Callback<ReactionCount>() {
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

    private void createComment(Integer id) {

    }

    public void toggleView(View view){
        if(view.getVisibility()==View.GONE)
            view.setVisibility(View.VISIBLE);
        else if(view.getVisibility()==View.VISIBLE)
            view.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }
}
