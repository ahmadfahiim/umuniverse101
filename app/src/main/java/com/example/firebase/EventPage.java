package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EventPage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventList;
    private Button sortByRelevanceButton;
    private ImageView createEventButton;
    private DatabaseReference databaseReference;

    public String databaseURL = "https://umuniverse-1d81d-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_page);

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance(databaseURL).getReference("Events");

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        eventList = new ArrayList<>();
        eventAdapter = new EventAdapter(eventList, event -> {
            Intent intent = new Intent(this, EventDescriptionPage.class);
            intent.putExtra("eventId", event.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(eventAdapter);

        sortByRelevanceButton = findViewById(R.id.sortByRelevanceButton);
        sortByRelevanceButton.setOnClickListener(v -> {
            sortEventsByRelevance();
            Toast.makeText(this, "Events sorted by relevance", Toast.LENGTH_SHORT).show();
        });

        fetchEventsFromFirebase();

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterEvents(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterEvents(newText);
                return true;
            }
        });

        setupFilterButtons();

        createEventButton = findViewById(R.id.createEvent);
        createEventButton.setOnClickListener(v -> {
            Intent intent = new Intent(EventPage.this, CreateEventActivity.class);
            startActivity(intent);
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationBar);
        bottomNavigationView.setSelectedItemId(R.id.events);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                startActivity(new Intent(getApplicationContext(), HomePage.class));
            } else if (itemId == R.id.events) {
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
                Toast.makeText(EventPage.this, "Failed to load events", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterEvents(String query) {
        List<Event> filteredList = new ArrayList<>();
        for (Event event : eventList) {
            if (event.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(event);
            }
        }
        eventAdapter.filterList(filteredList);
    }

    private void setupFilterButtons() {
        Button allButton = findViewById(R.id.allButton);
        Button musicButton = findViewById(R.id.musicButton);
        Button sportsButton = findViewById(R.id.sportsButton);
        Button techButton = findViewById(R.id.techButton);
        Button eduButton = findViewById(R.id.eduButton);
        Button socButton = findViewById(R.id.socButton);

        allButton.setOnClickListener(v -> eventAdapter.filterList(eventList));

        musicButton.setOnClickListener(v -> filterByCategory("Music"));
        sportsButton.setOnClickListener(v -> filterByCategory("Sports"));
        techButton.setOnClickListener(v -> filterByCategory("Technology"));
        eduButton.setOnClickListener(v -> filterByCategory("Education"));
        socButton.setOnClickListener(v -> filterByCategory("Social"));
    }

    private void filterByCategory(String category) {
        List<Event> filteredList = new ArrayList<>();
        for (Event event : eventList) {
            if (event.getCategory().equalsIgnoreCase(category)) {
                filteredList.add(event);
            }
        }
        eventAdapter.filterList(filteredList);
    }

    private void sortEventsByRelevance() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        Collections.sort(eventList, (event1, event2) -> {
            try {
                Date date1 = dateFormat.parse(event1.getDate());
                Date date2 = dateFormat.parse(event2.getDate());

                int dateComparison = date1.compareTo(date2);
                if (dateComparison != 0) {
                    return dateComparison;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            int startTimeComparison = event1.getStartTime().compareTo(event2.getStartTime());
            if (startTimeComparison != 0) {
                return startTimeComparison;
            }

            return event1.getLocation().compareTo(event2.getLocation());
        });

        eventAdapter.notifyDataSetChanged();
    }
}
