package com.example.musicapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SongsViewAdapter extends RecyclerView.Adapter<SongsViewHolder>{


    Context context;

    List<Song> items;

    public SongsViewAdapter(Context context, List<Song> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public SongsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SongsViewHolder(LayoutInflater.from(context).inflate(R.layout.all_songs_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SongsViewHolder holder, int position) {
        holder.songName.setText(items.get(position).getSongName());
        holder.artistName.setText(items.get(position).getArtistName());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}