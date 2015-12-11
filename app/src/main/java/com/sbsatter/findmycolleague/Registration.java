package com.sbsatter.findmycolleague;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class Registration extends AppCompatActivity {

    @Bind(R.id.loginButton)Button loginButton;
    @Bind(R.id.registerButton)Button registerButton;
    @Bind(R.id.content_registration_mobile) EditText mobileEt;

    private CheckBox showPassword;
    private EditText name;
    private EditText userName;
    private EditText password;
    private EditText confirmPassword;
    public static String BASE_URL_ADD ="http://192.168.0.100/fmc/index2.php";
    public static String BASE_URL_CHECK ="http://192.168.0.100/fmc/index.php";
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    AsyncHttpClient asyncHttpClient= new AsyncHttpClient();
    private boolean registrationSuccessful=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        asyncHttpClient.setMaxRetriesAndTimeout(5, 20000);
        ButterKnife.bind(this);
        prefs= PreferenceManager.getDefaultSharedPreferences(this);
        editor=prefs.edit();
        initOtherViews();



        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userName.length() < 1) {
                    Toast.makeText(Registration.this, "Invalid username", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }


                if (password.getText().toString().equals(confirmPassword.getText().toString())) {
                } else {
                    Toast.makeText(Registration.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    password.setText("");
                    confirmPassword.setText("");
                    return;
                }
                if (checkUniquenessOfUsername(userName.getText().toString())) {
                    Toast.makeText(Registration.this, "Username already taken, please choose another!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Log.i("TAG", "Registering");
                    postRegistrationDetail();

                }


//                SOAPQueryClass soapqc= new SOAPQueryClass(Registration.this);
//                boolean result= soapqc.validateNameAndID(name.getText().toString(), userName.getText()
//                        .toString());
//                Log.i("TAG", "Registration result is : " + result);
////                progressDialog.dismiss();
//                if(result) {
//                    register();
//                    Toast.makeText(Registration.this, "Login Successful", Toast.LENGTH_SHORT).show();
//                }
            }

        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Registration.this, Login.class);
                startActivity(i);
            }
        });

        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    confirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    password.setInputType(129);
                    confirmPassword.setInputType(129);
                }
            }
        });




    }

    private void initOtherViews() {
        showPassword=(CheckBox) findViewById(R.id.content_registration_showPasswordCheckbox);
        password=(EditText) findViewById(R.id.content_registration_password);
        name =(EditText) findViewById(R.id.content_registration_name);
        userName =(EditText) findViewById(R.id.content_registration_username);
        confirmPassword=(EditText) findViewById(R.id.content_registration_confirmPassword);
    }

    private void postRegistrationDetail() {
        RequestParams params= new RequestParams();
        params.put("Name",name.getText().toString());
        params.put("username",userName.getText().toString());
        params.put("password", password.getText().toString());
        params.put("mobile", mobileEt.getText().toString());
        asyncHttpClient.post(BASE_URL_ADD, params/*null*/, new JsonHttpResponseHandler() {
            ProgressDialog dialog;

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                        super.onSuccess(statusCode, headers, response);
                try {
                    Log.i("TAG", "onSuccess() Array returned: " + response.toString(4));
//                    Toast.makeText(Registration.this, "onSuccess() Array returned: " + response.toString(4),
//                            Toast.LENGTH_SHORT).show();
                    registrationSuccessful=true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                        super.onSuccess(statusCode, headers, response);
                try {
                    Log.i("TAG","onSuccess() Array returned: " + response.toString(4));
//                    Toast.makeText(Registration.this, "onSuccess() Array returned: " + response.toString(4),
//                            Toast.LENGTH_SHORT).show();

                    registrationSuccessful=true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStart() {
                super.onStart();
                dialog = new ProgressDialog(Registration.this);
                dialog.setIndeterminate(true);
                dialog.setMessage("Request is processing");
                dialog.show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dialog.dismiss();
                if(registrationSuccessful){
                    register();
                }
            }
        });

    }

    private boolean checkUniquenessOfUsername(String username) {
        RequestParams params= new RequestParams();
        params.put("verify_username", username);
        final boolean[] result = new boolean[1];

        asyncHttpClient.post(BASE_URL_CHECK, params, new JsonHttpResponseHandler() {
            ProgressDialog dialog;

            @Override
            public void onStart() {
                super.onStart();
                dialog= new ProgressDialog(Registration.this);
                dialog.setIndeterminate(true);
                dialog.setMessage("Checking username");
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    Log.i("TAG","onSuccess() (verification) returned: " + response.toString(4));
                    Toast.makeText(Registration.this, "JSONObject response: "+response.toString(4),
                            Toast
                            .LENGTH_SHORT)
                            .show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    Log.i("TAG","exists: "+response.getString("exists"));
                    result[0] = (response.getString("exists")).equals("true");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFinish() {
                super.onFinish();
                dialog.dismiss();
            }
        });
        return result[0];

    }


    public void register() {


        editor.putString(getString(R.string.pref_loggedIn_username), userName.getText().toString());
        editor.putString(getString(R.string.pref_loggedIn_name), name.getText().toString());
        editor.putString(getString(R.string.pref_loggedIn_password),password.getText().toString());
        editor.commit();
//        TextView drawerHeader=((TextView) findViewById(R.id.nav_head_text));
//        Log.i("TAG",(drawerHeader==null)+"= drawerheader");
//        drawerHeader.setText(name.getText()
//                .toString());

//        Log.i("TAG", prefs.getString(getString(R.string.pref_user_pin), null) + "-" + name.getText()
//                .toString());
        Intent i = new Intent(Registration.this, DrawerActivity.class);
        Log.i("TAG","Logging in as :"+prefs.getString(getString(R.string.pref_loggedIn_name),
                "No Name stored")+" ("+prefs.getString(getString(R.string.pref_loggedIn_name),
                "username not available")+")");
        startActivity(i);

        //  (new saveInDatabase(this)).execute(userName.getText().toString(),password.getText().toString(),"Inactive","0.000","0.000");

    }




}
