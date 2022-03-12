package com.example.safemedicare;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class caregiver_relative_control_Activity extends AppCompatActivity {
    CaregiverClass caregiver;
    CaregiverClass[] caregiverList;

    EditText editTextName ;
    Switch switchMedicationLog,switchSchedule,switchTimeline,switchAddCaregiver;

    private String name, type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caregiver_relative);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("USERNAME");
            type = extras.getString("TYPE");
        }
        editTextName = (EditText) findViewById(R.id.editTextTextPersonName);
        Button add = findViewById(R.id.addPatinetORcaregiver);
        Button delete = findViewById(R.id.deletePatinetORcaregiver);
        switchMedicationLog =(Switch) findViewById(R.id.switchMedicationLog);
        switchSchedule = (Switch)findViewById(R.id.switchSchedule);
        switchTimeline = (Switch)findViewById(R.id.switchTimeline);
        switchAddCaregiver = (Switch)findViewById(R.id.switchAddCaregiver);



        // toolbar buttons
        Button Profile = findViewById(R.id.firstB);
        Button Schedule = findViewById(R.id.SecondB);
        Button Add = findViewById(R.id.thirdB);
        Button SOS = findViewById(R.id.SOS);
        ImageButton imageButton = findViewById(R.id.imageButton);

        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(caregiver_relative_control_Activity.this, Profile_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(caregiver_relative_control_Activity.this, Schedule_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(caregiver_relative_control_Activity.this, Add_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        SOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(caregiver_relative_control_Activity.this, SOS_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equalsIgnoreCase("patient")) {
                    Intent intent = new Intent(caregiver_relative_control_Activity.this, Home_Page_Activity.class);
                    intent.putExtra("USERNAME", name);
                    intent.putExtra("TYPE", type);
                    startActivity(intent);

                } else if (type.equalsIgnoreCase("caregiver")) {
                    Intent intent = new Intent(caregiver_relative_control_Activity.this, caregiver_homePage_activity.class);
                    intent.putExtra("USERNAME", name);
                    intent.putExtra("TYPE", type);
                    startActivity(intent);

                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addCaregiver(view);
            }
        });

        ///////////////////////END TOOLBAR BUTTON//////////////////////////////////////////

    }
    public void addCaregiver(View view) {
        String username = editTextName.getText().toString();
        String patientName = name;
        Boolean swMedLog=switchMedicationLog.isChecked();
        Boolean swSchedule=switchSchedule.isChecked();
        Boolean swTimeline=switchTimeline.isChecked();
        Boolean swAddAsCaregiver=switchAddCaregiver.isChecked();
        db1BackgroundWorker backgroundWorker = new db1BackgroundWorker(this);
        backgroundWorker.execute(username,patientName,swMedLog.toString(),swSchedule.toString(),swTimeline.toString(),swAddAsCaregiver.toString());
    }

    ////////////////////// ADD CAREGIVER/////////////////////////////////////////////


    private class db1BackgroundWorker extends AsyncTask<String, Void, String> {
        Context context;
        AlertDialog alertDialog;

        db1BackgroundWorker(Context ctx) {
            context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {

            String login_url = "http://192.168.100.193/pc.php";


            try {
                String user_name = params[0];
                String patientName = params[1];
                String swMedLog = params[2];
                String swSchedule = params[3];
                String swTimeline = params[4];
                String swAddAsCaregiver = params[5];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("userName", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8")
                        + "&" + URLEncoder.encode("patient", "UTF-8") + "=" + URLEncoder.encode(patientName, "UTF-8")
                        + "&" + URLEncoder.encode("swMedLog", "UTF-8") + "=" + URLEncoder.encode(swMedLog, "UTF-8")
                        + "&" + URLEncoder.encode("swSchedule", "UTF-8") + "=" + URLEncoder.encode(swSchedule, "UTF-8")
                        + "&" + URLEncoder.encode("swTimeline", "UTF-8") + "=" + URLEncoder.encode(swTimeline, "UTF-8")
                        + "&" + URLEncoder.encode("swAddAsCaregiver", "UTF-8") + "=" + URLEncoder.encode(swAddAsCaregiver, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return result;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPreExecute() {
            alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Login Status");
        }

        @Override
        protected void onPostExecute(String result) {
            // if patient

            if (result.toString().equalsIgnoreCase("Already added")) {
                editTextName.setText(null);
                switchMedicationLog.setChecked(false);
                switchSchedule.setChecked(false);
                switchTimeline.setChecked(false);
                switchAddCaregiver.setChecked(false);

                alertDialog.setMessage(result);
                alertDialog.show();

            }
            // if caregiver
            else if (result.toString().equalsIgnoreCase("DONE")) {
                editTextName.setText(null);
                switchMedicationLog.setChecked(false);
                switchSchedule.setChecked(false);
                switchTimeline.setChecked(false);
                switchAddCaregiver.setChecked(false);
                alertDialog.setMessage(result);
                alertDialog.show();

            } else if (result.toString().equalsIgnoreCase("User not found ")) {
                switchMedicationLog.setChecked(false);
                switchSchedule.setChecked(false);
                switchTimeline.setChecked(false);
                switchAddCaregiver.setChecked(false);
                alertDialog.setMessage(result);
                alertDialog.show();
            }



        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }

}