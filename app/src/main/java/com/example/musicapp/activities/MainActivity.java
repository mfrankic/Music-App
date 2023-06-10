package com.example.musicapp.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import com.example.musicapp.R;
import com.example.musicapp.entities.Album;
import com.example.musicapp.entities.DataSingleton;
import com.example.musicapp.entities.Playlist;
import com.example.musicapp.entities.Song;
import com.example.musicapp.entities.User;
import com.example.musicapp.fragments.AllUsersViewFragment;
import com.example.musicapp.fragments.ArtistViewFragment;
import com.example.musicapp.fragments.HomeFragment;
import com.example.musicapp.fragments.LibraryFragment;
import com.example.musicapp.fragments.PlaylistCreateFragment;
import com.example.musicapp.fragments.SearchFragment;
import com.example.musicapp.fragments.SettingsFragment;
import com.example.musicapp.fragments.SocialFragment;
import com.example.musicapp.fragments.UploadSongFragment;
import com.example.musicapp.fragments.UserViewFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    public final HomeFragment homeFragment = new HomeFragment();
    protected final UploadSongFragment uploadSongFragment = new UploadSongFragment();
    protected final SearchFragment searchFragment = new SearchFragment();
    public final SettingsFragment settingsFragment = new SettingsFragment();
    public final LibraryFragment libraryFragment = new LibraryFragment();
    public ArtistViewFragment artistViewFragment = new ArtistViewFragment();
    public PlaylistCreateFragment playlistCreateFragment = new PlaylistCreateFragment();
    public SocialFragment socialFragment = new SocialFragment();
    public AllUsersViewFragment allUsersViewFragment = new AllUsersViewFragment();
    public UserViewFragment userViewFragment = new UserViewFragment();

    private View uploadButtonItem;

    public final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseStorage storage;
    private StorageReference storageRef;

    private boolean isArtist;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public ArrayList<Song> allSongs, artistSongs, genreSongs, releaseYearSongs;
    public ArrayList<Album> allAlbums;
    public ArrayList<String> allArtists;
    ArrayList<String> allReleaseYears;
    ArrayList<User> allUsers;
    private Map<String, String> usersSongCollRef;
    private Map<String, String> usersIDAndBio;
    boolean userIDsFetchfinished, songsFetchFinished, albumsFetchFinished;
    private int numOfSongsFetched, numOfURLsFetched;
    private Handler loadDataHandler = new Handler();


    private SharedPreferences.OnSharedPreferenceChangeListener sharedPrefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
            if (prefs.getBoolean("isArtist", false)) {
                uploadButtonItem.setVisibility(View.VISIBLE);
            } else {
                uploadButtonItem.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.song_name).setSelected(true);
        findViewById(R.id.artist_name).setSelected(true);

        // open music player when clicked on music player bar
        findViewById(R.id.music_player_bar).setOnClickListener(v -> {
            //DataSingleton.getDataSingleton().setAllSongs(libraryFragment.getAllSongsGetter());
            Intent intent = new Intent(this, MusicPlayerActivity.class);
            ActivityOptions options = ActivityOptions.makeCustomAnimation(this, R.anim.slide_in_up, R.anim.fade_out);
            startActivity(intent, options.toBundle());
        });

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = sharedPreferences.edit();
        sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPrefListener);
        boolean darkModeEnabled = sharedPreferences.getBoolean("dark_mode_enabled", false);
        if (darkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        NavigationBarView bottomNavigationView = findViewById(R.id.bottom_bar);
        bottomNavigationView.setOnItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home_button);

        if (savedInstanceState != null) {
            String fragmentName = savedInstanceState.getString("FRAGMENT");
            Log.d("MainActivity", "Fragment name: " + fragmentName);
            if (fragmentName != null) {
                try {
                    Class<?> fragmentClass = Class.forName(fragmentName);
                    Fragment fragment = (Fragment) fragmentClass.newInstance();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_fragment_container, fragment)
                            .commit();
                } catch (ClassNotFoundException | IllegalAccessException |
                         InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }

        // Show/hide upload song, depending on user account type
        uploadButtonItem = bottomNavigationView.findViewById(R.id.upload_song_button);

        DocumentReference userDoc = db.collection("users").document(auth.getUid());
        userDoc.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String isArtistString = String.valueOf(document.get("isArtist"));
                    isArtist = Boolean.valueOf(isArtistString);
                    editor.putBoolean("isArtist", isArtist);
                    editor.apply();
                    if (!isArtist) {
                        uploadButtonItem.setVisibility(View.GONE);
                    } else {
                        uploadButtonItem.setVisibility(View.VISIBLE);
                    }
                    Log.d("artcheck", "DocumentSnapshot data: " + isArtist);
                } else {
                    Log.d("artcheck", "No such document");
                }
            } else {
                Log.d("artcheck", "get failed with ", task.getException());
            }
        });

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        getAllBackendData();
        getCurrentUserData();
        isArtistChange();
    }


    private void getCurrentUserData() {

        FirebaseUser currentUser = auth.getCurrentUser();
        DataSingleton.getDataSingleton().setCurrentUserID(currentUser.getUid());
        Log.d("currentUser", DataSingleton.getDataSingleton().getCurrentUserID());
        DocumentReference docRef = db.collection("users").document(currentUser.getUid());
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d("SETTINGS", "DocumentSnapshot data: " + document.get("name"));
                    DataSingleton.getDataSingleton().setCurrentUserName(String.valueOf(document.get("name")));

                } else {
                    Log.d("SETTINGS", "No such document");
                }
            } else {
                Log.d("SETTINGS", "get failed with ", task.getException());
            }
        });
    }

    private void getUsersIDs() {
        usersSongCollRef = new HashMap<String, String>();
        usersIDAndBio = new HashMap<String, String>();

        // Users collection reference
        CollectionReference usersCollRef = db.collection("users");

        usersCollRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<User> allUsers = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("allSongs", document.getId() + " => " + document.getData());
                        usersSongCollRef.put(document.getId().toString(), document.getString("name"));
                        usersIDAndBio.put(document.getId().toString(), document.getString("bio"));

                        User user = new User();
                        user.setUserID(document.getId());
                        user.setUserBio(document.getString("bio"));
                        user.setUserName(document.getString("name"));


                        String isArtistString = String.valueOf(document.get("isArtist"));
                        isArtist = Boolean.valueOf(isArtistString);
                        if (isArtist) {
                            user.setArtist(true);
                        } else {
                            user.setArtist(false);
                        }

                        allUsers.add(user);

                    }
                    DataSingleton.getDataSingleton().setAllUsers(allUsers);
                    userIDsFetchfinished = true;
                    getSongsDocuments();
                } else {
                    Log.d("allSongs", "Error getting documents: ", task.getException());
                }
            }
        });
    }


    private void getSongsDocuments() {
        Log.d("allSongs", usersSongCollRef.toString());
        Log.d("brojPoziva", "POZIV");

        for (Map.Entry<String, String> entry : usersSongCollRef.entrySet()) {
            CollectionReference userSongColl = db.collection("users/" + entry.getKey() + "/songs");
            Log.d("allSongs", "looking for user: " + entry.getKey());


            userSongColl.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("allSongs", document.getId() + " => " + document.getData());

                            Song song = new Song();
                            song.setArtistBio(usersIDAndBio.get(entry.getKey()));
                            song.setAlbumUUDI(document.getString("album"));
                            song.setGenre(document.getString("genre"));
                            song.setSongName(document.getString("songName"));
                            song.setSongFileUUID(document.getString("songUUID"));
                            song.setArtistName(entry.getValue());
                            song.setArtistID(entry.getKey());
                            song.setNumberOfLikes(document.getString("numberOfLikes"));
                            song.setNumberOfListens(Integer.valueOf(document.getString("numberOfListens")));
                            //song.setSongPath(document.get);
                            Log.d("pesma", song.toString());

                            allSongs.add(song);
                        }

                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("allSongs", allSongs);

                        homeFragment.setArguments(bundle);
                        // refresh fragment
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.detach(homeFragment).attach(homeFragment).commit();
                    } else {
                        Log.d("allSongs", "Error getting documents: ", task.getException());
                    }

                    // Finally pass songs to adapter to show them in recycle view
                    //setAllSongsAdapter(allSongs);
                    /*
                    getAlbumData();
                    getAllArtists();
                    getSongsURL();
                    */
                    //getAlbumData();

                    numOfSongsFetched += 1;

                    Log.d("allSongs", String.valueOf(numOfSongsFetched) + " " + String.valueOf(usersSongCollRef.size()));
                    if (numOfSongsFetched == usersSongCollRef.size()) {
                        getAllArtists();
                        updateSongsWithAlbumData();
                        getSongsURL();

                    }
                }
            });

        }
        //songsFetchFinished = true;
        getAlbumData();
    }

    private void getAlbumData() {
        CollectionReference albums = db.collection("albums/");
        albums.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Object timestampObject = document.get("releaseDate");
                        com.google.firebase.Timestamp timestamp = (com.google.firebase.Timestamp) timestampObject;
                        long timestampMillis = timestamp.toDate().getTime();
                        Timestamp releaseDate = new Timestamp(timestamp.toDate().getTime());
                        // Use the timestampMillis as needed
                        Log.d("vreme", String.valueOf(releaseDate));

                        Album album = new Album();
                        album.setAlbumName(document.get("albumName").toString());
                        album.setReleaseDate(releaseDate);
                        album.setAlbumID(document.getId().toString());
                        album.setArtistID(document.getString("artistID"));
                        allAlbums.add(album);

                    }
                    Log.d("allAlbums", allAlbums.toString());

                } else {
                    Log.d("allSongs", "Error getting documents: ", task.getException());
                }
                //updateSongsWithAlbumData();
                //DataSingleton.getDataSingleton().setAllSongs(allSongs);

                // ovo se zove getAllReleaseYears();
                DataSingleton.getDataSingleton().setAllAlbums(allAlbums);
            }
        });
    }

    private void getAllArtists() {
        for (Song song : allSongs) {
            if (!allArtists.contains(song.getArtistName())) {
                allArtists.add(song.getArtistName());
            }
        }
        ArrayList<String> allArtistsCopy = new ArrayList<>(allArtists);
        Log.d("allartists", allArtists.toString());
        DataSingleton.getDataSingleton().setAllArtists(allArtists);
    }

    private void updateSongsWithAlbumData() {
        for (Song song : allSongs) {
            String songAlbumID = song.getAlbumUUDI();
            for (Album album : allAlbums) {
                if (album.getAlbumID().equals(songAlbumID)) {
                    song.setAlbumName(album.getAlbumName());
                    song.setReleaseDate(album.getReleaseDate());
                    Log.d("songAlbum", songAlbumID + "  " + album.getAlbumID());
                    Log.d("songAlbum", song.getReleaseDate().toString());
                }
            }

        }


    }

    private void getSongsURL() {


        for (Song song : allSongs) {
            try {
                StorageReference songRef = storageRef.child("/" + "songs/" + song.getSongFileUUID() + ".mp3");

                songRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadUrl) {

                        //Log.d("URLgetzika", song.getSongPath());
                        if (song.getSongPath() == null) {
                            song.setSongPath(downloadUrl.toString());

                        }
                        Log.d("URLgetzika", String.valueOf(song.getSongPath()));
                        Log.d("URLgetzika", String.valueOf(numOfURLsFetched) + " " + String.valueOf(allSongs.size()));
                        numOfURLsFetched += 1;
                        if (numOfURLsFetched == allSongs.size()) {
                            DataSingleton.getDataSingleton().setAllSongs(allSongs);
                            getPlaylists();
                            Toast.makeText(MainActivity.this, "Backend data refresh finished", Toast.LENGTH_SHORT).show();
                        }
                        Log.d("URLgetzika", String.valueOf(numOfURLsFetched) + " " + String.valueOf(allSongs.size()));

                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        numOfURLsFetched += 1;
                        if (numOfURLsFetched == allSongs.size()) {
                            DataSingleton.getDataSingleton().setAllSongs(allSongs);
                            getPlaylists();
                            Toast.makeText(MainActivity.this, "Data load finished", Toast.LENGTH_SHORT).show();
                            libraryFragment.dataUpdate();
                        }
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public void getPlaylists() {
        ArrayList<Playlist> allPlaylists = new ArrayList<>();

        CollectionReference playlistColl = db.collection("playlist");
        playlistColl.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {


                        Playlist playlist = new Playlist();
                        playlist.setPlaylistName(document.getString("name"));
                        playlist.setCreatorID(document.getString("creatorID"));
                        playlist.setCreatorName(document.getString("creatorName"));

                        String isPrivateString = String.valueOf(document.get("private"));
                        Boolean isPrivate = Boolean.valueOf(isPrivateString);

                        if (isPrivate) {
                            playlist.setPrivate(true);
                        } else {
                            playlist.setPrivate(false);
                        }
                        ArrayList<Song> playlistSongs = new ArrayList<>();
                        ArrayList<String> playlistSongsIDs = (ArrayList<String>) (document.get("songs"));
                        for(String songID: playlistSongsIDs){
                            for(Song song: allSongs){
                                if(songID.equals(song.getSongFileUUID())){
                                    playlistSongs.add(song);
                                }

                            }
                        }
                    }
                    playlist.setPlaylistSongs(playlistSongs);

                    allPlaylists.add(playlist);

                }

                DataSingleton.getDataSingleton().setAllPlaylists(allPlaylists);
                Toast.makeText(MainActivity.this, "Backend data refresh finished", Toast.LENGTH_SHORT).show();
                Log.d("playlistLoad", DataSingleton.getDataSingleton().getAllPlaylists().toString());
            } else {
                //Log.d(TAG, "Error getting documents: ", task.getException());
            }

        });

    }

    public void getAllBackendData() {
        allSongs = new ArrayList<>();
        allAlbums = new ArrayList<>();
        allArtists = new ArrayList<>();
        allUsers = new ArrayList<>();

        userIDsFetchfinished = false;
        songsFetchFinished = false;
        albumsFetchFinished = false;

        numOfSongsFetched = 0;
        numOfURLsFetched = 0;

        getUsersIDs();

        Toast.makeText(MainActivity.this, "Backend data refresh started", Toast.LENGTH_LONG).show();

    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        String fragmentName = Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.main_fragment_container)).getClass().getName();
        outState.putString("FRAGMENT", fragmentName);
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_fragment_container);
        if (currentFragment instanceof HomeFragment) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment_container, homeFragment)
                    .commit();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.home_button) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                    .beginTransaction()
                     .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.main_fragment_container, homeFragment);

            if (allSongs != null && allSongs.size() > 0) {

                Log.d("MainActivity", allSongs.toString());
                fragmentTransaction.commit();

                return true;
            }
            // try again until allSongs is fetched
            loadDataHandler.postDelayed(() -> onNavigationItemSelected(item), 200);

            return false;
        } else if (itemId == R.id.search_button) {
            Log.d("MainActivity", "Search button clicked");
            getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.main_fragment_container, searchFragment)
                    .commit();
            //isArtistChange();
            return true;
        } else if (itemId == R.id.library_button) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).replace(R.id.main_fragment_container, libraryFragment).commit();
            Log.d("MainActivity", "Library button clicked");
            //isArtistChange();
            return true;
        } else if (itemId == R.id.upload_song_button) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).replace(R.id.main_fragment_container, uploadSongFragment).addToBackStack(null).commit();
            Log.d("MainActivity", "Upload song clicked");
            //isArtistChange();
            return true;
        }
        return false;
    }

    public void isArtistChange() {
        isArtist = sharedPreferences.getBoolean("isArtist", false);

        if (isArtist) {
            Log.d("isArtist", "true");
            uploadButtonItem.setVisibility(View.VISIBLE);
        } else {
            uploadButtonItem.setVisibility(View.GONE);
            Log.d("isArtist", "false");
        }

    }
}
