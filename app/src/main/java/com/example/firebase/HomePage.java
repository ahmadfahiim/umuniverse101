package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomePage extends AppCompatActivity {

    FirebaseAuth auth;
    Button logoutBtn;
    TextView textView;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        // Initialize Firebase and UI components
        auth = FirebaseAuth.getInstance();
        logoutBtn = findViewById(R.id.logout);
        textView = findViewById(R.id.user_email); // The TextView for displaying the username
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Users"); // Path to the "Users" node in Firebase

        // Check if the user is logged in
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            textView.setText(user.getEmail());
        }

        // Logout button functionality
        logoutBtn.setOnClickListener(v -> {
            auth.signOut();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        });

        // Bottom navigation functionality
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationBar);
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                return true;
            } else if (itemId == R.id.events) {
                startActivity(new Intent(getApplicationContext(), EventPage.class));
                return true;
            } else if (itemId == R.id.bookings) {
                Intent eventsIntent = new Intent(getApplicationContext(), BookingPage.class);
                startActivity(eventsIntent);
                return true;
            } else if (itemId == R.id.profile) {
                startActivity(new Intent(getApplicationContext(), ProfilePage.class));
                return true;
            }
            return false;
        });
    }
}
