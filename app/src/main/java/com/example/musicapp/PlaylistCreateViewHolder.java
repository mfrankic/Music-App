package com.example.musicapp;

import android.content.Context;
import android.view.MenuInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.TextView;

public class PlaylistCreateViewHolder extends SongsViewHolder implements CheckBox.OnCheckedChangeListener{

    private CheckBox includeInPlaylistCheck;
    public Song song;

    public PlaylistCreateViewHolder(View view, Context context){
        super(view, context);
        includeInPlaylistCheck = (CheckBox) itemView.findViewById(R.id.include_in_playlist);
        includeInPlaylistCheck.setOnCheckedChangeListener(this);
    }



    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(b){
            DataSingleton.getDataSingleton().addPlaylistCreateSongs(song);
        } else {
            DataSingleton.getDataSingleton().removeFromPlaylistCreateSongs(song);
        }
    }
}
