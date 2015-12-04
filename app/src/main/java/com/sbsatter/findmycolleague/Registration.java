package com.sbsatter.findmycolleague;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Registration extends AppCompatActivity {

    @Bind(R.id.loginButton)Button loginButton;

 //   private Button loginButton;
    private CheckBox showPassword;
    private EditText password;
    private EditText confirmPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

ButterKnife.bind(this);
     //   loginButton=(Button) findViewById(R.id.loginButton);
        showPassword=(CheckBox) findViewById(R.id.content_registration_showPasswordCheckbox);
        password=(EditText) findViewById(R.id.content_registration_password);
        confirmPassword=(EditText) findViewById(R.id.content_registration_confirmPassword);

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
                if(isChecked) {
                    password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    confirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                } else {
                    password.setInputType(129);
                    confirmPassword.setInputType(129);
                }
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

    @OnClick(R.id.loginButton)
    public void submit(){
        Intent i = new Intent(Registration.this, Login.class);
        startActivity(i);
    }


}
