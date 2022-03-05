package com.example.safemedicare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class register2Activity extends AppCompatActivity {

    public EditText HomeAddress, PrimeHospital;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_2);

        HomeAddress = (EditText) findViewById(R.id.HomeAddress);
        PrimeHospital = (EditText) findViewById(R.id.PrimeHospital);

        Button Confirm = findViewById(R.id.Confirm2);

        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check if there is a null value
                if (HomeAddress.getText().toString().equalsIgnoreCase("") || PrimeHospital.getText().toString().equalsIgnoreCase("")){
                    // toast to fill all the edit text
                    Toast.makeText(register2Activity.this, "Please fill all blanks", Toast.LENGTH_LONG).show();
                }else {  // if there is no null value


                    Intent intent = new Intent(register2Activity.this, Home_Page_Activity.class);
                    startActivity(intent);

                }



            }
        });

        // Back button
        Button Back = findViewById(R.id.Back);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(register2Activity.this, register1Activity.class);
                startActivity(intent);
            }
        });

    }
}