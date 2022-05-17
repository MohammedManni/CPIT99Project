package com.example.safemedicare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class Patient_Profile_Activity extends AppCompatActivity {

    private String name, type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("USERNAME");
            type = extras.getString("TYPE");
            Button medicationLog = findViewById(R.id.buttonMedicationLog);
            Button caregiver_relative_control = findViewById(R.id.buttonCaregiver);
            Button SecondB = findViewById(R.id.SecondB);
            Button add = findViewById(R.id.thirdB);
            if (type.matches("caregiver")) {
                SecondB.setVisibility(View.GONE);
                add.setVisibility(View.GONE);
                medicationLog.setVisibility(View.GONE);
                caregiver_relative_control.setVisibility(View.GONE);
            }
        }
        /////////////////////////////////////////////////////////////////////
        // toolbar
        toolbar();


        // profile page button //
        Button personalInfo = findViewById(R.id.buttonPersonalInfo);
        Button medicationLog = findViewById(R.id.buttonMedicationLog);
        Button caregiver_relative_control = findViewById(R.id.buttonCaregiver);
        Button signout = findViewById(R.id.signout);

        personalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Patient_Profile_Activity.this, personal_info_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        medicationLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Patient_Profile_Activity.this, medicationLog_page_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });


        caregiver_relative_control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Patient_Profile_Activity.this, caregiver_relative_control_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });
        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Patient_Profile_Activity.this, sign_activity.class);
                startActivity(intent);
            }
        });

    }
    public void toolbar() {
        // toolbar buttons
        Button Profile = findViewById(R.id.firstB);
        Button Schedule = findViewById(R.id.SecondB);
        Button Add = findViewById(R.id.thirdB);
        Button SOS = findViewById(R.id.SOS);
        ImageButton imageButton= findViewById(R.id.imageButton);

        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Patient_Profile_Activity.this, Patient_Profile_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Patient_Profile_Activity.this, Schedule_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Patient_Profile_Activity.this, Add_Medicine_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        SOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Patient_Profile_Activity.this, SOS_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equalsIgnoreCase("patient")){
                    Intent intent = new Intent(Patient_Profile_Activity.this, Home_Page_Activity.class);
                    intent.putExtra("USERNAME", name);
                    intent.putExtra("TYPE", type);
                    startActivity(intent);

                }else if (type.equalsIgnoreCase("caregiver")){
                    Intent intent = new Intent(Patient_Profile_Activity.this, caregiver_homePage_activity.class);
                    intent.putExtra("USERNAME", name);
                    intent.putExtra("TYPE", type);
                    startActivity(intent);

                }

            }
        });

        ///////////////////////END TOOLBAR BUTTON//////////////////////////////////////////
    }
}
