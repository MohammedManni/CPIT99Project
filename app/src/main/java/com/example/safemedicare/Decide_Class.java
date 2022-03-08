package com.example.safemedicare;

import androidx.appcompat.app.AppCompatActivity;

public class Decide_Class extends AppCompatActivity {
    /*
    public static final String user = "user";
    String name, pass;
    int choice = 0;
    String data1, data2;
    int data3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.home_page_patient);

        //  Intent i = getIntent();
        // name = i.getStringExtra(user);
        // Intent myIntent = new Intent(Decide_Class.this, Home_Page_Activity.class);
        //  myIntent.putExtra("Data1", "data1");
        //  myIntent.putExtra("Data2", "data2");
        //  myIntent.putExtra("Data3", 123);
        //  Decide_Class.this.startActivity(myIntent);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("USERNAME");
            pass = extras.getString("PASSWORD");

        }

        Connection connection = new Connection();
        connection.execute();

    }

    class Connection extends AsyncTask<String, String, String> {
        // starting the connection
        @Override
        protected String doInBackground(String... strings) {
            String result = "";
            String Caregiver_url = "http://192.168.100.171/LooooooogIn.php";
           // String Caregiver_url = "http://192.168.100.171/LooooooogIn.php?user_name="+name+"& user_pass="+pass;

            try {




                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(Caregiver_url));
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

                        Intent myIntent = new Intent(Decide_Class.this, caregiver_homePage_activity.class);

                        startActivity(myIntent);

                }else if (success == 2) {

                        Intent myIntent = new Intent(Decide_Class.this, Home_Page_Activity.class);

                        startActivity(myIntent);

                }
                else {
                    Toast.makeText(getApplicationContext(), "no there", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

     */
}