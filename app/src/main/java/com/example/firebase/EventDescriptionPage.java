package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EventDescriptionPage extends AppCompatActivity {

    private TextView nameTextView, dateTextView, venueTextView, startTimeTextView, endTimeTextView, categoryTextView, descriptionTextView, ownerTextView, ownerEmailTextView;
    private ImageView eventImageView, ivOwnerIcon;
    private Button joinEventButton, btnBack, btnViewParticipants, btnDeleteEvent;
    private DatabaseReference eventRef;
    private DatabaseReference userRef;
    private FirebaseAuth auth;
    String databaseURL = "https://umuniverse-1d81d-default-rtdb.asia-southeast1.firebasedatabase.app/";

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
        ivOwnerIcon = findViewById(R.id.ivOwnerIcon);
        ownerTextView = findViewById(R.id.ownerTextView);
        ownerEmailTextView = findViewById(R.id.ownerEmailTextView);
        joinEventButton = findViewById(R.id.joinEventButton);
        btnBack = findViewById(R.id.btnBack);
        btnViewParticipants = findViewById(R.id.btnViewParticipants);
        btnDeleteEvent = findViewById(R.id.btnDeleteEvent);

        btnDeleteEvent.setVisibility(View.GONE);


        // Get event ID from the intent
        String eventId = getIntent().getStringExtra("eventId");

        // Validate event ID
        if (eventId == null || eventId.isEmpty()) {
            Toast.makeText(this, "Event ID not provided", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize Firebase Database reference
        eventRef = FirebaseDatabase.getInstance(databaseURL).getReference("Events").child(eventId);
        DatabaseReference userRef = FirebaseDatabase.getInstance(databaseURL).getReference("Users");

        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "What the fuck? How did we even get here?", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity if no user is logged in
            return;
        }

        // Fetch event details from Firebase Realtime Database
        fetchEventDetails();

        if (user != null) {
            String userId = user.getUid();
            userRef.child(userId).child("role").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String role = snapshot.getValue(String.class);
                        if ("admin".equalsIgnoreCase(role)) {
                            btnDeleteEvent.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(EventDescriptionPage.this, "Failed to fetch user role.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Handle Join Event button click
        joinEventButton.setOnClickListener(v -> {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            if (userId != null) {
                joinEvent(userId, eventId);
            } else {
                Toast.makeText(EventDescriptionPage.this, "Please log in to join the event.", Toast.LENGTH_SHORT).show();
            }

        });

        btnBack.setOnClickListener(v -> {
            Intent eventsIntent = new Intent(getApplicationContext(), EventPage.class);
            startActivity(eventsIntent);
        });

        btnViewParticipants.setOnClickListener(v -> {
            Intent participantsIntent = new Intent(getApplicationContext(), ProfileList.class);
            participantsIntent.putExtra("eventId", eventId);
            startActivity(participantsIntent);

        });

        btnDeleteEvent.setOnClickListener(v -> {
            // Show a confirmation dialog
            new AlertDialog.Builder(EventDescriptionPage.this)
                    .setTitle("Delete Event")
                    .setMessage("Are you sure you want to delete this event? This action cannot be undone.")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Disable the delete button to prevent multiple clicks
                        btnDeleteEvent.setEnabled(false);

                        // Call the deleteEvent method
                        deleteEvent(eventId, new DeleteEventCallback() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(EventDescriptionPage.this, "Event successfully deleted.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), EventPage.class);
                                startActivity(intent);
                                finish(); // Close the current activity
                            }

                            @Override
                            public void onFailure(String errorMessage) {
                                Toast.makeText(EventDescriptionPage.this, "Failed to delete event: " + errorMessage, Toast.LENGTH_SHORT).show();
                                btnDeleteEvent.setEnabled(true); // Re-enable the button
                            }
                        });
                    })
                    .setNegativeButton("No", null)
                    .show();
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
                    String userId = snapshot.child("owner").getValue(String.class);

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

                    if (userId != null && !userId.isEmpty()) {
                        DatabaseReference userRef = FirebaseDatabase.getInstance(databaseURL).getReference("Users").child(userId);
                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                if (userSnapshot.exists()) {
                                    // Fetch owner details
                                    String username = userSnapshot.child("username").getValue(String.class);
                                    String email = userSnapshot.child("email").getValue(String.class);
                                    String profilePictureUrl = userSnapshot.child("profilePictureUrl").getValue(String.class);

                                    // Populate UI
                                    ownerTextView.setText(username != null ? username : "Owner Name Not Available");
                                    ownerEmailTextView.setText(email != null ? email : "Owner Email Not Available");

                                    if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
                                        Glide.with(EventDescriptionPage.this).load(profilePictureUrl).into(ivOwnerIcon);
                                    } else {
                                        ivOwnerIcon.setImageResource(R.drawable.logo); // Placeholder image
                                        System.out.println("For some reason, it's not loading the profile image");
                                    }
                                } else {
                                    ownerTextView.setText("Owner Not Found");
                                    ownerEmailTextView.setText("Owner Email Not Found");
                                    ivOwnerIcon.setImageResource(R.drawable.logo); // Placeholder image
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(EventDescriptionPage.this, "Failed to load owner details", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        ownerTextView.setText("Owner ID Not Available");
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

    private void joinEvent(String userId, String eventId) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance(databaseURL).getReference();

        // Update the "joinedUsers" under the specific event
        DatabaseReference eventJoinedUsersRef = databaseRef.child("Events").child(eventId).child("joinedUsers").child(userId);
        eventJoinedUsersRef.setValue(true).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Update the "joinedEvents" under the specific user
                DatabaseReference userJoinedEventsRef = databaseRef.child("Users").child(userId).child("joinedEvents").child(eventId);
                userJoinedEventsRef.setValue(true).addOnCompleteListener(userTask -> {
                    if (userTask.isSuccessful()) {
                        Toast.makeText(EventDescriptionPage.this, "Successfully joined the event!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(EventDescriptionPage.this, "Failed to update user's joined events. Try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(EventDescriptionPage.this, "Failed to update event's joined users. Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void deleteEvent(String eventId, DeleteEventCallback callback) {
        DatabaseReference eventsRef = FirebaseDatabase.getInstance(databaseURL).getReference("Events");
        DatabaseReference usersRef = FirebaseDatabase.getInstance(databaseURL).getReference("Users");

        // Reference to the specific event to delete
        DatabaseReference eventRef = eventsRef.child(eventId);

        // Step 1: Get the list of users who joined the event
        eventRef.child("joinedUsers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Remove references to the event in the Users node
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String userId = userSnapshot.getKey();
                        usersRef.child(userId).child("joinedEvents").child(eventId).removeValue();
                    }
                }

                // Step 2: Delete the event from the Events node
                eventRef.removeValue()
                        .addOnSuccessListener(aVoid -> callback.onSuccess())
                        .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onFailure(error.getMessage());
            }
        });
    }

    public interface DeleteEventCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }
}
