package com.example.safemedicare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class Home_Page_Activity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_patient);

        Toolbar toolbar = findViewById(R.id.toolbarh);
        setSupportActionBar(toolbar);

        /////////////////////////////////////////////////////////////////////

        // toolbar buttons
        Button Profile = findViewById(R.id.firstB);
        Button Schedule = findViewById(R.id.SecondB);
        Button Add = findViewById(R.id.thirdB);
        Button SOS = findViewById(R.id.SOS);


        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_Page_Activity.this, Profile_Activity.class);
                startActivity(intent);
            }
        });

        Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_Page_Activity.this, Schedule_Activity.class);
                startActivity(intent);
            }
        });

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_Page_Activity.this, Add_Activity.class);
                startActivity(intent);
            }
        });

        SOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_Page_Activity.this, SOS_Activity.class);
                startActivity(intent);
            }
        });

        //////////////////////////////  end toolbar button//////////////////////////////////////////////

        ///// START GRID VIEW /////
        GridView gridList;
        ArrayList eventList=new ArrayList<>();
            gridList = (GridView) findViewById(R.id.gridView);
            eventList.add(new GridItem("Event 1",R.drawable.medicare1));
            eventList.add(new GridItem("Event 2",R.drawable.medicare1));
            eventList.add(new GridItem("Event 3",R.drawable.medicare1));
            eventList.add(new GridItem("Event 4",R.drawable.medicare1));
            eventList.add(new GridItem("Event 5",R.drawable.medicare1));
            eventList.add(new GridItem("Event 6",R.drawable.medicare1));
            eventList.add(new GridItem("Event 7",R.drawable.medicare1));
            eventList.add(new GridItem("Event 8",R.drawable.medicare1));
            eventList.add(new GridItem("Event 9",R.drawable.medicare1));
            eventList.add(new GridItem("Event 10",R.drawable.medicare1));
            eventList.add(new GridItem("Event 11",R.drawable.medicare1));
            eventList.add(new GridItem("Event 12",R.drawable.medicare1));

            GridAdapter myAdapter=new GridAdapter(this,R.layout.gridview_item,eventList);
            gridList.setAdapter(myAdapter);

        // implement setOnItemClickListener event on GridView
        gridList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // set an Intent to Another Activity
                Intent intent = new Intent(Home_Page_Activity.this, SecondActivity.class);
                intent.putExtra("image", eventList.add(position)); // put image data in Intent
                startActivity(intent); // start Intent
            }
        });



        }
}
