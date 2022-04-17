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
import java.util.Calendar;

public class Event_Adjustment extends AppCompatActivity {
    private String name, type;
    int eventId;
    EditText nameOfEventET, DescriptionEditText;
    TimePicker timePicker;
    Button date;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event__adjustment);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("USERNAME");
            type = extras.getString("TYPE");
            eventId = extras.getInt("EVENT_ID");
        }
        new ConnectionToReadEvent().execute();

        nameOfEventET = findViewById(R.id.nameOfMedicineET);
        DescriptionEditText = findViewById(R.id.DescriptionEditText);

        //////////////////////////////////////////////////////////////////////////////////
        //toolbar
        toolbar();

        ////////////////////////////////UPDATE EVENTS///////////////////////////////////////////////////
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
                datePickerDialog = new DatePickerDialog(Event_Adjustment.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // set day of month , month and year value in the edit text
                        date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


        Button update = findViewById(R.id.updateButton);
        Button delete = findViewById(R.id.deleteButton);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!nameOfEventET.getText().toString().equalsIgnoreCase("") && !DescriptionEditText.getText().toString().equalsIgnoreCase("")) {

                    //update the information
                    updateEvent(view);
                } else {
                    nameOfEventET.setError("ENTER the Event Name ");
                    DescriptionEditText.setError("ENTER the Description ");
                }

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // Delete the information
                DeleteEvent(view);


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
                Intent intent = new Intent(Event_Adjustment.this, Patient_Profile_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Event_Adjustment.this, Schedule_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Event_Adjustment.this, Add_Medicine_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        SOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Event_Adjustment.this, SOS_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equalsIgnoreCase("patient")) {
                    Intent intent = new Intent(Event_Adjustment.this, Home_Page_Activity.class);
                    intent.putExtra("USERNAME", name);
                    intent.putExtra("TYPE", type);
                    startActivity(intent);

                } else if (type.equalsIgnoreCase("caregiver")) {
                    Intent intent = new Intent(Event_Adjustment.this, caregiver_homePage_activity.class);
                    intent.putExtra("USERNAME", name);
                    intent.putExtra("TYPE", type);
                    startActivity(intent);

                }
            }
        });


        ///////////////////////END TOOLBAR BUTTON//////////////////////////////////////////

    }

    ////////////////////////////////METHOD FOR UPDATE////////////////////////////////////////////////////////////////
    public void updateEvent(View view) {
        int hours = timePicker.getHour();
        int minutes = timePicker.getMinute();
        String timeH = String.valueOf(hours);
        String timeM = String.valueOf(minutes);
        String operation = "update";
        String nameOfEvent = nameOfEventET.getText().toString();
        String descriptionOfEvent = DescriptionEditText.getText().toString();
        String dateOfEvent = date.getText().toString();
        String eventID = String.valueOf(eventId);
        UpdateEvent db1BackgroundWorker = new UpdateEvent(this);
        db1BackgroundWorker.execute(operation, nameOfEvent, descriptionOfEvent, dateOfEvent, timeH, timeM, eventID);

    }

    public void DeleteEvent(View view) {

        String operation = "delete";
        String eventID = String.valueOf(eventId);
        UpdateEvent db1BackgroundWorker = new UpdateEvent(this);
        db1BackgroundWorker.execute(operation, eventID);

    }

    ///////////////////////////// class for read from DB ///////////////////////////////////////////////////////////////////
    class ConnectionToReadEvent extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String readEvent_url = "http://192.168.100.171/readEvent.php";
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(readEvent_url));
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
                    JSONArray eventData = jsonResult.getJSONArray("event");
                    for (int i = 0; i < eventData.length(); i++) {
                        JSONObject eventObject = eventData.getJSONObject(i);
                        int id = eventObject.getInt("id");


                        if (id == eventId) {
                            String eventName = eventObject.getString("eventName");
                            String eventDescription = eventObject.getString("eventDescription");
                            String date1 = eventObject.getString("date");
                            String timeH = eventObject.getString("timeH");
                            String timeM = eventObject.getString("timeM");
                            nameOfEventET.setText(eventName);
                            DescriptionEditText.setText(eventDescription);
                            date.setText(date1);
                            timePicker.setHour(Integer.parseInt(timeH));
                            timePicker.setMinute(Integer.parseInt(timeM));

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

    ///////////////////////////////////// Update class ///////////////////////////////////////////////////////////////////////////////////
    public class UpdateEvent extends AsyncTask<String, Void, String> {
        Context context;
        AlertDialog alertDialog;

        UpdateEvent(Context ctx) {
            context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            String operation = params[0];
            String Url_updateEvent = "http://192.168.100.171/updateEvent.php";

            if (operation.equals("update")) {
                try {
                    String event_Name = params[1];
                    String eventDescription = params[2];
                    String date = params[3];
                    String timeH = params[4];
                    String timeM = params[5];
                    String eventID = params[6];
                    URL url = new URL(Url_updateEvent);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("eventName", "UTF-8") + "=" + URLEncoder.encode(event_Name, "UTF-8") + "&"
                            + URLEncoder.encode("eventDescription", "UTF-8") + "=" + URLEncoder.encode(eventDescription, "UTF-8") + "&"
                            + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8") + "&"
                            + URLEncoder.encode("timeH", "UTF-8") + "=" + URLEncoder.encode(timeH, "UTF-8") + "&"
                            + URLEncoder.encode("timeM", "UTF-8") + "=" + URLEncoder.encode(timeM, "UTF-8") + "&"
                            + URLEncoder.encode("operation", "UTF-8") + "=" + URLEncoder.encode(operation, "UTF-8") + "&"
                            + URLEncoder.encode("eventID", "UTF-8") + "=" + URLEncoder.encode(eventID, "UTF-8");
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

            } else if (operation.equals("delete")) {
                try {
                    // avoid null reference
                    String event_Name = " ";
                    String eventDescription = " ";
                    String date = " ";
                    String timeH = " ";
                    String timeM = " ";
                    String eventID = params[1];
                    URL url = new URL(Url_updateEvent);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String post_data = URLEncoder.encode("eventName", "UTF-8") + "=" + URLEncoder.encode(event_Name, "UTF-8") + "&"
                            + URLEncoder.encode("eventDescription", "UTF-8") + "=" + URLEncoder.encode(eventDescription, "UTF-8") + "&"
                            + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8") + "&"
                            + URLEncoder.encode("timeH", "UTF-8") + "=" + URLEncoder.encode(timeH, "UTF-8") + "&"
                            + URLEncoder.encode("timeM", "UTF-8") + "=" + URLEncoder.encode(timeM, "UTF-8") + "&"
                            + URLEncoder.encode("operation", "UTF-8") + "=" + URLEncoder.encode(operation, "UTF-8") + "&"
                            + URLEncoder.encode("eventID", "UTF-8") + "=" + URLEncoder.encode(eventID, "UTF-8");
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
            alertDialog.setTitle("Event Status");
        }

        @Override
        protected void onPostExecute(String result) {


            if (result.toString().equalsIgnoreCase("Event Updated")) {

                alertDialog.setMessage(result);
                alertDialog.show();


            } else if (result.toString().equalsIgnoreCase("Event Deleted")) {
                alertDialog.setMessage(result);
                alertDialog.show();
            } else {
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