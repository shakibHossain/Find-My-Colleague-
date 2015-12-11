package com.sbsatter.findmycolleague;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Status extends AppCompatActivity {
    @Bind(R.id.til_customMsg) TextInputLayout til;
    @Bind(R.id.til_customMsgET) EditText customMsgET;
    @Bind(R.id.til_customMsgBtn) Button customMsgBtn;
    @Bind(R.id.radio_customMsg) RadioButton r_customMsg;
    @Bind(R.id.radio_inAMeeting) RadioButton r_inAMeeting;
    @Bind(R.id.radio_inOffice) RadioButton r_inOffice;
    @Bind(R.id.radio_outOfOffice) RadioButton r_outOfOffice;
    static RadioButton radio_inAMeeting,radio_inOffice, radio_outOfOffice, radio_customMsg;
    static EditText staticCustomMsgET;
    static SharedPreferences.Editor editor;
    String status="";
    static SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        ButterKnife.bind(this);
        initUselessRadioButtons();
        customMsgET.setEnabled(false);
        customMsgBtn.setEnabled(false);
        prefs= PreferenceManager.getDefaultSharedPreferences(this);
        editor= prefs.edit();
        status=prefs.getString("savedstatus","status not set");
        Log.i("TAG","before fetching status was "+status);
        AsyncHttpClientHelper helper= new AsyncHttpClientHelper(this);
        helper.getStatus();


    }

    private void initUselessRadioButtons() {
        radio_inAMeeting=r_inAMeeting;
        radio_inOffice=r_inOffice;
        radio_outOfOffice=r_outOfOffice;
        radio_customMsg=r_customMsg;
        staticCustomMsgET= customMsgET;
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        String newStatus="";
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_customMsg:
                if (checked) {
                    customMsgET.setEnabled(true);
                    customMsgBtn.setEnabled(true);
                }
                break;
            default:
                setStatus(view.getId(),((RadioButton) view).getText().toString());
                break;

        }
    }
    @OnClick(R.id.til_customMsgBtn)
    public void setCustomMessage(){
        String newStatus= customMsgET.getText().toString();
        if(newStatus.length()==0){
            Toast.makeText(Status.this, "Status is blank!", Toast.LENGTH_SHORT).show();
            return;
        }
        setStatus(R.id.radio_customMsg,newStatus);
    }

    private void setStatus(int viewId, String status) {
        AsyncHttpClientHelper helper= new AsyncHttpClientHelper(this);
        helper.updateStatus(status);
    }

    public static void getStatus(String status){
        editor.putString("savedstatus",status);
        editor.commit();
        getStatus();
    }
    public static void getStatus() {
        String status=prefs.getString("savedstatus","status not set");
        Log.i("TAG",status);

        switch (status){
            case "In a Meeting":
                radio_inAMeeting.setChecked(true);
                break;
            case "In Office":
                radio_inOffice.setChecked(true);
                break;
            case "Out of Office":
                radio_outOfOffice.setChecked(true);
                break;
            default:
                radio_customMsg.setChecked(true);
                staticCustomMsgET.setEnabled(true);
                staticCustomMsgET.setText(status);
                break;
        }

    }
}
