package com.example.musicapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private EditText emailEditText, passwordEditText, confirmPasswordEditText;
    private FirebaseUser currentUser;

    View.OnClickListener registerBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String passwordConfirm = confirmPasswordEditText.getText().toString();

            // Check if passwords match
            if (password.equals(passwordConfirm)){
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
                Toast.makeText(view.getContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
            }



        }
    };

    private void updateUI() {
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

        registerBtn.setOnClickListener(registerBtnListener);

    }
}