package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class EventPage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;
    private List<Event> eventList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_page);

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

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Populate Event List
        eventList = new ArrayList<>();
        eventList.add(new Event("Football Game", "Sonic Stadium", "~7 KM", "24-Oct-2021", R.drawable.home_4_svgrepo_com, true));
        eventList.add(new Event("Table Tennis Game", "YMCA Auditorium", "5 KM", "29-Oct-2021", R.drawable.ic_android_black_24dp, true));

        // Initialize Adapter
        eventAdapter = new EventAdapter(eventList, event -> {
            // Handle Clicks on Events
            Toast.makeText(this, "Clicked: " + event.getTitle(), Toast.LENGTH_SHORT).show();
        });

        recyclerView.setAdapter(eventAdapter);
    }
}
