package com.example.musicapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    MaterialButton registerActivityBtn;
    Intent goToRegisterActivity;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;
    private TextInputEditText emailEditText, passwordEditText;

    View.OnClickListener loginBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String email = Objects.requireNonNull(emailEditText.getText()).toString();
            String password = Objects.requireNonNull(passwordEditText.getText()).toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("LOGIN", "signInWithEmail:success");
                            currentUser = auth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Logged in!",
                                    Toast.LENGTH_SHORT).show();
                            recreate();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("LOGIN", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    };

    View.OnClickListener registerBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(goToRegisterActivity);
            finish();
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
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        MaterialButton loginBtn = findViewById(R.id.loginButton);

        loginBtn.setOnClickListener(loginBtnListener);

    }
}