package com.example.musicapp.views;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.R;
import com.example.musicapp.activities.MainActivity;
import com.example.musicapp.entities.Song;
import com.example.musicapp.entities.TempSong;
import com.example.musicapp.services.MusicPlayerService;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class CarouselViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener, PopupMenu.OnMenuItemClickListener {

    public ImageView imageView;
    public TextView textView;
    public Song song;
    private MainActivity activity;
    private MediaBrowserCompat mediaBrowser;
    private MediaControllerCompat mediaController;

    public CarouselViewHolder(MainActivity activity, View itemView) {
        super(itemView);
        this.activity = activity;
        imageView = itemView.findViewById(R.id.image_view);
        textView = itemView.findViewById(R.id.text_view);
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
        mediaBrowser = new MediaBrowserCompat(activity,
                new ComponentName(activity, MusicPlayerService.class),
                connectionCallbacks,
                null);
    }

    @Override
    public void onClick(View view) {
        // send song to MusicPlayerService
        MaterialCardView musicPlayerBar = activity.findViewById(R.id.music_player_bar);
        musicPlayerBar.setVisibility(View.VISIBLE);
        initializeData();
        mediaBrowser.connect();
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    private final MediaBrowserCompat.ConnectionCallback connectionCallbacks =
            new MediaBrowserCompat.ConnectionCallback() {
                @Override
                public void onConnected() {
                    // Get the token for the MediaSession
                    MediaSessionCompat.Token token = mediaBrowser.getSessionToken();
                    mediaController = new MediaControllerCompat(activity.getBaseContext(), token);
                    MediaControllerCompat.setMediaController(activity, mediaController);
                    mediaController.getTransportControls().play();
                }

                @Override
                public void onConnectionSuspended() {
                    // The Service has crashed. Disable transport controls until it automatically reconnects
                }

                @Override
                public void onConnectionFailed() {
                    // The Service has refused our connection
                }
            };

    private void initializeData() {
        ArrayList<Integer> songList = new ArrayList<>();
        TempSong firstSong = new TempSong(1, "Danza Kuduro", "Don Omar", "Don Omar Presents: Meet The Orphans", "https://firebasestorage.googleapis.com/v0/b/music-app-7dc1d.appspot.com/o/songs%2F0ee95f21-6bd9-41aa-8bdd-50ee26c216f4.mp3?alt=media&token=412ea96d-008b-4b6b-a19e-db57d1d0fb24");
        TempSong secondSong = new TempSong(2, "In Da Club", "50 Cent", "Get Rich Or Die Tryin'", "https://firebasestorage.googleapis.com/v0/b/music-app-7dc1d.appspot.com/o/songs%2Fde4d5dcf-6d7c-4b60-87bb-6a0f044d1923.mp3?alt=media&token=0e420a8f-92c2-4f1a-8aba-0ae79de098d5");
        songList.add(firstSong.getSongId());
        songList.add(secondSong.getSongId());

        ArrayList<String> songPaths = new ArrayList<>();
        songPaths.add(firstSong.getSongPath());
        songPaths.add(secondSong.getSongPath());

        Intent startServiceIntent = new Intent(activity, MusicPlayerService.class);
        startServiceIntent.putExtra("songList", songPaths);

        Bundle songBundle = new Bundle();
        songBundle.putIntegerArrayList("songList", songList);
        mediaBrowser.subscribe("media", songBundle, new MediaBrowserCompat.SubscriptionCallback() {
            @Override
            public void onChildrenLoaded(@NonNull String parentId, @NonNull List<MediaBrowserCompat.MediaItem> children) {
                super.onChildrenLoaded(parentId, children);
            }
        });

        activity.startService(startServiceIntent);
    }
}
