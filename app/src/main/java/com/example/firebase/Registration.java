package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Objects;

public class Registration extends AppCompatActivity {

    EditText usernameField, emailField, passwordField;
    AppCompatButton signUpButton;
    TextView goToLogin;
    String databaseURL = "https://umuniverse-1d81d-default-rtdb.asia-southeast1.firebasedatabase.app/";
    FirebaseAuth mAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.register_page);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance(databaseURL).getReference("Users");

        // Declare the EditTexts and Buttons
        usernameField = findViewById(R.id.usernameField);
        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        signUpButton = findViewById(R.id.signUpButton);
        goToLogin = findViewById(R.id.gotoLogin);

        // onClickListener for signUpButton
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username, email, password;
                username = usernameField.getText().toString();
                email = emailField.getText().toString();
                password = passwordField.getText().toString();

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(Registration.this, "Please enter username", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Registration.this, "Please enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Registration.this, "Please enter password", Toast.LENGTH_SHORT).show();
                    return;
                }

                registerUser(username, email, password);

                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });

        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Move to the login page
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });

    }

    // All done by chatgpt, I have no idea how this works.
    private void registerUser(String username, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                String bio = "Placeholder bio. Please add one if you wish.";
                String faculty = "Placeholder faculty. Please add one if you wish.";
                int eventsJoined = 0;
                if (task.isSuccessful()) {
                    String userId = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                    AppUser user = new AppUser(username, email, bio, faculty, eventsJoined);

                    System.out.println("Authentication successful. User ID: " + userId);

                    databaseReference.child(userId).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Registration successful.", Toast.LENGTH_SHORT).show();
                                System.out.println("User data saved to database.");
                            } else {
                                Toast.makeText(getApplicationContext(), "Failed to save user data.", Toast.LENGTH_SHORT).show();
                                System.err.println("Failed to save user data to database: " + task.getException());
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Registration failed.", Toast.LENGTH_SHORT).show();
                    System.err.println("Authentication failed: " + task.getException());
                }
            }
        });

        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Move to the login page
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
        });
    }

}