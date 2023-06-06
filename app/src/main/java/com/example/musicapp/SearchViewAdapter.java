package com.example.musicapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SearchViewAdapter extends RecyclerView.Adapter<SearchViewHolder>{


    Context context;

    List<SearchElement> items;

    public SearchViewAdapter(Context context, List<SearchElement> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchViewHolder(LayoutInflater.from(context).inflate(R.layout.search_songs_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.elementName.setText(items.get(position).getElementName());
        holder.elementType.setText(items.get(position).getElementType());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}