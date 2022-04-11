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

public class shared_activity extends AppCompatActivity {

    EditText patientNameET, ageET, phoneET;
    Button medicationLog, schedule, timeline;
    String name, type, patientUserNameFromGetIntent, userName, userNameInDB;
    String swMedicationLog, swSchedule, swTimeline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_activity);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("USERNAME");
            type = extras.getString("TYPE");
            patientUserNameFromGetIntent = extras.getString("PATIENT_USERNAME");

            Button SecondB = findViewById(R.id.SecondB);
            Button add = findViewById(R.id.thirdB);
            if (type.matches("caregiver")) {
                SecondB.setVisibility(View.GONE);
                add.setVisibility(View.GONE);
            }
        }

        // toolbar
        toolbar();

        patientNameET = (EditText) findViewById(R.id.patientNameEditText);
        ageET = (EditText) findViewById(R.id.ageEditText);
        phoneET = (EditText) findViewById(R.id.phoneEditText);

        medicationLog = (Button) findViewById(R.id.medicationLogID);
        schedule = (Button) findViewById(R.id.scheduleID);
        timeline = (Button) findViewById(R.id.timelineID);


        new PatientInfo().execute();
        new CheckData().execute();


    }

    ///////////////////////////CLASS READ PATIENT/////////////////////////////////
    class PatientInfo extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String readPatient_url = "http://192.168.100.171/readPatient.php";
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
                    JSONArray patientData = jsonResult.getJSONArray("patient");
                    for (int i = 0; i < patientData.length(); i++) {
                        JSONObject patientObject = patientData.getJSONObject(i);

                        userNameInDB = patientObject.getString("userName");

                        if (userNameInDB.equalsIgnoreCase(patientUserNameFromGetIntent)) {
                            String patientName = patientObject.getString("name");
                            int phoneNumber = patientObject.getInt("phoneNumber");
                            int age = patientObject.getInt("age");


                            patientNameET.setText(patientName);
                            patientNameET.setEnabled(false);
                            ageET.setText(String.valueOf(age));
                            ageET.setEnabled(false);
                            phoneET.setText(String.valueOf(phoneNumber));
                            phoneET.setEnabled(false);
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

    ///////////////////////////CLASS TO CHECK WHICH DATA CAN SHOW BY CAREGIVER/////////////////////////////////
    class CheckData extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String readPatient_url = "http://192.168.100.171/readPC.php";
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
                    JSONArray patientData = jsonResult.getJSONArray("patient");
                    for (int i = 0; i < patientData.length(); i++) {
                        JSONObject patientObject = patientData.getJSONObject(i);
                        userName = patientObject.getString("userNameC");
                        userNameInDB = patientObject.getString("userNameP");

                        if (userName.equalsIgnoreCase(name) && patientUserNameFromGetIntent.equalsIgnoreCase(userNameInDB)) {
                            swMedicationLog = patientObject.getString("swMedLog");
                            swSchedule = patientObject.getString("swSchedule");
                            swTimeline = patientObject.getString("swTimeline");

                            if (swMedicationLog.equalsIgnoreCase("false")) {
                                medicationLog.setVisibility(View.GONE);
                            } else {
                                medicationLog.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(shared_activity.this, Shared_MedicationLog.class);
                                        intent.putExtra("USERNAME", name);
                                        intent.putExtra("TYPE", type);
                                        intent.putExtra("PatientUserName", patientUserNameFromGetIntent);
                                        startActivity(intent);


                                    }
                                });

                            }
                            if (swSchedule.equalsIgnoreCase("false")) {
                                schedule.setVisibility(View.GONE);
                            } else {
                                schedule.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(shared_activity.this, shared_schedule.class);
                                        intent.putExtra("USERNAME", name);
                                        intent.putExtra("TYPE", type);
                                        intent.putExtra("PatientUserName", patientUserNameFromGetIntent);
                                        startActivity(intent);


                                    }
                                });

                            }
                            if (swTimeline.equalsIgnoreCase("false")) {
                                timeline.setVisibility(View.GONE);
                            }else {
                                timeline.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(shared_activity.this, shared_timeline.class);
                                        intent.putExtra("USERNAME", name);
                                        intent.putExtra("TYPE", type);
                                        intent.putExtra("PatientUserName", patientUserNameFromGetIntent);
                                        startActivity(intent);
                                    }
                                });

                            }
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
                Intent intent = new Intent(shared_activity.this, personal_info_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(shared_activity.this, Schedule_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(shared_activity.this, Add_Medicine_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        SOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(shared_activity.this, SOS_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equalsIgnoreCase("patient")) {
                    Intent intent = new Intent(shared_activity.this, Home_Page_Activity.class);
                    intent.putExtra("USERNAME", name);
                    intent.putExtra("TYPE", type);
                    startActivity(intent);

                } else if (type.equalsIgnoreCase("caregiver")) {
                    Intent intent = new Intent(shared_activity.this, caregiver_homePage_activity.class);
                    intent.putExtra("USERNAME", name);
                    intent.putExtra("TYPE", type);
                    startActivity(intent);

                }

            }
        });
    }
}