package com.example.safemedicare;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
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

    EditText medName, numOfTime, amount;
    public String name, type , userName;
    ArrayList medicationList = new ArrayList<>();
    ArrayList medicationChild = new ArrayList();
    ////////attributes medication to read from DB/////////
    ListView list;
    ArrayAdapter<String> adapter;
    Medication medication;
    MedicationLog medicationLog ;
    /////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medication_log);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("USERNAME");
            type = extras.getString("TYPE");

        }



        // Edit text
        medName = (EditText) findViewById(R.id.EditMedicineName);
        numOfTime = (EditText) findViewById(R.id.editTextNumberOfTime);
        amount = (EditText) findViewById(R.id.editTextAmount);
        ////////////////////////////////////////////////////////////////
        //toolbar
        toolbar();


        //////// database /////////////////
        //////////attributes medication to read from DB////////////////////////////////////////////////

        new ConnectionToReadMedication().execute();
        /////////////////////////////////////////////////////////////////////////////////////////
        // saveChange button
        Button save = findViewById(R.id.Add_medicine);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                medicationLog(view);
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
                Intent intent = new Intent(medicationLog_page_Activity.this, Profile_Activity.class);
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

    public void medicationLog(View view) {
        String medicationName = medName.getText().toString();
        String numberOfTime = numOfTime.getText().toString();
        String doseAmount = amount.getText().toString();

        String type = "medication";
        db1BackgroundWorker db1BackgroundWorker = new db1BackgroundWorker(this);
        db1BackgroundWorker.execute(type, medicationName, numberOfTime, doseAmount);
    }

    ///////////////////////////// class for read from DB ///////////////////////////////////////////////////////////////////
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
                        userName = patientObject.getString("userName");


                        if (userName.equalsIgnoreCase(name)) {


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

                            // add to the array list medicationList
                            Medication m = new Medication(String.valueOf(id), userName, medicineName, numberOfTime, doseAmountNumber, doseAmountText, duration, durationByText, startDayDate, timeH, timeM, everyH, repeated);

                            medicationList.add(m);
                            //  eventList.add(new GridItem("Medicine: " + m.getMedicineName(), "Amount: " + m.getDoseAmountNumber() + " Pill/s", "Time " + m.getTimeH() + " : " + m.getTimeM()));

                        }


                    }
                   // addMedicationChild();
                    //gridList.setAdapter(myAdapter);

                } else {
                    Toast.makeText(getApplicationContext(), "The retrieve was not successful ", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}