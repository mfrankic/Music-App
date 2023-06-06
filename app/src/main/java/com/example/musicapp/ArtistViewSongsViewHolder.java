package com.example.musicapp;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ArtistViewSongsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, PopupMenu.OnMenuItemClickListener{


    TextView songName;
    String songFileUUID;
    private Context context;
    View view;
    //MainActivity activity;
    //String artistID;

    public ArtistViewSongsViewHolder(View itemView, Context context){
        super(itemView);
        songName = (TextView)itemView.findViewById(R.id.artist_view_song_name);
        view  = itemView;
        this.context = context;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        //this.activity = activity;
    }

    @Override
    public void onClick(View v){

        Intent intent = new Intent(context, MusicPlayerActivity.class);
        ActivityOptions options = ActivityOptions.makeCustomAnimation(context, R.anim.slide_in_up, R.anim.fade_out);
        context.startActivity(intent, options.toBundle());
    }

    @Override
    public boolean onLongClick(View v){
        PopupMenu popup = new PopupMenu(context, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.artist_song_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();



        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.artist_view_add_to_queue:
                addSongToQueue();
                return true;
            default:
                return false;
        }
    }

    public String getSongFileUUID() {
        return songFileUUID;
    }

    public void setSongFileUUID(String songFileUUID) {
        this.songFileUUID = songFileUUID;
    }



    private void addSongToQueue(){
        for(Song song: DataSingleton.getDataSingleton().getAllSongs()){
            if(song.getSongFileUUID().equals(this.songFileUUID)){
                DataSingleton.getDataSingleton().getSongsQueue().add(song);
            }
        }
        Log.d("Songqueue", DataSingleton.getDataSingleton().getSongsQueue().toString());
        Toast.makeText(context, "Added to playing queue", Toast.LENGTH_SHORT).show();
    }
}
