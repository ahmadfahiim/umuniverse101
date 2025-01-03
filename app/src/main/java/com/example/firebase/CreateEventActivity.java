package com.example.firebase;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.HashMap;
import android.Manifest;


public class CreateEventActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_PERMISSIONS = 100;

    private EditText etEventName, etEventDate, etEventStartTime, etEventEndTime, etEventLocation;
    private Spinner spinnerCategory;
    private TextView tvLockdownMessage;
    private Button btnSubmitEvent, btnBack;
    private Uri selectedImageUri;
    private DatabaseReference databaseReference;
    private EditText etEventPhotoUrl;
    String databaseURL = "https://umuniverse-1d81d-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        // Initialize views
        etEventName = findViewById(R.id.etEventName);
        etEventDate = findViewById(R.id.etEventDate);
        etEventStartTime = findViewById(R.id.etEventStartTime);
        etEventEndTime = findViewById(R.id.etEventEndTime);
        etEventLocation = findViewById(R.id.etEventLocation);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        tvLockdownMessage = findViewById(R.id.tvLockdownMessage);
        etEventPhotoUrl = findViewById(R.id.etEventPhotoUrl);
        btnSubmitEvent = findViewById(R.id.btnSubmitEvent);
        btnBack = findViewById(R.id.btnBack);

        // Initialize Firebase reference
        databaseReference = FirebaseDatabase.getInstance(databaseURL).getReference("Events");

        Log.d("FirebaseDebug", "Database Reference Initialized: " + databaseReference.toString());

        System.out.println("Why doesnt this work????");

        btnBack.setOnClickListener(v -> {
            Intent createEventIntent = new Intent(CreateEventActivity.this, EventPage.class);
            startActivity(createEventIntent);
        });

//         Setup spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        // Event Date Picker
        etEventDate.setOnClickListener(v -> showDatePicker());

        // Event Start Time Picker
        etEventStartTime.setOnClickListener(v -> showTimePicker(etEventStartTime));

        // Event End Time Picker
        etEventEndTime.setOnClickListener(v -> showTimePicker(etEventEndTime));

        // Submit button click listener
        btnSubmitEvent.setOnClickListener(v -> {
            validateAndSubmitEvent();
            Intent createEventIntent = new Intent(CreateEventActivity.this, EventPage.class);
            startActivity(createEventIntent);
        });
    }
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "-" + (month1 + 1) + "-" + year1;
                    etEventDate.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePicker(EditText timeField) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute1) -> {
                    String selectedTime = String.format("%02d:%02d", hourOfDay, minute1);
                    timeField.setText(selectedTime);
                }, hour, minute, true);
        timePickerDialog.show();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            selectedImageUri = data.getData();
//            Glide.with(this)
//                    .load(selectedImageUri)
//                    .into(ivEventPhoto);
//        } else {
//            Toast.makeText(this, "Failed to pick image. Please try again.", Toast.LENGTH_SHORT).show();
//        }
//    }



    private void validateAndSubmitEvent() {
        String eventName = etEventName.getText().toString();
        String eventDate = etEventDate.getText().toString();
        String eventStartTime = etEventStartTime.getText().toString();
        String eventEndTime = etEventEndTime.getText().toString();
        String eventLocation = etEventLocation.getText().toString();
        String eventCategory = spinnerCategory.getSelectedItem().toString();
        String eventPhotoUrl = etEventPhotoUrl.getText().toString();

        System.out.println("running validateAndSubmitEvent...");

        // Simple validation
        if (eventName.isEmpty() || eventDate.isEmpty() || eventStartTime.isEmpty()
                || eventEndTime.isEmpty() || eventLocation.isEmpty() || eventPhotoUrl.isEmpty()) {
            Toast.makeText(this, "Please fill out all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (eventLocation.contains("lockdown")) {
            tvLockdownMessage.setVisibility(View.VISIBLE);
            return;
        } else {
            tvLockdownMessage.setVisibility(View.GONE);
        }

        // Push data to Firebase
        String eventId = databaseReference.push().getKey();
        if (eventId == null) {
            Toast.makeText(this, "Failed to generate Event ID", Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String, Object> eventDetails = new HashMap<>();
        eventDetails.put("id", eventId);
        eventDetails.put("name", eventName);
        eventDetails.put("date", eventDate);
        eventDetails.put("startTime", eventStartTime);
        eventDetails.put("endTime", eventEndTime);
        eventDetails.put("location", eventLocation);
        eventDetails.put("category", eventCategory);
        eventDetails.put("photoUrl", eventPhotoUrl);


        assert eventId != null;
        databaseReference.child(eventId).setValue(eventDetails).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Event Created Successfully", Toast.LENGTH_SHORT).show();
                finish(); // Close activity after successful submission
            } else {
                Toast.makeText(this, "Failed to create event. Try again!", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
