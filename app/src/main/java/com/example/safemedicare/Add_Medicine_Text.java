package com.example.safemedicare;

import android.app.DatePickerDialog;
import android.content.Intent;
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

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class Add_Medicine_Text extends AppCompatActivity {
     String name, type;
    Spinner numberOfTimeSpin,amountNumberSpinner,amountTextSpinner ,numberDurationSpin,textDurationSpin;
    DatePickerDialog datePickerDialog;

    ArrayList spin1,spin2,spin3,spin4,spin5 ;
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
        EditText medicineNameET  = findViewById(R.id.EditMedicineName);
         numberOfTimeSpin = (Spinner) findViewById(R.id.numberOfTimeSpin);
         amountNumberSpinner = (Spinner) findViewById(R.id.amountNumberSpinner);
         amountTextSpinner = (Spinner) findViewById(R.id.amountTextSpinner);
         numberDurationSpin = (Spinner) findViewById(R.id.numberDurationSpin);
         textDurationSpin = (Spinner) findViewById(R.id.TextDurationSpin);
        CheckBox saturday  = findViewById(R.id.Saturday);
        CheckBox sunday  = findViewById(R.id.Sunday);
        CheckBox monday  = findViewById(R.id.Monday);
        CheckBox tuesday  = findViewById(R.id.Tuesday);
        CheckBox wednesday  = findViewById(R.id.Wednesday);
        CheckBox thursday  = findViewById(R.id.Thursday);
        CheckBox friday  = findViewById(R.id.Friday);
        CheckBox all  = findViewById(R.id.ALL);
        Button start_day_DATE = findViewById(R.id.Start_day_DATE);
        Button add_medicine = findViewById(R.id.Add_medicine);
        Button back = findViewById(R.id.back);

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
                        start_day_DATE.setText(dayOfMonth + "/" + (monthOfYear+1) + "/" + year);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        /////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

        ArrayAdapter adapter1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spin1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        numberOfTimeSpin.setAdapter(adapter1);
        numberOfTimeSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // wantToDelete = bankNamesDelete.get(i).toString();

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
                // wantToDelete = bankNamesDelete.get(i).toString();

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
                // wantToDelete = bankNamesDelete.get(i).toString();

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
                // wantToDelete = bankNamesDelete.get(i).toString();

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
                // wantToDelete = bankNamesDelete.get(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void forLoopSpinner() {
      // for number of time
        for (int i =1; i<=6;i++){
            spin1.add(i);
        }
        // for number of time
        for (int i =1; i<=4;i++){
            spin2.add(i);
        }
        // for number of time
        //for (int i =1; i<=6;i++){
            spin3.add("Pills");
        //}
        // for duration number
        for (int i =1; i<=30;i++){
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
}