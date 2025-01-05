package com.example.firebase;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ProfileList extends AppCompatActivity {

    private RecyclerView recyclerViewProfiles;
    private ProfileAdapter profileAdapter;
    private List<Profile> profileList;
    private DatabaseReference eventRef, userRef;
    private Button btnBack;
    String databaseURL = "https://umuniverse-1d81d-default-rtdb.asia-southeast1.firebasedatabase.app/";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_list);

        recyclerViewProfiles = findViewById(R.id.recyclerViewProfiles);
        recyclerViewProfiles.setLayoutManager(new LinearLayoutManager(this));
        profileList = new ArrayList<>();
        profileAdapter = new ProfileAdapter(this, profileList);
        recyclerViewProfiles.setAdapter(profileAdapter);

        String eventId = getIntent().getStringExtra("eventId");
        if (eventId != null) {
            eventRef = FirebaseDatabase.getInstance(databaseURL).getReference("Events").child(eventId).child("joinedUsers");
            userRef = FirebaseDatabase.getInstance(databaseURL).getReference("Users");
            fetchParticipants();
        }

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            onBackPressed(); // Go back to the previous activity
            finish();
        });
    }

    private void fetchParticipants() {
        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                profileList.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String userId = userSnapshot.getKey();
                    fetchUserDetails(userId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void fetchUserDetails(String userId) {
        userRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String username = snapshot.child("username").getValue(String.class);
                    String profilePictureUrl = snapshot.child("profilePictureUrl").getValue(String.class);
                    Profile profile = new Profile(userId, username, profilePictureUrl);
                    profileList.add(profile);
                    profileAdapter.notifyDataSetChanged(); // Update RecyclerView
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }
}
