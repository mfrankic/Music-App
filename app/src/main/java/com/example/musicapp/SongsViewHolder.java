package com.example.musicapp;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public  class SongsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
    TextView songName;
    TextView artistName;
    String songFileUUID;
    private Context context;
    View view;

    public SongsViewHolder(View itemView, Context context){
        super(itemView);
        songName = (TextView)itemView.findViewById(R.id.library_song_name);
        artistName = (TextView)itemView.findViewById(R.id.library_artist_name);
        view  = itemView;
        this.context = context;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View v){
        Log.d("onesong", "Stisnuto");
        ArrayList<Song> songQueue = new ArrayList<>();

        for(Song song: DataSingleton.getDataSingleton().getAllSongs()){
            if(song.getSongFileUUID().equals(this.songFileUUID)){
                songQueue.add(song);
            }
        }
        DataSingleton.getDataSingleton().setSongsQueue(songQueue);

        Intent intent = new Intent(context, MusicPlayerActivity.class);
        ActivityOptions options = ActivityOptions.makeCustomAnimation(context, R.anim.slide_in_up, R.anim.fade_out);
        context.startActivity(intent, options.toBundle());
    }

    @Override
    public boolean onLongClick(View v){
        for(Song song: DataSingleton.getDataSingleton().getAllSongs()){
            if(song.getSongFileUUID().equals(this.songFileUUID)){
                DataSingleton.getDataSingleton().getSongsQueue().add(song);
            }
        }
        Log.d("Songqueue", DataSingleton.getDataSingleton().getSongsQueue().toString());
        Toast.makeText(context, "Added to playing queue", Toast.LENGTH_SHORT).show();
        return true;
    }

    public String getSongFileUUID() {
        return songFileUUID;
    }

    public void setSongFileUUID(String songFileUUID) {
        this.songFileUUID = songFileUUID;
    }
}