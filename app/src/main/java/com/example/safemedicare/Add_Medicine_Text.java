package com.example.safemedicare;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.ArrayList;
import java.util.Calendar;

public class Add_Medicine_Text extends AppCompatActivity {
    String name, type, MedicineName;
    EditText medicineNameET;
    Spinner numberOfTimeSpin, amountNumberSpinner, amountTextSpinner, numberDurationSpin, textDurationSpin, RepeatSpin;
    String NOTS, ANS, ATS, NDS, TDS, RS;

    DatePickerDialog datePickerDialog;
    Button start_day_DATE;
    ArrayList spin1, spin2, spin3, spin4, spin5, spin6, conflictMedicine, actionMedicine;
    TextView everyH;
    TimePicker timePicker;
    int eh, lastACTION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_medicine_text);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("USERNAME");
            type = extras.getString("TYPE");
            MedicineName = extras.getString("medName");

        }

        toolbar();
        spinner();
        actionMedicine = new ArrayList<>();
        lastACTION = 0;
        medicineNameET = findViewById(R.id.EditMedicineName);
        medicineNameET.setText(MedicineName);
        numberOfTimeSpin = (Spinner) findViewById(R.id.numberOfTimeSpin);
        amountNumberSpinner = (Spinner) findViewById(R.id.amountNumberSpinner);
        amountTextSpinner = (Spinner) findViewById(R.id.amountTextSpinner);
        numberDurationSpin = (Spinner) findViewById(R.id.numberDurationSpin);
        textDurationSpin = (Spinner) findViewById(R.id.TextDurationSpin);

        start_day_DATE = findViewById(R.id.Start_day_DATE);
        everyH = findViewById(R.id.everyH);
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
                if (!medicineNameET.getText().toString().isEmpty()) {


                    // check if all input interred
                    if (ANS.equalsIgnoreCase("Select")) {
                        Toast.makeText(getApplicationContext(), "Please select the Amount", Toast.LENGTH_SHORT).show();
                    } else if (ATS.equalsIgnoreCase("Select")) {
                        Toast.makeText(getApplicationContext(), "Please select the Amount type", Toast.LENGTH_SHORT).show();
                    } else if (NDS.equalsIgnoreCase("Select")) {
                        Toast.makeText(getApplicationContext(), "Please select the Duration", Toast.LENGTH_SHORT).show();
                    } else if (TDS.equalsIgnoreCase("Select")) {
                        Toast.makeText(getApplicationContext(), "Please select duration in text", Toast.LENGTH_SHORT).show();
                    } else if (RS.equalsIgnoreCase("Select")) {
                        Toast.makeText(getApplicationContext(), "Please select the repetition", Toast.LENGTH_SHORT).show();
                    } else if ((TDS.equalsIgnoreCase("Month/s") && Integer.parseInt(NDS) > 12) || (TDS.equalsIgnoreCase("Year") && Integer.parseInt(NDS) > 1)) {
                        Toast.makeText(getApplicationContext(), "Please Change the duration Number", Toast.LENGTH_SHORT).show();
                    } else if (start_day_DATE.getText().toString().matches("Start day DATE")) {
                        Toast.makeText(getApplicationContext(), "Please select the start day", Toast.LENGTH_SHORT).show();
                    } else {
                        new ConnectionToMedicationConflict().execute();


                    }


                } else {
                    //toast select day
                    Toast.makeText(getApplicationContext(), "Please Enter the medicine name", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    class ConnectionToMedicationConflict extends AsyncTask<String, String, String> {
        // starting the connection
        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String event_url = "http://192.168.100.171/Medication_Conflicte.php";
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
            conflictMedicine = new ArrayList<>();

            try {
                JSONObject jsonResult = new JSONObject(result);
                int success = jsonResult.getInt("success");
                if (success == 1) {
                    JSONArray patientData = jsonResult.getJSONArray("medication");
                    for (int i = 0; i < patientData.length(); i++) {
                        JSONObject patientObject = patientData.getJSONObject(i);
                        String medication = patientObject.getString("medicine_name");
                        String conflict = patientObject.getString("conflict");
                        String[] s = conflict.split(",");
                        //Toast.makeText(getApplicationContext(),medication, Toast.LENGTH_LONG).show();

                        if (medication.equalsIgnoreCase(medicineNameET.getText().toString().trim())) {

                            for (int j = 0; j < s.length; j++) {
                                conflictMedicine.add(s[j]);
                            }
                            new ConnectionToMedicine().execute();
                        }


                    }
                    if (conflictMedicine.isEmpty()) {
                        AddMedicine();
                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class ConnectionToMedicine extends AsyncTask<String, String, String> {
        // starting the connection
        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String event_url = "http://192.168.100.171/readMedication.php";
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
                if (success == 2) {
                    JSONArray patientData = jsonResult.getJSONArray("medication");
                    for (int i = 0; i < patientData.length(); i++) {
                        JSONObject patientObject = patientData.getJSONObject(i);
                        String userName = patientObject.getString("userName");
                        String medicineName = null;

                        if (name.equalsIgnoreCase(userName)) {
                            medicineName = patientObject.getString("medicineName");

                            for (int j = 0; j < conflictMedicine.size(); j++) {

                                if (conflictMedicine.get(j).toString().equalsIgnoreCase(medicineName)) {
                                    //Toast.makeText(getApplicationContext(),"conflict found with "+ conflictMedicine.get(j).toString(), Toast.LENGTH_LONG).show();
                                    // actionMedicine.add(conflictMedicine.get(j).toString());
                                    lastACTION = 1;
                                }
                            }

                        }


                    }
                    if (lastACTION == 1) {
                        Alert();
                    }

                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
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
                        + URLEncoder.encode("textDurationSpin", "UTF-8") + "=" + URLEncoder.encode(textDurationSpin, "UTF-8") + "&"
                        + URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date, "UTF-8") + "&"
                        + URLEncoder.encode("userName", "UTF-8") + "=" + URLEncoder.encode(userName, "UTF-8") + "&"
                        + URLEncoder.encode("timeH", "UTF-8") + "=" + URLEncoder.encode(timeH, "UTF-8") + "&"
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


    public void Alert() {

        // Create the object of AlertDialog Builder class
        AlertDialog.Builder builder = new AlertDialog.Builder(Add_Medicine_Text.this);

        // Set the message show for the Alert time
        builder.setMessage("There is a medication conflict with another medicine\nYou need to check with your Doctor \nDo you stile want to add the medicine");

        // Set Alert Title
        builder.setTitle("Conflict Check Alert !");

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false);

        // Set the positive button with yes name OnClickListener method is use of DialogInterface interface.

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // When the user click yes button then app will role back
                AddMedicine();
            }
        });

        // Set the Negative button with No name OnClickListener method is use of DialogInterface interface.
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // If user click no then dialog box is canceled.
                dialog.cancel();
            }
        });

        // Create the Alert dialog
        AlertDialog alertDialog = builder.create();

        // Show the Alert Dialog box
        alertDialog.show();
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
                double EH = Math.round(24 / Double.parseDouble(spin1.get(i).toString()));
                eh = (int) EH;
                everyH.setText("Every " + eh + " hr");
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

                }
                /*else if ( spin5.get(i).toString().equalsIgnoreCase("Year") ){
                    spin4.clear();
                    spin4.add("Select");
                    spin4.add("1");
                    Toast.makeText(getApplicationContext(), "Maximum duration is one year", Toast.LENGTH_SHORT).show();
                }
                                 */
                else {

                    spin4.clear();
                    spin4.add("Select");
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
        //spin5.add("Year");
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

    public void AddMedicine() {
        int hours = timePicker.getHour(); // after api level 23
        int minutes = timePicker.getMinute(); // after api level 23
        String medicineName = medicineNameET.getText().toString().trim();
        String numberOfTime = NOTS;
        String amountNumberSpinner = ANS;
        String amountTextSpinner = ATS;
        String numberDurationSpin = NDS;
        String textDurationSpin = TDS;

        String date = start_day_DATE.getText().toString();
        String userName = name;
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

        addMedicineToDB addEventToDB = new addMedicineToDB(this);
        addEventToDB.execute(medicineName, numberOfTime, amountNumberSpinner,
                amountTextSpinner, numberDurationSpin, textDurationSpin, date, userName, timeH, timeM, repeated);

    }


}