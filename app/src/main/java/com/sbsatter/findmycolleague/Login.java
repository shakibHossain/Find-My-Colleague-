package com.sbsatter.findmycolleague;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Login extends AppCompatActivity {

    @Bind(R.id.loginButton)Button loginButton;

    private EditText username;
    private EditText password;
    public static String savedUserName;
    public static final String PREFS_NAME = "AOP_PREFS";
 //   SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        username=(EditText) findViewById(R.id.content_login_username);
        password=(EditText) findViewById(R.id.content_login_password);

        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    //username saved
                    savedUserName=username.getText().toString();

                }
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                }
            }
        });

        ButterKnife.bind(this);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLoginDetails();
                Intent i = new Intent(Login.this, DrawerActivity.class);
                startActivity(i);
            }
        });



//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

//    @OnClick(R.id.loginButton)
//    public void submit(){
//
//
//
//
//
//        Intent i = new Intent(Login.this, DrawerActivity.class);
//        startActivity(i);
//    }

    private void saveLoginDetails() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor;
        editor=prefs.edit();
        editor.putString(getString(R.string.pref_user_pin),username.getText().toString());
        editor.putString(getString(R.string.pref_user_password),password.getText().toString());
        editor.commit();
        Log.v("Login", getString(R.string.pref_user_pin)+" "+getString(R.string.pref_user_password));
    }


    public void goToRegistration(View view) {
        view.setBackgroundColor(Color.BLUE);
        Intent i=new Intent(Login.this,Registration.class);
        startActivity(i);
        finish();
    }
}
