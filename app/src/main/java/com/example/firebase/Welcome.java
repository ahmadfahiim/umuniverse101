package com.example.firebase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Welcome extends AppCompatActivity {

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // Call super first
        EdgeToEdge.enable(this);
        setContentView(R.layout.welcome_page);

        // Initialize FirebaseAuth instance
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        // Check SharedPreferences for Remember Me preference
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        boolean rememberMe = prefs.getBoolean("rememberMe", false);

        // Check if user is logged in and Remember Me is enabled
        if (user != null && rememberMe) {
            // Navigate to Home Page
            Intent intent = new Intent(getApplicationContext(), HomePage.class);
            startActivity(intent);
            finish();
        } else {
            Log.d("WelcomeActivity", "No user is logged in or Remember Me is disabled.");
            // Stay on Welcome Page
            // Optionally, provide a message or handle UI appropriately
        }

        // Declarations of buttons for login and registration
        Button btnLogin = findViewById(R.id.loginbutton);
        Button btnRegister = findViewById(R.id.registerbutton);

        // onClickListener for login button
        btnLogin.setOnClickListener(view -> {
            // Move to the login page
            Intent intent = new Intent(Welcome.this, Login.class);
            startActivity(intent);
        });

        // onClickListener for registration button
        btnRegister.setOnClickListener(view -> {
            // Move to the registration page
            Intent intent = new Intent(Welcome.this, Registration.class);
            startActivity(intent);
        });
    }

}