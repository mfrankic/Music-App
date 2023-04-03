package com.example.musicapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

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


        /*test
        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

        // Add a new document with a generated ID
        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("DB", "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("DB", "Error adding document", e);
                    }
                });
         */
    }

    private void updateUI() {
        finish();
        startActivity(getIntent());
    }

}
