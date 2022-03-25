package com.example.safemedicare;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Event_Adjustment extends AppCompatActivity {
    private String name, type,eventName, eventNameToReadDB;
    EditText nameOfEventET ,DescriptionEditText,dateET,timeET;
    Event event= new Event();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event__adjustment);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("USERNAME");
            type = extras.getString("TYPE");
            eventName = extras.getString("EVENT_NAME");
        }
        new ConnectionToReadEvent().execute();
        // toolbar buttons
        Button Profile = findViewById(R.id.firstB);
        Button Schedule = findViewById(R.id.SecondB);
        Button Add = findViewById(R.id.thirdB);
        Button SOS = findViewById(R.id.SOS);
        ImageButton imageButton = findViewById(R.id.imageButton);

        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Event_Adjustment.this, Profile_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Event_Adjustment.this, Schedule_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Event_Adjustment.this, Add_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        SOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Event_Adjustment.this, SOS_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equalsIgnoreCase("patient")){
                    Intent intent = new Intent(Event_Adjustment.this, Home_Page_Activity.class);
                    intent.putExtra("USERNAME", name);
                    intent.putExtra("TYPE", type);
                    startActivity(intent);

                }else if (type.equalsIgnoreCase("caregiver")){
                    Intent intent = new Intent(Event_Adjustment.this, caregiver_homePage_activity.class);
                    intent.putExtra("USERNAME", name);
                    intent.putExtra("TYPE", type);
                    startActivity(intent);

                }
            }
        });


        ///////////////////////END TOOLBAR BUTTON//////////////////////////////////////////

        ////////////////////////////////DISPLAY EVENTS///////////////////////////////////////////////////

        nameOfEventET = findViewById(R.id.nameOfEventET);
        DescriptionEditText = findViewById(R.id.DescriptionEditText);
        dateET = findViewById(R.id.dateET);
        timeET = findViewById(R.id.timeET);

    }
        ///////////////////////////// class for read from DB ///////////////////////////////////////////////////////////////////
        class ConnectionToReadEvent extends AsyncTask<String, String, String> {
            @Override
            protected String doInBackground(String... strings) {
                String result = "";
                String readPatient_url = "http://192.168.100.10/readEvent.php";
                try {
                    HttpClient client = new DefaultHttpClient();
                    HttpGet request = new HttpGet();
                    request.setURI(new URI(readPatient_url));
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
                try {
                    JSONObject jsonResult = new JSONObject(result);
                    int success = jsonResult.getInt("success");
                    if (success == 1) {
                        JSONArray patientData = jsonResult.getJSONArray("event");
                        for (int i = 0; i < patientData.length(); i++) {
                            JSONObject patientObject = patientData.getJSONObject(i);
                             eventNameToReadDB = patientObject.getString("eventName");



                            if (eventNameToReadDB.equalsIgnoreCase(eventName)) {
                                String eventDescription = patientObject.getString("eventDescription");
                                String date = patientObject.getString("date");
                                String timeH = patientObject.getString("timeH");
                                String timeM = patientObject.getString("timeM");
                                // add to the array list
                                //eventlist.add(new Event(eventName, eventDescription, timeH, timeM, date));
                                nameOfEventET.setText(eventNameToReadDB);
                                DescriptionEditText.setText(eventDescription);
                                dateET.setText(date);
                                String time=timeH+":"+timeM;
                                timeET.setText(time);
                            }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "no there", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }