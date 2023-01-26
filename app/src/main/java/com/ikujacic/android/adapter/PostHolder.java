package com.ikujacic.android.adapter;

import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.ikujacic.android.R;

public class PostHolder extends RecyclerView.ViewHolder {

    TextView title, text, author, reactions, linkToCommunity;
    AutoCompleteTextView flairText;
    MaterialButton upvote, downvote, flair;
    public ConstraintLayout constraintLayout;

    public PostHolder(@NonNull View itemView, @NonNull View viewForFlare) {
        super(itemView);
        title = itemView.findViewById(R.id.postListItem_title);
        text = itemView.findViewById(R.id.postListItem_text);
        author = itemView.findViewById(R.id.postListItem_author);
        linkToCommunity = itemView.findViewById(R.id.linkToCommunity);
        upvote = itemView.findViewById(R.id.upvote);
        downvote = itemView.findViewById(R.id.downvote);
        reactions = itemView.findViewById(R.id.reactions);
        flairText = viewForFlare.findViewById(R.id.flairText);
        flair = itemView.findViewById(R.id.flairPostList);
        constraintLayout = itemView.findViewById(R.id.post_id);
    }
}