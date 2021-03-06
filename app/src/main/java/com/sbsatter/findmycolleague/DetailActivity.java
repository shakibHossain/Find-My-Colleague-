package com.sbsatter.findmycolleague;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    @Bind(R.id.content_detail_name)TextView name;
    @Bind(R.id.content_detail_phone)TextView mobile;
    @Bind(R.id.content_detail_status)TextView status;
    @Bind(R.id.content_detail_coords)TextView coords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        setTitle("FindMyColleague");
        String data= getIntent().getStringExtra("Name");
//        Bundle bundle= getIntent().getBundleExtra("bundle");
        name.setText(data);
        mobile.setText(getIntent().getStringExtra("mobile"));
        status.setText(getIntent().getStringExtra("status"));
        coords.setText(getIntent().getStringExtra("coordinates"));
//        designation.setText(data.get("Designation").toString());
//        sex.setText(data.get("Sex").toString());
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


    }

}
