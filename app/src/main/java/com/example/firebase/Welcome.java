package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Welcome extends AppCompatActivity {

    FirebaseAuth mAuth;

    // If the user has logged in before, straight away go to the home page, skip the welcome page (UNFINISHED)
//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            Intent intent = new Intent(getApplicationContext(), HomePage.class);
//            startActivity(intent);
//            finish();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.welcome_page);

        // Declarations of buttons for login and registration
        Button btnLogin = findViewById(R.id.loginbutton);
        Button btnRegister = findViewById(R.id.registerbutton);

        // onClickListener for login button
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Move to the login page
                Intent intent = new Intent(Welcome.this, Login.class);
                startActivity(intent);
            }
        });

        // onClickListener for registration button
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Move to registration page
                Intent intent = new Intent(Welcome.this, Registration.class);
                startActivity(intent);
            }
        });
    }
}