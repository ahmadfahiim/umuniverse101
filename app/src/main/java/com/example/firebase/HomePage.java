package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomePage extends AppCompatActivity {

    FirebaseAuth auth;
    RecyclerView recyclerViewAnnouncements;
    Button logoutBtn, btnAddAnnouncement;
    AnnouncementAdapter announcementAdapter;
    TextView tvUsername, tvGreeting;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference ref, announcementsRef, userRef;
    ImageView profileImageView;
    List<Announcement> announcementList = new ArrayList<>();
    String databaseURL = "https://umuniverse-1d81d-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        btnAddAnnouncement = findViewById(R.id.btnAddAnnouncement);
        recyclerViewAnnouncements = findViewById(R.id.recyclerViewAnnouncements);
        recyclerViewAnnouncements.setLayoutManager(new LinearLayoutManager(this));

        // Initialize Firebase and UI components
        auth = FirebaseAuth.getInstance();
        logoutBtn = findViewById(R.id.logout);
        tvUsername = findViewById(R.id.tvUsername);
        tvGreeting = findViewById(R.id.tvGreeting);
        profileImageView = findViewById(R.id.profilePic); // Profile picture ImageView

        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance(databaseURL);
        ref = database.getReference("Users");
        userRef = FirebaseDatabase.getInstance(databaseURL).getReference("Users").child(user.getUid());
        announcementsRef = FirebaseDatabase.getInstance(databaseURL).getReference("Announcements");

        // Check if user is logged in
        if (user == null) {
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        } else {
            loadProfilePictureandUsername(user.getUid());
        }

        announcementAdapter = new AnnouncementAdapter(this, announcementList);
        recyclerViewAnnouncements.setAdapter(announcementAdapter);

        checkAdminRole();
        loadAnnouncements();

        btnAddAnnouncement.setOnClickListener(v -> {
            startActivity(new Intent(HomePage.this, CreateAnnouncementActivity.class));
        });

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

    private void checkAdminRole() {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String role = snapshot.child("role").getValue(String.class);

                    // Check if role exists and is "admin"
                    if ("admin".equals(role)) {
                        btnAddAnnouncement.setVisibility(View.VISIBLE);
                    } else {
                        btnAddAnnouncement.setVisibility(View.GONE);
                    }
                } else {
                    // Handle case where user node does not exist
                    Log.d("AdminCheck", "User snapshot does not exist.");
                    btnAddAnnouncement.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("AdminCheck", "Error: " + error.getMessage());
                btnAddAnnouncement.setVisibility(View.GONE);
            }
        });
    }

    private void loadAnnouncements() {
        announcementsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                announcementList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Announcement announcement = data.getValue(Announcement.class);
                    announcementList.add(announcement);
                }
                announcementAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomePage.this, "Failed to load announcements", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to load profile picture from Firebase
    private void loadProfilePictureandUsername(String userId) {
        DatabaseReference userRef = ref.child(userId); // Reference to the user's data
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Fetch the profile picture URL
                    String profilePicUrl = snapshot.child("profilePictureUrl").getValue(String.class);
                    if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
                        Glide.with(HomePage.this)
                                .load(profilePicUrl)
                                .placeholder(R.drawable.logo) // Optional placeholder image
                                .into(profileImageView);
                    } else {
                        profileImageView.setImageResource(R.drawable.logo); // Default image
                    }

                    // Fetch the username
                    String username = snapshot.child("username").getValue(String.class);
                    updateGreetingMessage(username);
                    if (username != null && !username.isEmpty()) {
                        tvUsername.setText(username); // Assuming you have a TextView for the username
                    } else {
                        tvUsername.setText("Username Not Available");
                    }
                } else {
                    // Handle the case where the snapshot does not exist
                    profileImageView.setImageResource(R.drawable.logo); // Default image
                    tvUsername.setText("User Not Found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.err.println("Failed to load user data: " + error.getMessage());
            }
        });
    }

    private void updateGreetingMessage(String username) {
        // Get the current hour
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        // Determine the time of day
        String greeting;
        if (hour >= 5 && hour < 12) {
            greeting = "Good morning, ";
        } else if (hour >= 12 && hour < 17) {
            greeting = "Good afternoon, ";
        } else if (hour >= 17 && hour < 21) {
            greeting = "Good evening, ";
        } else {
            greeting = "Good night, ";
        }

        // Update the TextView
        tvGreeting.setText(greeting);
    }
}
