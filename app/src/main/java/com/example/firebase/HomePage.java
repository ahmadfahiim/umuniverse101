package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomePage extends AppCompatActivity {

    FirebaseAuth auth;
    Button logoutBtn;
    TextView textView;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference ref;
    ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        // Initialize Firebase and UI components
        auth = FirebaseAuth.getInstance();
        logoutBtn = findViewById(R.id.logout);
        textView = findViewById(R.id.user_email);
        profileImageView = findViewById(R.id.profilePic); // Profile picture ImageView

        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Users");

        // Check if user is logged in
        if (user == null) {
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        } else {
            textView.setText(user.getEmail());
            loadProfilePicture(user.getUid());
        }

        // Logout functionality
        logoutBtn.setOnClickListener(v -> {
            auth.signOut();
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        });

        // Bottom Navigation Setup
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
                startActivity(new Intent(getApplicationContext(), BookingPage.class));
                return true;
            } else if (itemId == R.id.profile) {
                startActivity(new Intent(getApplicationContext(), ProfilePage.class));
                return true;
            }
            return false;
        });
    }

    // Method to load profile picture from Firebase
    private void loadProfilePicture(String userId) {
        DatabaseReference userRef = ref.child(userId).child("profilePictureUrl");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String profilePicUrl = snapshot.getValue(String.class);
                if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
                    Glide.with(HomePage.this)
                            .load(profilePicUrl)
                            .placeholder(R.drawable.logo) // Optional placeholder image
                            .into(profileImageView);
                } else {
                    profileImageView.setImageResource(R.drawable.logo); // Default image
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.err.println("Failed to load profile picture: " + error.getMessage());
            }
        });
    }
}
