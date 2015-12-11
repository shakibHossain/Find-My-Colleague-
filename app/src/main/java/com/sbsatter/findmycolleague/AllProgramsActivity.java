package com.sbsatter.findmycolleague;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
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

import butterknife.Bind;
import butterknife.ButterKnife;

public class AllProgramsActivity extends AppCompatActivity {
    @Bind(R.id.programsListView)ListView listview;
    private String SOAP_ACTION,URL,METHOD_NAME,NAMESPACE;
    private SoapSerializationEnvelope soapEnvelop;
    protected static ArrayList<String> detail;
    ArrayAdapter<String> listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_programs);
        setTitle("All Programs");
        ButterKnife.bind(this);
        init();
        SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
        soapEnvelop = new SoapSerializationEnvelope(SoapEnvelope.VER12);
        soapEnvelop.dotNet = true;
        soapEnvelop.setOutputSoapObject(request);

        HttpTransportSE ht = new HttpTransportSE(URL);
        Log.i("TAG", "after ht declaration");
        new ExecuteHTCall().execute(ht);
        listViewAdapter=new ArrayAdapter<String>(
                this, // The current context (this activity)
                R.layout.list_item_info, // The name of the layout ID.
                R.id.list_item_forecast_textview // The ID of the textview to populate.
                );

    }


    private void init() {
        SOAP_ACTION = "http://dss.brac.net/GetAllPrograms";
        METHOD_NAME = "GetAllPrograms";
        NAMESPACE = "http://dss.brac.net/";
        URL = "http://dss.brac.net/bracstandingdata/Service.asmx";

    }

    class ExecuteHTCall extends AsyncTask<HttpTransportSE, Void, String> {
        ProgressDialog progressDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog= new ProgressDialog(AllProgramsActivity.this);
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(HttpTransportSE... httpTransportSE) {
            Exception ex;
            try {
                Log.i("TAG", "doInBackground");
                httpTransportSE[0].call(SOAP_ACTION,soapEnvelop);
                return "" + (SoapPrimitive) soapEnvelop.getResponse();
            } catch (IOException e) {
                ex=e;
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                ex=e;
                e.printStackTrace();
            }
            Log.i("TAG", "doInBackground ends with exception: " + ex.getMessage());
            return "ERROR";
        }

        @Override
        protected void onPostExecute(String s) {

//                Toast.makeText(DrawerActivity.this, s, Toast.LENGTH_SHORT).show();
            if(s.equals("ERROR")){
                Log.i("TAG", "ERROR on Post Execute");
//                progressDialog.dismiss();
                return;
            }
            detail= new ArrayList<>();
            try {
                programs(s);
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(AllProgramsActivity.this, "JSONException caught: "+e.getMessage(), Toast
                        .LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }
        private void programs(String s) throws JSONException {
            JSONArray jsonArray = new JSONArray(s);
            detail=new ArrayList<>();
            Log.i("TAG","programs() & array length "+jsonArray.length());
            String text = "";
//            HashMap<String,String> programDetail=new HashMap<String, String>();
//            programDetail.put("Type","Program");
//            detail.add();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = (JSONObject) jsonArray.get(i);
//                programDetail=new HashMap<String, String>();


                String info = obj.getString("ProgramName").toUpperCase() +" (ID: "+obj.getString("ProgramID")
                        +")";
//                programDetail.put("Program",info);
                listViewAdapter.add(info);
//                listViewAdapter.notifyDataSetChanged();
                text = text + "\n" + info + "\n====================================\n";
            }

//            listViewAdapter.(detail);
            Log.i("TAG",detail.toString());
            listview.setAdapter(listViewAdapter);
        }

    }
}

