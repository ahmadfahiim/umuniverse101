package com.example.firebase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Settings extends AppCompatActivity {

    private Switch rememberMeToggle, hideProfileToggle;
    private Button btnBack;
    private TextView termsAndConditions;
    String databaseURL = "https://umuniverse-1d81d-default-rtdb.asia-southeast1.firebasedatabase.app/";
    FirebaseAuth auth = FirebaseAuth.getInstance();
    DatabaseReference userRef = FirebaseDatabase.getInstance(databaseURL).getReference("Users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.settings_page);

        rememberMeToggle = findViewById(R.id.rememberMeToggle);
        hideProfileToggle = findViewById(R.id.hideProfileToggle);

        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        boolean isRememberMeEnabled = prefs.getBoolean("rememberMe", false);
        rememberMeToggle.setChecked(isRememberMeEnabled);

        rememberMeToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = prefs.edit();
            if (isChecked) {
                saveCredentials(editor);
                Toast.makeText(Settings.this, "Remember Me Enabled", Toast.LENGTH_SHORT).show();
            } else {
                editor.clear();
                editor.apply();
                Toast.makeText(getApplicationContext(), "Remember Me disabled", Toast.LENGTH_SHORT).show();
            }
        });

        userRef.child(auth.getCurrentUser().getUid()).child("hideProfile").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                boolean isHidden = Boolean.TRUE.equals(task.getResult().getValue(Boolean.class));
                hideProfileToggle.setChecked(isHidden);
            }
        });

        hideProfileToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            userRef.child(auth.getCurrentUser().getUid()).child("hideProfile").setValue(isChecked)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String message = isChecked ? "Profile hidden" : "Profile visible";
                            Toast.makeText(Settings.this, message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Settings.this, "Failed to update profile visibility", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            onBackPressed();
        });

        termsAndConditions = findViewById(R.id.termsAndConditions);
        termsAndConditions.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.this, TermsAndConditions.class);
            startActivity(intent);
        });


    }

    private void saveCredentials(SharedPreferences.Editor editor) {
        String userEmail = auth.getCurrentUser().getEmail();
        if (userEmail != null) {
            editor.putString("email", userEmail);
            editor.putBoolean("rememberMe", true);
            editor.apply();
        }
    }
}