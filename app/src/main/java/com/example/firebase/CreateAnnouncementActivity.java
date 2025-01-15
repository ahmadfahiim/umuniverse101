package com.example.firebase;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAnnouncementActivity extends AppCompatActivity {

    EditText etTitle, etDescription, etImageUrl;
    Button btnSubmit;
    DatabaseReference announcementsRef;
    String databaseURL = "https://umuniverse-1d81d-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_announcement);

        etTitle = findViewById(R.id.etTitle);
        etDescription = findViewById(R.id.etDescription);
        etImageUrl = findViewById(R.id.etImageUrl);
        btnSubmit = findViewById(R.id.btnSubmit);
        announcementsRef = FirebaseDatabase.getInstance(databaseURL).getReference("Announcements");

        btnSubmit.setOnClickListener(v -> submitAnnouncement());
    }

    private void submitAnnouncement() {
        String title = etTitle.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String imageUrl = etImageUrl.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty() || imageUrl.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = announcementsRef.push().getKey();
        Announcement announcement = new Announcement(id, imageUrl, title, description);

        announcementsRef.child(id).setValue(announcement)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Announcement created", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to create announcement", Toast.LENGTH_SHORT).show());
    }
}
