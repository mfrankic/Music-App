package com.example.musicapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingsFragment extends Fragment {

    public TextView name, email, bio;
    public SwitchMaterial artistSwitch;
    protected FirebaseAuth auth;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainActivity activity = (MainActivity) getActivity();
        assert activity != null;

        MaterialToolbar toolbar = view.findViewById(R.id.top_bar);
        toolbar.setNavigationOnClickListener(v -> activity.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_fragment_container, activity.homeFragment)
                .commit());

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(activity.getApplicationContext());
        SwitchMaterial themeSwitch = view.findViewById(R.id.switch_theme);
        themeSwitch.setChecked(sharedPreferences.getBoolean("dark_mode_enabled", false));

        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("dark_mode_enabled", isChecked);
            editor.apply();
            activity.onSaveInstanceState(new Bundle());
            activity.recreate();
        });


        view.findViewById(R.id.logout_button).setOnClickListener(v -> {
            activity.auth.signOut();
            activity.recreate();
        });

        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        DocumentReference docRef = db.collection("users").document(currentUser.getUid());

        name = view.findViewById(R.id.settings_text_name);
        email = view.findViewById(R.id.settings_label_email);
        bio = view.findViewById(R.id.settings_text_bio);
        artistSwitch = view.findViewById(R.id.switch_artist);

        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d("SETTINGS", "DocumentSnapshot data: " + document.get("name"));
                    name.setText(String.valueOf(document.get("name")));
                    bio.setText(String.valueOf(document.get("bio")));
                    artistSwitch.setChecked(document.get("isArtist").equals(true));

                } else {
                    Log.d("SETTINGS", "No such document");
                }
            } else {
                Log.d("SETTINGS", "get failed with ", task.getException());
            }
        });

        email.setText(currentUser.getEmail());


    }
}