package com.example.safemedicare;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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

public class MedicationLog_Adjustment extends AppCompatActivity {

    TextView textView1, textView2, text3, textView4, textView6, textView5, textView7, textView13, textView14, textView15, textView16, textView17, textView18, textView20, textView21, textView22;
    String id, NameM, numberOfTime, doseAmountNumber, doseAmountText, duration, durationByText, timeH, timeM, everyH, repeated, eventDescription, date;
    String NOTS, ANS, ATS, NDS, TDS, RS;
    EditText medicineNameET;
    Spinner numberOfTimeSpin, amountNumberSpinner, amountTextSpinner, numberDurationSpin, textDurationSpin, RepeatSpin;
    ArrayList spin1, spin2, spin3, spin4, spin5, spin6;
    DatePickerDialog datePickerDialog;
    Button start_day_DATE, update, delete;
    TimePicker timePicker;
    int eh;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medication_log_adjustment);

        textView7 = findViewById(R.id.everyH0);



        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id = extras.getString("id");
            NameM = extras.getString("NameM");
            numberOfTime = extras.getString("numberOfTime");
            doseAmountNumber = extras.getString("doseAmountNumber");
            doseAmountText = extras.getString("doseAmountText");
            duration = extras.getString("duration");
            durationByText = extras.getString("durationByText");
            date = extras.getString("startDayDate");
            timeH = extras.getString("timeH");
            timeM = extras.getString("timeM");
            everyH = extras.getString("everyH");
            repeated = extras.getString("repeated");



        }

        medicineNameET = findViewById(R.id.EditMedicineName0);
        spinner();
        start_day_DATE = findViewById(R.id.Start_day_DATE0);
        update = findViewById(R.id.update0);
        delete = findViewById(R.id.delete0);
        timePicker = (TimePicker) findViewById(R.id.timePickerMedicine0);
        timePicker.setCurrentHour(12); // before api level 23
        timePicker.setHour(12); // from api level 23
