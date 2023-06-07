package com.example.musicapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SongsViewAdapter extends RecyclerView.Adapter<SongsViewHolder>{


    Context context;

    List<Song> items;
    MainActivity activity;

    public SongsViewAdapter(Context context, List<Song> items, MainActivity activity) {
        this.context = context;
        this.items = items;
        this.activity = activity;
    }

    @NonNull
    @Override
    public SongsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SongsViewHolder(LayoutInflater.from(context).inflate(R.layout.all_songs_list_item, parent, false), context, activity);
    }

    @Override
    public void onBindViewHolder(@NonNull SongsViewHolder holder, int position) {
        holder.songName.setText(items.get(position).getSongName());
        holder.artistName.setText(items.get(position).getArtistName());
        Log.d("adapter", String.valueOf(items.get(position).getNumberOfLikes()));
        holder.numberOfLikes.setText("Number of likes: " + String.valueOf(items.get(position).getNumberOfLikes()));
        holder.setSongFileUUID(items.get(position).getSongFileUUID());
        holder.setArtistID(items.get(position).getArtistID());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}