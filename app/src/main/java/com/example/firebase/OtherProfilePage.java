package com.example.firebase;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class OtherProfilePage extends AppCompatActivity {

    String databaseURL = "https://umuniverse-1d81d-default-rtdb.asia-southeast1.firebasedatabase.app/";
    private ImageView profile_picture, adminIcon;
    private TextView profileName, event_count, personal_bio, personal_faculty;
    private Button btnBack;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.other_profile_page);

        profile_picture = findViewById(R.id.profile_picture);
        profileName = findViewById(R.id.profileName);
        event_count = findViewById(R.id.event_count);
        personal_bio = findViewById(R.id.personal_bio);
        personal_faculty = findViewById(R.id.personal_faculty);
        adminIcon = findViewById(R.id.adminIcon);

        String userId = getIntent().getStringExtra("userId");
        if (userId != null) {
            userRef = FirebaseDatabase.getInstance(databaseURL).getReference("Users").child(userId);
            loadUserProfile(userId);
        }

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });

    }


    private void loadUserProfile(String userId) {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Check if the profile is hidden
                    Boolean hideProfile = snapshot.child("hideProfile").getValue(Boolean.class);
                    if (Boolean.TRUE.equals(hideProfile)) {
                        // Profile is hidden - display a placeholder or message
                        profileName.setText("This profile is hidden");
                        personal_bio.setText("The user has hidden their profile.");
                        personal_faculty.setText("The user has hidden their profile");
                        event_count.setText("-");
                        profile_picture.setImageResource(R.drawable.logo); // Default profile picture
                        return; // Stop further processing
                    }

                    // Load and display user profile data
                    String username = snapshot.child("username").getValue(String.class);
                    String profilePictureUrl = snapshot.child("profilePictureUrl").getValue(String.class);
                    String bio = snapshot.child("bio").getValue(String.class);
                    String faculty = snapshot.child("faculty").getValue(String.class);

                    String role = snapshot.child("role").getValue(String.class);

                    // Count the number of events in joinedEvents
                    DataSnapshot joinedEventsSnapshot = snapshot.child("joinedEvents");
                    int eventsCount = 0;
                    if (joinedEventsSnapshot.exists()) {
                        eventsCount = (int) joinedEventsSnapshot.getChildrenCount();
                    }

                    profileName.setText(username != null ? username : "Username Not Available");
                    personal_bio.setText(bio != null ? bio : "Bio Not Available");
                    personal_faculty.setText(faculty != null ? faculty : "Faculty Not Available");
                    event_count.setText(String.valueOf(eventsCount));

                    // Display or hide the admin icon based on the role
                    if ("admin".equalsIgnoreCase(role)) {
                        adminIcon.setVisibility(View.VISIBLE);
                    } else {
                        adminIcon.setVisibility(View.GONE);
                    }

                    if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
                        Glide.with(getApplicationContext()).load(profilePictureUrl).into(profile_picture);
                    } else {
                        profile_picture.setImageResource(R.drawable.logo);
                    }
                } else {
                    // Handle case where user data does not exist
                    profileName.setText("Profile not found");
                    personal_bio.setText("");
                    personal_faculty.setText("");
                    event_count.setText("");
                    profile_picture.setImageResource(R.drawable.logo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }


}