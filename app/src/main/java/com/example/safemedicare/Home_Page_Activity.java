package com.example.safemedicare;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Home_Page_Activity extends AppCompatActivity {

    private String name, type, userName, formattedDate;
    GridView gridList;
    ArrayList eventList = new ArrayList<>();
    ArrayList<Event> eventlistChild = new ArrayList<>();
    ArrayList medicationList = new ArrayList<>();
    ArrayList medicationChild = new ArrayList();
    GridAdapter myAdapter;
    String Meridien;
    int timeIn12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_patient);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("USERNAME");
            type = extras.getString("TYPE");

        }
        /////////////////////////////////////////////////////////////////////
        // toolbar
        toolbar();
        //////////////////////////////  end toolbar button//////////////////////////////////////////////

        ///// START GRID VIEW /////
        gridList = (GridView) findViewById(R.id.gridView);


        ///////////////////////////////////GET CURRENT DATE//////////////////////////////////////////////////////////////////
        Date getDate = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("d/M/yyyy", Locale.getDefault());
        formattedDate = dateFormat.format(getDate);


        ///////////////////////////////////END GET CURRENT DATE//////////////////////////////////////////////////////////////////


        new ConnectionToReadEvent().execute();
        new ConnectionToReadMedication().execute();

        myAdapter = new GridAdapter(this, R.layout.gridview_item, eventList);


        // implement setOnItemClickListener event on GridView
        gridList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // set an Intent to Another Activity
                GridItem e = (GridItem) eventList.get(position);
                String s = e.getEventListName();
                String[] spilt = s.split(":");


                Intent intent = new Intent(Home_Page_Activity.this, Medication_Information.class);
                if (spilt[0].equalsIgnoreCase("Medicine")) {
                    Medication medication = null;
                    int a = 0;
                    for (int i = 0; i < medicationChild.size(); i++) {
                        medication = (Medication) medicationChild.get(i);
                        if (spilt[1].equalsIgnoreCase(" " + medication.getMedicineName())) {

                            //intent.putExtra("image", logos[position]); // put image data in Intent
                            intent.putExtra("operation", "1");
                            //intent.putExtra("userName", medication.getUser_name());
                            intent.putExtra("NameM", medication.getMedicineName());
                            intent.putExtra("numberOfTime", medication.getNumberOfTime());
                            intent.putExtra("doseAmountNumber", medication.getDoseAmountNumber());
                            intent.putExtra("doseAmountText", medication.getDoseAmountText());
                            intent.putExtra("duration", medication.getDuration());
                            intent.putExtra("durationByText", medication.getTextDurationSpin());
                            intent.putExtra("startDayDate", medication.getStartDayDate());
                            intent.putExtra("timeH", medication.getTimeH());
                            intent.putExtra("timeM", medication.getTimeM());
                            intent.putExtra("everyH", medication.getEveryH());
                            intent.putExtra("repeated", medication.getRepeated());

                        }

                    }
                    startActivity(intent); // start Intent

                } else if (spilt[0].equalsIgnoreCase("Name")) {
                    for (int i = 0; i < eventlistChild.size(); i++) {
                        Event event = (Event) eventlistChild.get(i);

                        if (spilt[1].matches(" " + event.getEventName())) {

                            //intent.putExtra("image", logos[position]); // put image data in Intent
                            intent.putExtra("operation", "2");
                            //intent.putExtra("userName", event.getEventName());
                            intent.putExtra("eventName", event.getEventName());
                            intent.putExtra("eventDescription", event.getEventDetails());
                            intent.putExtra("date", event.getEventDate());
                            intent.putExtra("timeH", event.getEventTimeH());
                            intent.putExtra("timeM", event.getEventTimeM());

                        }
                    }
                    //Toast.makeText(getApplicationContext(),spilt[1] +" not found", Toast.LENGTH_SHORT).show();
                    startActivity(intent); // start Intent
                }


            }
        });


    }

    private void startAlarm(Calendar c,String eventName,String eventDescription) {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        intent.putExtra("EVENT_NAME", eventName);
        intent.putExtra("EVENT_DESCRIPTION", eventDescription);

        Random random = new Random();
        int requestCode = random.nextInt(9999 - 1000) + 1000;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, 0);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    public void onTimeSet(int hourOfDay, int minute,String eventName,String eventDescription) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        //Toast.makeText(getApplicationContext(), ""+formattedDate, Toast.LENGTH_SHORT).show();

        startAlarm(c,eventName,eventDescription);
    }


    class ConnectionToReadEvent extends AsyncTask<String, String, String> {
        // starting the connection
        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String event_url = "http://192.168.100.171/readEvent.php";
            try {

                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(event_url));
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
                    JSONArray patientData = jsonResult.getJSONArray("event");
                    for (int i = 0; i < patientData.length(); i++) {
                        JSONObject patientObject = patientData.getJSONObject(i);
                        userName = patientObject.getString("userName");


                        if (userName.equalsIgnoreCase(name)) {
                            int id = patientObject.getInt("id");
                            String eventName = patientObject.getString("eventName");
                            String eventDescription = patientObject.getString("eventDescription");
                            String date = patientObject.getString("date");
                            String timeH = patientObject.getString("timeH");
                            String timeM = patientObject.getString("timeM");

                            // add to the array list
                            eventlistChild.add(new Event(eventName, eventDescription, timeH, timeM, date, id));
                            if (date.equalsIgnoreCase(formattedDate)) {
                                onTimeSet(Integer.parseInt(timeH),Integer.parseInt(timeM),"Event Name: "+eventName,eventDescription);
                            }
                        }


                    }
                    // sort the array list
                    Collections.sort(eventlistChild, new Comparator<Event>() {
                        @Override
                        public int compare(Event event1, Event event2) {
                            return event1.getEventTimeH().compareTo(event2.getEventTimeH());
                        }
                    });
                    // add the array list to the event list related to the grid view
                    for (int i = 0; i < eventlistChild.size(); i++) {
                        Event e = new Event();
                        e = eventlistChild.get(i);
                        int check = Integer.parseInt(e.getEventTimeH());
                        if (e.getEventDate().matches(formattedDate)) {
                            convert12(check);
                            eventList.add(new GridItem("Name: " + e.getEventName(), "Date: " + e.getEventDate(), "Time: " + (timeIn12 + " : " + e.getEventTimeM())));
                        }
                        /*
                        if (e.getEventDate().matches(formattedDate)) {
                            if (Integer.parseInt(e.getEventTimeH()) >= 12 && Integer.parseInt(e.getEventTimeH()) < 24) {
                                if (Integer.parseInt(e.getEventTimeH()) > 12) {
                                    eventList.add(new GridItem("Name: " + e.getEventName(), "Date: " + e.getEventDate(), "Time: " + ((Integer.parseInt(e.getEventTimeH()) - 12) + ":" + e.getEventTimeM() + " pm")));

                                } else {
                                    eventList.add(new GridItem("Name: " + e.getEventName(), "Date: " + e.getEventDate(), "Time: " + (e.getEventTimeH() + ":" + e.getEventTimeM() + " pm")));

                                }
                            } else if (Integer.parseInt(e.getEventTimeH()) == 0) {
                                eventList.add(new GridItem("Name: " + e.getEventName(), "Date: " + e.getEventDate(), "Time: " + ("12" + ":" + e.getEventTimeM() + " am")));

                            } else {
                                eventList.add(new GridItem("Name: " + e.getEventName(), "Date: " + e.getEventDate(), "Time: " + (e.getEventTimeH() + ":" + e.getEventTimeM() + " am")));
                            }
                        }
                    }

 */
                    }
                    gridList.setAdapter(myAdapter);


                } else {
                    Toast.makeText(getApplicationContext(), "The retrieve was not successful ", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

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
                    addMedicationChild();
                    //gridList.setAdapter(myAdapter);

                } else {
                    Toast.makeText(getApplicationContext(), "The retrieve was not successful ", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException | ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public void addMedicationChild() throws ParseException {

        for (int i = 0; i < medicationList.size(); i++) {
            Medication medication = (Medication) medicationList.get(i);
            String Duration = medication.getDuration();
            String DurationText = medication.getTextDurationSpin();
            //date
            String date = medication.getStartDayDate();
            String[] splitedDate = date.split("/");
            int day = Integer.parseInt(splitedDate[0]);
            int month = Integer.parseInt(splitedDate[1]);
            int year = Integer.parseInt(splitedDate[2]);

            if (DurationText.equalsIgnoreCase("Day/s")) {

                for (int j = 0; j < Integer.parseInt(Duration); j++) {
                    int everyH = Integer.parseInt(medication.getEveryH());
                    if (everyH == 24) {
                        if (medication.getRepeated().equalsIgnoreCase("1")) {//every day once
                            medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, medication.getTimeH(), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                            day = day + 1;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }
                        } else if (medication.getRepeated().equalsIgnoreCase("2")) { // every two days once

                            medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, medication.getTimeH(), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                            day = day + 2;
                            if (day > 30) {
                                day %= 30;
                            }
                        } else if (medication.getRepeated().equalsIgnoreCase("3")) {// every three days once
                            medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, medication.getTimeH(), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                            day = day + 3;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }

                        }


                    } else if (everyH == 12) {
                        int h = Integer.parseInt(medication.getTimeH());
                        if (medication.getRepeated().equalsIgnoreCase("1")) {//every day twice

                            for (int k = 0; k < 2; k++) {
                                medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, String.valueOf(h), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                                h += 12;
                                if (h > 24) {
                                    h %= 24;
                                }

                            }
                            day = day + 1;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }
                            //
                        } else if (medication.getRepeated().equalsIgnoreCase("2")) { // every two days twice
                            for (int k = 0; k < 2; k++) {
                                medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, String.valueOf(h), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                                h = h + 12;
                                if (h > 24) {
                                    h %= 24;

                                }
                            }
                            day = day + 2;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }

                        } else if (medication.getRepeated().equalsIgnoreCase("3")) {// every three days twice
                            for (int k = 0; k < 2; k++) {
                                medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, String.valueOf(h), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                                h = h + 12;
                                if (h > 24) {
                                    h %= 24;

                                }
                            }
                            day = day + 3;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }

                        }

                    } else if (everyH == 8) {
                        int h = Integer.parseInt(medication.getTimeH());
                        if (medication.getRepeated().equalsIgnoreCase("1")) {//every day three times

                            for (int k = 0; k < 3; k++) {
                                medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, String.valueOf(h), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                                h = h + 8;
                                if (h > 24) {
                                    h %= 24;

                                }
                            }
                            day = day + 1;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }
                        } else if (medication.getRepeated().equalsIgnoreCase("2")) { // every two days three times
                            for (int k = 0; k < 3; k++) {
                                medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, String.valueOf(h), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                                h = h + 8;
                                if (h > 24) {
                                    h %= 24;

                                }
                            }
                            day = day + 2;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }

                        } else if (medication.getRepeated().equalsIgnoreCase("3")) {// every three days three times
                            for (int k = 0; k < 3; k++) {
                                medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, String.valueOf(h), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                                h = h + 8;
                                if (h > 24) {
                                    h %= 24;

                                }
                            }
                            day = day + 3;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }

                        }
                    } else if (everyH == 6) {
                        int h = Integer.parseInt(medication.getTimeH());
                        if (medication.getRepeated().equalsIgnoreCase("1")) {//every day 4 times

                            for (int k = 0; k < 4; k++) {
                                medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, String.valueOf(h), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                                h = h + 6;
                                if (h > 24) {
                                    h %= 24;

                                }
                            }
                            day = day + 1;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }
                        } else if (medication.getRepeated().equalsIgnoreCase("2")) { // every two days 4 times
                            for (int k = 0; k < 4; k++) {
                                medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, String.valueOf(h), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                                h = h + 6;
                                if (h > 24) {
                                    h %= 24;

                                }
                            }
                            day = day + 2;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }

                        } else if (medication.getRepeated().equalsIgnoreCase("3")) {// every three days 4 times
                            for (int k = 0; k < 4; k++) {
                                medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, String.valueOf(h), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                                h = h + 6;
                                if (h > 24) {
                                    h %= 24;

                                }
                            }
                            day = day + 3;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }

                        }
                    }


                }
            }
            else if (DurationText.equalsIgnoreCase("Week/s")) {

                for (int j = 0; j < (Integer.parseInt(Duration) * 7); j++) {
                    int everyH = Integer.parseInt(medication.getEveryH());
                    if (everyH == 24) {
                        if (medication.getRepeated().equalsIgnoreCase("1")) {//every day once
                            medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, medication.getTimeH(), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                            day = day + 1;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }
                        } else if (medication.getRepeated().equalsIgnoreCase("2")) { // every two days once

                            medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, medication.getTimeH(), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                            day = day + 2;
                            if (day > 30) {
                                day %= 30;
                            }
                        } else if (medication.getRepeated().equalsIgnoreCase("3")) {// every three days once
                            medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, medication.getTimeH(), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                            day = day + 3;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }

                        }


                    } else if (everyH == 12) {
                        int h = Integer.parseInt(medication.getTimeH());
                        if (medication.getRepeated().equalsIgnoreCase("1")) {//every day twice

                            for (int k = 0; k < 2; k++) {
                                medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, String.valueOf(h), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                                h += 12;
                                if (h > 24) {
                                    h %= 24;
                                }

                            }
                            day = day + 1;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }
                            //
                        } else if (medication.getRepeated().equalsIgnoreCase("2")) { // every two days twice
                            for (int k = 0; k < 2; k++) {
                                medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, String.valueOf(h), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                                h = h + 12;
                                if (h > 24) {
                                    h %= 24;

                                }
                            }
                            day = day + 2;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }

                        } else if (medication.getRepeated().equalsIgnoreCase("3")) {// every three days twice
                            for (int k = 0; k < 2; k++) {
                                medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, String.valueOf(h), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                                h = h + 12;
                                if (h > 24) {
                                    h %= 24;

                                }
                            }
                            day = day + 3;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }

                        }

                    } else if (everyH == 8) {
                        int h = Integer.parseInt(medication.getTimeH());
                        if (medication.getRepeated().equalsIgnoreCase("1")) {//every day three times

                            for (int k = 0; k < 3; k++) {
                                medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, String.valueOf(h), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                                h = h + 8;
                                if (h > 24) {
                                    h %= 24;

                                }
                            }
                            day = day + 1;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }
                        } else if (medication.getRepeated().equalsIgnoreCase("2")) { // every two days three times
                            for (int k = 0; k < 3; k++) {
                                medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, String.valueOf(h), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                                h = h + 8;
                                if (h > 24) {
                                    h %= 24;

                                }
                            }
                            day = day + 2;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }

                        } else if (medication.getRepeated().equalsIgnoreCase("3")) {// every three days three times
                            for (int k = 0; k < 3; k++) {
                                medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, String.valueOf(h), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                                h = h + 8;
                                if (h > 24) {
                                    h %= 24;

                                }
                            }
                            day = day + 3;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }

                        }
                    } else if (everyH == 6) {
                        int h = Integer.parseInt(medication.getTimeH());
                        if (medication.getRepeated().equalsIgnoreCase("1")) {//every day 4 times

                            for (int k = 0; k < 4; k++) {
                                medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, String.valueOf(h), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                                h = h + 6;
                                if (h > 24) {
                                    h %= 24;

                                }
                            }
                            day = day + 1;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }
                        } else if (medication.getRepeated().equalsIgnoreCase("2")) { // every two days 4 times
                            for (int k = 0; k < 4; k++) {
                                medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, String.valueOf(h), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                                h = h + 6;
                                if (h > 24) {
                                    h %= 24;

                                }
                            }
                            day = day + 2;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }

                        } else if (medication.getRepeated().equalsIgnoreCase("3")) {// every three days 4 times
                            for (int k = 0; k < 4; k++) {
                                medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, String.valueOf(h), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                                h = h + 6;
                                if (h > 24) {
                                    h %= 24;

                                }
                            }
                            day = day + 3;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }

                        }
                    }
                }

            }
            else if (DurationText.equalsIgnoreCase("Month/s")) {
                for (int j = 0; j < (Integer.parseInt(Duration) * 30); j++) {
                    int everyH = Integer.parseInt(medication.getEveryH());
                    if (everyH == 24) {
                        if (medication.getRepeated().equalsIgnoreCase("1")) {//every day once
                            medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, medication.getTimeH(), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                            day = day + 1;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }
                        } else if (medication.getRepeated().equalsIgnoreCase("2")) { // every two days once

                            medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, medication.getTimeH(), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                            day = day + 2;
                            if (day > 30) {
                                day %= 30;
                            }
                        } else if (medication.getRepeated().equalsIgnoreCase("3")) {// every three days once
                            medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, medication.getTimeH(), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                            day = day + 3;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }

                        }


                    } else if (everyH == 12) {
                        int h = Integer.parseInt(medication.getTimeH());
                        if (medication.getRepeated().equalsIgnoreCase("1")) {//every day twice

                            for (int k = 0; k < 2; k++) {
                                medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, String.valueOf(h), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                                h += 12;
                                if (h > 24) {
                                    h %= 24;
                                }

                            }
                            day = day + 1;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }
                            //
                        } else if (medication.getRepeated().equalsIgnoreCase("2")) { // every two days twice
                            for (int k = 0; k < 2; k++) {
                                medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, String.valueOf(h), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                                h = h + 12;
                                if (h > 24) {
                                    h %= 24;

                                }
                            }
                            day = day + 2;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }

                        } else if (medication.getRepeated().equalsIgnoreCase("3")) {// every three days twice
                            for (int k = 0; k < 2; k++) {
                                medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, String.valueOf(h), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                                h = h + 12;
                                if (h > 24) {
                                    h %= 24;

                                }
                            }
                            day = day + 3;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }

                        }

                    } else if (everyH == 8) {
                        int h = Integer.parseInt(medication.getTimeH());
                        if (medication.getRepeated().equalsIgnoreCase("1")) {//every day three times

                            for (int k = 0; k < 3; k++) {
                                medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, String.valueOf(h), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                                h = h + 8;
                                if (h > 24) {
                                    h %= 24;

                                }
                            }
                            day = day + 1;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }
                        } else if (medication.getRepeated().equalsIgnoreCase("2")) { // every two days three times
                            for (int k = 0; k < 3; k++) {
                                medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, String.valueOf(h), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                                h = h + 8;
                                if (h > 24) {
                                    h %= 24;

                                }
                            }
                            day = day + 2;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }

                        } else if (medication.getRepeated().equalsIgnoreCase("3")) {// every three days three times
                            for (int k = 0; k < 3; k++) {
                                medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, String.valueOf(h), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                                h = h + 8;
                                if (h > 24) {
                                    h %= 24;

                                }
                            }
                            day = day + 3;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }

                        }
                    } else if (everyH == 6) {
                        int h = Integer.parseInt(medication.getTimeH());
                        if (medication.getRepeated().equalsIgnoreCase("1")) {//every day 4 times

                            for (int k = 0; k < 4; k++) {
                                medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, String.valueOf(h), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                                h = h + 6;
                                if (h > 24) {
                                    h %= 24;

                                }
                            }
                            day = day + 1;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }
                        } else if (medication.getRepeated().equalsIgnoreCase("2")) { // every two days 4 times
                            for (int k = 0; k < 4; k++) {
                                medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, String.valueOf(h), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                                h = h + 6;
                                if (h > 24) {
                                    h %= 24;

                                }
                            }
                            day = day + 2;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }

                        } else if (medication.getRepeated().equalsIgnoreCase("3")) {// every three days 4 times
                            for (int k = 0; k < 4; k++) {
                                medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, String.valueOf(h), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                                h = h + 6;
                                if (h > 24) {
                                    h %= 24;

                                }
                            }
                            day = day + 3;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }

                        }
                    }
                }
            }
            else if (DurationText.equalsIgnoreCase("Year")) {
                for (int j = 0; j < (Integer.parseInt(Duration) * 365); j++) {
                    int everyH = Integer.parseInt(medication.getEveryH());
                    if (everyH == 24) {
                        if (medication.getRepeated().equalsIgnoreCase("1")) {//every day once
                            medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, medication.getTimeH(), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                            day = day + 1;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }
                        } else if (medication.getRepeated().equalsIgnoreCase("2")) { // every two days once

                            medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, medication.getTimeH(), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                            day = day + 2;
                            if (day > 30) {
                                day %= 30;
                            }
                        } else if (medication.getRepeated().equalsIgnoreCase("3")) {// every three days once
                            medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, medication.getTimeH(), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                            day = day + 3;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }

                        }


                    } else if (everyH == 12) {
                        int h = Integer.parseInt(medication.getTimeH());
                        if (medication.getRepeated().equalsIgnoreCase("1")) {//every day twice

                            for (int k = 0; k < 2; k++) {
                                medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, String.valueOf(h), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                                h += 12;
                                if (h > 24) {
                                    h %= 24;
                                }

                            }
                            day = day + 1;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }
                            //
                        } else if (medication.getRepeated().equalsIgnoreCase("2")) { // every two days twice
                            for (int k = 0; k < 2; k++) {
                                medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, String.valueOf(h), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                                h = h + 12;
                                if (h > 24) {
                                    h %= 24;

                                }
                            }
                            day = day + 2;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }

                        } else if (medication.getRepeated().equalsIgnoreCase("3")) {// every three days twice
                            for (int k = 0; k < 2; k++) {
                                medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, String.valueOf(h), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                                h = h + 12;
                                if (h > 24) {
                                    h %= 24;

                                }
                            }
                            day = day + 3;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }

                        }

                    } else if (everyH == 8) {
                        int h = Integer.parseInt(medication.getTimeH());
                        if (medication.getRepeated().equalsIgnoreCase("1")) {//every day three times

                            for (int k = 0; k < 3; k++) {
                                medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, String.valueOf(h), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                                h = h + 8;
                                if (h > 24) {
                                    h %= 24;

                                }
                            }
                            day = day + 1;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }
                        } else if (medication.getRepeated().equalsIgnoreCase("2")) { // every two days three times
                            for (int k = 0; k < 3; k++) {
                                medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, String.valueOf(h), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                                h = h + 8;
                                if (h > 24) {
                                    h %= 24;

                                }
                            }
                            day = day + 2;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }

                        } else if (medication.getRepeated().equalsIgnoreCase("3")) {// every three days three times
                            for (int k = 0; k < 3; k++) {
                                medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, String.valueOf(h), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                                h = h + 8;
                                if (h > 24) {
                                    h %= 24;

                                }
                            }
                            day = day + 3;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }

                        }
                    } else if (everyH == 6) {
                        int h = Integer.parseInt(medication.getTimeH());
                        if (medication.getRepeated().equalsIgnoreCase("1")) {//every day 4 times

                            for (int k = 0; k < 4; k++) {
                                medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, String.valueOf(h), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                                h = h + 6;
                                if (h > 24) {
                                    h %= 24;

                                }
                            }
                            day = day + 1;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }
                        } else if (medication.getRepeated().equalsIgnoreCase("2")) { // every two days 4 times
                            for (int k = 0; k < 4; k++) {
                                medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, String.valueOf(h), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                                h = h + 6;
                                if (h > 24) {
                                    h %= 24;

                                }
                            }
                            day = day + 2;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }

                        } else if (medication.getRepeated().equalsIgnoreCase("3")) {// every three days 4 times
                            for (int k = 0; k < 4; k++) {
                                medicationChild.add(new Medication(medication.getId(), medication.getUser_name(), medication.getMedicineName(), medication.getNumberOfTime(), medication.getDoseAmountNumber(), medication.getDoseAmountText(), medication.getDuration(), medication.getTextDurationSpin(), day + "/" + month + "/" + year, String.valueOf(h), medication.getTimeM(), medication.getEveryH(), medication.getRepeated()));
                                h = h + 6;
                                if (h > 24) {
                                    h %= 24;

                                }
                            }
                            day = day + 3;
                            if (day > 30) {
                                day %= 30;
                                month++;
                                if (month > 12) {
                                    day %= 12;
                                    year++;
                                }
                            }

                        }
                    }
                }
            }
            else {

            }
        }
        setAdapterForMedicationChild();
        notificationForMedicine();
    }

    public void setAdapterForMedicationChild() {


        for (int i = 0; i < medicationChild.size(); i++) {
            Medication m = (Medication) medicationChild.get(i);
            int check = Integer.parseInt(m.getTimeH());
            if (m.getStartDayDate().matches(formattedDate)) {
                convert12(check);

                eventList.add(new GridItem("Medicine: " + m.getMedicineName(), "Amount: " + m.getDoseAmountNumber() + " " + m.getDoseAmountText(), "Time: " + timeIn12 + " : " + m.getTimeM()));

            }
        }
        Collections.sort(eventList, new Comparator<GridItem>() {
            @Override
            public int compare(GridItem event1, GridItem event2) {
                return event1.eventListTime.compareToIgnoreCase(event2.eventListTime);
            }
        });
        gridList.setAdapter(myAdapter);
    }
    public void notificationForMedicine() {
        for (int i = 0; i < medicationChild.size(); i++) {
            Medication m = (Medication) medicationChild.get(i);
            if (m.getStartDayDate().equalsIgnoreCase(formattedDate)) {

                onTimeSet(Integer.parseInt(m.getTimeH()),Integer.parseInt(m.getTimeM()),"Medicine Name: "+m.getMedicineName(),"Take "+m.getDoseAmountNumber()+" "+m.getDoseAmountText());
            }
        }

    }
    public void convert12(int h1) {

        // Get Hours

        //h1 %= 12;

        // Handle 00 and 12 case separately
        if (h1 == 0) {
            //System.out.print("12");
            timeIn12 = 24;

        } else {
            // System.out.print(h1);
            timeIn12 = h1;

        }


    }

    public void AmPm(int h1) {
        // Finding out the Meridien of time
        // ie. AM or PM

        if (h1 < 12) {
            Meridien = "am";

        } else {
            Meridien = "pm";
        }


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
                Intent intent = new Intent(Home_Page_Activity.this, Patient_Profile_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_Page_Activity.this, Schedule_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_Page_Activity.this, Add_Medicine_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        SOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_Page_Activity.this, SOS_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equalsIgnoreCase("patient")) {
                    Intent intent = new Intent(Home_Page_Activity.this, Home_Page_Activity.class);
                    intent.putExtra("USERNAME", name);
                    intent.putExtra("TYPE", type);
                    startActivity(intent);

                } else if (type.equalsIgnoreCase("caregiver")) {
                    Intent intent = new Intent(Home_Page_Activity.this, caregiver_homePage_activity.class);
                    intent.putExtra("USERNAME", name);
                    intent.putExtra("TYPE", type);
                    startActivity(intent);

                }
            }
        });
    }
}