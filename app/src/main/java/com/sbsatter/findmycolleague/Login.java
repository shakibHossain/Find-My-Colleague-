package com.sbsatter.findmycolleague;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Login extends AppCompatActivity {

    @Bind(R.id.loginButton)Button loginButton;

    private EditText username;
    private EditText password;
    public static String savedUserName;
    public static final String PREFS_NAME = "AOP_PREFS";
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    public static boolean searchComplete;
    public static int search_auth_status_code=-1;

    //   SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor=prefs.edit();
        username=(EditText) findViewById(R.id.content_login_username);
        password=(EditText) findViewById(R.id.content_login_password);

        ButterKnife.bind(this);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticate(username.getText().toString(), password.getText()
                        .toString());


            }
        });


    }

    private void authenticate(String id, String pass) {
        AsyncHttpClientHelper helper= new AsyncHttpClientHelper(this);
        helper.veryifyUsernamePassword(id, pass);
    }





    public void goToRegistration(View view) {
        view.setBackgroundColor(Color.BLUE);
        Intent i=new Intent(Login.this,Registration.class);
        startActivity(i);
        finish();
    }
}
