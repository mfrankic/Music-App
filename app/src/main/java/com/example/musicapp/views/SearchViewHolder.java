package com.example.musicapp.views;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.R;

public class SearchViewHolder extends RecyclerView.ViewHolder {
    public TextView elementName;
    public TextView elementType;
    View view;

    public SearchViewHolder(View itemView) {
        super(itemView);
        elementName = (TextView) itemView.findViewById(R.id.search_element_name);
        elementType = (TextView) itemView.findViewById(R.id.search_element_type);
        view = itemView;
    }


}