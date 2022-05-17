package com.example.safemedicare;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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

public class register1Activity extends AppCompatActivity {
    EditText Name, PhoneNumber, Age, Username, Password, ConfirmPassword;
    RadioGroup radioGroup;
    RadioButton radioButtonP, radioButtonC;
    Context context;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_1);
        // Edit text
        Username = (EditText) findViewById(R.id.UserNameRE);
        Name = (EditText) findViewById(R.id.PersonName);
        PhoneNumber = (EditText) findViewById(R.id.PhoneNumber);
        Age = (EditText) findViewById(R.id.Age);
        Password = (EditText) findViewById(R.id.Password1);
        ConfirmPassword = (EditText) findViewById(R.id.ConPassword1);
        // Radio Button
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioButtonP = (RadioButton) findViewById(R.id.PatientRadioButton);
        radioButtonC = (RadioButton) findViewById(R.id.CaregiverRadioButton2);


        // continue button
        Button Continue = findViewById(R.id.Continue);
        Continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // check is all the input are filled
                if ((Username.getText().toString().equalsIgnoreCase("") || Name.getText().toString().equalsIgnoreCase("") ||
                        PhoneNumber.getText().toString().equalsIgnoreCase("") || Age.getText().toString().equalsIgnoreCase("") ||
                        Password.getText().toString().equalsIgnoreCase("") || ConfirmPassword.getText().toString().equalsIgnoreCase(""))
                        || (!radioButtonP.isChecked() && !radioButtonC.isChecked())) {
                    // toast test to fill all the input
                    Toast.makeText(getApplicationContext(), "Please fill all the requirement ", Toast.LENGTH_SHORT).show();

                    if (Username.getText().toString().isEmpty()) {
                        Username.setError("ENTER the User Name ");

                    }
                    if (Name.getText().toString().isEmpty()) {
                        Name.setError("ENTER your full Name ");

                    }
                    if (PhoneNumber.getText().toString().isEmpty()) {
                        PhoneNumber.setError("ENTER your Phone Number ");

                    }
                    if (Age.getText().toString().isEmpty()) {
                        Age.setError("ENTER your Age ");

                    }
                    if (Password.getText().toString().isEmpty()) {
                        Password.setError("ENTER the Password ");

                    }
                    if (ConfirmPassword.getText().toString().isEmpty()) {
                        ConfirmPassword.setError("Confirm the Password ");

                    }
                    if ((!radioButtonP.isChecked() && !radioButtonC.isChecked())) {
                        radioButtonP.setError("Choose one please ");
                        radioButtonC.setError("Choose one please ");

                    }



                } else { // else whine all the input filled

                    // the password match check
                    if (Password.getText().toString().equals(ConfirmPassword.getText().toString())) {
                        // the check if patient or care giver
                        if (radioButtonP.isChecked()) {
                            // send to database
                            registerPatient(view);



                        } else if (radioButtonC.isChecked()) {
                            // send to database
                            registerCaregiver(view);
                            // end to database


                        } else {

                        }

                    } else {// else for not matching pass
                        Password.setText("");
                        ConfirmPassword.setText("");
                        Password.setError("ENTER the Password ");
                        ConfirmPassword.setError("Confirm the Password ");
                        Toast.makeText(getApplicationContext(), "The password not matching", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });


        // Back button
        Button Back = findViewById(R.id.Back);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(register1Activity.this, sign_activity.class);
                startActivity(intent);
            }
        });

    }

    public void registerPatient(View view) {
        String username = Username.getText().toString();
        String name = Name.getText().toString();
        String phoneNumber = PhoneNumber.getText().toString();
        String age = Age.getText().toString();
        String password = Password.getText().toString();
        String type = "register";
        registerUser registerUser = new registerUser(this);
        registerUser.execute(type, username, name, phoneNumber, age, password);
    }

    public void registerCaregiver(View view) {
        String username = Username.getText().toString();
        String name = Name.getText().toString();
        String phoneNumber = PhoneNumber.getText().toString();
        String age = Age.getText().toString();
        String password = Password.getText().toString();
        String type = "registerC";
        registerUser registerUser = new registerUser(this);
        registerUser.execute(type, username, name, phoneNumber, age, password);
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public class registerUser extends AsyncTask<String, Void, String> {
        Context context;
        AlertDialog alertDialog;

        registerUser(Context ctx) {
            context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String register_url = "http://192.168.100.126/register.php";
            String registerC_url = "http://192.168.100.126/registerC.php";

            if (type.equals("register")) {
                try {
                    String username = params[1];
                    String name = params[2];
                    String phoneNumber = params[3];
                    String age = params[4];
                    String password = params[5];


                    URL url = new URL(register_url);

                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("userName", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&"
                            + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&"
                            + URLEncoder.encode("phoneNumber", "UTF-8") + "=" + URLEncoder.encode(phoneNumber, "UTF-8") + "&"
                            + URLEncoder.encode("age", "UTF-8") + "=" + URLEncoder.encode(age, "UTF-8") + "&"
                            + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

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
            } else if (type.equals("registerC")) {
                try {
                    String username = params[1];
                    String name = params[2];
                    String phoneNumber = params[3];
                    String age = params[4];
                    String password = params[5];



                    URL url = new URL(registerC_url);

                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("userName", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&"
                            + URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(name, "UTF-8") + "&"
                            + URLEncoder.encode("phoneNumber", "UTF-8") + "=" + URLEncoder.encode(phoneNumber, "UTF-8") + "&"
                            + URLEncoder.encode("age", "UTF-8") + "=" + URLEncoder.encode(age, "UTF-8") + "&"
                            + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8") ;

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
            alertDialog.setTitle("Register Status");
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.toString().equalsIgnoreCase("User name already exist !!! ")) {
                alertDialog.setMessage(result);
                alertDialog.show();

            }
            // if was patient
            else if (result.toString().equalsIgnoreCase("Patient Register successfully")) {
                // end to database
                Intent intent = new Intent(register1Activity.this, sign_activity.class);
                intent.putExtra("USERNAME", Username.getText().toString());
                intent.putExtra("TYPE", "patient");
                startActivity(intent);

            }
            // if was patient
             else if (result.toString().equalsIgnoreCase("Caregiver Register successfully")) {
                // Caregiver home page
                Intent intent = new Intent(register1Activity.this, sign_activity.class);
                intent.putExtra("USERNAME", Username.getText().toString());
                intent.putExtra("TYPE", "caregiver");
                startActivity(intent);
            }

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }


}