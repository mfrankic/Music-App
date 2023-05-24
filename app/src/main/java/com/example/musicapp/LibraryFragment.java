package com.example.musicapp;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class LibraryFragment extends Fragment {

    private  FirebaseFirestore db;
    private Map<String, String> usersSongCollRef;
    ArrayList<Song> allSongs;
    RecyclerView allSongsView;
    LinearLayoutManager allSongsViewManager;
    SongsViewAdapter songsViewAdapter;
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

    private void getSongsDocuments(){
        Log.d("allSongs", usersSongCollRef.toString());

        for(Map.Entry<String, String> entry: usersSongCollRef.entrySet()){
            CollectionReference userSongColl = db.collection("users/" + entry.getKey() + "/songs");
            Log.d("allSongs", "looking for user: " + entry.getKey());


            userSongColl.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("allSongs", document.getId() + " => " + document.getData());

                            Song song = new Song();
                            song.setAlbumUUDI(document.getString("album"));
                            song.setGenre(document.getString("genre"));
                            song.setSongName(document.getString("songName"));
                            song.setSongFileUUID(document.getString("songUUID"));
                            song.setArtistName(entry.getValue());
                            song.setArtistID(entry.getKey());
                            Log.d("pesma", song.toString());

                            allSongs.add(song);
                        }
                    } else {
                        Log.d("allSongs", "Error getting documents: ", task.getException());
                    }

                    // Finally pass songs to adapter to show them in recycle view
                    setAllSongsAdapter();

                }
            });

        }
    }
    private void  getAllSongs(){
        usersSongCollRef = new HashMap<String, String>();

        // Users collection reference
        CollectionReference usersCollRef = db.collection("users");

        usersCollRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("allSongs", document.getId() + " => " + document.getData());
                        usersSongCollRef.put(document.getId().toString(), document.getString("name"));
                    }
                    getSongsDocuments();
                } else {
                    Log.d("allSongs", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void setAllSongsAdapter(){
        songsViewAdapter = new SongsViewAdapter(getContext(), allSongs);
        allSongsView.setAdapter(songsViewAdapter);
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;

        db = FirebaseFirestore.getInstance();
        allSongs = new ArrayList<>();

        allSongsView = view.findViewById(R.id.all_songs_view);
        allSongsViewManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        allSongsView.setLayoutManager(allSongsViewManager);

        getAllSongs();



    }

}
