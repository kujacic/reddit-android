package com.ikujacic.android.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ikujacic.android.R;

public class CommunityHolder extends RecyclerView.ViewHolder {

    TextView name, description;
    public ConstraintLayout constraintLayout;

    public CommunityHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.communityListItem_name);
        description = itemView.findViewById(R.id.communityListItem_desc);
        constraintLayout = itemView.findViewById(R.id.community_id);
    }
}