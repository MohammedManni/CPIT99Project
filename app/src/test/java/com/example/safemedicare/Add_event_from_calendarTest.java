package com.example.safemedicare;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import static org.junit.Assert.*;

public class Add_event_from_calendarTest {
    String r=null;
    @Test
    public void addEvent() {
        String username = "manni";
        String operation = "AddEvent";
        String login_url = "http://192.168.100.9/AddEvent.php";
        try {
            String user_name = username; String type1 ="patient";String eventName = "Try Test";String Description = "Test";
            String Date = "5/14/2022"; String timeH = "9";  String timeM = "40";
            URL url = new URL(login_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("userName", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8") + "&"
                    + URLEncoder.encode("type1", "UTF-8") + "=" + URLEncoder.encode(type1, "UTF-8") + "&"
                    + URLEncoder.encode("eventName", "UTF-8") + "=" + URLEncoder.encode(eventName, "UTF-8") + "&"
                    + URLEncoder.encode("Description", "UTF-8") + "=" + URLEncoder.encode(Description, "UTF-8") + "&"
                    + URLEncoder.encode("Date", "UTF-8") + "=" + URLEncoder.encode(Date, "UTF-8") + "&"
                    + URLEncoder.encode("timeH", "UTF-8") + "=" + URLEncoder.encode(timeH, "UTF-8") + "&"
                    + URLEncoder.encode("timeM", "UTF-8") + "=" + URLEncoder.encode(timeM, "UTF-8") + "&"
                    + URLEncoder.encode("operation", "UTF-8") + "=" + URLEncoder.encode(operation, "UTF-8");
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String result = "";
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            r=result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assertEquals("Event Added",r);
    }
}