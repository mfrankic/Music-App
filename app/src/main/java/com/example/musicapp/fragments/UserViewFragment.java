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

import com.example.musicapp.R;
import com.example.musicapp.activities.MainActivity;
import com.example.musicapp.adapters.ArtistViewAlbumsAdapter;
import com.example.musicapp.adapters.ArtistViewSongsAdapter;
import com.example.musicapp.adapters.UserViewPlaylistAdapter;
import com.example.musicapp.entities.DataSingleton;
import com.example.musicapp.entities.Playlist;
import com.example.musicapp.entities.User;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;

public class UserViewFragment extends Fragment {

    public TextView name, bio;
    private RecyclerView playlistView;
    private ArrayList<Playlist> playlists;
    RecyclerView playlistsView;
    LinearLayoutManager playlistManager;
    UserViewPlaylistAdapter playlistAdapter;

    public UserViewFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;

        MaterialToolbar toolbar = view.findViewById(R.id.artist_profile_top_bar);
        toolbar.setNavigationOnClickListener(v -> activity.getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.main_fragment_container, activity.allUsersViewFragment)
                .commit());


        name = view.findViewById(R.id.user_profile_user_name);
        bio = view.findViewById(R.id.user_profile_user_bio);
        name.setText(DataSingleton.getDataSingleton().getCurrentUserName());
        bio.setText(DataSingleton.getDataSingleton().getCurrentUserBio());


        getPlaylists();



        playlistView = view.findViewById(R.id.users_playlists_recycler_view);
        playlistManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        playlistView.setLayoutManager(playlistManager);
        playlistAdapter = new UserViewPlaylistAdapter(getContext(), playlists);
        playlistView.setAdapter(playlistAdapter);




    }

    private  void  getPlaylists() {
        playlists = DataSingleton.getDataSingleton().getAllPlaylists();
    }
}
