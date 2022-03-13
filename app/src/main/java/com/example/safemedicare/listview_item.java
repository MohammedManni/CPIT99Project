package com.example.safemedicare;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class listview_item extends AppCompatActivity {
     String name, type , patientName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_item);
/*
        Button patientMedicationLog = findViewById(R.id.patientMedicationLog);
        Button patientEvents = findViewById(R.id.patientEvents);
        Button addMedicationForPatient = findViewById(R.id.addMedicationForPatient);




        TextView patientINlistView =(TextView) findViewById(R.id.patientINlistView);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("USERNAME");
            type = extras.getString("TYPE");
            patientName = extras.getString("PatientName");
            patientINlistView.setText(patientName);
         //   patientMedicationLog.setText(type);
          //  addMedicationForPatient.setText(patientName);
        }
  */
    }
}