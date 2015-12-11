package com.sbsatter.findmycolleague;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class saveInDatabase extends AsyncTask<String, Void, String> {

    private Context context;

    public saveInDatabase(Context context) {
        this.context = context;
    }

    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(String... arg0) {
        String pin = arg0[0];
        String password = arg0[1];
        String status = arg0[2];
        String lat = arg0[3];
        String lng = arg0[4];

        String link;
        String data;
        BufferedReader bufferedReader;
        String result="";

        try {
            data = "?pin=" + URLEncoder.encode(pin, "UTF-8");
            data += "&status=" + URLEncoder.encode(status, "UTF-8");
            data += "&password=" + URLEncoder.encode(password, "UTF-8");
            data += "&lat=" + URLEncoder.encode(lat, "UTF-8");
            data += "&lng=" + URLEncoder.encode(lng, "UTF-8");

            link = "http://192.168.0.39/fmc/index2.php" + data;
            URL url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setDoOutput(true);

            conn.setChunkedStreamingMode(0);
            //       conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/plain");

      //      String input = YOUR_INPUT_STRING;

            OutputStream os = conn.getOutputStream();
        //    os.write(input.getBytes());
            os.flush();
//            conn.setReadTimeout(10000);
//            conn.setConnectTimeout(15000);
//            conn.setRequestMethod("POST");
//            conn.setDoInput(true);
//            conn.setDoOutput(true);
//            Log.v("SaveInDatabase URL///",""+url);
//            OutputStream os = conn.getOutputStream();
//            BufferedWriter writer = new BufferedWriter(
//                    new OutputStreamWriter(os, "UTF-8"));
//      //      Log.v("SaveInDatabase buff",""+bufferedReader);
//            writer.write(getQuery(params));
//            writer.flush();
//            writer.close();
//            os.close();
//
//            conn.connect();
//      //      Log.v("SaveInDatabase RES",""+result);
            return result;
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String result) {
        String jsonStr = result;
        Log.v("SaveInDatabase//////",jsonStr);
        if (jsonStr != null) {
            try {
                JSONObject jsonObj = new JSONObject(jsonStr);
                String query_result = jsonObj.getString("query_result");
                if (query_result.equals("SUCCESS")) {
                    Toast.makeText(context, "Data inserted successfully. Signup successfull.", Toast.LENGTH_SHORT).show();
                } else if (query_result.equals("FAILURE")) {
                    Toast.makeText(context, "Data could not be inserted. Signup failed.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Couldn't connect to remote database.", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context, "Error parsing JSON data.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Couldn't get any JSON data.", Toast.LENGTH_SHORT).show();
        }
    }
}