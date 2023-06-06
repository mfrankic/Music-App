package com.example.musicapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;
        activity.isArtistChange();

        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        String greeting;
        if (hour < 5) {
            greeting = "Good morning? \uD83E\uDD14";
        } else if (hour < 12) {
            greeting = "Good morning!";
        } else if (hour < 18) {
            greeting = "Good afternoon!";
        } else {
            greeting = "Good evening!";
        }

        MaterialTextView greetingTextView = view.findViewById(R.id.greeting_message);
        greetingTextView.setText(greeting);

        AppCompatImageButton notificationButton = view.findViewById(R.id.notification_button);
        notificationButton.setOnClickListener(v -> {
            // handle notification button click
            System.out.println("Notification button clicked");
        });

        AppCompatImageButton recentlyPlayedButton = view.findViewById(R.id.recently_played_button);
        recentlyPlayedButton.setOnClickListener(v -> {
            // handle recently played button click
            System.out.println("Recently played button clicked");
        });

        AppCompatImageButton settingsButton = view.findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(v -> activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, activity.settingsFragment)
                .commit());


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


        RecyclerView recommendedRecyclerView = view.findViewById(R.id.recommended_recycler);
        LinearLayoutManager recommendedLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        recommendedRecyclerView.setLayoutManager(recommendedLayoutManager);

        CarouselAdapter recommendedAdapter = new CarouselAdapter(items);
        recommendedRecyclerView.setAdapter(recommendedAdapter);


        RecyclerView recentlyPlayedRecyclerView = view.findViewById(R.id.recently_played_recycler);
        LinearLayoutManager recentlyPlayedLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        recentlyPlayedRecyclerView.setLayoutManager(recentlyPlayedLayoutManager);

        CarouselAdapter recentlyPlayedAdapter = new CarouselAdapter(items);
        recentlyPlayedRecyclerView.setAdapter(recentlyPlayedAdapter);


        RecyclerView popularRecyclerView = view.findViewById(R.id.popular_recycler);
        LinearLayoutManager popularLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        popularRecyclerView.setLayoutManager(popularLayoutManager);

        CarouselAdapter popularAdapter = new CarouselAdapter(items);
        popularRecyclerView.setAdapter(popularAdapter);


        RecyclerView featuredRecyclerView = view.findViewById(R.id.featured_recycler);
        LinearLayoutManager featuredLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false);
        featuredRecyclerView.setLayoutManager(featuredLayoutManager);

        CarouselAdapter featuredAdapter = new CarouselAdapter(items);
        featuredRecyclerView.setAdapter(featuredAdapter);
    }
}