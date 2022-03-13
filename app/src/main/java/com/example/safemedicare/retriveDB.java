package com.example.safemedicare;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
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

public class retriveDB extends AppCompatActivity {
    ListView list;
    ArrayAdapter<String> adapter;
    CaregiverClass caregiver;
    CaregiverClass[] caregiverList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrive_d_b);


        list = (ListView) findViewById(R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView =findViewById(R.id.textVi);
                textView.setText(adapter.getItem(i).toString());
            }
        });
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        list.setAdapter(adapter);
        new Connection().execute();
    }

    class Connection extends AsyncTask<String, String, String> {
        // starting the connection
        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String medication_url = "http://192.168.100.197/readCaregiver.php";
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
                        caregiver= new CaregiverClass(name,userName,id,id,phoneNum,Age);
                        caregiverList = new CaregiverClass[caregiverData.length()];
                        caregiverList[i]= caregiver;
                        String line = name + " - " + userName + " - " + id + " - " + id + " - " + phoneNum + " - " + Age ;
                        adapter.add(line);

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