package com.example.safemedicare;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.util.ArrayList;
import java.util.Calendar;

public class Add_Medicine_Text extends AppCompatActivity {
    String name, type;
    EditText medicineNameET;
    Spinner numberOfTimeSpin, amountNumberSpinner, amountTextSpinner, numberDurationSpin, textDurationSpin, RepeatSpin;
    String NOTS, ANS, ATS, NDS, TDS, RS;
    CheckBox saturday, sunday, monday, tuesday, wednesday, thursday, friday, all;
    DatePickerDialog datePickerDialog;
    Button start_day_DATE;
    ArrayList spin1, spin2, spin3, spin4, spin5, spin6;
    TextView everyH;
    TimePicker timePicker;
    int eh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_medicine_text);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("USERNAME");
            type = extras.getString("TYPE");

            Button SecondB = findViewById(R.id.SecondB);
            if (type.matches("caregiver")) {
                SecondB.setVisibility(View.GONE);
            }
        }

        toolbar();
        spinner();
        medicineNameET = findViewById(R.id.EditMedicineName);
        numberOfTimeSpin = (Spinner) findViewById(R.id.numberOfTimeSpin);
        amountNumberSpinner = (Spinner) findViewById(R.id.amountNumberSpinner);
        amountTextSpinner = (Spinner) findViewById(R.id.amountTextSpinner);
        numberDurationSpin = (Spinner) findViewById(R.id.numberDurationSpin);
        textDurationSpin = (Spinner) findViewById(R.id.TextDurationSpin);

        start_day_DATE = findViewById(R.id.Start_day_DATE);
        everyH= findViewById(R.id.everyH);
        timePicker = (TimePicker) findViewById(R.id.timePickerMedicine); // initiate a time picker
        timePicker.setCurrentHour(12); // before api level 23
        timePicker.setHour(12); // from api level 23
        Button add_medicine = findViewById(R.id.Add_medicine);




        ///////////////////SELECT THE DATE /////////////////////////////////
        start_day_DATE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(Add_Medicine_Text.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // set day of month , month and year value in the edit text
                        start_day_DATE.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////

        add_medicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //AddMedicine(view);
                if (!medicineNameET.getText().toString().isEmpty()){



                          // check if all input interred
                              if ( ANS.equalsIgnoreCase("Select") ){
                                Toast.makeText(getApplicationContext(), "Please select the Amount", Toast.LENGTH_SHORT).show();
                            }else  if ( ATS.equalsIgnoreCase("Select") ){
                                  Toast.makeText(getApplicationContext(), "Please select the Amount type", Toast.LENGTH_SHORT).show();
                              }else  if (  NDS.equalsIgnoreCase("Select") ){
                                Toast.makeText(getApplicationContext(), "Please select the Duration", Toast.LENGTH_SHORT).show();
                            }else  if ( TDS.equalsIgnoreCase("Select")  ){
                                Toast.makeText(getApplicationContext(), "Please select duration in text", Toast.LENGTH_SHORT).show();
                            }else  if ( RS.equalsIgnoreCase("Select") ){
                                  Toast.makeText(getApplicationContext(), "Please select the repetition", Toast.LENGTH_SHORT).show();
                              }else  if ( (TDS.equalsIgnoreCase("Month/s") && Integer.parseInt(NDS)>12)  || (TDS.equalsIgnoreCase("Year") && Integer.parseInt(NDS)>1) ){
                                  Toast.makeText(getApplicationContext(), "Please Change the duration Number", Toast.LENGTH_SHORT).show();
                              }else  if ( start_day_DATE.getText().toString().matches("Start day DATE") ){
                                  Toast.makeText(getApplicationContext(), "Please select the start day", Toast.LENGTH_SHORT).show();
                              }else {
                                AddMedicine(view);
                            }



                }else {
                    //toast select day
                    Toast.makeText(getApplicationContext(), "Please Enter the medicine name", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    public void spinner() {
        numberOfTimeSpin = (Spinner) findViewById(R.id.numberOfTimeSpin);
        amountNumberSpinner = (Spinner) findViewById(R.id.amountNumberSpinner);
        amountTextSpinner = (Spinner) findViewById(R.id.amountTextSpinner);
        numberDurationSpin = (Spinner) findViewById(R.id.numberDurationSpin);
        textDurationSpin = (Spinner) findViewById(R.id.TextDurationSpin);
        RepeatSpin = (Spinner) findViewById(R.id.RepeatSpin);
        spin1 = new ArrayList<>();
        spin2 = new ArrayList<>();
        spin3 = new ArrayList<>();
        spin4 = new ArrayList<>();
        spin5 = new ArrayList<>();
        spin6 = new ArrayList<>();


        ItemSelectedListener();

    }

    public void ItemSelectedListener() {
        forLoopSpinner();
        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spin1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Setting the ArrayAdapter data on the Spinner
        numberOfTimeSpin.setAdapter(adapter1);
        numberOfTimeSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // NOTS = bankNamesDelete.get(i).toString();
                double EH = Math.round( 24/Double.parseDouble(spin1.get(i).toString())) ;
                 eh = (int) EH;
                everyH.setText("Every "+eh+" hr");
                NOTS = spin1.get(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spin2);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        amountNumberSpinner.setAdapter(adapter2);
        amountNumberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ANS = spin2.get(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter adapter3 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spin3);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        amountTextSpinner.setAdapter(adapter3);
        amountTextSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ATS = spin3.get(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter adapter4 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spin4);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        numberDurationSpin.setAdapter(adapter4);
        numberDurationSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                NDS = spin4.get(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter adapter5 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spin5);
        adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        textDurationSpin.setAdapter(adapter5);
        textDurationSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if ( spin5.get(i).toString().equalsIgnoreCase("Month/s") ){
                   if (Integer.parseInt(NDS)>12){
                       spin4.clear();
                       for (int j = 1; j <= 12; j++) {
                           spin4.add(j);
                       }
                       Toast.makeText(getApplicationContext(), "Maximum duration is 12 Months", Toast.LENGTH_SHORT).show();

                   }

                     }else if ( spin5.get(i).toString().equalsIgnoreCase("Year") ){
                    spin4.clear();
                    spin4.add("1");
                    Toast.makeText(getApplicationContext(), "Maximum duration is one year", Toast.LENGTH_SHORT).show();
                }else {
                    spin4.clear();
                    for (int j = 1; j <= 30; j++) {
                        spin4.add(j);
                    }
                }
                TDS = spin5.get(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter adapter6 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spin6);
        adapter6.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        RepeatSpin.setAdapter(adapter6);
        RepeatSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                RS = spin6.get(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void forLoopSpinner() {
        // for number of time

        for (int i = 1; i <= 4; i++) {
            spin1.add(i);
        }
        // for number of time

        spin2.add("Select");
        for (int i = 1; i <= 4; i++) {
            spin2.add(i);
        }
        // for number of time
        //for (int i =1; i<=6;i++){
        spin3.add("Select");
        spin3.add("Pill/s");
        spin3.add("Capsule/s");
        //}
        // for duration number
        spin4.add("Select");
        for (int i = 1; i <= 30; i++) {
            spin4.add(i);
        }
        // for duration text
        spin5.add("Select");
        spin5.add("Day/s");
        spin5.add("Week/s");
        spin5.add("Month/s");
        spin5.add("Year");
        spin6.add("Select");
        spin6.add("Day");
        spin6.add("Two Days");
        spin6.add("Three Days");

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
                Intent intent = new Intent(Add_Medicine_Text.this, Profile_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Add_Medicine_Text.this, Schedule_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Add_Medicine_Text.this, Add_Medicine_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        SOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Add_Medicine_Text.this, SOS_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equalsIgnoreCase("patient")) {
                    Intent intent = new Intent(Add_Medicine_Text.this, Home_Page_Activity.class);
                    intent.putExtra("USERNAME", name);
                    intent.putExtra("TYPE", type);
                    startActivity(intent);

                } else if (type.equalsIgnoreCase("caregiver")) {
                    Intent intent = new Intent(Add_Medicine_Text.this, caregiver_homePage_activity.class);
                    intent.putExtra("USERNAME", name);
                    intent.putExtra("TYPE", type);
                    startActivity(intent);

                }
            }
        });
    }

    public void AddMedicine(View view) {
        int hours = timePicker.getHour(); // after api level 23
        int minutes = timePicker.getMinute(); // after api level 23
        String medicineName = medicineNameET.getText().toString();
        String numberOfTime = NOTS;
        String amountNumberSpinner = ANS;
        String amountTextSpinner = ATS;
        String numberDurationSpin = NDS;
        String textDurationSpin = TDS;

        String date = start_day_DATE.getText().toString();
        String userName = name;
        String timeH ,timeM, repeated = null;
        timeH = String.valueOf(hours);
        timeM = String.valueOf(minutes);
        if (RS.equalsIgnoreCase("Day")){
            repeated = "1";
        }else  if (RS.equalsIgnoreCase("Two Days")){
            repeated = "2";
        }else  if (RS.equalsIgnoreCase("Three Days")){
            repeated = "3";
        }

        addMedicineToDB addEventToDB = new addMedicineToDB(this);
        addEventToDB.execute( medicineName, numberOfTime, amountNumberSpinner,
                amountTextSpinner, numberDurationSpin, textDurationSpin, date, userName,timeH ,timeM , repeated);

    }

    public class addMedicineToDB extends AsyncTask<String, Void, String> {
        Context context;
        AlertDialog alertDialog;

        addMedicineToDB(Context ctx) {
            context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            String operation = params[0];
            String login_url = "http://192.168.100.171/AddMedication.php";


            // if (operation.equals("AddEvent")) {
            try {

                String medicineNameET = params[0];
                String numberOfTimeSpin = params[1];
                String amountNumberSpinner = params[2];
                String amountTextSpinner = params[3];
                String numberDurationSpin = params[4];
                String textDurationSpin = params[5];


                String date = params[6];
                String userName = params[7];
                String timeH = params[8];
                String timeM = params[9];
                String repeated = params[10];
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("medicineNameET", "UTF-8") + "=" + URLEncoder.encode(medicineNameET, "UTF-8") + "&"
                        + URLEncoder.encode("numberOfTimeSpin", "UTF-8") + "=" + URLEncoder.encode(numberOfTimeSpin, "UTF-8") + "&"
                        + URLEncoder.encode("amountNumberSpinner", "UTF-8") + "=" + URLEncoder.encode(amountNumberSpinner, "UTF-8") + "&"
                        + URLEncoder.encode("amountTextSpinner", "UTF-8") + "=" + URLEncoder.encode(amountTextSpinner, "UTF-8") + "&"
                        + URLEncoder.encode("numberDurationSpin", "UTF-8") + "=" + URLEncoder.encode(numberDurationSpin, "UTF-8") + "&"
                        + URLEncoder.encode("textDurationSpin", "UTF-8") + "=" + URLEncoder.encode(textDurationSpin, "UTF-8")+ "&"
                        + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8") + "&"
                        + URLEncoder.encode("userName", "UTF-8") + "=" + URLEncoder.encode(userName, "UTF-8")+ "&"
                        + URLEncoder.encode("timeH", "UTF-8") + "=" + URLEncoder.encode(timeH, "UTF-8")+ "&"
                        + URLEncoder.encode("timeM", "UTF-8") + "=" + URLEncoder.encode(timeM, "UTF-8")+ "&"
                        + URLEncoder.encode("everyH", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(eh), "UTF-8")+ "&"
                        + URLEncoder.encode("repeated", "UTF-8") + "=" + URLEncoder.encode(repeated, "UTF-8");
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

            //  }
            return null;
        }

        @Override
        protected void onPreExecute() {
            alertDialog = new AlertDialog.Builder(context).create();
            alertDialog.setTitle("Add Medicine Status");
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