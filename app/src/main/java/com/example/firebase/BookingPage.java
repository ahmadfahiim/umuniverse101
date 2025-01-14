package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;


import java.util.ArrayList;
import java.util.List;

public class BookingPage extends AppCompatActivity {

    private EditText etEventName;
    private Button btnBookEvent;
    private DatabaseReference databaseReference;
    private BottomNavigationView bottomNavigationBar;
    private RecyclerView recyclerView;
    private List<Event> eventList;
    private EventAdapter eventAdapter;
    private FirebaseAuth auth;
    String databaseURL = "https://umuniverse-1d81d-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        setContentView(R.layout.booking_page);

        bottomNavigationBar = findViewById(R.id.bottomNavigationBar);

        databaseReference = FirebaseDatabase.getInstance(databaseURL).getReference("Events");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        eventList = new ArrayList<>();
        // Initialize the EventAdapter with a click listener
        eventAdapter = new EventAdapter(eventList, event -> {
            // Create an intent to navigate to BookingDescriptionPage
            Intent intent = new Intent(this, BookingDescriptionPage.class);
            // Pass the event ID to the BookingDescriptionPage
            intent.putExtra("eventId", event.getId());
            startActivity(intent);
        });

        recyclerView.setAdapter(eventAdapter);

        String userId = auth.getUid();

        System.out.println(userId);

        fetchRegisteredEvents(userId);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationBar);
        bottomNavigationView.setSelectedItemId(R.id.bookings);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                Intent homeIntent = new Intent(getApplicationContext(), HomePage.class);
                startActivity(homeIntent);
            } else if (itemId == R.id.events) {
                Intent eventsIntent = new Intent(getApplicationContext(), EventPage.class);
                startActivity(eventsIntent);
                return true;
            } else if (itemId == R.id.bookings) {
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
                eventList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Event event = snapshot.getValue(Event.class);
                    eventList.add(event);
                }
                eventAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Failed to load events", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchRegisteredEvents(String userId) {
        // Fetch the joinedEvents node for the user
        DatabaseReference userRef = FirebaseDatabase.getInstance(databaseURL).getReference("Users").child(userId).child("joinedEvents");
        DatabaseReference eventsRef = FirebaseDatabase.getInstance(databaseURL).getReference("Events");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    eventList.clear(); // Clear the existing event list
                    for (DataSnapshot eventSnapshot : snapshot.getChildren()) {
                        String eventId = eventSnapshot.getKey(); // Get the event ID

                        // Fetch the specific event details from the events node
                        eventsRef.child(eventId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot eventDetailsSnapshot) {
                                Event event = eventDetailsSnapshot.getValue(Event.class);
                                if (event != null) {
                                    eventList.add(event); // Add the event to the list
                                    eventAdapter.notifyDataSetChanged(); // Notify the adapter of changes
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getApplicationContext(), "Failed to load event details", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No registered events found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
