package com.example.musicapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private EditText emailEditText, passwordEditText, confirmPasswordEditText, nameEditText, bioEditText;
    private CheckBox artistCheckBox;
    private FirebaseUser currentUser;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    // Get a reference to the collection you want to add the document to
    CollectionReference collectionRef = db.collection("users");
    View.OnClickListener registerBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String passwordConfirm = confirmPasswordEditText.getText().toString();

            // Check if passwords match and fields are not empty
            if (password.equals(passwordConfirm) && !email.isEmpty() && !password.isEmpty()){
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegisterActivity.this, task -> {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("LOGIN", "createUserWithEmail:success");
                                currentUser = auth.getCurrentUser();
                                Toast.makeText(RegisterActivity.this, "Account created", Toast.LENGTH_LONG).show();
                                updateUI();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("LOGIN", "createUserWithEmail:failure", task.getException());

                                String errorMessage = task.getException().toString();
                                String[] lines = errorMessage.split("\n");
                                String firstLine = lines[0];
                                String[] parts = firstLine.split(":");
                                String error = parts.length > 1 ? parts[1].trim() : firstLine;

                                Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                                //Log.d("LOGINexep", task.getException().toString());
                                //updateUI();
                            }
                        });
            }
            // If they dont match
            else{
                Toast.makeText(view.getContext(), "Passwords don't match or fields are empty", Toast.LENGTH_SHORT).show();
            }



        }
    };

    private void updateUI() {
        // Create a new document with a custom ID
        String customId = currentUser.getUid().toString();
        DocumentReference documentRef = collectionRef.document(customId);

        // Add entry to user colletcion
        Map<String, Object> user = new HashMap<>();
        user.put("name", nameEditText.getText().toString());
        user.put("bio", bioEditText.getText().toString());
        user.put("isArtist", artistCheckBox.isChecked());

        documentRef.set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Register", "Document added with ID: " + customId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Register", "Error adding document", e);
                    }
                });

        finish();
        startActivity(getIntent());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        auth = FirebaseAuth.getInstance();
        Button registerBtn = findViewById(R.id.registerButton);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        nameEditText = findViewById(R.id.nameEditText);
        bioEditText = findViewById(R.id.bioEditText);
        artistCheckBox = findViewById(R.id.artistCheckBox);

        registerBtn.setOnClickListener(registerBtnListener);

    }
}