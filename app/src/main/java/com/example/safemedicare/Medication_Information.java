package com.example.safemedicare;

import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Medication_Information extends AppCompatActivity {

    TextView textView1, textView2, text3, textView4,  textView6, textView7, textView13, textView14, textView15, textView16, textView17, textView18,  textView20, textView21, textView22;
    String operation, type, NameM,numberOfTime, doseAmountNumber,doseAmountText, duration,durationByText,timeH, timeM, everyH, repeated ,eventDescription, date ;
    Switch takenMedicineSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridview_details);
        //lift
        textView1= findViewById(R.id.View1);
        textView2= findViewById(R.id.View2);
        text3= findViewById(R.id.View3);
        textView4= findViewById(R.id.View4);
        textView6= findViewById(R.id.View6);
        textView7= findViewById(R.id.View5);

        //right
        textView13= findViewById(R.id.textView13);
        textView14= findViewById(R.id.textView14);
        textView15= findViewById(R.id.textView15);
        textView16= findViewById(R.id.textView16);
        textView17= findViewById(R.id.textView17);
        textView18= findViewById(R.id.textView18);
        textView20= findViewById(R.id.textView20);
        textView21= findViewById(R.id.textView21);
        textView22= findViewById(R.id.textView22);

        takenMedicineSwitch= findViewById(R.id.takenMedicineSwitch);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            operation = extras.getString("operation");
            type = extras.getString("type");

            if (operation.matches("1")){
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
                //set the left text view
                textView1.setText("Medicine Name: ");
                textView2.setText("Number of time: " );
                text3.setText("Dose Amount: ");
                textView4.setText("Duration: ");

                textView6.setText("Time: ");
                textView7.setText("Taken every: ");



                // set the right text view
                textView13.setText(NameM);
                textView14.setText(numberOfTime+" time/s");
                textView15.setText(doseAmountNumber+" ");
                textView16.setText(doseAmountText);
                textView17.setText(duration+" ");
                textView18.setText(durationByText);

                textView20.setText(timeH+" : ");
                textView21.setText(timeM);
                textView22.setText(everyH+" Hours");

                if (type.equalsIgnoreCase("caregiver")){
                    takenMedicineSwitch.setVisibility(View.GONE);
                }

            }else if (operation.matches("2")){

                NameM = extras.getString("eventName");
                eventDescription = extras.getString("eventDescription");
                date = extras.getString("date");
                timeH = extras.getString("timeH");
                timeM = extras.getString("timeM");

                //set the left text view
                textView1.setText("Event Name: ");
                textView2.setText("Event Description: ");
                text3.setText("Time: ");

                // set the right text view
                textView13.setText(NameM);
                textView14.setText(eventDescription);
                textView15.setText(timeH+" : ");
                textView16.setText(timeM);

                //lift
                textView4.setVisibility(View.GONE);
                textView6.setVisibility(View.GONE);
                textView7.setVisibility(View.GONE);

                //right
                textView17.setVisibility(View.GONE);
                textView18.setVisibility(View.GONE);
                textView20.setVisibility(View.GONE);
                textView21.setVisibility(View.GONE);
                textView22.setVisibility(View.GONE);
                //switch


                takenMedicineSwitch.setVisibility(View.GONE);

            }



        }


    }
}