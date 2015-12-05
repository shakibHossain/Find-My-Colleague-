package com.sbsatter.findmycolleague;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DrawerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Spinner spinner;
    protected static final String SOAP_ACTION = "http://dss.brac.net/GetAllProjects";
    protected static final String METHOD_NAME = "GetAllProjects";
    private static final String NAMESPACE = "http://dss.brac.net/";
    private static final String URL = "http://dss.brac.net/bracstandingdata/Service.asmx";
    protected static SoapSerializationEnvelope soapEnvelop;
    public static ArrayList<HashMap<String,String>> detail;
    ProgressDialog progressDialog;
    HashMap<String, String> staffInfoByName, staffInfoByPin, getAllPrograms;
    String searchString="";
    int selectedItemSpinner=2;

    View viewToChange;


    @Bind(R.id.searchEt)EditText searchEt;
    private android.content.Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context=this;
        spinner = (Spinner) findViewById(R.id.search_options);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.spinner_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedItemSpinner=position;
                if(selectedItemSpinner==1){
                    searchEt.setInputType(InputType.TYPE_CLASS_NUMBER);
                }else{
                    searchEt.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ButterKnife.bind(this);
      /*  FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
*/
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);





    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
            Intent i = new Intent(DrawerActivity.this, EditProfile.class);
            startActivity(i);
        } else if (id == R.id.nav_gallery) {
            Intent i = new Intent(DrawerActivity.this, Status.class);
            startActivity(i);

        } else if (id == R.id.nav_slideshow) {
            Intent i = new Intent(DrawerActivity.this, Login.class);
            startActivity(i);
            finish();
        }
        //else if (id == R.id.nav_manage) {

        // }
        //     else if (id == R.id.nav_share) {

        //   } else if (id == R.id.nav_send) {

        //  }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void search(View view) {
        searchString= searchEt.getText().toString();
        viewToChange=view;
        if(searchString.length()==0){
            Toast.makeText(DrawerActivity.this, "Please enter a valid search term", Toast.LENGTH_SHORT).show();
            return;
        }
        if(selectedItemSpinner==SOAPRequestHandler.StaffInfoByPIN && searchString.length()!=8){
            Toast.makeText(DrawerActivity.this, "PIN consist of 8 digits\nEnter them all", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, String> searchCriteria = new HashMap<>();
        SOAPRequestHandler soapRequestHandler;
        if(selectedItemSpinner==SOAPRequestHandler.StaffInfoByName){
            searchCriteria.put("strStaffName", searchString);
            soapRequestHandler = new SOAPRequestHandler(SOAPRequestHandler
                    .StaffInfoByName, searchCriteria);
        }else if(selectedItemSpinner==SOAPRequestHandler.StaffInfoByPIN){
            searchCriteria.put("strStaffPIN", searchString);
            soapRequestHandler = new SOAPRequestHandler(SOAPRequestHandler
                    .StaffInfoByPIN, searchCriteria);
        }else{
            soapRequestHandler = new SOAPRequestHandler(SOAPRequestHandler
                    .GetAllPrograms);
        }
        progressDialog= new ProgressDialog(this);
        progressDialog.setIndeterminate(false);

        soapRequestHandler.getJsonObject();



    }

    public void makeFragmentChanges(View view){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, new EmployeeInformation()).commit();
        InputMethodManager imm = (InputMethodManager)this.getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        Toast.makeText(this, "fragment commited", Toast.LENGTH_SHORT).show();
    }


    class SOAPRequestHandler {
        private static final int StaffInfoByName = 0;
        private static final int StaffInfoByPIN = 01;
        private static final int GetAllPrograms = 02;
        //    private static final int StaffInfoByName=0;
//    private static final int StaffInfoByName=0;
        private int MODE;
        private SoapSerializationEnvelope soapEnvelop;
        HashMap<String, String> params, requestProperty;

        public SOAPRequestHandler(int mode, HashMap<String, String> property) {
            MODE = mode;
            params = generateSOAPParameters();
            requestProperty = property;
        }

        public SOAPRequestHandler(int mode) {
            MODE = mode;
            params = generateSOAPParameters();
        }

        private void getJsonObject() {

            soapRequest();
            switch (MODE) {
                case 0:
                    staffInfoByName();
                    break;
                case 1:
                    staffInfoByPin();
                    break;
                case 2:
                    getAllPrograms();
                    break;
            }

        }

        private void staffInfoByPin() {

        }

        private void staffInfoByName() {


        }

        private void getAllPrograms() {

        }

        private void soapRequest() {
            SoapObject request = new SoapObject(params.get("NAMESPACE"), params.get("METHOD_NAME"));
            soapEnvelop = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            if (MODE != GetAllPrograms) {
                if (MODE == StaffInfoByName) {
                    request.addProperty("strStaffName", requestProperty.get("strStaffName"));
                } else {
                    request.addProperty("strStaffPIN", requestProperty.get("strStaffPIN"));
                }
            }
            soapEnvelop.dotNet = true;
            soapEnvelop.setOutputSoapObject(request);

            HttpTransportSE ht = new HttpTransportSE(params.get("URL"));
            Log.i("TAG", "after ht declaration");
            new ExecuteHTCall().execute(ht);

        }


        private HashMap<String, String> generateSOAPParameters() {
            String SOAP_ACTION = "http://tempuri.org/StaffInfoByName";
            String METHOD_NAME = "StaffInfoByName";
            String NAMESPACE = "http://tempuri.org/";
            String URL = "http://dataservice.brac.net:800/StaffInfo.asmx";
            HashMap<String, String> staffInfoByName = new HashMap<>(4);
            staffInfoByName.put("SOAP_ACTION", SOAP_ACTION);
            staffInfoByName.put("METHOD_NAME", METHOD_NAME);
            staffInfoByName.put("NAMESPACE", NAMESPACE);
            staffInfoByName.put("URL", URL);


            SOAP_ACTION = "http://tempuri.org/StaffInfoByPIN";
            METHOD_NAME = "StaffInfoByPIN";
            NAMESPACE = "http://tempuri.org/";
            URL = "http://dataservice.brac.net:800/StaffInfo.asmx";
            HashMap<String, String> staffInfoByPin = new HashMap<>(4);
            staffInfoByPin.put("SOAP_ACTION", SOAP_ACTION);
            staffInfoByPin.put("METHOD_NAME", METHOD_NAME);
            staffInfoByPin.put("NAMESPACE", NAMESPACE);
            staffInfoByPin.put("URL", URL);


            SOAP_ACTION = "http://dss.brac.net/GetAllPrograms";
            METHOD_NAME = "GetAllPrograms";
            NAMESPACE = "http://dss.brac.net/";
            URL = "http://dss.brac.net/bracstandingdata/Service.asmx";
            HashMap<String, String> getAllPrograms = new HashMap<>(4);
            getAllPrograms.put("SOAP_ACTION", SOAP_ACTION);
            getAllPrograms.put("METHOD_NAME", METHOD_NAME);
            getAllPrograms.put("NAMESPACE", NAMESPACE);
            getAllPrograms.put("URL", URL);


            switch (MODE) {
                case 0:
                    return staffInfoByName;
                case 1:
                    return staffInfoByPin;
                case 2:
                    return getAllPrograms;
            }

            return null;

        }

        class ExecuteHTCall extends AsyncTask<HttpTransportSE, Void, String> {
            ProgressDialog progressDialog;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog= new ProgressDialog(context);
                progressDialog.setIndeterminate(false);
                progressDialog.show();
            }

            @Override
            protected String doInBackground(HttpTransportSE... httpTransportSE) {
                try {
                    httpTransportSE[0].call(params.get("SOAP_ACTION"), soapEnvelop);
                    return "" + (SoapPrimitive) soapEnvelop.getResponse();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }

                return "ERROR";
            }

            @Override
            protected void onPostExecute(String s) {

                Toast.makeText(DrawerActivity.this, s, Toast.LENGTH_SHORT).show();
                detail= new ArrayList<>();
                try {
                    if (MODE != 2) {
                        nameOrId(s);
                    } else {
                        programs(s);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                progressView.resetAnimation();
                makeFragmentChanges(viewToChange);
                progressDialog.dismiss();
            }

            private void programs(String s) throws JSONException {
                JSONArray jsonArray = new JSONArray(s);
//                detail.put("ProgramID")
                String text = "";
                HashMap<String,String> programDetail=new HashMap<String, String>();
                programDetail.put("Type","Program");
                detail.add(programDetail);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = (JSONObject) jsonArray.get(i);
                    programDetail=new HashMap<String, String>();
                    String mname, mnameSpace = "";

                    String info = obj.getString("ProgramID") + ". " + obj.getString("ProgramName");
//                    response.add(info);
                    programDetail.put("Program",info);
                    detail.add(programDetail);
                    text = text + "\n" + info + "\n====================================\n";
                }
//                setTextToTextview(text);
            }

            private void nameOrId(String s) throws JSONException {
                JSONArray jsonArray = new JSONArray(s);
                String text = "";
                HashMap<String,String> empInfo= new HashMap<>();
                empInfo.put("Type","Employee");
                detail.add(empInfo);
                ArrayList<String> response = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = (JSONObject) jsonArray.get(i);
                    String mname, mnameSpace = "";
                    String name=obj.getString("Fname") + " " + (mname = obj.getString
                            ("Mname")) +
                            (mnameSpace = (!mname.equals("")) ? " " : "") + obj
                            .getString("Lname");
                    empInfo=new HashMap<String, String>();
                    empInfo.put("Name",name);
                    empInfo.put("Mobile",((mnameSpace=obj.getString("Mobile")).equals(""))?"N/A":
                            mnameSpace);
                    empInfo.put("Designation", obj.getString("Designation"));
                    detail.add(empInfo);
                }
//                setTextToTextview(text);
            }
        }

    }

}
