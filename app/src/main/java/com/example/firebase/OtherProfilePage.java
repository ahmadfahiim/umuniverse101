package com.example.firebase;

import android.os.Bundle;
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
    private ImageView profile_picture;
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
                    String username = snapshot.child("username").getValue(String.class);
                    String profilePictureUrl = snapshot.child("profilePictureUrl").getValue(String.class);
                    String bio = snapshot.child("bio").getValue(String.class);
                    String faculty = snapshot.child("faculty").getValue(String.class);

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

                    if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
                        Glide.with(getApplicationContext()).load(profilePictureUrl).into(profile_picture);
                    } else {
                        profile_picture.setImageResource(R.drawable.logo);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }


}