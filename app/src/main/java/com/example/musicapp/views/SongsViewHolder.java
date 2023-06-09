package com.example.musicapp.views;

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

import com.example.musicapp.entities.DataSingleton;
import com.example.musicapp.R;
import com.example.musicapp.entities.Song;
import com.example.musicapp.activities.MainActivity;
import com.example.musicapp.activities.MusicPlayerActivity;

import java.util.ArrayList;

public class SongsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, PopupMenu.OnMenuItemClickListener {
    public TextView songName;
    public TextView artistName;
    public TextView numberOfLikes;
    String songFileUUID;
    private Context context;
    View view;
    MainActivity activity;
    String artistID;

    public SongsViewHolder(View itemView, Context context, MainActivity activity) {
        super(itemView);
        songName = (TextView) itemView.findViewById(R.id.library_song_name);
        artistName = (TextView) itemView.findViewById(R.id.library_artist_name);
        numberOfLikes = (TextView) itemView.findViewById(R.id.library_song_view_num_of_likes);
        //Log.d("holder", numberOfLikes.getText().toString());
        view = itemView;
        this.context = context;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        this.activity = activity;
    }

    public SongsViewHolder(View itemView, Context context){
        super(itemView);
        this.context = context;
        songName = (TextView)itemView.findViewById(R.id.library_song_name);
        artistName = (TextView)itemView.findViewById(R.id.library_artist_name);

    }

    @Override
    public void onClick(View v) {
        Log.d("onesong", "Stisnuto");
        ArrayList<Song> songQueue = new ArrayList<>();

        for (Song song : DataSingleton.getDataSingleton().getAllSongs()) {
            if (song.getSongFileUUID().equals(this.songFileUUID)) {
                songQueue.add(song);
            }
        }
        DataSingleton.getDataSingleton().setSongsQueue(songQueue);

        Intent intent = new Intent(context, MusicPlayerActivity.class);
        ActivityOptions options = ActivityOptions.makeCustomAnimation(context, R.anim.slide_in_up, R.anim.fade_out);
        context.startActivity(intent, options.toBundle());
    }

    @Override
    public boolean onLongClick(View v) {
        PopupMenu popup = new PopupMenu(context, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.song_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(this);
        popup.show();


        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_to_queue:
                addSongToQueue();
                return true;
            case R.id.artist_profile:
                activity.artistViewFragment.setArtistID(artistID);
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_fragment_container, activity.artistViewFragment)
                        .commit();
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

    public void setArtistID(String artistID) {
        this.artistID = artistID;
    }

    private void addSongToQueue() {
        for (Song song : DataSingleton.getDataSingleton().getAllSongs()) {
            if (song.getSongFileUUID().equals(this.songFileUUID)) {
                DataSingleton.getDataSingleton().getSongsQueue().add(song);
            }
        }
        Log.d("Songqueue", DataSingleton.getDataSingleton().getSongsQueue().toString());
        Toast.makeText(context, "Added to playing queue", Toast.LENGTH_SHORT).show();
    }
}