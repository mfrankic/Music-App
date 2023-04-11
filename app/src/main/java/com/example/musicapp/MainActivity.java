package com.example.musicapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    protected final HomeFragment homeFragment = new HomeFragment();
    protected final SettingsFragment settingsFragment = new SettingsFragment();

    protected final FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
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
            return true;
        } else if (itemId == R.id.library_button) {
            Log.d("MainActivity", "Library button clicked");
            return true;
        }
        return false;
    }

}
