package com.example.safemedicare;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
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
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class caregiver_relative_control_Activity extends AppCompatActivity {
    String wantToDelete, wantToADD;

    ArrayList bankNamesDelete = new ArrayList<>();
    ArrayList bankNamesADD = new ArrayList<>();
    EditText editTextName;
    Switch switchMedicationLog, switchSchedule, switchTimeline, switchAddCaregiver;

    private String name, type, user_name;

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
        switchMedicationLog = (Switch) findViewById(R.id.switchMedicationLog);
        switchSchedule = (Switch) findViewById(R.id.switchSchedule);
        switchTimeline = (Switch) findViewById(R.id.switchTimeline);
        switchAddCaregiver = (Switch) findViewById(R.id.switchAddCaregiver);
        Button add = findViewById(R.id.addPatinetORcaregiver);
        Button delete = findViewById(R.id.deletePatinetORcaregiver);
        Button BackBT = findViewById(R.id.BackBT);
        BackBT.setVisibility(View.GONE);

        Spinner spin = (Spinner) findViewById(R.id.simpleSpinner);
        Spinner spinADD = (Spinner) findViewById(R.id.simpleSpinnerADD);

        refresh();

        // Toast.makeText(getApplicationContext(), spin.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, bankNamesDelete);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                wantToDelete = bankNamesDelete.get(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        ArrayAdapter arrayAdapterADD = new ArrayAdapter(this, android.R.layout.simple_spinner_item, bankNamesADD);
        arrayAdapterADD.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinADD.setAdapter(arrayAdapterADD);
        spinADD.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                wantToADD = bankNamesADD.get(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //another seiner ready to show all the caregivers
        spinADD.setVisibility(View.GONE);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!switchMedicationLog.isChecked() && !switchSchedule.isChecked() && !switchTimeline.isChecked() && !switchAddCaregiver.isChecked()) {

                    Toast.makeText(getApplicationContext(), "Please choose at least one option", Toast.LENGTH_LONG).show();
                } else {

                    addCaregiver(view);
                    refresh();

                }

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteCaregiver(view);
                refresh();
            }
        });

        BackBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(caregiver_relative_control_Activity.this, Profile_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });


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

        ///////////////////////END TOOLBAR BUTTON//////////////////////////////////////////

    }

    public void refresh() {
        bankNamesDelete.clear();
        bankNamesDelete.add("Select Caregiver to DELETE");
        bankNamesADD.clear();
        bankNamesADD.add("Select Caregiver to ADD");
        new Connection().execute();
        new Connection1().execute();
    }

    public void addCaregiver(View view) {
        String operation = "AddCaregiver";
        String username = editTextName.getText().toString();
        String patientName = name;
        Boolean swMedLog = switchMedicationLog.isChecked();
        Boolean swSchedule = switchSchedule.isChecked();
        Boolean swTimeline = switchTimeline.isChecked();
        Boolean swAddAsCaregiver = switchAddCaregiver.isChecked();
        connection_to_DB backgroundWorker = new connection_to_DB(this);
        backgroundWorker.execute(operation, username, patientName, swMedLog.toString(), swSchedule.toString(), swTimeline.toString(), swAddAsCaregiver.toString());
    }

    public void deleteCaregiver(View view) {

        if (wantToDelete.matches("Select Caregiver to DELETE")) {
            Toast.makeText(getApplicationContext(), "Please Select a Caregiver", Toast.LENGTH_LONG).show();
        } else {
            String operation = "DeleteCaregiver";
            String username = wantToDelete;
            String patientName = name;
            connection_to_DB backgroundWorker = new connection_to_DB(this);
            backgroundWorker.execute(operation, username, patientName);
        }


    }

    ////////////////////// ADD CAREGIVER/////////////////////////////////////////////


    private class connection_to_DB extends AsyncTask<String, Void, String> {
        Context context;
        AlertDialog alertDialog;

        connection_to_DB(Context ctx) {
            context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            String operation = params[0];
            String addCaregiver = "http://192.168.100.10/pc.php";
            String deleteCaregiver = "http://192.168.100.171/Delete_From_PC.php";

            if (operation.equals("AddCaregiver")) {
                try {
                    String user_name = params[1];
                    String patientName = params[2];
                    String swMedLog = params[3];
                    String swSchedule = params[4];
                    String swTimeline = params[5];
                    String swAddAsCaregiver = params[6];
                    URL url = new URL(addCaregiver);
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
            } else if (operation.equals("DeleteCaregiver")) {
                try {
                    user_name = params[1];
                    String patientName = params[2];
                    URL url = new URL(deleteCaregiver);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("userName", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8") + "&"
                            + URLEncoder.encode("patient", "UTF-8") + "=" + URLEncoder.encode(patientName, "UTF-8");
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

            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Operation Status");
        }

        @Override
        protected void onPostExecute(String result) {

            // if caregiver was added already
            if (result.toString().equalsIgnoreCase("Already added")) {
                switchMedicationLog.setChecked(false);
                switchSchedule.setChecked(false);
                switchTimeline.setChecked(false);
                switchAddCaregiver.setChecked(false);
                alertDialog.setMessage(result);
                alertDialog.show();

            }
            // if caregiver was added successfully
            else if (result.toString().equalsIgnoreCase("The Caregiver was added")) {
                switchMedicationLog.setChecked(false);
                switchSchedule.setChecked(false);
                switchTimeline.setChecked(false);
                switchAddCaregiver.setChecked(false);
                editTextName.setText(null);
                alertDialog.setMessage(result);
                alertDialog.show();

            }
            // if caregiver was not found in the caregiver data base
            else if (result.toString().equalsIgnoreCase("No caregiver with that user name")) {
                switchMedicationLog.setChecked(false);
                switchSchedule.setChecked(false);
                switchTimeline.setChecked(false);
                switchAddCaregiver.setChecked(false);
                alertDialog.setMessage(result);
                alertDialog.show();
            }
            // if caregiver was deleted successfully
            else if (result.toString().equalsIgnoreCase(user_name + " was deleted")) {
                switchMedicationLog.setChecked(false);
                switchSchedule.setChecked(false);
                switchTimeline.setChecked(false);
                switchAddCaregiver.setChecked(false);
                editTextName.setText(null);
                alertDialog.setMessage(result);
                alertDialog.show();
            }
            // if caregiver was not found with the patient
            else if (result.toString().equalsIgnoreCase(user_name + " not found")) {
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

    class Connection extends AsyncTask<String, String, String> {
        // starting the connection
        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String medication_url = "http://192.168.100.10/readPC.php";
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
            try {
                JSONObject jsonResult = new JSONObject(result);
                int success = jsonResult.getInt("success");
                if (success == 1) {
                    JSONArray patientData = jsonResult.getJSONArray("patient");
                    for (int i = 0; i < patientData.length(); i++) {
                        JSONObject patientObject = patientData.getJSONObject(i);
                        String caregiverName = patientObject.getString("userNameC");
                        String patientName = patientObject.getString("userNameP");

                        if (patientName.equalsIgnoreCase(name)) {
                            bankNamesDelete.add(caregiverName.toUpperCase());
                        }




                    }
                } else if (success == 2) {
                    JSONArray caregiverData = jsonResult.getJSONArray("caregiver");
                    for (int i = 0; i < caregiverData.length(); i++) {
                        JSONObject patientObject = caregiverData.getJSONObject(i);
                        String userName = patientObject.getString("userName");

                        for (int j = 0; j < bankNamesDelete.size(); j++) {
                            if (!bankNamesDelete.get(i).toString().matches(userName)){
                                bankNamesADD.add(userName.toUpperCase());
                            }



                        }


                    }
                }else {
                    Toast.makeText(getApplicationContext(), "no there", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    class Connection1 extends AsyncTask<String, String, String> {
        // starting the connection
        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String medication_url = "http://192.168.100.10/readCaregiver.php";
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
            try {
                JSONObject jsonResult = new JSONObject(result);
                int success = jsonResult.getInt("success");
                if (success == 2) {
                    JSONArray caregiverData = jsonResult.getJSONArray("caregiver");
                    for (int i = 0; i < caregiverData.length(); i++) {
                        JSONObject patientObject = caregiverData.getJSONObject(i);
                        String userName = patientObject.getString("userName");


                            bankNamesADD.add(userName.toUpperCase());





                    }
                }else {
                    Toast.makeText(getApplicationContext(), "no there", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }
}