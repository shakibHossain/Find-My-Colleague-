package com.sbsatter.findmycolleague;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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

/**
 * Created by ASUS on 12/5/2015.
 */
public class SOAPQueryClass {


    private Context context;
    private static final int StaffInfoByName = 0;
    private static final int StaffInfoByPIN = 01;
    private static final int GetAllPrograms = 02;
    protected boolean resultValue=false;
    protected String nameToBeValidated="";
    //    private static final int StaffInfoByName=0;
//    private static final int StaffInfoByName=0;
    private int MODE;
    private SoapSerializationEnvelope soapEnvelop;
    HashMap<String, String> params, requestProperty;

    public SOAPQueryClass(int mode, HashMap<String, String> property, Context context) {
        MODE = mode;
        params = generateSOAPParameters();
        requestProperty = property;
        this.context=context;
    }

    public SOAPQueryClass(Context context){

        this.context=context;
    }

    public SOAPQueryClass(int mode, Context context) {
        MODE = mode;
        params = generateSOAPParameters();
        this.context=context;
    }

    public boolean validateNameAndID(String fullName, String ID){
        nameToBeValidated=fullName;
        HashMap<String, String> property= new HashMap<>();
        property.put("strStaffPIN",ID);
        if(requestProperty==null){
            requestProperty=property;
        }
        MODE=SOAPQueryClass.StaffInfoByPIN;
        params=generateSOAPParameters();
        soapRequest();
        return resultValue;
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
        resultValue=false;
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
            default:
                throw new NullPointerException("MODE NOT SET PROPERLY");
        }


    }

    class ExecuteHTCall extends AsyncTask<HttpTransportSE, Void, String> {
        ProgressDialog progressDialog;
        private ArrayList detail;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog= new ProgressDialog(context);
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(HttpTransportSE... httpTransportSE) {
            Exception ex;
            try {
                Log.i("TAG","doInBackground");
                httpTransportSE[0].call(params.get("SOAP_ACTION"), soapEnvelop);
                return "" + (SoapPrimitive) soapEnvelop.getResponse();
            } catch (IOException e) {
                ex=e;
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                ex=e;
                e.printStackTrace();
            }
            Log.i("TAG","doInBackground ends with exception: "+ex.getMessage());
            return "ERROR";
        }

        @Override
        protected void onPostExecute(String s) {

//                Toast.makeText(DrawerActivity.this, s, Toast.LENGTH_SHORT).show();
            if(s.equals("ERROR")){
                Log.i("TAG","ERROR on Post Execute");
                progressDialog.dismiss();
                return;
            }
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
//                makeFragmentChanges(viewToChange);
            progressDialog.dismiss();
        }
        private void nameOrId(String s) throws JSONException {
            JSONArray jsonArray = new JSONArray(s);
            Log.i("TAG","nameOrId() & array length "+jsonArray.length());
            String text = "";
            HashMap<String,String> empInfo= new HashMap<>();
            empInfo.put("Type","Employee");
            detail.add(empInfo);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = (JSONObject) jsonArray.get(i);
                String mname, mnameSpace = "";
                String name=obj.getString("Fname") + " " + (mname = obj.getString
                        ("Mname")) +
                        (mnameSpace = (!mname.equals("")) ? " " : "") + obj
                        .getString("Lname");
                Log.i("TAG",name+" as opposed to input: "+ nameToBeValidated);
                empInfo=new HashMap<String, String>();
                empInfo.put("Name",name);
                empInfo.put("Mobile",((mnameSpace=obj.getString("Mobile")).equals(""))?"N/A":
                        mnameSpace);
                empInfo.put("Designation", obj.getString("Designation"));
                detail.add(empInfo);
            }

            if(MODE==SOAPQueryClass.StaffInfoByPIN && jsonArray.length()!=1){
                resultValue=false;
                Log.i("TAG","result didnt match");
            }else if(MODE==StaffInfoByPIN){
                HashMap<String,String> map= (HashMap<String, String>) detail.get(1);
                if((nameToBeValidated.toLowerCase()).equals(map.get("Name").toString().toLowerCase())){
                    resultValue=true;
                    Log.i("TAG","result match!!!");
                }
            }
//                setTextToTextview(text);
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


    }


}
