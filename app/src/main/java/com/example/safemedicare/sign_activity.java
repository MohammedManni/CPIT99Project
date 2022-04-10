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

public class sign_activity extends AppCompatActivity {
    EditText Username, Password;
    RadioButton radioButtonC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        Username = (EditText) findViewById(R.id.UserName);
        Password = (EditText) findViewById(R.id.Password);

        radioButtonC = (RadioButton) findViewById(R.id.CaregiverRadioButton2);

        Button register = findViewById(R.id.Register);
        Button logIn = findViewById(R.id.LogIN);


        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Username.getText().toString().isEmpty() && Password.getText().toString().isEmpty()) {
                    Username.setError("ENTER the User Name ");
                    Password.setError("ENTER the Password ");

                } else if (Username.getText().toString().isEmpty()) {
                    Username.setError("ENTER the User Name ");

                } else if (Password.getText().toString().isEmpty()) {
                    Password.setError("ENTER the Password ");

                } else {
                    OnLogin(view);
                }
                // check caregiver
                // Username.onSaveInstanceState();
                // end caregiver

                // if for empty edit text

             /*
                Intent myIntent = new Intent(sign_activity.this, Decide_Class.class);
                myIntent.putExtra("USERNAME", Username.getText().toString());
                myIntent.putExtra("PASSWORD", Password.getText().toString());
                startActivity(myIntent);

              */
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sign_activity.this, register1Activity.class);
                startActivity(intent);
            }
        });

    }

    public void OnLogin(View view) {
        String username = Username.getText().toString().trim();
        String password = Password.getText().toString().trim();
        String type = "login";
        db1BackgroundWorker backgroundWorker = new db1BackgroundWorker(this);
        backgroundWorker.execute(type, username, password);
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private class db1BackgroundWorker extends AsyncTask<String, Void, String> {
        Context context;
        AlertDialog alertDialog;

        db1BackgroundWorker(Context ctx) {
            context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String login_url = "http://192.168.100.10/login.php";


            try {
                String user_name = params[1];
                String password = params[2];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("userName", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8") + "&"
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
            if (result.toString().equalsIgnoreCase("patient")) {
                Intent myIntent = new Intent(sign_activity.this, Home_Page_Activity.class);
                myIntent.putExtra("USERNAME", Username.getText().toString());
                myIntent.putExtra("TYPE", "patient");
                startActivity(myIntent);

            }
            // if caregiver
            else if (result.toString().equalsIgnoreCase("caregiver")) {
                Intent myIntent = new Intent(sign_activity.this, caregiver_homePage_activity.class);
                myIntent.putExtra("USERNAME", Username.getText().toString());
                myIntent.putExtra("TYPE", "caregiver");
                startActivity(myIntent);

            } else if (result.toString().equalsIgnoreCase("login not success")) {
                alertDialog.setMessage(result);
                alertDialog.show();
            }else {
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