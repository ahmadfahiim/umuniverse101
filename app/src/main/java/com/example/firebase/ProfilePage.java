package com.example.firebase;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.net.URL;

public class ProfilePage extends AppCompatActivity {

    private TextView profileName, eventCount, personalBio, personalFaculty;
    private ImageView edit_icon;
    private View blurBackground;
    private CardView bioCard, facultyCard;
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

        auth = FirebaseAuth.getInstance();

        // Get current user
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid(); // Get dynamically assigned userId
            userRef = FirebaseDatabase.getInstance(databaseURL).getReference("Users").child(userId);
            System.out.println("The logged in user for the profile page is: " + userId);
            fetchData();
        } else {
            System.err.println("How the fuck did we reach here? This shouldn't be possible.");
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

            System.out.println("The edit profile fragment should be up.");
        });




        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationBar);
        bottomNavigationView.setSelectedItemId(R.id.profile);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                Intent homeIntent = new Intent(getApplicationContext(), HomePage.class);
                startActivity(homeIntent);
                return true;
            } else if (itemId == R.id.events) {
                Intent eventsIntent = new Intent(getApplicationContext(), EventPage.class);
                startActivity(eventsIntent);
                return true;
            } else if (itemId == R.id.bookings) {// To do
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
                    String eventsJoined = eventsJoinedLong != null ? String.valueOf(eventsJoinedLong) : "0";

                    System.out.println(name + bio);

                    profileName.setText(name);
                    eventCount.setText(eventsJoined);
                    personalBio.setText(bio);
                    personalFaculty.setText(faculty);

                    // Find a way to get the image from the database and load it.
                } else {
                    System.err.println("Something went wrong when fetching data for profile.");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onFragmentDismissed() {
        System.out.println("This is the fragment dismiss in the profile activity.");
        FrameLayout fragmentContainer = findViewById(R.id.fragment_container);
        View blurBackground = findViewById(R.id.blur_background);
        View bioCard = findViewById(R.id.bioCard);
        View facultyCard = findViewById(R.id.facultyCard);

        fragmentContainer.setVisibility(View.GONE);
        blurBackground.setVisibility(View.GONE);
        bioCard.setVisibility(View.VISIBLE);
        facultyCard.setVisibility(View.VISIBLE);

        System.out.println("The UI has been reset after fragment dismissal.");
    }
}
