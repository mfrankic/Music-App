package com.example.musicapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.entities.Album;
import com.example.musicapp.entities.DataSingleton;
import com.example.musicapp.R;
import com.example.musicapp.entities.Song;
import com.example.musicapp.activities.MainActivity;
import com.example.musicapp.adapters.ArtistViewAlbumsAdapter;
import com.example.musicapp.adapters.ArtistViewSongsAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ArtistViewFragment extends Fragment {


    public TextView name, bio;

    protected FirebaseAuth auth;
    private ArrayList<Song> songs;
    private ArrayList<Album> albums;
    private String artistName, artistBio, artistID;
    RecyclerView songsView, albumsView;
    LinearLayoutManager songsViewManager, albumViewManager;
    ArtistViewSongsAdapter songsViewAdapter;
    ArtistViewAlbumsAdapter albumViewAdapter;

    public ArtistViewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_artist_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;

        MaterialToolbar toolbar = view.findViewById(R.id.artist_profile_top_bar);
        toolbar.setNavigationOnClickListener(v -> activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, activity.homeFragment)
                .commit());


        name = view.findViewById(R.id.artist_profile_artist_name);
        bio = view.findViewById(R.id.artist_profile_artist_bio);
        getNameAndBio();
        name.setText(artistName);
        bio.setText(artistBio);


        getSongsAndAlbums();


        songsView = view.findViewById(R.id.artist_songs_recycler_view);
        songsViewManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        songsView.setLayoutManager(songsViewManager);
        songsViewAdapter = new ArtistViewSongsAdapter(getContext(), songs);
        songsView.setAdapter(songsViewAdapter);

        albumsView = view.findViewById(R.id.artist_albums_recycler_view);
        albumViewManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        albumsView.setLayoutManager(albumViewManager);
        albumViewAdapter = new ArtistViewAlbumsAdapter(getContext(), albums);
        albumsView.setAdapter(albumViewAdapter);


    }


    private void getSongsAndAlbums() {
        songs = new ArrayList<>();
        albums = new ArrayList<>();

        for (Song song : DataSingleton.getDataSingleton().getAllSongs()) {
            if (song.getArtistID().equals(artistID)) {
                songs.add(song);
            }
        }

        for (Album album : DataSingleton.getDataSingleton().getAllAlbums()) {
            if (album.getArtistID().equals(artistID)) {
                albums.add(album);
            }
        }
    }

    private void getNameAndBio() {
        for (Song song : DataSingleton.getDataSingleton().getAllSongs()) {
            if (song.getArtistID().equals(this.artistID)) {
                artistName = song.getArtistName();
                artistBio = song.getArtistBio();
            }
        }
    }

    public void setArtistID(String artistID) {
        this.artistID = artistID;

    }
}
