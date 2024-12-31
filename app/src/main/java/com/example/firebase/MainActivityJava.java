package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivityJava extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.welcome_page); // Ensure the layout file is named correctly

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }
}
// User model class
class User {
    public String email;
    public String username;

    public User() {
        // Default constructor required for Firebase
    }

    public User(String email, String username) {
        this.email = email;
        this.username = username;
    }
}
