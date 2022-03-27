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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

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
    Spinner numberOfTimeSpin, amountNumberSpinner, amountTextSpinner, numberDurationSpin, textDurationSpin;
    String NOTS, ANS, ATS, NDS,TDS;
    CheckBox saturday, sunday, monday, tuesday, wednesday, thursday, friday, all;
    DatePickerDialog datePickerDialog;
    Button start_day_DATE;
    ArrayList spin1, spin2, spin3, spin4, spin5;

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
        saturday = findViewById(R.id.Saturday);
        sunday = findViewById(R.id.Sunday);
        monday = findViewById(R.id.Monday);
        tuesday = findViewById(R.id.Tuesday);
        wednesday = findViewById(R.id.Wednesday);
        thursday = findViewById(R.id.Thursday);
        friday = findViewById(R.id.Friday);
        all = findViewById(R.id.ALL);
         start_day_DATE = findViewById(R.id.Start_day_DATE);
        Button add_medicine = findViewById(R.id.Add_medicine);
        Button back = findViewById(R.id.back);

        all.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (all.isChecked()) {
                    saturday.setChecked(true);
                    saturday.setClickable(false);
                    sunday.setChecked(true);
                    sunday.setClickable(false);
                    monday.setChecked(true);
                    monday.setClickable(false);
                    tuesday.setChecked(true);
                    tuesday.setClickable(false);
                    wednesday.setChecked(true);
                    wednesday.setClickable(false);
                    thursday.setChecked(true);
                    thursday.setClickable(false);
                    friday.setChecked(true);
                    friday.setClickable(false);
                }else {
                    saturday.setChecked(false);
                    saturday.setClickable(true);
                    sunday.setChecked(false);
                    sunday.setClickable(true);
                    monday.setChecked(false);
                    monday.setClickable(true);
                    tuesday.setChecked(false);
                    tuesday.setClickable(true);
                    wednesday.setChecked(false);
                    wednesday.setClickable(true);
                    thursday.setChecked(false);
                    thursday.setClickable(true);
                    friday.setChecked(false);
                    friday.setClickable(true);

                }
            }
        });

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


               // checkBox();
            }
        });


    }

    public void spinner() {
        numberOfTimeSpin = (Spinner) findViewById(R.id.numberOfTimeSpin);
        amountNumberSpinner = (Spinner) findViewById(R.id.amountNumberSpinner);
        amountTextSpinner = (Spinner) findViewById(R.id.amountTextSpinner);
        numberDurationSpin = (Spinner) findViewById(R.id.numberDurationSpin);
        textDurationSpin = (Spinner) findViewById(R.id.TextDurationSpin);
        spin1 = new ArrayList<>();
        spin2 = new ArrayList<>();
        spin3 = new ArrayList<>();
        spin4 = new ArrayList<>();
        spin5 = new ArrayList<>();

        forLoopSpinner();
        ItemSelectedListener();

    }

    public String checkBox() {
        String day = "";
        if (all.isChecked()) {

            day = day + ":";

        } else {
            if (saturday.isChecked()) {
                day = day + ":";
            }
            if (sunday.isChecked()) {
                day = day + ":";
            }
            if (monday.isChecked()) {
                day = day + ":";
            }
            if (tuesday.isChecked()) {
                day = day + ":";
            }
            if (wednesday.isChecked()) {
                day = day + ":";
            }
            if (thursday.isChecked()) {
                day = day + ":";
            }
            if (friday.isChecked()) {
                day = day + ":";
            }
        }
        return day;

    }

    public void ItemSelectedListener() {
        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spin1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        numberOfTimeSpin.setAdapter(adapter1);
        numberOfTimeSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // NOTS = bankNamesDelete.get(i).toString();
                NOTS = numberOfTimeSpin.getChildAt(i).toString();
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
                ANS = amountNumberSpinner.getChildAt(i).toString();

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
                ATS = amountTextSpinner.getChildAt(i).toString();

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
                NDS = numberDurationSpin.getChildAt(i).toString();
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
                TDS= textDurationSpin.getChildAt(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    public void forLoopSpinner() {
        // for number of time
        for (int i = 1; i <= 6; i++) {
            spin1.add(i);
        }
        // for number of time
        for (int i = 1; i <= 4; i++) {
            spin2.add(i);
        }
        // for number of time
        //for (int i =1; i<=6;i++){
        spin3.add("Pills");
        //}
        // for duration number
        for (int i = 1; i <= 30; i++) {
            spin4.add(i);
        }
        // for duration text
        spin5.add("Day");
        spin5.add("Week");
        spin5.add("Month");
        spin5.add("Year");


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
                Intent intent = new Intent(Add_Medicine_Text.this, Add_Activity.class);
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

    public void AddEvent(View view) {

        String medicineName = medicineNameET.getText().toString();
        String numberOfTime = NOTS;
        String amountNumberSpinner = ANS;
        String amountTextSpinner = ATS;
        String numberDurationSpin = NDS;
        String textDurationSpin = TDS;
        String Saturday = String.valueOf(saturday.isChecked()) ;
        String Sunday = String.valueOf(sunday.isChecked()) ;
        String Monday = String.valueOf(monday.isChecked()) ;
        String Tuesday =String.valueOf(tuesday.isChecked()) ;
        String Wednesday = String.valueOf(wednesday.isChecked()) ;
        String Thursday =String.valueOf(thursday.isChecked()) ;
        String Friday = String.valueOf(friday.isChecked()) ;
        String date = start_day_DATE.getText().toString();
        String userName= name;



        addEventToDB addEventToDB = new addEventToDB(this);
        addEventToDB.execute( " ",medicineName ,numberOfTime ,amountNumberSpinner ,
                amountTextSpinner , numberDurationSpin , textDurationSpin ,
                Saturday ,Sunday ,Monday , Tuesday ,Wednesday ,Thursday ,
                Friday ,date , userName);

    }

    public class addEventToDB extends AsyncTask<String, Void, String> {
        Context context;
        AlertDialog alertDialog;

        addEventToDB(Context ctx) {
            context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            String operation = params[0];
            String login_url = "http://192.168.100.171/AddMedication.php";


           // if (operation.equals("AddEvent")) {
                try {

                    String medicineNameET = params[1];
                    String numberOfTimeSpin = params[2];
                    String amountNumberSpinner = params[3];
                    String amountTextSpinner = params[4];
                    String numberDurationSpin = params[5];
                    String textDurationSpin = params[6];
                    String saturday = params[7];
                    String sunday = params[8];
                    String monday = params[9];
                    String tuesday = params[10];
                    String wednesday = params[11];
                    String thursday = params[12];
                    String friday = params[13];
                    String all = params[14];
                    String date = params[15];
                    String userName= params[16];
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
                            + URLEncoder.encode("textDurationSpin", "UTF-8") + "=" + URLEncoder.encode(textDurationSpin, "UTF-8") + "&"
                            + URLEncoder.encode("saturday", "UTF-8") + "=" + URLEncoder.encode(saturday, "UTF-8") + "&"
                            + URLEncoder.encode("sunday", "UTF-8") + "=" + URLEncoder.encode(sunday, "UTF-8")+ "&"
                            + URLEncoder.encode("monday", "UTF-8") + "=" + URLEncoder.encode(monday, "UTF-8")+ "&"
                            + URLEncoder.encode("tuesday", "UTF-8") + "=" + URLEncoder.encode(tuesday, "UTF-8")+ "&"
                            + URLEncoder.encode("wednesday", "UTF-8") + "=" + URLEncoder.encode(wednesday, "UTF-8")+ "&"
                            + URLEncoder.encode("thursday", "UTF-8") + "=" + URLEncoder.encode(thursday, "UTF-8")+ "&"
                            + URLEncoder.encode("friday", "UTF-8") + "=" + URLEncoder.encode(friday, "UTF-8")+ "&"
                            + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8")+ "&"
                            + URLEncoder.encode("userName", "UTF-8") + "=" + URLEncoder.encode(userName, "UTF-8");
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