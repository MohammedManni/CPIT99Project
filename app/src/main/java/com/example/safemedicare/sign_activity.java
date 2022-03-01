package com.example.safemedicare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class sign_activity extends AppCompatActivity {
    EditText Username, Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        Username = (EditText) findViewById(R.id.UserName);
        Password = (EditText) findViewById(R.id.Password);

        Button register = findViewById(R.id.Register);
        Button logIn = findViewById(R.id.LogIN);

        if (Username.getText().toString().isEmpty()) {
            Username.setError("ENTER the User User Name ");

        }
        if (Password.getText().toString().isEmpty()) {
            Password.setError("ENTER the Password ");

        }
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
// if for empty edit text
OnLogin(view);
                Intent intent = new Intent(sign_activity.this, Home_Page_Activity.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(sign_activity.this, register1Activity.class);
                startActivity(intent);
            }
        });

    }
    public void OnLogin(View view) {
        String username = Username.getText().toString();
        String password = Password.getText().toString();
        String type = "login";
        db1BackgroundWorker backgroundWorker = new db1BackgroundWorker(this);
        backgroundWorker.execute(type, username, password);     }

}