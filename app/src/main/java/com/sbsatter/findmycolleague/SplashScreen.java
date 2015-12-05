package com.sbsatter.findmycolleague;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

public class SplashScreen extends AppCompatActivity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    //    getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i;
                if(getLoginDetails()) {
                    i = new Intent(SplashScreen.this, DrawerActivity.class);
                }
                else{
                    i = new Intent(SplashScreen.this, Login.class);
                }
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    private boolean getLoginDetails() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String pin = prefs.getString(getString(R.string.pref_user_pin), "`");
        String password = prefs.getString(getString(R.string.pref_user_password), "`");
        Log.v("Splash screen", pin+" "+password);
        if(pin=="`" || password=="`"){
            return false;
        }
        return true;
    }
}


