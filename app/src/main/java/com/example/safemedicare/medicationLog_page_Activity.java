package com.example.safemedicare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class medicationLog_page_Activity extends AppCompatActivity {

    EditText medName, numOfTime, amount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medication_log);

        // Edit text
        medName = (EditText) findViewById(R.id.EditMedicineName);
        numOfTime = (EditText) findViewById(R.id.editTextNumberOfTime);
        amount = (EditText) findViewById(R.id.editTextAmount);
        ////////////////////////////////////////////////////////////////

        // toolbar buttons
        Button Profile = findViewById(R.id.firstB);
        Button Schedule = findViewById(R.id.SecondB);
        Button Add = findViewById(R.id.thirdB);
        Button SOS = findViewById(R.id.SOS);
        ImageButton imageButton= findViewById(R.id.imageButton);

        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(medicationLog_page_Activity.this, Profile_Activity.class);
                startActivity(intent);
            }
        });

        Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(medicationLog_page_Activity.this, Schedule_Activity.class);
                startActivity(intent);
            }
        });

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(medicationLog_page_Activity.this, Add_Activity.class);
                startActivity(intent);
            }
        });

        SOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(medicationLog_page_Activity.this, SOS_Activity.class);
                startActivity(intent);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(medicationLog_page_Activity.this, Home_Page_Activity.class);
                startActivity(intent);
            }
        });

        ///////////////////////END TOOLBAR BUTTON//////////////////////////////////////////

        //////// database /////////////////

        // saveChange button
        Button save = findViewById(R.id.Changes);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
medicationLog(view);
            }
        });
    }


    public void medicationLog(View view) {
        String medicationName = medName.getText().toString();
        String numberOfTime = numOfTime.getText().toString();
        String doseAmount = amount.getText().toString();

        String type = "medication";
        db1BackgroundWorker db1BackgroundWorker = new db1BackgroundWorker(this);
        db1BackgroundWorker.execute(type, medicationName, numberOfTime, doseAmount);
    }

}