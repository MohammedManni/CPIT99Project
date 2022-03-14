package com.example.safemedicare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Add_event_from_calendar extends AppCompatActivity {

    private String name, type ,selectedDateString;
    EditText eventNameET,DescriptionET;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("USERNAME");
            type = extras.getString("TYPE");
            selectedDateString = extras.getString("DATE");

        }
        // toolbar buttons
        Button Profile = findViewById(R.id.firstB);
        Button Schedule = findViewById(R.id.SecondB);
        Button Add = findViewById(R.id.thirdB);
        Button SOS = findViewById(R.id.SOS);
        ImageButton imageButton= findViewById(R.id.imageButton);

        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Add_event_from_calendar.this, Profile_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Add_event_from_calendar.this, Schedule_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Add_event_from_calendar.this, Add_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        SOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Add_event_from_calendar.this, SOS_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equalsIgnoreCase("patient")){
                    Intent intent = new Intent(Add_event_from_calendar.this, Home_Page_Activity.class);
                    intent.putExtra("USERNAME", name);
                    intent.putExtra("TYPE", type);
                    startActivity(intent);

                }else if (type.equalsIgnoreCase("caregiver")){
                    Intent intent = new Intent(Add_event_from_calendar.this, caregiver_homePage_activity.class);
                    intent.putExtra("USERNAME", name);
                    intent.putExtra("TYPE", type);
                    startActivity(intent);

                }
            }
        });

        ///////////////////////END TOOLBAR BUTTON//////////////////////////////////////////

        // timeline button    dateView
         eventNameET= findViewById(R.id.eventNameET);
         DescriptionET= findViewById(R.id.DescriptionET);
        TextView dateView= findViewById(R.id.dateView);
        dateView.setText(selectedDateString);
        Button buttonSave = findViewById(R.id.buttonSave);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eventNameET.setText(selectedDateString);
            }
        });

        Button buttonBack = findViewById(R.id.buttonBack);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Add_event_from_calendar.this, Schedule_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });
    }

    public void PasswordUpdate(View view) {

        String operation = "save";
        String username = name;
        String type1 = type;
        String eventName = eventNameET.getText().toString();
        String Description = DescriptionET.getText().toString();
        String Date = selectedDateString;
        //String time = ;


       // personal_info_Activity.db1BackgroundWorker db1BackgroundWorker = new personal_info_Activity.db1BackgroundWorker(this);
       // db1BackgroundWorker.execute(operation, username, type1, password);

    }
}