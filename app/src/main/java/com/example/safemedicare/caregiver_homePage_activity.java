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

public class caregiver_homePage_activity extends AppCompatActivity {
    ListView list;
    ArrayAdapter<String> adapter;

        /*ListView patientList;
        String listName[] = {"Ali", "Ahmed", "Khalid", "Nasser", "Fares", "Ali"};
        int flags[] = {R.drawable.logo19, R.drawable.logo19, R.drawable.logo19,
                R.drawable.logo19, R.drawable.logo19, R.drawable.logo19};
         */

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_caregiver_home_page);
           /*
            patientList = (ListView) findViewById(R.id.patientList);
            ListAdabter customAdapter = new ListAdabter(getApplicationContext(), listName, flags);
            patientList.setAdapter(customAdapter);
            */


            ////////////// read from database///////////////////////////

            list = (ListView) findViewById(R.id.patientList);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    TextView textView =findViewById(R.id.textVi);
                    textView.setText(adapter.getItem(i).toString());
                }
            });
            adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
            list.setAdapter(adapter);
            new ConnectionToReadPatient().execute();
        }
    ///////////////////////////// class for read from DB ///////////////////////////////////////////////////////////////////
    class ConnectionToReadPatient extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String readPatient_url = "http://192.168.100.10/readPatient.php";
            try {
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(readPatient_url));
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
                if (success == 1) {
                    JSONArray patientData = jsonResult.getJSONArray("patient");
                    for (int i = 0; i < patientData.length(); i++) {
                        JSONObject patientObject = patientData.getJSONObject(i);
                        int id = patientObject.getInt("id");
                        String userName = patientObject.getString("userName");
                        String name = patientObject.getString("name");
                        int phoneNum = patientObject.getInt("phoneNumber");
                        int age = patientObject.getInt("age");


                        String line = id + " - " + userName + " - " + name + " - " + phoneNum + " - "+age;
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