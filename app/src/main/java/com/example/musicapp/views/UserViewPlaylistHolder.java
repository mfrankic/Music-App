package com.example.musicapp.views;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.R;
import com.example.musicapp.entities.Playlist;

public class UserViewPlaylistHolder  extends RecyclerView.ViewHolder{

    public TextView playlistName;
    public Playlist playlist;
    private Context context;
    View view;

    public UserViewPlaylistHolder(View itemView, Context context) {
        super(itemView);
        playlistName = (TextView) itemView.findViewById(R.id.user_view_playlist_name);
        view = itemView;
        this.context = context;

    }
}
