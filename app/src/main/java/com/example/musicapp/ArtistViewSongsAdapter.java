package com.example.musicapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ArtistViewSongsAdapter extends RecyclerView.Adapter<ArtistViewSongsViewHolder>{

    Context context;

    List<Song> items;
    //MainActivity activity;

    public ArtistViewSongsAdapter(Context context, List<Song> items) {
        this.context = context;
        this.items = items;
        //this.activity = activity;
    }

    @NonNull
    @Override
    public ArtistViewSongsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ArtistViewSongsViewHolder(LayoutInflater.from(context).inflate(R.layout.artist_view_songs_item, parent, false), context);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewSongsViewHolder holder, int position) {
        holder.songName.setText(items.get(position).getSongName());
        holder.setSongFileUUID(items.get(position).getSongFileUUID());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
