package com.ikujacic.android.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.ikujacic.android.R;

public class CommentHolder extends RecyclerView.ViewHolder {

    TextView text, author, reactions;
    MaterialButton upvote, downvote;
    public ConstraintLayout constraintLayout;


    public CommentHolder(@NonNull View itemView) {
        super(itemView);
        text = itemView.findViewById(R.id.commentListItem_text);
        author = itemView.findViewById(R.id.commentListItem_author);
        upvote = itemView.findViewById(R.id.upvote_comment);
        downvote = itemView.findViewById(R.id.downvote_comment);
        reactions = itemView.findViewById(R.id.comment_reactions);
        constraintLayout = itemView.findViewById(R.id.post_id);
    }
}
