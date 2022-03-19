package com.example.safemedicare;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class caregiver_homePage_activity extends AppCompatActivity {
    ListView list;
    String names[];
    ArrayAdapter<String> adapter;
    CaregiverClass caregiver;
    CaregiverClass[] caregiverList;
    Patient patient;
    Patient[] patientList;
    private String name, type;
    String userName, patientName;
    ArrayList <String> p=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver_home_page);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("USERNAME");
            type = extras.getString("TYPE");

            // Toast.makeText(getApplicationContext(), "Welcome "+name, Toast.LENGTH_SHORT).show();
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
                Intent intent = new Intent(caregiver_homePage_activity.this, personal_info_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);


            }
        });

        Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(caregiver_homePage_activity.this, Schedule_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(caregiver_homePage_activity.this, Add_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        SOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(caregiver_homePage_activity.this, SOS_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        //////////////////////////////  end toolbar button//////////////////////////////////////////////

        ////////////// read from database///////////////////////////
        list = (ListView) findViewById(R.id.patientList);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               // Intent intent = new Intent(caregiver_homePage_activity.this, Profile_Activity.class);
               // intent.putExtra("USERNAME", name);
               // intent.putExtra("TYPE", type);
               // intent.putExtra("PatientName", p.get(i));
                //startActivity(intent);
                Toast.makeText(getApplicationContext(), "Welcome "+p.get(i), Toast.LENGTH_SHORT).show();
            }
        });
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        list.setAdapter(adapter);
        new ConnectionToReadPatient().execute();

    }

    ///////////////////////////// class for read from DB ///////////////////////////////////////////////////////////////////
    class ConnectionToReadPatient extends AsyncTask<String, String, String> {

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
                        int id = patientObject.getInt("id");
                        userName = patientObject.getString("userNameC");
                        patientName = patientObject.getString("userNameP");

                        if (userName.equalsIgnoreCase(name)) {
                            p.add(patientName);
                            String line = id + " - " + patientName;
                            adapter.add(line);
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