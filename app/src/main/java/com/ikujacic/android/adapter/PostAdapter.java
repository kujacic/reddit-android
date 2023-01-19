package com.ikujacic.android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ikujacic.android.R;
import com.ikujacic.android.model.Post;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostHolder> {

    private List<Post> postList;
    private ClickListener clickListener;

    public PostAdapter(List<Post> postList, ClickListener clickListener) {
        this.postList = postList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_list_item, parent, false);
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
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}