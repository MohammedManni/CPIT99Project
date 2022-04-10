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

public class Shared_MedicationLog extends AppCompatActivity {
    String name, type,patientUserName,userNamePatient;
    GridView gridList;
    GridAdapterMedicationLog myAdapter;
    ArrayList medicationList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared__medication_log);

        // toolbar
        toolbar();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("USERNAME");
            type = extras.getString("TYPE");
            patientUserName = extras.getString("PatientUserName");
        }
        new ConnectionToReadMedication().execute();

        ///// START GRID VIEW /////
        gridList = (GridView) findViewById(R.id.gridViewM);
        myAdapter = new GridAdapterMedicationLog(this, R.layout.grid_adapter_medication_log, medicationList);
        // implement setOnItemClickListener
        gridList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // set an Intent to Another Activity
                /*Medication m = (Medication) medicationList.get(position);
                Intent intent = new Intent(Shared_MedicationLog.this, MedicationLog_Adjustment.class);
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
                 */
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
                Intent intent = new Intent(Shared_MedicationLog.this, personal_info_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);


            }
        });

        Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Shared_MedicationLog.this, Schedule_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Shared_MedicationLog.this, Add_Medicine_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        SOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Shared_MedicationLog.this, SOS_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equalsIgnoreCase("patient")) {
                    Intent intent = new Intent(Shared_MedicationLog.this, Home_Page_Activity.class);
                    intent.putExtra("USERNAME", name);
                    intent.putExtra("TYPE", type);
                    startActivity(intent);

                } else if (type.equalsIgnoreCase("caregiver")) {
                    Intent intent = new Intent(Shared_MedicationLog.this, caregiver_homePage_activity.class);
                    intent.putExtra("USERNAME", name);
                    intent.putExtra("TYPE", type);
                    startActivity(intent);

                }

            }
        });

        //////////////////////////////  end toolbar button//////////////////////////////////////////////

    }
    ///////////////////////////// class for read from DB ///////////////////////////////////////////////////////////////////
    class ConnectionToReadMedication extends AsyncTask<String, String, String> {
        // starting the connection
        @Override
        protected String doInBackground(String... strings) {
            String result = "";

            String medication_url = "http://192.168.100.10/readMedication.php";
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
                    JSONArray patientData = jsonResult.getJSONArray("medication");
                    for (int i = 0; i < patientData.length(); i++) {
                        JSONObject patientObject = patientData.getJSONObject(i);
                        userNamePatient = patientObject.getString("userName");
                        //Toast.makeText(getApplicationContext(), userNamePatient, Toast.LENGTH_SHORT).show();

                        if (userNamePatient.equalsIgnoreCase(patientUserName)) {
                            Toast.makeText(getApplicationContext(), userNamePatient, Toast.LENGTH_SHORT).show();

                            int id = patientObject.getInt("id");
                            String medicineName = patientObject.getString("medicineName");
                            String numberOfTime = patientObject.getString("numberOfTime");
                            String doseAmountNumber = patientObject.getString("doseAmountNumber");
                            String doseAmountText = patientObject.getString("doseAmountText");
                            String duration = patientObject.getString("duration");
                            String durationByText = patientObject.getString("durationByText");
                            String startDayDate = patientObject.getString("startDayDate");
                            String timeH = patientObject.getString("timeH");
                            String timeM = patientObject.getString("timeM");
                            String everyH = patientObject.getString("everyH");
                            String repeated = patientObject.getString("repeated");

                            medicationList.add(new Medication(String.valueOf(id), userNamePatient, medicineName, numberOfTime, doseAmountNumber, doseAmountText, duration, durationByText, startDayDate, timeH, timeM, everyH, repeated));

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
