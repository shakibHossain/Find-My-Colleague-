package com.sbsatter.findmycolleague;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(Login.this, DrawerActivity.class);
////                startActivity(i);
//
//            }
//        });



//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    @OnClick(R.id.loginButton)
    public void submit(){
        Intent i = new Intent(Login.this, DrawerActivity.class);
//        startActivity(i);
        SOAPQueryClass soapqc= new SOAPQueryClass(Login.this);
        if(soapqc.validateNameAndID(username.getText().toString(),password.getText()
                .toString())){
            Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_SHORT).show();
        }
    }


    public void goToRegistration(View view) {
        Intent i=new Intent(Login.this,Registration.class);
        startActivity(i);
        finish();
    }
}
