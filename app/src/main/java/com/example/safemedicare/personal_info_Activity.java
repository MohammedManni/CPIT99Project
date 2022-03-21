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

public class personal_info_Activity extends AppCompatActivity {

    private String name, type, userName;
    CaregiverClass caregiver;

    EditText UserNameINProfileEDITText, PersonNameINProfileEditText, PhoneNumberINProfileEditText, AgeEditText, ConfirmPassword, Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_info);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("USERNAME");
            type = extras.getString("TYPE");
            Button SecondB = findViewById(R.id.SecondB);
            if (type.matches("caregiver")){
                SecondB.setVisibility(View.GONE);
            }
            new ConnectionToCaregiver().execute();
        }
        caregiver = new CaregiverClass();
        UserNameINProfileEDITText = (EditText) findViewById(R.id.UserNameINProfileEDITText);
        PersonNameINProfileEditText = (EditText) findViewById(R.id.PersonNameINProfileEditText);
        PhoneNumberINProfileEditText = (EditText) findViewById(R.id.PhoneNumberINProfileEditText);
        AgeEditText = (EditText) findViewById(R.id.AgeEditText);
        Password = (EditText) findViewById(R.id.PasswordINProfileEditText);
        ConfirmPassword = (EditText) findViewById(R.id.ConPasswordINProfileEditText);


        // toolbar buttons
        Button Profile = findViewById(R.id.firstB);
        Button Schedule = findViewById(R.id.SecondB);
        Button Add = findViewById(R.id.thirdB);
        Button SOS = findViewById(R.id.SOS);
        ImageButton imageButton = findViewById(R.id.imageButton);

        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equalsIgnoreCase("patient")) {
                    Intent intent = new Intent(personal_info_Activity.this, Profile_Activity.class);
                    intent.putExtra("USERNAME", name);
                    intent.putExtra("TYPE", type);
                    startActivity(intent);

                } else if (type.equalsIgnoreCase("caregiver")) {
                    Intent intent = new Intent(personal_info_Activity.this, personal_info_Activity.class);
                    intent.putExtra("USERNAME", name);
                    intent.putExtra("TYPE", type);
                    startActivity(intent);

                }
            }
        });

        Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(personal_info_Activity.this, Schedule_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(personal_info_Activity.this, Add_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        SOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(personal_info_Activity.this, SOS_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equalsIgnoreCase("patient")) {
                    Intent intent = new Intent(personal_info_Activity.this, Home_Page_Activity.class);
                    intent.putExtra("USERNAME", name);
                    intent.putExtra("TYPE", type);
                    startActivity(intent);

                } else if (type.equalsIgnoreCase("caregiver")) {
                    Intent intent = new Intent(personal_info_Activity.this, caregiver_homePage_activity.class);
                    intent.putExtra("USERNAME", name);
                    intent.putExtra("TYPE", type);
                    startActivity(intent);

                }
            }
        });

        ///////////////////////END TOOLBAR BUTTON//////////////////////////////////////////

        Button saveChange = findViewById(R.id.saveChange);
        saveChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(personal_info_Activity.this, sign_activity.class);
                //startActivity(intent);


                if (!Password.getText().toString().equalsIgnoreCase("") && !ConfirmPassword.getText().toString().equalsIgnoreCase("")) {
                    // the password match check
                    if (Password.getText().toString().equals(ConfirmPassword.getText().toString())) {

                        // update the information
                        PasswordUpdate(view);
                        Password.setText(null);
                        ConfirmPassword.setText(null);

                    } else {// else for not matching pass
                        Password.setText(null);
                        ConfirmPassword.setText(null);
                        Toast.makeText(personal_info_Activity.this, "the Password not matching", Toast.LENGTH_LONG).show();
                    }

                } else {// else for phone number update
                    PhoneUpdate(view);
                    //Toast.makeText(personal_info_Activity.this, "Phone Number update", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    public void PasswordUpdate(View view) {

        String operation = "password";
        String username = name;
        String type1 = type;

        String password = Password.getText().toString();

        db1BackgroundWorker db1BackgroundWorker = new db1BackgroundWorker(this);
        db1BackgroundWorker.execute(operation, username, type1, password);

    }
    public void PhoneUpdate(View view) {

        String type1 = type;
        String username = name;
        String operation = "phoneNumber";
        String phoneNumber =  PhoneNumberINProfileEditText.getText().toString();


        db1BackgroundWorker db1BackgroundWorker = new db1BackgroundWorker(this);
        db1BackgroundWorker.execute(operation, username, type1, phoneNumber);

    }

    ///////////////////////////// DO NOT CHANGE ANYTHING ///////////////////////////////////////////////////////////////////
    ///////////////////////////// class for read from DB ///////////////////////////////////////////////////////////////////
    class ConnectionToCaregiver extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String readPatient_url = "http://192.168.100.171/returnINFO.php";
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

                        userName = patientObject.getString("userName");


                        if (userName.equalsIgnoreCase(name)) {
                            String fullName = patientObject.getString("name");
                            int phoneNumber = Integer.parseInt(patientObject.getString("phoneNumber"));
                            int age = Integer.parseInt(patientObject.getString("age"));

                            caregiver.setId(Integer.parseInt(patientObject.getString("id")));
                            caregiver.setUsername(userName);
                            caregiver.setFullName(fullName);
                            caregiver.setPhone_number(phoneNumber);
                            caregiver.setAge(age);

                            UserNameINProfileEDITText.setText(caregiver.getUsername());
                            PersonNameINProfileEditText.setText(caregiver.getFullName());
                            PhoneNumberINProfileEditText.setText(String.valueOf(caregiver.getPhone_number()));
                            AgeEditText.setText(String.valueOf(caregiver.getAge()));
                            UserNameINProfileEDITText.setEnabled(false);
                            PersonNameINProfileEditText.setEnabled(false);

                            AgeEditText.setEnabled(false);

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
    ///////////////////////////// DO NOT CHANGE ANYTHING  ( UNTIL HERE ) ///////////////////////////////////////////////////////////////////

    ///////////////////////////////////// Update class ///////////////////////////////////////////////////////////////////////////////////
    public class db1BackgroundWorker extends AsyncTask<String, Void, String> {
        Context context;
        AlertDialog alertDialog;

        db1BackgroundWorker(Context ctx) {
            context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            String operation = params[0];
            String UrlPaasPhone = "http://192.168.100.171/UpdatePassword.php";

            if (operation.equals("password")) {
                try {
                    String user_name = params[1];
                    String password = params[3];
                    String type1 = params[2];
                    String phoneNumber = " ";
                    URL url = new URL(UrlPaasPhone);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("userName", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8") + "&"
                            + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8")+ "&"
                            + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type1, "UTF-8")+ "&"
                            + URLEncoder.encode("Phone_number", "UTF-8") + "=" + URLEncoder.encode(phoneNumber, "UTF-8")+ "&"
                            + URLEncoder.encode("operation", "UTF-8") + "=" + URLEncoder.encode(operation, "UTF-8");
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

            } else if (operation.equals("phoneNumber")) {
                try {
                    String user_name = params[1];
                    String password = " ";
                    String type1 = params[2];
                    String phoneNumber = params[3];
                    URL url = new URL(UrlPaasPhone);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("userName", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8") + "&"
                            + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8")+ "&"
                            + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type1, "UTF-8")+ "&"
                            + URLEncoder.encode("Phone_number", "UTF-8") + "=" + URLEncoder.encode(phoneNumber, "UTF-8")+ "&"
                            + URLEncoder.encode("operation", "UTF-8") + "=" + URLEncoder.encode(operation, "UTF-8");
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
            alertDialog.setTitle("Update Status");
        }

        @Override
        protected void onPostExecute(String result) {
                alertDialog.setMessage(result);
                alertDialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

    }
}