package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfilePage extends AppCompatActivity {

    private TextView profileName, eventCount, personalBio, personalFaculty;
    private ImageView edit_icon, profileImage;
    private View blurBackground;
    private FrameLayout bioCard, facultyCard;
    private DatabaseReference userRef;
    private FirebaseAuth auth;
    String databaseURL = "https://umuniverse-1d81d-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);

        profileName = findViewById(R.id.profileName);
        eventCount = findViewById(R.id.event_count);
        personalBio = findViewById(R.id.personal_bio);
        personalFaculty = findViewById(R.id.personal_faculty);
        bioCard = findViewById(R.id.bioCard);
        facultyCard = findViewById(R.id.facultyCard);
        edit_icon = findViewById(R.id.edit_icon);
        blurBackground = findViewById(R.id.blur_background);
        profileImage = findViewById(R.id.profile_picture); // New ImageView for profile picture

        auth = FirebaseAuth.getInstance();

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            userRef = FirebaseDatabase.getInstance(databaseURL).getReference("Users").child(userId);
            fetchData();
        }

        edit_icon.setOnClickListener(v -> {
            FrameLayout fragmentContainer = findViewById(R.id.fragment_container);
            fragmentContainer.setVisibility(View.VISIBLE);
            blurBackground.setVisibility(View.VISIBLE);
            bioCard.setVisibility(View.INVISIBLE);
            facultyCard.setVisibility(View.INVISIBLE);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            EditProfileFragment editProfileFragment = new EditProfileFragment();
            fragmentTransaction.replace(R.id.fragment_container, editProfileFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationBar);
        bottomNavigationView.setSelectedItemId(R.id.profile);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                startActivity(new Intent(getApplicationContext(), HomePage.class));
                return true;
            } else if (itemId == R.id.events) {
                startActivity(new Intent(getApplicationContext(), EventPage.class));
                return true;
            } else if (itemId == R.id.bookings) {
                startActivity(new Intent(getApplicationContext(), BookingPage.class));
                return true;
            } else if (itemId == R.id.profile) {
                return true;
            }
            return false;
        });
    }

    public void fetchData() {
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("username").getValue(String.class);
                    Long eventsJoinedLong = snapshot.child("eventsJoined").getValue(Long.class);
                    String bio = snapshot.child("bio").getValue(String.class);
                    String faculty = snapshot.child("faculty").getValue(String.class);
                    String profilePicUrl = snapshot.child("profilePictureUrl").getValue(String.class);
                    String eventsJoined = eventsJoinedLong != null ? String.valueOf(eventsJoinedLong) : "0";

                    profileName.setText(name);
                    eventCount.setText(eventsJoined);
                    personalBio.setText(bio);
                    personalFaculty.setText(faculty);

                    // Load profile picture using Glide if the URL is valid
                    if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
                        Glide.with(ProfilePage.this)
                                .load(profilePicUrl)
                                .placeholder(R.drawable.logo) // Optional: default image
                                .into(profileImage);
                    }
                } else {
                    System.err.println("User data not found.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.err.println("Error fetching data: " + error.getMessage());
            }
        });
    }

    public void onFragmentDismissed() {
        FrameLayout fragmentContainer = findViewById(R.id.fragment_container);
        View blurBackground = findViewById(R.id.blur_background);
        View bioCard = findViewById(R.id.bioCard);
        View facultyCard = findViewById(R.id.facultyCard);

        fragmentContainer.setVisibility(View.GONE);
        blurBackground.setVisibility(View.GONE);
        bioCard.setVisibility(View.VISIBLE);
        facultyCard.setVisibility(View.VISIBLE);

        // Refresh data after editing profile
        fetchData();
    }
}
