package com.example.safemedicare;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class register1Activity extends AppCompatActivity {
    EditText Name, PhoneNumber, Age, Username, Password, ConfirmPassword;
    RadioGroup radioGroup;
    RadioButton radioButtonP, radioButtonC;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_1);
        // Edit text
        Username = (EditText) findViewById(R.id.UserNameRE);
        Name = (EditText) findViewById(R.id.PersonName);
        PhoneNumber = (EditText) findViewById(R.id.PhoneNumber);
        Age = (EditText) findViewById(R.id.Age);
        Password = (EditText) findViewById(R.id.Password1);
        ConfirmPassword = (EditText) findViewById(R.id.ConPassword1);
        // Radio Button
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioButtonP = (RadioButton) findViewById(R.id.PatientRadioButton);
        radioButtonC = (RadioButton) findViewById(R.id.CaregiverRadioButton2);


        // continue button
        Button con = findViewById(R.id.Continue);
        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   String a1 = Username.getText().toString();
                // check is all the input are filled
                if ((Username.getText().toString().equalsIgnoreCase("") || Name.getText().toString().equalsIgnoreCase("") ||
                        PhoneNumber.getText().toString().equalsIgnoreCase("") || Age.getText().toString().equalsIgnoreCase("") ||
                        Password.getText().toString().equalsIgnoreCase("") || ConfirmPassword.getText().toString().equalsIgnoreCase(""))
                        || (!radioButtonP.isChecked() && !radioButtonC.isChecked())) {
                    // toast test to fill all the input


                } else { // else whine all the input filled

                    // the password match check
                    if (Password.getText().toString().equals(ConfirmPassword.getText().toString())) {
                        // the check if patient or care giver
                        if (radioButtonP.isChecked()) {
                            // next register page Patient
                            Intent intent = new Intent(register1Activity.this, register2Activity.class);
                            startActivity(intent);


                        } else if (radioButtonC.isChecked()) {
                            // Caregiver home page
                            Intent intent = new Intent(register1Activity.this, caregiver_homePage_activity.class);
                            startActivity(intent);

                        } else {

                        }

                    } else {// else for not matching pass

                    }

                }

            }
        });


        // Back button
        Button Back = findViewById(R.id.Back);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(register1Activity.this, sign_activity.class);
                startActivity(intent);
            }
        });

    }

    public void OnRegister(View view) {
        String username = Name.getText().toString();
        String userSurname = PhoneNumber.getText().toString();
        String useAge = Age.getText().toString();
        String userUsername = Username.getText().toString();
        String password = Password.getText().toString();
        String type = "register";
        db1BackgroundWorker db1BackgroundWorker = new db1BackgroundWorker(this);
        db1BackgroundWorker.execute(type, username, userSurname, useAge, userUsername, password);
    }
}