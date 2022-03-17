package com.example.safemedicare;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class Home_Page_Activity extends AppCompatActivity {

    private String name, type, userName, formattedDate;
    GridView gridList;
    ArrayList eventList = new ArrayList<>();
    GridAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_patient);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("USERNAME");
            type = extras.getString("TYPE");
            //Toast.makeText(getApplicationContext(), "Welcome "+name, Toast.LENGTH_SHORT).show();
        }
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
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });
        Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_Page_Activity.this, Schedule_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_Page_Activity.this, Add_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });
        SOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_Page_Activity.this, SOS_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });
        //////////////////////////////  end toolbar button//////////////////////////////////////////////

        ///// START GRID VIEW /////

        gridList = (GridView) findViewById(R.id.gridView);


        int logos[] = {R.drawable.logo19, R.drawable.logo19, R.drawable.logo19, R.drawable.logo19,
                R.drawable.logo19, R.drawable.logo19};
        ///////////////////////////////////GET CURRENT DATE//////////////////////////////////////////////////////////////////
        Date getDate = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("d/M/yyyy", Locale.getDefault());
        formattedDate = dateFormat.format(getDate);
        Toast.makeText(getApplicationContext(), formattedDate, Toast.LENGTH_SHORT).show();

        ///////////////////////////////////END GET CURRENT DATE//////////////////////////////////////////////////////////////////


        new Connection().execute();
        myAdapter = new GridAdapter(this, R.layout.gridview_item, eventList);

        //new Connection().execute();

        // implement setOnItemClickListener event on GridView
        gridList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // set an Intent to Another Activity
                Intent intent = new Intent(Home_Page_Activity.this, SecondActivity.class);
                intent.putExtra("image", logos[position]); // put image data in Intent
                startActivity(intent); // start Intent
            }
        });


    }

    class Connection extends AsyncTask<String, String, String> {
        // starting the connection
        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String medication_url = "http://192.168.100.171/readEvent.php";
            try {

                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(medication_url));
                HttpResponse response = client.execute(request);
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuffer stringBuffer = new StringBuffer("");
                String line = "";
                while ((line = reader.readLine()) != null) {
                    stringBuffer.append(line);
                    break;
                }
                reader.close();
                result = stringBuffer.toString();
            } catch (Exception e) {
                return new String("error");
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            ArrayList<Event> eventlist = new ArrayList<>();

            Event ev = new Event("", "", "1", "0", "");
            eventlist.add(ev);
            try {
                JSONObject jsonResult = new JSONObject(result);
                int success = jsonResult.getInt("success");
                if (success == 1) {
                    JSONArray patientData = jsonResult.getJSONArray("event");
                    for (int i = 0; i < patientData.length(); i++) {
                        JSONObject patientObject = patientData.getJSONObject(i);
                        userName = patientObject.getString("userName");


                        if (userName.equalsIgnoreCase(name)) {
                            String eventName = patientObject.getString("eventName");
                            String eventDescription = patientObject.getString("eventDescription");
                            String date = patientObject.getString("date");
                            String timeH = patientObject.getString("timeH");
                            String timeM = patientObject.getString("timeM");

                            // initiate a date picker
                            eventlist.add(new Event(eventName, eventDescription, timeH, timeM, date));
                        }


                    }

                    Collections.sort(eventlist, new Comparator<Event>() {
                        @Override
                        public int compare(Event event1, Event event2) {
                            return event1.getEventTimeH().compareToIgnoreCase(event2.getEventTimeH());
                        }
                    });

                    for (int i = 0; i < eventlist.size(); i++) {
                        Event e = new Event();
                        e = eventlist.get(i);
                        if (e.getEventDate().matches(formattedDate)) {
                            if (Integer.parseInt(e.getEventTimeH()) >= 12 && Integer.parseInt(e.getEventTimeH()) < 24) {
                                if (Integer.parseInt(e.getEventTimeH()) > 12) {
                                    eventList.add(new GridItem(e.getEventName(), e.getEventDate(), ((Integer.parseInt(e.getEventTimeH()) - 12) + ":" + e.getEventTimeM() + " pm")));

                                } else {
                                    eventList.add(new GridItem(e.getEventName(), e.getEventDate(), (e.getEventTimeH() + ":" + e.getEventTimeM() + " pm")));

                                }
                            } else {
                                eventList.add(new GridItem(e.getEventName(), e.getEventDate(), (e.getEventTimeH() + ":" + e.getEventTimeM() + " am")));

                            }
                        }
                    }
                    gridList.setAdapter(myAdapter);

                } else {
                    Toast.makeText(getApplicationContext(), "no there", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}