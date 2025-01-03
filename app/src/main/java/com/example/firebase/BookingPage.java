package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BookingPage extends AppCompatActivity {

    private EditText etEventName;
    private Button btnBookEvent;
    private DatabaseReference databaseReference;
    private BottomNavigationView bottomNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.booking_page);

        etEventName = findViewById(R.id.etEventName);
        btnBookEvent = findViewById(R.id.btnBookEvent);
        bottomNavigationBar = findViewById(R.id.bottomNavigationBar);

        databaseReference = FirebaseDatabase.getInstance().getReference("Bookings");

        btnBookEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventName = etEventName.getText().toString().trim();
                if (!eventName.isEmpty()) {
                    String bookingId = databaseReference.push().getKey();
                    databaseReference.child(bookingId).setValue(eventName);
                    Toast.makeText(BookingPage.this, "Event Booked Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(BookingPage.this, "Please enter an event name", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
}
