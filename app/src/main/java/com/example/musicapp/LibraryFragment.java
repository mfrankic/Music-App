package com.example.musicapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LibraryFragment extends Fragment {

    public LibraryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_library, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;


        RecyclerView allSongsView = view.findViewById(R.id.all_songs_view);
        LinearLayoutManager allSongsViewManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        allSongsView.setLayoutManager(allSongsViewManager);

        List<CarouselItem> items = new ArrayList<>();

        items.add(new CarouselItem(R.drawable.playlist_image, "Album 1"));
        items.add(new CarouselItem(R.drawable.playlist_or_artist_image, "Album 2"));
        items.add(new CarouselItem(R.drawable.playlist_image, "Album 3"));
        items.add(new CarouselItem(R.drawable.playlist_or_artist_image, "Album 4"));
        items.add(new CarouselItem(R.drawable.playlist_image, "Album 5"));
        items.add(new CarouselItem(R.drawable.playlist_or_artist_image, "Album 6"));
        items.add(new CarouselItem(R.drawable.playlist_image, "Album 7"));
        items.add(new CarouselItem(R.drawable.playlist_or_artist_image, "Album 8"));
        items.add(new CarouselItem(R.drawable.playlist_image, "Album 9"));
        items.add(new CarouselItem(R.drawable.playlist_or_artist_image, "Album 10"));

        CarouselAdapter allSongsAdapter = new CarouselAdapter(items);
        allSongsView.setAdapter(allSongsAdapter);
    }

}
