package com.example.safemedicare;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class shared_schedule extends AppCompatActivity {
    String name, type,userName, date1,patientUserName;
    DatePicker datePicker;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String currentDate;
    ListView list;
    ArrayAdapter<String> adapter;
    ArrayList<Event> eventlist = new ArrayList<>();
    ArrayList<Event> selectedDateEvent = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_schedule);

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd/M/yyyy");
        currentDate = dateFormat.format(calendar.getTime());

        // toolbar
        toolbar();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("USERNAME");
            type = extras.getString("TYPE");
            patientUserName=extras.getString("PatientUserName");
            Button SecondB = findViewById(R.id.SecondB);
            if (type.matches("caregiver")) {
                SecondB.setVisibility(View.GONE);
            }
        }




        // fill the list view
        new ReadEvent().execute();
        list = (ListView) findViewById(R.id.listEvent_sharedSchedule);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        list.setAdapter(adapter);

       list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Intent intent = new Intent(caregiver_homePage_activity.this, Profile_Activity.class);
                // intent.putExtra("PatientName", p.get(i));
                //startActivity(intent);

                if (position >= 0) {
                   /* Intent intent = new Intent(Schedule_Activity.this, Event_Adjustment.class);
                    intent.putExtra("USERNAME", name);
                    intent.putExtra("TYPE", type);
                    intent.putExtra("EVENT_ID", selectedDateEvent.get(position).getId());
                    startActivity(intent);
                    */
                }

            }

        });


        datePicker = (DatePicker) findViewById(R.id.datePicker);
        date1 = currentDate;
        datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // use the date choose and update the list view
                date1 = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                adapter.clear();
                eventlist.clear();
                selectedDateEvent.clear();
                new ReadEvent().execute();
            }
        });

    }


    ///////////////////////////// class for read from DB ///////////////////////////////////////////////////////////////////
    class ReadEvent extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String readPatient_url = "http://192.168.100.10/readEvent.php";
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
                    JSONArray patientData = jsonResult.getJSONArray("event");
                    for (int i = 0; i < patientData.length(); i++) {
                        JSONObject patientObject = patientData.getJSONObject(i);
                        userName = patientObject.getString("userName");

                        if (userName.equalsIgnoreCase(patientUserName)) {
                            int id = patientObject.getInt("id");
                            String eventName = patientObject.getString("eventName");
                            String eventDescription = patientObject.getString("eventDescription");
                            String date = patientObject.getString("date");
                            String timeH = patientObject.getString("timeH");
                            String timeM = patientObject.getString("timeM");
                            // add to the array list
                            eventlist.add(new Event(eventName, eventDescription, timeH, timeM, date, id));
                        }

                    }
                    // sort the array list
                    Collections.sort(eventlist, new Comparator<Event>() {
                        @Override
                        public int compare(Event event1, Event event2) {
                            return event1.getEventTimeH().compareToIgnoreCase(event2.getEventTimeH());
                        }
                    });
                    // add the array list to the event list related to the list view
                    for (int i = 0; i < eventlist.size(); i++) {
                        Event e = new Event();
                        e = eventlist.get(i);
                        String line;
                        if (e.getEventDate().matches(date1)) {
                            // show the time in 12 hours
                            if (Integer.parseInt(e.getEventTimeH()) >= 12) {
                                // for pm time
                                if (Integer.parseInt(e.getEventTimeH()) > 12) {
                                    line ="Event name: "+ e.getEventName() +"  Description: " +e.getEventDetails()+"  Time" + ((Integer.parseInt(e.getEventTimeH()) - 12) + ":" + e.getEventTimeM() + " pm");
                                    adapter.add(line);
                                    // add the line to the selectedDateEvent for further use ( Event ID )
                                    selectedDateEvent.add(eventlist.get(i));
                                } else {
                                    line ="Event name: "+ e.getEventName() +"  Description: " +e.getEventDetails()+"  Time"+ (e.getEventTimeH() + ":" + e.getEventTimeM() + " pm");
                                    adapter.add(line);
                                    // add the line to the selectedDateEvent for further use ( Event ID )
                                    selectedDateEvent.add(eventlist.get(i));
                                }
                            }
                            // for am time
                            else if (Integer.parseInt(e.getEventTimeH()) == 0) {
                                line ="Event name: "+ e.getEventName() +"  Description: " +e.getEventDetails()+"  Time"+ ("12" + ":" + e.getEventTimeM() + " am");
                                adapter.add(line);
                                // add the line to the selectedDateEvent for further use ( Event ID )
                                selectedDateEvent.add(eventlist.get(i));
                            } else {
                                line ="Event name: "+ e.getEventName() +"  Description: "+e.getEventDetails()+"  Time"+ (e.getEventTimeH() + ":" + e.getEventTimeM() + " am");
                                adapter.add(line);
                                // add the line to the selectedDateEvent for further use ( Event ID )
                                selectedDateEvent.add(eventlist.get(i));
                            }
                        }
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "The retrieve was not successful ", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
                Intent intent = new Intent(shared_schedule.this, personal_info_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);


            }
        });

        Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(shared_schedule.this, Schedule_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(shared_schedule.this, Add_Medicine_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        SOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(shared_schedule.this, SOS_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equalsIgnoreCase("patient")) {
                    Intent intent = new Intent(shared_schedule.this, Home_Page_Activity.class);
                    intent.putExtra("USERNAME", name);
                    intent.putExtra("TYPE", type);
                    startActivity(intent);

                } else if (type.equalsIgnoreCase("caregiver")) {
                    Intent intent = new Intent(shared_schedule.this, caregiver_homePage_activity.class);
                    intent.putExtra("USERNAME", name);
                    intent.putExtra("TYPE", type);
                    startActivity(intent);

                }

            }
        });

        //////////////////////////////  end toolbar button//////////////////////////////////////////////
    }
}
