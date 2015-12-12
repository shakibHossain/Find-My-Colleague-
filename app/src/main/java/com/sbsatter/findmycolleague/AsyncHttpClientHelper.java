package com.sbsatter.findmycolleague;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by ASUS on 12/11/2015.
 */
public class AsyncHttpClientHelper {


    AsyncHttpClient asyncHttpClient;
    public static String BASE_URL ="http://192.168.0.102/fmc/";
    public static final String BASE_URL_SEARCHNAME =BASE_URL+"search.php";
    public static String BASE_URL_STATUS = BASE_URL + "status.php";
    public static String BASE_URL_ADD =BASE_URL+"index2.php";
    public static String BASE_URL_CHECK =BASE_URL+"index.php";
    public static String BASE_URL_VERIFY_USERNAME =BASE_URL+"verifyUsernamePassword.php";
    private static String username;
    private static String pass;
    private static String mobile;
    private static String name;
    private static String status;
    private static double lat;
    private static double lon;
    private static RequestParams params;
    private static Context context;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    ProgressDialog dialog;
    AsyncHttpClientHelper(Context context){
        asyncHttpClient= new AsyncHttpClient();
        this.context=context;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        editor=prefs.edit();

    }

    public void veryifyUsernamePassword(String username, String pass) {
        this.username= username;
        this.pass=pass;
        params= new RequestParams();
        params.put("password", this.pass);
        params.put("username", this.username);
        final int []result= new int[1];
        result[0]=-1;
        asyncHttpClient.post(BASE_URL_VERIFY_USERNAME, params, new JsonHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                dialog = new ProgressDialog(context);
                dialog.setIndeterminate(true);
                dialog.setMessage("Verifying username");
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Log.i("TAG", "onSuccess() (login verification) returned: " + response.toString
                            (4));
                    Toast.makeText(context, "JSONObject response: " + response.toString(4),
                            Toast
                                    .LENGTH_SHORT)
                            .show();

                    Log.i("TAG", "username & password match: " + response.getString("match"));
                    if ((response.getString("match")).equals("true")) {
                        name = response.getString("Name");
                        mobile = response.getString("mobile");
                        status = response.getString("status");
                        lat = response.getDouble("lat");
                        lon = response.getDouble("lon");
                        saveLoginDetails();
                        Intent intent = new Intent(context, DrawerActivity.class);
                        context.startActivity(intent);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dialog.dismiss();
                Login.searchComplete = true;
            }
        });


    }

    private void saveLoginDetails() {

        editor.putString("savedusername", username);
        editor.putString("savedmobile", mobile);
        editor.putFloat("savedlat", (float) lat);
        editor.putFloat("savedlon", (float) lon);
        editor.putString("savedpassword", pass);
        editor.putString("savedname", name);
        editor.putString("savedstatus", status);
        editor.commit();
        Log.i("TAG", "Logging in as :" + prefs.getString("savedname",
                "Name not available") + " (username: " + prefs.getString("savedusername",
                "username not available") + ")");
    }

    public void updateStatus(String status) {
        editor.putString("savedstatus", status);
        editor.commit();
        String username= prefs.getString("savedusername","Name unavailable");
        if(username.equals("Name unavailable"))
            Toast.makeText(context, "Status update failed because name is unavailable", Toast
                    .LENGTH_SHORT)
                    .show();
        params= new RequestParams();
        params.put("status", status);
        params.put("username", username);
        asyncHttpClient.post(BASE_URL_STATUS, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Toast.makeText(context, "Status update succeeded", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(context, "Status update failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStart() {
                super.onStart();
                dialog = new ProgressDialog(context);
                dialog.setIndeterminate(false);
                dialog.setMessage("Updating");
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dialog.dismiss();
            }
        });


    }

    public void getStatus() {
        params= new RequestParams();
        String username= prefs.getString("savedusername","username not set");
        if(username.equals("username not set")){
            Toast.makeText(context, "Username not set in preferences", Toast.LENGTH_SHORT).show();
            return;
        }
        params.put("username",username);
        params.put("getStatus","TRUE");
        asyncHttpClient.post(BASE_URL_STATUS,params,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Log.i("TAG",response.toString(4));
                    String status= response.getString("status");
                    Log.i("TAG","in helper class: status="+status);
//                    editor.putString("savedstatus",status);
                    Status.getStatus(status);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
