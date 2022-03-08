package com.example.safemedicare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class personal_info_Activity extends AppCompatActivity {

    private String name, type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_info);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("USERNAME");
            type = extras.getString("TYPE");

        }

        EditText UserNameINProfileEDITText = (EditText) findViewById(R.id.UserNameINProfileEDITText);
        EditText Password = (EditText) findViewById(R.id.PasswordINProfileEditText);

        EditText ConfirmPassword = (EditText) findViewById(R.id.ConPasswordINProfileEditText);

        UserNameINProfileEDITText.setText(name);
        // toolbar buttons
        Button Profile = findViewById(R.id.firstB);
        Button Schedule = findViewById(R.id.SecondB);
        Button Add = findViewById(R.id.thirdB);
        Button SOS = findViewById(R.id.SOS);
        ImageButton imageButton = findViewById(R.id.imageButton);

        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(personal_info_Activity.this, Profile_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(personal_info_Activity.this, Schedule_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(personal_info_Activity.this, Add_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        SOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(personal_info_Activity.this, SOS_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type.equalsIgnoreCase("patient")){
                    Intent intent = new Intent(personal_info_Activity.this, Home_Page_Activity.class);
                    intent.putExtra("USERNAME", name);
                    intent.putExtra("TYPE", type);
                    startActivity(intent);

                }else if (type.equalsIgnoreCase("caregiver")){
                    Intent intent = new Intent(personal_info_Activity.this, caregiver_homePage_activity.class);
                    intent.putExtra("USERNAME", name);
                    intent.putExtra("TYPE", type);
                    startActivity(intent);

                }
            }
        });

        ///////////////////////END TOOLBAR BUTTON//////////////////////////////////////////

        Button saveChange = findViewById(R.id.saveChange);
        saveChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(personal_info_Activity.this, sign_activity.class);
                //startActivity(intent);


                if (!Password.getText().toString().equalsIgnoreCase("") && !ConfirmPassword.getText().toString().equalsIgnoreCase("")) {
                    // the password match check
                    if (Password.getText().toString().equals(ConfirmPassword.getText().toString())) {

                        // update the information
                        OnUpdate(view);


                    } else {// else for not matching pass
                        Toast.makeText(personal_info_Activity.this, "the Password not matching", Toast.LENGTH_LONG).show();
                    }

                } else {// else for not matching pass
                    Toast.makeText(personal_info_Activity.this, "Nothing to update", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    public void OnUpdate(View view) {
 /*
       String username = Username.getText().toString();
        String name = Name.getText().toString();
        String phoneNumber = PhoneNumber.getText().toString();
        String age = Age.getText().toString();
        String password = Password.getText().toString();
        String type = "register";
        db1BackgroundWorker db1BackgroundWorker = new db1BackgroundWorker(this);
        db1BackgroundWorker.execute(type, username, name, phoneNumber, age, password);
*/
    }
}