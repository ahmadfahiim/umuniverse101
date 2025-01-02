package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.*;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class EventPage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventList;
    private ImageView createEventButton;
    private DatabaseReference databaseReference;

    String databaseURL = "https://umuniverse-1d81d-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_page);

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance(databaseURL).getReference("Events");

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(eventList, event -> {
            // Handle Clicks on Events
            Toast.makeText(this, "Clicked: " + event.getTitle(), Toast.LENGTH_SHORT).show();
        });
        recyclerView.setAdapter(eventAdapter);

        // Fetch Events from Firebase
        fetchEventsFromFirebase();

        createEventButton = findViewById(R.id.createEvent);
        createEventButton.setOnClickListener(v -> {
            Intent createEventIntent = new Intent(EventPage.this, CreateEventActivity.class);
            startActivity(createEventIntent);
        });

        // Initialize Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationBar);
        bottomNavigationView.setSelectedItemId(R.id.events);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                Intent homeIntent = new Intent(getApplicationContext(), HomePage.class);
                startActivity(homeIntent);
            } else if (itemId == R.id.events) {
                return true;
            } else if (itemId == R.id.bookings) {
                Toast.makeText(this, "Bookings Feature Coming Soon!", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.profile) {
                Intent profileIntent = new Intent(getApplicationContext(), ProfilePage.class);
                startActivity(profileIntent);
                return true;
            }
            return false;
        });
    }

    private void fetchEventsFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventList.clear(); // Clear current list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Event event = snapshot.getValue(Event.class);
                    eventList.add(event);
                }
                eventAdapter.notifyDataSetChanged(); // Update adapter
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(EventPage.this, "Failed to load events", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
