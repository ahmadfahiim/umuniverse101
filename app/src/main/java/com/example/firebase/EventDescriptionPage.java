package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EventDescriptionPage extends AppCompatActivity {

    private TextView nameTextView, dateTextView, venueTextView, startTimeTextView, endTimeTextView, categoryTextView, descriptionTextView;
    private ImageView eventImageView;
    private Button joinEventButton ;
    private DatabaseReference eventRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_description_page);

        // Initialize UI components
        nameTextView = findViewById(R.id.eventNameTextView);
        dateTextView = findViewById(R.id.eventDateTextView);
        venueTextView = findViewById(R.id.eventVenueTextView);
        startTimeTextView = findViewById(R.id.eventStartTimeTextView);
        endTimeTextView = findViewById(R.id.eventEndTimeTextView);
        categoryTextView = findViewById(R.id.eventCategoryTextView);
        descriptionTextView = findViewById(R.id.eventDescriptionTextView);
        eventImageView = findViewById(R.id.eventImageView);
        joinEventButton = findViewById(R.id.joinEventButton);

        // Get event ID from the intent
        String eventId = getIntent().getStringExtra("eventId");

        // Validate event ID
        if (eventId == null || eventId.isEmpty()) {
            Toast.makeText(this, "Event ID not provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize Firebase Database reference
        eventRef = FirebaseDatabase.getInstance().getReference("events").child(eventId);

        // Fetch event details from Firebase Realtime Database
        fetchEventDetails();

        // Handle Join Event button click
        joinEventButton.setOnClickListener(v -> {
            Intent eventPageIntent = new Intent(EventDescriptionPage.this, EventPage.class);
            eventPageIntent.putExtra("eventId", eventId); // Pass the event ID if needed
            startActivity(eventPageIntent);
        });
    }

    private void fetchEventDetails() {
        eventRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Parse event details
                    String name = snapshot.child("name").getValue(String.class);
                    String date = snapshot.child("date").getValue(String.class);
                    String venue = snapshot.child("location").getValue(String.class);
                    String startTime = snapshot.child("startTime").getValue(String.class);
                    String endTime = snapshot.child("endTime").getValue(String.class);
                    String category = snapshot.child("category").getValue(String.class);
                    String description = snapshot.child("description").getValue(String.class);
                    String photoUrl = snapshot.child("photoUrl").getValue(String.class);

                    // Populate the UI with fetched data
                    nameTextView.setText(name != null ? name : "Event Name Not Available");
                    dateTextView.setText(date != null ? date : "Date Not Available");
                    venueTextView.setText(venue != null ? venue : "Venue Not Available");
                    startTimeTextView.setText(startTime != null ? startTime : "Start Time Not Available");
                    endTimeTextView.setText(endTime != null ? endTime : "End Time Not Available");
                    categoryTextView.setText(category != null ? category : "Category Not Available");
                    descriptionTextView.setText(description != null ? description : "Description Not Available");

                    // Load event image or set placeholder
                    if (photoUrl != null && !photoUrl.isEmpty()) {
                        Glide.with(EventDescriptionPage.this).load(photoUrl).into(eventImageView);
                    } else {
                        eventImageView.setImageResource(R.drawable.home_4_svgrepo_com);
                    }
                } else {
                    Toast.makeText(EventDescriptionPage.this, "Event not found", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EventDescriptionPage.this, "Failed to load event details", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
