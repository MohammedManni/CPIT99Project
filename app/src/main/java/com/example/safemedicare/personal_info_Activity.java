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

public class personal_info_Activity extends AppCompatActivity {

    private String name, type, userName;
    CaregiverClass caregiver;

    EditText UserNameINProfileEDITText ,PersonNameINProfileEditText , PhoneNumberINProfileEditText , AgeEditText,ConfirmPassword,Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_info);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("USERNAME");
            type = extras.getString("TYPE");
            new ConnectionToCaregiver().execute();
        }

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
                        OnUpdate(view);


                    } else {// else for not matching pass
                        Toast.makeText(personal_info_Activity.this, "the Password not matching", Toast.LENGTH_LONG).show();
                    }

                } else {// else for not matching pass
                    Toast.makeText(personal_info_Activity.this, "Nothing to update", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    public void OnUpdate(View view) {

       String username = caregiver.getUsername();
        String name = caregiver.getFullName();
        String phoneNumber = String.valueOf(caregiver.getPhone_number());
        String age = String.valueOf(caregiver.getAge());
        String password = Password.getText().toString();
        String type = "UpdatePass";
        db1BackgroundWorker db1BackgroundWorker = new db1BackgroundWorker(this);
        db1BackgroundWorker.execute(type, username, name, phoneNumber, age, password);

    }

    ///////////////////////////// DO NOT CHANGE ANYTHING ///////////////////////////////////////////////////////////////////
    ///////////////////////////// class for read from DB ///////////////////////////////////////////////////////////////////
    class ConnectionToCaregiver extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String readPatient_url = "http://192.168.100.193/returnINFO.php";
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


                        if (userName.equalsIgnoreCase(name)){
                            String fullName = patientObject.getString("name");
                            String phoneNumber = patientObject.getString("phoneNumber");
                            String age = patientObject.getString("age");

                            UserNameINProfileEDITText.setText(userName);
                            PersonNameINProfileEditText.setText(fullName);
                            PhoneNumberINProfileEditText .setText(phoneNumber);
                            AgeEditText.setText(age);

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
}