package com.ikujacic.android.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ikujacic.android.R;

public class CommunityHolder extends RecyclerView.ViewHolder {

    TextView name, description;

    public CommunityHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.communityListItem_name);
        description = itemView.findViewById(R.id.communityListItem_desc);
    }
}