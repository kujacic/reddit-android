package com.ikujacic.android.adapter;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ikujacic.android.R;
import com.ikujacic.android.model.Comment;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentHolder> {

    private String user;
    List<Comment> commentList;
    ColorStateList orange, gray, downvote;

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
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }
}
