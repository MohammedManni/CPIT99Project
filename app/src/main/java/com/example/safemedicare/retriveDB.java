package com.example.safemedicare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultUserTokenHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

public class retriveDB extends AppCompatActivity {
ListView list;
ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrive_d_b);


        list=(ListView)findViewById(R.id.list);
        adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
   list.setAdapter(adapter);
        new Connection().execute();
    }
    class Connection extends AsyncTask<String,String,String>{

        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String medication_url = "http://192.168.100.10/readCaregiver.php";
            try {
                HttpClient client=new DefaultHttpClient();
                HttpGet request =new HttpGet();
                request.setURI(new URI(medication_url));
                HttpResponse response= client.execute(request);
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuffer stringBuffer = new StringBuffer("");
                String line = "";
                while ((line = reader.readLine()) != null) {
                    stringBuffer.append(line);
                    break;
                }
                reader.close();
                result=stringBuffer.toString();


            }catch (Exception e){
                return new String("error");
            }



            return result;
        }
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonResult=new JSONObject(result);
                int success= jsonResult.getInt("success");
                if (success ==1){
                    JSONArray caregiverData= jsonResult.getJSONArray("caregiver");
                    for (int i=0; i<caregiverData.length();i++){
                        JSONObject caregiverObject=caregiverData.getJSONObject(i);
                        int id= caregiverObject.getInt("id");
                        String userName=caregiverObject.getString("userName");
                        String name=caregiverObject.getString("name");
                        int phoneNum=caregiverObject.getInt("phoneNumber");
                        TextView t=(TextView) findViewById(R.id.textVi);
                        t.setText(userName);
                        String line=id+" - "+userName+" - "+name+" - "+phoneNum;
                        adapter.add(line);

                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"no there",Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}