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
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    Button registerActivityBtn;
    Intent goToRegisterActivity;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;
    private EditText emailEditText, passwordEditText;


    View.OnClickListener loginBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            auth.signInWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                    .addOnCompleteListener(LoginActivity.this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("LOGIN", "signInWithEmail:success");
                            currentUser = auth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Logged in!",
                                    Toast.LENGTH_SHORT).show();
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("LOGIN", "signInWithEmail:failure", task.getException());
                            String errorMessage = task.getException().toString();
                            String[] lines = errorMessage.split("\n");
                            String firstLine = lines[0];
                            String[] parts = firstLine.split(":");
                            String error = parts.length > 1 ? parts[1].trim() : firstLine;

                            Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();

                            //dupdateUI();
                        }
                    });
        }
    };

    private void updateUI() {
        finish();
        startActivity(getIntent());
    }

    View.OnClickListener registerBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(goToRegisterActivity);
            // Just so that back from register activity works :)
            //finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        registerActivityBtn = findViewById(R.id.registerActivityButton);
        goToRegisterActivity = new Intent(this, RegisterActivity.class);
        registerActivityBtn.setOnClickListener(registerBtnListener);
        auth = FirebaseAuth.getInstance();
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        Button loginBtn = findViewById(R.id.loginButton);

        loginBtn.setOnClickListener(loginBtnListener);

    }
}