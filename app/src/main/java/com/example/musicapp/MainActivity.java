package com.example.musicapp;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    protected final HomeFragment homeFragment = new HomeFragment();
    protected final UploadSongFragment uploadSongFragment = new UploadSongFragment();
    protected final SearchFragment searchFragment = new SearchFragment();
    protected final SettingsFragment settingsFragment = new SettingsFragment();
    public final LibraryFragment libraryFragment = new LibraryFragment();

    private View uploadButtonItem;

    protected final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private boolean isArtist;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


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
            DataSingleton.getDataSingleton().setAllSongs(libraryFragment.getAllSongsGetter());
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
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment_container, homeFragment)
                    .commit();
            return true;
        } else if (itemId == R.id.search_button) {
            Log.d("MainActivity", "Search button clicked");
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_fragment_container, searchFragment)
                    .commit();
            return true;
        } else if (itemId == R.id.library_button) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, libraryFragment).commit();
            Log.d("MainActivity", "Library button clicked");
            return true;
        } else if (itemId == R.id.upload_song_button) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment_container, uploadSongFragment).commit();
            Log.d("MainActivity", "Upload song clicked");
            return true;
        }
        return false;
    }

    public void isArtistChange() {
        isArtist = sharedPreferences.getBoolean("isArtist", false);

        if (isArtist) {
            uploadButtonItem.setVisibility(View.VISIBLE);
        } else {
            uploadButtonItem.setVisibility(View.GONE);
        }

    }
}
