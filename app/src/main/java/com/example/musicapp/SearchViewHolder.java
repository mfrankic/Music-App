package com.example.musicapp;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public  class SearchViewHolder extends RecyclerView.ViewHolder {
    TextView elementName;
    TextView elementType;
    View view;

    public SearchViewHolder(View itemView){
        super(itemView);
        elementName = (TextView)itemView.findViewById(R.id.search_element_name);
        elementType = (TextView)itemView.findViewById(R.id.search_element_type);
        view  = itemView;
    }


}