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

public class ArtistViewAlbumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


    TextView albumName;

    private Context context;
    View view;
    //MainActivity activity;
    //String artistID;

    public ArtistViewAlbumViewHolder(View itemView, Context context){
        super(itemView);
        albumName = (TextView)itemView.findViewById(R.id.artist_view_album_name);
        view  = itemView;
        this.context = context;
        itemView.setOnClickListener(this);

        //this.activity = activity;
    }

    @Override
    public void onClick(View v){

        Intent intent = new Intent(context, MusicPlayerActivity.class);
        ActivityOptions options = ActivityOptions.makeCustomAnimation(context, R.anim.slide_in_up, R.anim.fade_out);
        context.startActivity(intent, options.toBundle());
    }


}
