package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class EventPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_page);

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
                // To do
                return true;
            } else if (itemId == R.id.profile) {
                Intent profileIntent = new Intent(getApplicationContext(), ProfilePage.class);
                startActivity(profileIntent);
                return true;
            }
            return false;

        });
    }
}
