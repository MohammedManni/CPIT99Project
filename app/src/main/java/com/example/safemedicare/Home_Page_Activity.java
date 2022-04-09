package com.example.safemedicare;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
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
import java.util.Random;

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
                Intent intent = new Intent(Home_Page_Activity.this, Add_Medicine_Activity.class);
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
        //Toast.makeText(getApplicationContext(), formattedDate, Toast.LENGTH_SHORT).show();
        ///////////////////////////////////END GET CURRENT DATE//////////////////////////////////////////////////////////////////


        new Connection().execute();
        myAdapter = new GridAdapter(this, R.layout.gridview_item, eventList);

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

    public void onTimeSet(int hourOfDay, int minute,String eventName,String eventDescription) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        //Toast.makeText(getApplicationContext(), ""+formattedDate, Toast.LENGTH_SHORT).show();

        startAlarm(c,eventName,eventDescription);
    }

    private void startAlarm(Calendar c,String eventName,String eventDescription) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        intent.putExtra("EVENT_NAME", eventName);
        intent.putExtra("EVENT_DESCRIPTION", eventDescription);
        Random random = new Random();
        int m = random.nextInt(9999 - 1000) + 1000;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, m, intent, 0);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }


    class Connection extends AsyncTask<String, String, String> {
        // starting the connection
        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String medication_url = "http://192.168.100.10/readEvent.php";
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
            ArrayList<Event> eventlist1 = new ArrayList<>();

            try {
                JSONObject jsonResult = new JSONObject(result);
                int success = jsonResult.getInt("success");
                if (success == 1) {
                    JSONArray patientData = jsonResult.getJSONArray("event");
                    for (int i = 0; i < patientData.length(); i++) {
                        JSONObject patientObject = patientData.getJSONObject(i);
                        userName = patientObject.getString("userName");


                        if (userName.equalsIgnoreCase(name)) {
                            int id = patientObject.getInt("id");
                            String eventName = patientObject.getString("eventName");
                            String eventDescription = patientObject.getString("eventDescription");
                            String date = patientObject.getString("date");
                            String timeH = patientObject.getString("timeH");
                            String timeM = patientObject.getString("timeM");

                            // add to the array list
                            eventlist1.add(new Event(eventName, eventDescription, timeH, timeM, date, id));

                            ///////////////CREATE NOTIFICATION////////////////////////////////////////////////
                            // if version > oreo version

                                if (date.equalsIgnoreCase(formattedDate)) {
                                    //Toast.makeText(getApplicationContext(), ""+Integer.parseInt(timeH)+" "+Integer.parseInt(timeM)), Toast.LENGTH_SHORT).show();
                                    onTimeSet(Integer.parseInt(timeH),Integer.parseInt(timeM),eventName,eventDescription);
                            }
                            /////////////////////////////////////////////////////
                        }

                    }
                    // sort the array list
                    Collections.sort(eventlist1, new Comparator<Event>() {
                        @Override
                        public int compare(Event event1, Event event2) {
                            return event1.getEventTimeH().compareTo(event2.getEventTimeH());
                        }
                    });
                    // add the array list to the event list related to the grid view
                    for (int i = 0; i < eventlist1.size(); i++) {
                        Event e = new Event();
                        e = eventlist1.get(i);
                        if (e.getEventDate().matches(formattedDate)) {
                            if (Integer.parseInt(e.getEventTimeH()) >= 12 && Integer.parseInt(e.getEventTimeH()) < 24) {
                                if (Integer.parseInt(e.getEventTimeH()) > 12) {
                                    eventList.add(new GridItem(e.getEventName(), e.getEventDate(), ((Integer.parseInt(e.getEventTimeH()) - 12) + ":" + e.getEventTimeM() + " pm")));

                                } else {
                                    eventList.add(new GridItem(e.getEventName(), e.getEventDate(), (e.getEventTimeH() + ":" + e.getEventTimeM() + " pm")));

                                }
                            } else if (Integer.parseInt(e.getEventTimeH()) == 0) {
                                eventList.add(new GridItem(e.getEventName(), e.getEventDate(), ("12" + ":" + e.getEventTimeM() + " am")));

                            } else {
                                eventList.add(new GridItem(e.getEventName(), e.getEventDate(), (e.getEventTimeH() + ":" + e.getEventTimeM() + " am")));
                            }
                        }
                    }
                    gridList.setAdapter(myAdapter);

                } else {
                    Toast.makeText(getApplicationContext(), "The retrieve was not successful ", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}