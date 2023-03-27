package com.example.musicapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent goToLogin = new Intent(this, LoginActivity.class);
        Button loginBtn = findViewById(R.id.goToLogin);
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

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(goToLogin);
            }
        });
    }

    public void onNotificationButtonClick(View view) {
        // handle notification button click
        System.out.println("Notification button clicked");
    }

    public void onSettingsButtonClick(View view) {
        // handle settings button click
        System.out.println("Settings button clicked");
    }
}