///////////////////////////////////////////////////////////////////////////////////////////
        //set the info
        medicineNameET.setText(NameM);
        spin1.set(0,numberOfTime);
        spin2.set(0,doseAmountNumber);
        spin3.set(0,doseAmountText);
        spin4.set(0,duration);
        spin5.set(0,durationByText);
        spin6.set(0,repeated);
        start_day_DATE.setText(date);
        timePicker.setHour(Integer.parseInt(timeH));
        timePicker.setMinute(Integer.parseInt(timeM));
        textView7.setText("Every "+everyH+" H");







        start_day_DATE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(MedicationLog_Adjustment.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // set day of month , month and year value in the edit text
                        start_day_DATE.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //AddMedicine(view);
                if (!medicineNameET.getText().toString().isEmpty()) {
                    // check if all input interred
                     if ((TDS.equalsIgnoreCase("Month/s") && Integer.parseInt(NDS) > 12) || (TDS.equalsIgnoreCase("Year") && Integer.parseInt(NDS) > 1)) {
                        Toast.makeText(getApplicationContext(), "Please Change the duration Number", Toast.LENGTH_SHORT).show();
                    } else  {
                        UpdateMedicine();
                    }
                } else {
                    //toast select day
                    Toast.makeText(getApplicationContext(), "Please Enter the medicine name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteMedicine();
            }
        });
    }

    public void UpdateMedicine() {
        String operation = "update";
        int hours = timePicker.getHour(); // after api level 23
        int minutes = timePicker.getMinute(); // after api level 23
        String medicineName = medicineNameET.getText().toString().trim();
        String numberOfTime = NOTS;
        String amountNumberSpinner = ANS;
        String amountTextSpinner = ATS;
        String numberDurationSpin = NDS;
        String textDurationSpin = TDS;

        String date = start_day_DATE.getText().toString();
        String medid = id;
        String timeH, timeM, repeated = null;
        timeH = String.valueOf(hours);
        timeM = String.valueOf(minutes);
        if (RS.equalsIgnoreCase("Day")) {
            repeated = "1";
        } else if (RS.equalsIgnoreCase("Two Days")) {
            repeated = "2";
        } else if (RS.equalsIgnoreCase("Three Days")) {
            repeated = "3";
        }

        updateMedication db1BackgroundWorker = new updateMedication(this);
        db1BackgroundWorker.execute(operation, medicineName, numberOfTime, amountNumberSpinner,
                amountTextSpinner, numberDurationSpin, textDurationSpin, date, medid, timeH, timeM, repeated);

    }


    public void DeleteMedicine() {

        String operation = "delete";

        String eventID = String.valueOf(id);
        updateMedication db1BackgroundWorker = new updateMedication(this);
        db1BackgroundWorker.execute(operation, eventID);

    }

    public class updateMedication extends AsyncTask<String, Void, String> {
        Context context;
        AlertDialog alertDialog;

        updateMedication(Context ctx) {
            context = ctx;
        }

        @Override
        protected String doInBackground(String... params) {
            String operation = params[0];
            String Url_updateMedicine = "http://192.168.100.171/UpdateMedicine.php";

            if (operation.equals("update")) {
                try {

                    String medicineNameET = params[1];
                    String numberOfTimeSpin = params[2];
                    String amountNumberSpinner = params[3];
                    String amountTextSpinner = params[4];
                    String numberDurationSpin = params[5];
                    String textDurationSpin = params[6];
                    String date = params[7];
                    String medid = params[8];
                    String timeH = params[9];
                    String timeM = params[10];
                    String repeated = params[11];
                    URL url = new URL(Url_updateMedicine);
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
                            + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8") + "&"
                            + URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(medid, "UTF-8") + "&"
                            + URLEncoder.encode("timeH", "UTF-8") + "=" + URLEncoder.encode(timeH, "UTF-8") + "&"
                            + URLEncoder.encode("operation", "UTF-8") + "=" + URLEncoder.encode(operation, "UTF-8") + "&"
                            + URLEncoder.encode("timeM", "UTF-8") + "=" + URLEncoder.encode(timeM, "UTF-8") + "&"
                            + URLEncoder.encode("everyH", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(eh), "UTF-8") + "&"
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

            } else if (operation.equals("delete")) {
                try {
                    String medicineNameET = " ";
                    String numberOfTimeSpin = " ";
                    String amountNumberSpinner = " ";
                    String amountTextSpinner = " ";
                    String numberDurationSpin = " ";
                    String textDurationSpin = " ";
                    String date = " ";
                    String medid = params[1];
                    String timeH = " ";
                    String timeM = " ";
                    String repeated = " ";
                    URL url = new URL(Url_updateMedicine);
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
                            + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8") + "&"
                            + URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(medid, "UTF-8") + "&"
                            + URLEncoder.encode("timeH", "UTF-8") + "=" + URLEncoder.encode(timeH, "UTF-8") + "&"
                            + URLEncoder.encode("operation", "UTF-8") + "=" + URLEncoder.encode(operation, "UTF-8") + "&"
                            + URLEncoder.encode("timeM", "UTF-8") + "=" + URLEncoder.encode(timeM, "UTF-8") + "&"
                            + URLEncoder.encode("everyH", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(eh), "UTF-8") + "&"
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

    public void spinner() {
        numberOfTimeSpin = (Spinner) findViewById(R.id.numberOfTimeSpin0);
        amountNumberSpinner = (Spinner) findViewById(R.id.amountNumberSpinner0);
        amountTextSpinner = (Spinner) findViewById(R.id.amountTextSpinner0);
        numberDurationSpin = (Spinner) findViewById(R.id.numberDurationSpin0);
        textDurationSpin = (Spinner) findViewById(R.id.TextDurationSpin0);
        RepeatSpin = (Spinner) findViewById(R.id.RepeatSpin0);
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
                double EH = Math.round(24 / Double.parseDouble(spin1.get(i).toString()));
                eh = (int) EH;
                textView7.setText("Every " + eh + " hr");
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
                if (spin5.get(i).toString().equalsIgnoreCase("Month/s")) {
                    if (Integer.parseInt(NDS) > 12) {
                        spin4.clear();
                        for (int j = 1; j <= 12; j++) {
                            spin4.add(j);
                        }
                        Toast.makeText(getApplicationContext(), "Maximum duration is 12 Months", Toast.LENGTH_SHORT).show();

                    }

                } else if (spin5.get(i).toString().equalsIgnoreCase("Year")) {
                    spin4.clear();
                    spin4.add("1");
                    Toast.makeText(getApplicationContext(), "Maximum duration is one year", Toast.LENGTH_SHORT).show();
                } else {
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
        spin1.add("Select");
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
        spin3.add("Tablet/s");
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
}