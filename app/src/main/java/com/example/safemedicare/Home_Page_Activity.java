package com.example.safemedicare;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
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
    ArrayList medicationList = new ArrayList<>();
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
        // toolbar
        toolbar();

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


        new ConnectionToReadEvent().execute();
        new ConnectionToReadMedication().execute();

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

    class ConnectionToReadEvent extends AsyncTask<String, String, String> {
        // starting the connection
        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String event_url = "http://192.168.100.171/readEvent.php";
            try {

                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(event_url));
                HttpResponse response = client.execute(request);
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuffer stringBuffer = new StringBuffer("");
                String line = "";
                while ((line = reader.readLine()) != null) {
                    stringBuffer.append(line);
                    break;
                }
                reader.close();
                result = stringBuffer.toString() ;
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
                                    eventList.add(new GridItem("Name: "+e.getEventName(), "Date: "+e.getEventDate(),"Time: "+ ((Integer.parseInt(e.getEventTimeH()) - 12) + ":" + e.getEventTimeM() + " pm")));

                                } else {
                                    eventList.add(new GridItem("Name: "+e.getEventName(), "Date: "+e.getEventDate(),"Time: "+ (e.getEventTimeH() + ":" + e.getEventTimeM() + " pm")));

                                }
                            } else if (Integer.parseInt(e.getEventTimeH()) == 0) {
                                eventList.add(new GridItem("Name: "+e.getEventName(), "Date: "+e.getEventDate(),"Time: "+  ("12" + ":" + e.getEventTimeM() + " am")));

                            } else {
                                eventList.add(new GridItem("Name: "+e.getEventName(), "Date: "+e.getEventDate(),"Time: "+  (e.getEventTimeH() + ":" + e.getEventTimeM() + " am")));
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
    class ConnectionToReadMedication extends AsyncTask<String, String, String> {
        // starting the connection
        @Override
        protected String doInBackground(String... strings) {
            String result = "";

            String medication_url = "http://192.168.100.171/readMedication.php";
            try {


                //////////////////////////////////////////////////////////////////////////////////////////
                HttpClient clientM = new DefaultHttpClient();
                HttpGet requestM = new HttpGet();
                requestM.setURI(new URI(medication_url));
                HttpResponse responseM = clientM.execute(requestM);
                BufferedReader readerM = new BufferedReader(new InputStreamReader(responseM.getEntity().getContent()));
                StringBuffer stringBufferM = new StringBuffer("");
                String lineM = "";
                while ((lineM = readerM.readLine()) != null) {
                    stringBufferM.append(lineM);
                    break;
                }
                readerM.close();
                result =  stringBufferM.toString() ;
            } catch (Exception e) {
                return new String("error");
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {


            try {
                JSONObject jsonResult = new JSONObject(result);
                int success = jsonResult.getInt("success");
                if (success == 2) {
                    JSONArray patientData = jsonResult.getJSONArray("medication");
                    for (int i = 0; i < patientData.length(); i++) {
                        JSONObject patientObject = patientData.getJSONObject(i);
                        userName = patientObject.getString("userName");


                        if (userName.equalsIgnoreCase(name)) {


                            int id = patientObject.getInt("id");
                            String medicineName = patientObject.getString("medicineName");
                            String numberOfTime = patientObject.getString("numberOfTime");
                            String doseAmountNumber = patientObject.getString("doseAmountNumber");
                            String doseAmountText = patientObject.getString("doseAmountText");
                            String duration = patientObject.getString("duration");
                            String durationByText = patientObject.getString("durationByText");
                            String saturday = patientObject.getString("saturday");
                            String sunday = patientObject.getString("sunday");
                            String monday = patientObject.getString("monday");
                            String tuesday = patientObject.getString("tuesday");
                            String wednesday = patientObject.getString("wednesday");
                            String thursday = patientObject.getString("thursday");
                            String friday = patientObject.getString("friday");
                            String startDayDate = patientObject.getString("startDayDate");
                            String timeH = patientObject.getString("timeH");
                            String timeM = patientObject.getString("timeM");
                            String everyH = patientObject.getString("everyH");

                            // add to the array list medicationList
                            Medication m = new Medication(String.valueOf(id),userName, medicineName,numberOfTime , doseAmountNumber, doseAmountText, duration,durationByText, saturday, sunday, monday,  tuesday, wednesday , thursday, friday,  startDayDate,timeH,timeM, everyH);

                            medicationList.add(m);
                            eventList.add(new GridItem("Medicine: "+m.getMedicineName(),"Amount: "+m.getDoseAmountNumber()+" Pill/s","Time "+m.getTimeH()+" : "+m.getTimeM()));

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
    public void addMedicationChild(){
        ArrayList medicationChild= new ArrayList();
        for (int i = 0; i < medicationList.size(); i++) {
            Medication medication= (Medication) medicationList.get(i);
            String Duration = medication.getDuration();
            String DurationText = medication.getTextDurationSpin();
            if(DurationText.equalsIgnoreCase("Day")){
                if (Integer.parseInt(Duration)>1){
                    for (int j = 0; j < Integer.parseInt(Duration); j++) {
                        int everyH = Integer.parseInt(medication.getEveryH());
                        if (everyH==24){
                            medicationChild.add(medicationList.get(i));
                        }
                    }
                }
            }else if(DurationText.equalsIgnoreCase("Week")){

            }else if(DurationText.equalsIgnoreCase("Month")){

            }else if(DurationText.equalsIgnoreCase("Year")){

            }else {

            }
        }
    }
    public void toolbar() {
        // toolbar buttons
        Button Profile = findViewById(R.id.firstB);
        Button Schedule = findViewById(R.id.SecondB);
        Button Add = findViewById(R.id.thirdB);
        Button SOS = findViewById(R.id.SOS);
        ImageButton imageButton = findViewById(R.id.imageButton);

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

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equalsIgnoreCase("patient")) {
                    Intent intent = new Intent(Home_Page_Activity.this, Home_Page_Activity.class);
                    intent.putExtra("USERNAME", name);
                    intent.putExtra("TYPE", type);
                    startActivity(intent);

                } else if (type.equalsIgnoreCase("caregiver")) {
                    Intent intent = new Intent(Home_Page_Activity.this, caregiver_homePage_activity.class);
                    intent.putExtra("USERNAME", name);
                    intent.putExtra("TYPE", type);
                    startActivity(intent);

                }
            }
        });
    }
}