package com.example.safemedicare;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
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
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

public class Home_Page_Activity extends AppCompatActivity {

    private String name, type;
    GridView gridList;
    ArrayList eventList=new ArrayList<>();
    GridAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_patient);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("USERNAME");
            type = extras.getString("TYPE");
            //Toast.makeText(getApplicationContext(), "Welcome "+name, Toast.LENGTH_SHORT).show();
        }

        /////////////////////////////////////////////////////////////////////

        // toolbar buttons
        Button Profile = findViewById(R.id.firstB);
        Button Schedule = findViewById(R.id.SecondB);
        Button Add = findViewById(R.id.thirdB);
        Button SOS = findViewById(R.id.SOS);


        Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_Page_Activity.this, Profile_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_Page_Activity.this, Schedule_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_Page_Activity.this, Add_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        SOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home_Page_Activity.this, SOS_Activity.class);
                intent.putExtra("USERNAME", name);
                intent.putExtra("TYPE", type);
                startActivity(intent);
            }
        });

        //////////////////////////////  end toolbar button//////////////////////////////////////////////


        ///// START GRID VIEW /////

            gridList = (GridView) findViewById(R.id.gridView);



        int logos[] = {R.drawable.logo19, R.drawable.logo19, R.drawable.logo19, R.drawable.logo19,
                R.drawable.logo19, R.drawable.logo19};
        new Connection().execute();
     myAdapter=new GridAdapter(this,R.layout.gridview_item,eventList);

        //new Connection().execute();

        // implement setOnItemClickListener event on GridView
        gridList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // set an Intent to Another Activity
                Intent intent = new Intent(Home_Page_Activity.this, SecondActivity.class);
                intent.putExtra("image", logos[position]); // put image data in Intent
                startActivity(intent); // start Intent
            }
        });



        }

    class Connection extends AsyncTask<String, String, String> {
        // starting the connection
        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String medication_url = "http://192.168.100.171/readCaregiver.php";
            try {

                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(medication_url));
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
        // getting the data
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonResult = new JSONObject(result);
                int success = jsonResult.getInt("success");
                if (success == 1) {
                    JSONArray caregiverData = jsonResult.getJSONArray("caregiver");
                    for (int i = 0; i < caregiverData.length(); i++) {
                        JSONObject caregiverObject = caregiverData.getJSONObject(i);
                        int id = caregiverObject.getInt("id");
                        String userName = caregiverObject.getString("userName");
                        String name = caregiverObject.getString("name");
                        //  int linkID = caregiverObject.getInt("linkID");
                        int phoneNum = caregiverObject.getInt("phoneNumber");
                        int Age = caregiverObject.getInt("age");
                        //try to match the constructor fullName,  username,  id,  linkID,  phone_number,  age)
                        //eventList.add(new GridItem(name + "\n" + userName + "\n" + id + "\n" + id + "\n" + phoneNum + "\n" + Age,R.drawable.ic_add_location));
                        eventList.add(new GridItem(name + "\n" + userName + "\n" + id + "\n" + id + "\n" + phoneNum + "\n" + Age));
                        //eventList.add(new GridItem(userName,R.drawable.logo19));
                       // String line = name + " - " + userName + " - " + id + " - " + id + " - " + phoneNum + " - " + Age ;
                         gridList.setAdapter(myAdapter);

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "no there", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
