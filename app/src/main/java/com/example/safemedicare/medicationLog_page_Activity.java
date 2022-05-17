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
import java.util.ArrayList;

public class medicationLog_page_Activity extends AppCompatActivity {
    GridView gridList;
    GridAdapterMedicationLog myAdapter;
    public String name, type , userName;
    ArrayList medicationList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medication_log);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("USERNAME");
            type = extras.getString("TYPE");

        }

        //toolbar
        toolbar();
////////////////////////////////////// read from DB////////////////////////////////////////////////
        new ConnectionToReadMedication().execute();

        ///// START GRID VIEW /////
        gridList = (GridView) findViewById(R.id.gridViewM);
        myAdapter = new GridAdapterMedicationLog(this, R.layout.grid_adapter_medication_log, medicationList);
        // implement OnItemClickListener
        gridList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // set an Intent to Another Activity
                Medication m = (Medication) medicationList.get(position);
                Intent intent = new Intent(medicationLog_page_Activity.this, MedicationLog_Adjustment.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                intent.putExtra("id", m.getId());
                intent.putExtra("NameM", m.getMedicineName());
                intent.putExtra("numberOfTime", m.getNumberOfTime());
                intent.putExtra("doseAmountNumber", m.getDoseAmountNumber());
                intent.putExtra("doseAmountText", m.getDoseAmountText());
                intent.putExtra("duration", m.getDuration());
                intent.putExtra("durationByText", m.getTextDurationSpin());
                intent.putExtra("startDayDate", m.getStartDayDate());
                intent.putExtra("timeH", m.getTimeH());
                intent.putExtra("timeM", m.getTimeM());
                intent.putExtra("everyH", m.getEveryH());
                intent.putExtra("repeated", m.getRepeated());

                startActivity(intent); // start Intent

            }
        });






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
                Intent intent = new Intent(medicationLog_page_Activity.this, Patient_Profile_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(medicationLog_page_Activity.this, Schedule_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(medicationLog_page_Activity.this, Add_Medicine_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        SOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(medicationLog_page_Activity.this, SOS_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equalsIgnoreCase("patient")){
                    Intent intent = new Intent(medicationLog_page_Activity.this, Home_Page_Activity.class);
                    intent.putExtra("USERNAME", name);
                    intent.putExtra("TYPE", type);
                    startActivity(intent);

                }else if (type.equalsIgnoreCase("caregiver")){
                    Intent intent = new Intent(medicationLog_page_Activity.this, caregiver_homePage_activity.class);
                    intent.putExtra("USERNAME", name);
                    intent.putExtra("TYPE", type);
                    startActivity(intent);

                }
            }
        });

        ///////////////////////END TOOLBAR BUTTON//////////////////////////////////////////
    }



    ///////////////////////////// class to read from DB ///////////////////////////////////////////////////////////////////
    class ConnectionToReadMedication extends AsyncTask<String, String, String> {
        // starting the connection
        @Override
        protected String doInBackground(String... strings) {
            String result = "";

            String medication_url = "http://192.168.100.126/readMedication.php";
            try {

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
                result = stringBufferM.toString();
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
                    JSONArray medicationData = jsonResult.getJSONArray("medication");
                    for (int i = 0; i < medicationData.length(); i++) {
                        JSONObject medicationObject = medicationData.getJSONObject(i);
                        userName = medicationObject.getString("userName");


                        if (userName.equalsIgnoreCase(name)) {


                            int id = medicationObject.getInt("id");
                            String medicineName = medicationObject.getString("medicineName");
                            String numberOfTime = medicationObject.getString("numberOfTime");
                            String doseAmountNumber = medicationObject.getString("doseAmountNumber");
                            String doseAmountText = medicationObject.getString("doseAmountText");
                            String duration = medicationObject.getString("duration");
                            String durationByText = medicationObject.getString("durationByText");
                            String startDayDate = medicationObject.getString("startDayDate");
                            String timeH = medicationObject.getString("timeH");
                            String timeM = medicationObject.getString("timeM");
                            String everyH = medicationObject.getString("everyH");
                            String repeated = medicationObject.getString("repeated");

                            // add to the array list medicationList
                            medicationList.add(new Medication(String.valueOf(id), userName, medicineName, numberOfTime, doseAmountNumber, doseAmountText, duration, durationByText, startDayDate, timeH, timeM, everyH, repeated));

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