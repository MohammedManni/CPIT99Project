package com.example.safemedicare;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

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
import java.util.Calendar;

public class Add_event_from_calendar extends AppCompatActivity {

    private String name, type, selectedDateString;
    EditText eventNameET, DescriptionET;
    TimePicker timePicker;
    Button date;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("USERNAME");
            type = extras.getString("TYPE");
           // selectedDateString = extras.getString("DATE");

        }
        // toolbar buttons
        Button Profile = findViewById(R.id.firstB);
        Button Schedule = findViewById(R.id.SecondB);
        Button Add = findViewById(R.id.thirdB);
        Button SOS = findViewById(R.id.SOS);
        ImageButton imageButton = findViewById(R.id.imageButton);

        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Add_event_from_calendar.this, Profile_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Add_event_from_calendar.this, Schedule_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Add_event_from_calendar.this, Add_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        SOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Add_event_from_calendar.this, SOS_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equalsIgnoreCase("patient")) {
                    Intent intent = new Intent(Add_event_from_calendar.this, Home_Page_Activity.class);
                    intent.putExtra("USERNAME", name);
                    intent.putExtra("TYPE", type);
                    startActivity(intent);

                } else if (type.equalsIgnoreCase("caregiver")) {
                    Intent intent = new Intent(Add_event_from_calendar.this, caregiver_homePage_activity.class);
                    intent.putExtra("USERNAME", name);
                    intent.putExtra("TYPE", type);
                    startActivity(intent);

                }
            }
        });

        ///////////////////////END TOOLBAR BUTTON//////////////////////////////////////////

        // timeline button    dateView
        eventNameET = findViewById(R.id.eventNameET);
        DescriptionET = findViewById(R.id.DescriptionET);

        Button buttonSave = findViewById(R.id.buttonSave);
        timePicker = (TimePicker) findViewById(R.id.timePicker); // initiate a time picker
        timePicker.setCurrentHour(12); // before api level 23
        timePicker.setHour(12); // from api level 23

        date = (Button) findViewById(R.id.date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(Add_event_from_calendar.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // set day of month , month and year value in the edit text
                         date.setText(dayOfMonth + "/" + (monthOfYear+1) + "/" + year);
                         }
                         }, mYear, mMonth, mDay);
                         datePickerDialog.show();
                         }
                         });


        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (eventNameET.getText().toString().isEmpty()) {
                    eventNameET.setError("ENTER the event Name ");

                } else if (DescriptionET.getText().toString().isEmpty()) {
                    DescriptionET.setError("ENTER the Description ");

                } else {
                    AddEvent(view);
                    eventNameET.setText("");
                    DescriptionET.setText("");
                    date.setText("Select Date...");
                }
            }
        });

        Button buttonBack = findViewById(R.id.buttonBack);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Add_event_from_calendar.this, Schedule_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });
    }

    public void AddEvent(View view) {
        int hours = timePicker.getHour(); // after api level 23
        int minutes = timePicker.getMinute(); // after api level 23
        String operation = "AddEvent";
        String username = name;
        String type1 = type;
        String eventName = eventNameET.getText().toString();
        String Description = DescriptionET.getText().toString();
        String Date = date.getText().toString();
        String timeH = String.valueOf(hours);
        String timeM = String.valueOf(minutes);


        db1BackgroundWorker db1BackgroundWorker = new db1BackgroundWorker(this);
        db1BackgroundWorker.execute(operation, username, type1, eventName, Description, Date, timeH, timeM);

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////// Add to DB //////////////////////////////////////////////////////////////////////////
    public class db1BackgroundWorker extends AsyncTask<String, Void, String> {
        Context context;
        AlertDialog alertDialog;

        db1BackgroundWorker(Context ctx) {
            context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            String operation = params[0];
            String login_url = "http://192.168.100.10/AddEvent.php";


            if (operation.equals("AddEvent")) {
                try {
                    String user_name = params[1];
                    String type1 = params[2];
                    String eventName = params[3];
                    String Description = params[4];
                    String Date = params[5];
                    String timeH = params[6];
                    String timeM = params[7];
                    URL url = new URL(login_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("userName", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8") + "&"
                            + URLEncoder.encode("type1", "UTF-8") + "=" + URLEncoder.encode(type1, "UTF-8") + "&"
                            + URLEncoder.encode("eventName", "UTF-8") + "=" + URLEncoder.encode(eventName, "UTF-8") + "&"
                            + URLEncoder.encode("Description", "UTF-8") + "=" + URLEncoder.encode(Description, "UTF-8") + "&"
                            + URLEncoder.encode("Date", "UTF-8") + "=" + URLEncoder.encode(Date, "UTF-8") + "&"
                            + URLEncoder.encode("timeH", "UTF-8") + "=" + URLEncoder.encode(timeH, "UTF-8") + "&"
                            + URLEncoder.encode("timeM", "UTF-8") + "=" + URLEncoder.encode(timeM, "UTF-8") + "&"
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
            alertDialog.setTitle("Add Status");
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