package com.example.musicapp;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public  class SongsViewHolder extends RecyclerView.ViewHolder {
    TextView songName;
    TextView artistName;
    View view;

    public SongsViewHolder(View itemView){
        super(itemView);
        songName = (TextView)itemView.findViewById(R.id.library_song_name);
        artistName = (TextView)itemView.findViewById(R.id.library_artist_name);
        view  = itemView;
    }


}