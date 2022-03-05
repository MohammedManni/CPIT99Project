package com.example.safemedicare;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class caregiver_homePage_activity extends AppCompatActivity {


        ListView patientList;
        String listName[] = {"Ali", "Ahmed", "Khalid", "Nasser", "Fares", "Ali"};
        int flags[] = {R.drawable.logo19, R.drawable.logo19, R.drawable.logo19,
                R.drawable.logo19, R.drawable.logo19, R.drawable.logo19};

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_caregiver_home_page);
            patientList = (ListView) findViewById(R.id.patientList);
            ListAdabter customAdapter = new ListAdabter(getApplicationContext(), listName, flags);
            patientList.setAdapter(customAdapter);
        }


}