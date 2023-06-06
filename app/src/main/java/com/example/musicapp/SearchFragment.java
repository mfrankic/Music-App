package com.example.musicapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchFragment extends Fragment {


    private FirebaseFirestore db;
    private RecyclerView allSearchView;
    private LinearLayoutManager allSearchViewManager;
    private SearchViewAdapter searchViewAdapter;
    private Button searchBtn;
    private EditText searchText;
    private ArrayList<SearchElement> resultSearchElements;
    private MainActivity activity;
    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    private void setAllSearchViewAdapter(ArrayList<SearchElement> searchElements){
        searchViewAdapter = new SearchViewAdapter(getContext(), searchElements);
        allSearchView.setAdapter(searchViewAdapter);
    }


    private void searchSongs(String searchText){
        ArrayList<Song> songs = DataSingleton.getDataSingleton().getAllSongs();

        ArrayList<Album> albums = DataSingleton.getDataSingleton().getAllAlbums();



        resultSearchElements = new ArrayList<>();

        String patternString = "(?i).*" + searchText + ".*";
        Pattern pattern = Pattern.compile(patternString);
        for (Song song: songs){
            Matcher matcher = pattern.matcher(song.getSongName());
            if (matcher.find()){
                Log.d("reg", "match");
                SearchElement searchElement = new SearchElement();
                searchElement.setSong(song);
                searchElement.setAlbum(null);
                searchElement.setElementName(song.getSongName());
                searchElement.setElementType("Song");

                resultSearchElements.add(searchElement);
            }
        }

        for (Album album: albums){
            Matcher matcher = pattern.matcher(album.getAlbumName());
            if (matcher.find()){
                Log.d("albumreg", album.toString());
                SearchElement searchElement = new SearchElement();
                searchElement.setSong(null);
                searchElement.setAlbum(album);
                searchElement.setElementName(album.getAlbumName());
                searchElement.setElementType("Album");

                resultSearchElements.add(searchElement);
            }
        }

        setAllSearchViewAdapter(resultSearchElements);

    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppCompatImageButton settingsButton = view.findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(v -> activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, activity.settingsFragment)
                .commit());

        TextView greeting = view.findViewById(R.id.greeting_message);
        greeting.setText("Hello " + DataSingleton.getDataSingleton().getCurrentUserName());

        activity = (MainActivity) getActivity();
        assert activity != null;
        activity.isArtistChange();
        db = FirebaseFirestore.getInstance();

        /*
        ArrayList<SearchElement> searchElements = new ArrayList<>();
        SearchElement searchElement = new SearchElement();
        searchElement.setElementName("Test");
        searchElement.setElementType("album");
        SearchElement searchElement2 = new SearchElement();
        searchElement2.setElementName("Test");
        searchElement2.setElementType("album");
        searchElements.add(searchElement);
        searchElements.add(searchElement2);
        */
        allSearchView = view.findViewById(R.id.search_songs_view);
        allSearchViewManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        allSearchView.setLayoutManager(allSearchViewManager);

        //setAllSearchViewAdapter(searchElements);


        searchText = view.findViewById(R.id.song_search);

        searchBtn = view.findViewById(R.id.search_button);
        searchBtn.setOnClickListener(v -> {
            searchSongs(searchText.getText().toString());
        });


        Log.d("singleton", DataSingleton.getDataSingleton().getAllSongs().toString());

    }


}
