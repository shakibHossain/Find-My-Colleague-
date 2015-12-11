package com.sbsatter.findmycolleague;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class EditProfile extends AppCompatActivity {
    private EditText username;
    private EditText pin;
    private EditText designation;
    private EditText extension;
    private EditText phone;
    private EditText status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        username=(EditText) findViewById(R.id.content_edit_name);
        pin=(EditText) findViewById(R.id.content_edit_pin);
        designation=(EditText) findViewById(R.id.content_edit_designation);
        extension=(EditText) findViewById(R.id.content_edit_extension);
        phone=(EditText) findViewById(R.id.content_edit_phone);
       // status=(EditText) findViewById(R.id.content_edit_status);



//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }

    public void edit(View view) {

    }
}
