package com.ikujacic.android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ikujacic.android.R;
import com.ikujacic.android.model.Community;

import java.util.List;

public class CommunityAdapter extends RecyclerView.Adapter<CommunityHolder> {

    private List<Community> communityList;
    private ClickListener clickListener;

    public CommunityAdapter(List<Community> communityList, ClickListener clickListener) {
        this.communityList = communityList;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public CommunityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.community_list_item, parent, false);
        return new CommunityHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommunityHolder holder, int position) {
        Community community = communityList.get(position);
        holder.name.setText(community.getName());
        holder.description.setText(community.getDescription());
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onItemClicked(communityList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return communityList.size();
    }
}