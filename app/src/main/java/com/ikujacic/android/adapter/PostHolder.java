package com.ikujacic.android.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ikujacic.android.R;

public class PostHolder extends RecyclerView.ViewHolder {

    TextView title, text, author;
    public ConstraintLayout constraintLayout;

    public PostHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.postListItem_title);
        text = itemView.findViewById(R.id.postListItem_text);
        author = itemView.findViewById(R.id.postListItem_author);
        constraintLayout = itemView.findViewById(R.id.post_id);
    }
}