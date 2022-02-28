package com.example.safemedicare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class register1Activity extends AppCompatActivity {
    EditText Name, Surname,Age, Username,Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_1);
        Name = (EditText)findViewById(R.id.PersonName);
        Surname = (EditText)findViewById(R.id.PhoneNumber);
        Age = (EditText)findViewById(R.id.EmailAddress);
        Username = (EditText)findViewById(R.id.UserNameRE);
        Password = (EditText)findViewById(R.id.Password1);
        Button con = findViewById(R.id.Continue);

        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(register1Activity.this, register2Activity.class);
                startActivity(intent);
            }
        });

    }

    public void OnRegister(View view) {
        String username = Name.getText().toString();
        String userSurname = Surname.getText().toString();
        String useAge = Age.getText().toString();
        String userUsername = Username.getText().toString();
        String password = Password.getText().toString();
        String type = "register";
        db1BackgroundWorker db1BackgroundWorker = new db1BackgroundWorker(this);
        db1BackgroundWorker.execute(type, username, userSurname, useAge, userUsername, password);
    }
}