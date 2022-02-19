package com.example.safemedicare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Home_Page extends AppCompatActivity {
    Button Profile, Scheduale, Add, SOS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        // toolbar buttons
        Button Profile = findViewById(R.id.Confirm);
        Button Schedule = findViewById(R.id.Confirm);
        Button Add = findViewById(R.id.Confirm);
        Button SOS = findViewById(R.id.Confirm);

        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_Page.this, Profile.class);
                startActivity(intent);
            }
        });

        Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_Page.this, Schedule.class);
                startActivity(intent);
            }
        });

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_Page.this, Add.class);
                startActivity(intent);
            }
        });

        SOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_Page.this, SOS.class);
                startActivity(intent);
            }
        });
    }
}
