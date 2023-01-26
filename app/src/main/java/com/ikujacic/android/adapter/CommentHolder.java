package com.ikujacic.android.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.ikujacic.android.R;

public class CommentHolder extends RecyclerView.ViewHolder {

    TextView text, author, reactions;
    TextInputEditText reply_text;
    MaterialButton upvote, downvote;
    Button reply, submit_reply;
    public ConstraintLayout reply_dialog;


    public CommentHolder(@NonNull View itemView) {
        super(itemView);
        text = itemView.findViewById(R.id.commentListItem_text);
        author = itemView.findViewById(R.id.commentListItem_author);
        upvote = itemView.findViewById(R.id.upvote_comment);
        downvote = itemView.findViewById(R.id.downvote_comment);
        reactions = itemView.findViewById(R.id.comment_reactions);
        reply = itemView.findViewById(R.id.reply);
        reply_text = itemView.findViewById(R.id.reply_edit);
        submit_reply = itemView.findViewById(R.id.submit_reply);
        reply_dialog = itemView.findViewById(R.id.reply_dialog);
    }
}
