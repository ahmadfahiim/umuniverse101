package com.example.firebase;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Welcome extends AppCompatActivity {

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