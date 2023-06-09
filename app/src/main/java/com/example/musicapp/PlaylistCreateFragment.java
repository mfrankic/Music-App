package com.example.musicapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class PlaylistCreateFragment extends Fragment {


    private RecyclerView allSongsView;
    private LinearLayoutManager allSongsViewManager;
    private PlaylistCreateViewAdapter songsViewAdapter;
    private Button createPlaylistBtn;

    public PlaylistCreateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playlist_create, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;

        MaterialToolbar toolbar = view.findViewById(R.id.fragment_create_playlist_top_bar);
        toolbar.setNavigationOnClickListener(v -> activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, activity.homeFragment)
                .commit());

        createPlaylistBtn = view.findViewById(R.id.create_playlist_button);
        createPlaylistBtn.setOnClickListener(v -> {
                    Log.d("createPlaylist", DataSingleton.getDataSingleton().getPlaylistCreateSongs().toString());
                }
        );

        allSongsView = view.findViewById(R.id.playlist_create_fragment_all_songs_view);
        allSongsViewManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        allSongsView.setLayoutManager(allSongsViewManager);
        songsViewAdapter = new PlaylistCreateViewAdapter(getContext(), DataSingleton.getDataSingleton().getAllSongs());
        allSongsView.setAdapter(songsViewAdapter);


        DataSingleton.getDataSingleton().playlistCreateSongs = new ArrayList<>();

    }
}