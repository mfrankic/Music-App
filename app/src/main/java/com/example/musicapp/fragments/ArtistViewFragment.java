package com.example.musicapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.adapters.UserViewPlaylistAdapter;
import com.example.musicapp.entities.Album;
import com.example.musicapp.entities.DataSingleton;
import com.example.musicapp.R;
import com.example.musicapp.entities.Song;
import com.example.musicapp.activities.MainActivity;
import com.example.musicapp.adapters.ArtistViewAlbumsAdapter;
import com.example.musicapp.adapters.ArtistViewSongsAdapter;
import com.example.musicapp.entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    Button followBtn;
    Fragment calledFromFragment;
    FirebaseStorage storage;
    private StorageReference storageRef;
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
        toolbar.setNavigationOnClickListener(v -> {


            activity.getSupportFragmentManager()
                .beginTransaction()
                    .setCustomAnimations(android.R.anim.slide_out_right, android.R.anim.fade_out)
                .replace(R.id.main_fragment_container, calledFromFragment)
                .commit();
                });

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


        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        followBtn = view.findViewById(R.id.artist_follow_button);
        followBtn.setOnClickListener(v -> {
                    DocumentReference userDoc = FirebaseFirestore.getInstance().collection("users").document(DataSingleton.getDataSingleton().getCurrentUserID());

                    ArrayList<String> currentUserFollowingIDs = DataSingleton.getDataSingleton().getCurrentUserFollowingIDs();
                    //Log.d("follow", "singleton list " + currentUserFollowingIDs.toString());
                    if(currentUserFollowingIDs != null){
                        currentUserFollowingIDs.add(artistID);
                    }else {
                        currentUserFollowingIDs = new ArrayList<>();
                        currentUserFollowingIDs.add(artistID);
                    }

                    DataSingleton.getDataSingleton().setCurrentUserFollowingIDs(currentUserFollowingIDs);

                    Map<String, Object> updates = new HashMap<>();
                    updates.put("following", currentUserFollowingIDs);

            Log.d("follow", "current list " + currentUserFollowingIDs.toString());

            userDoc.update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        activity.getAllBackendData();
                    } else {

                    }
                }
            });
                }
        );


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

        if(name != null){
            getNameAndBio();
            getSongsAndAlbums();
            name.setText(artistName);
            bio.setText(artistBio);
            albumViewAdapter = new ArtistViewAlbumsAdapter(getContext(), albums);
            songsViewAdapter = new ArtistViewSongsAdapter(getContext(), songs);

        }else {
            Log.d("artistAllusers", "name == null");
        }

    }

    public Fragment getCalledFromFragment() {
        return calledFromFragment;
    }

    public void setCalledFromFragment(Fragment calledFromFragment) {
        this.calledFromFragment = calledFromFragment;
    }
}
