package com.example.musicapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

        Button logoutBtn = findViewById(R.id.logout);
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

        TextView greetingTextView = findViewById(R.id.greeting_textview);
        greetingTextView.setText(greeting);

        ImageView notificationButton = findViewById(R.id.notification_button);
        notificationButton.setOnClickListener(v -> {
            // handle notification button click
            System.out.println("Notification button clicked");
        });

        ImageView settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(v -> {
            // handle settings button click
            System.out.println("Settings button clicked");
        });

        logoutBtn.setOnClickListener((view) -> {
            auth.signOut();
            updateUI();
        });
    }

    private void updateUI() {
        finish();
        startActivity(getIntent());
    }

}
