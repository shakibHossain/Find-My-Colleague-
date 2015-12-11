package com.sbsatter.findmycolleague;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user pc on 12/4/2015.
 */
public class EmployeeInformation extends Fragment {
    private ArrayAdapter<String> mForecastAdapter;
    static ArrayList<String> detailActivityList= new ArrayList<>(7);

    ArrayList<HashMap<String,String>> list= new ArrayList<>();
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
        list=DrawerActivity.detail;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ArrayList<String> names= new ArrayList<>();
        if(list.get(0).get("Type").toString().equals("Employee")){
            for(int i=1; i<list.size(); i++){
                names.add(list.get(i).get("Name").toString());
            }
        }else {
            for(int i=1; i<list.size(); i++) {
                names.add(list.get(i).get("Program").toString());
            }
        }
        mForecastAdapter = new ArrayAdapter<String>(
                getActivity(), // The current context (this activity)
                R.layout.list_item_info, // The name of the layout ID.
                R.id.list_item_forecast_textview, // The ID of the textview to populate.
                //        weekForecast);
                //     new ArrayList<String>());
                names);


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        final ListView listView = (ListView) rootView.findViewById(R.id.listview_info);
        listView.setAdapter(mForecastAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //    String forecast = mForecastAdapter.getItem(position);
                Toast.makeText(getActivity(), "position " + position, Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(getActivity(), DetailActivity.class).putExtra(Intent.EXTRA_TEXT, forecast);

                Intent intent = new Intent(getActivity(), DetailActivity.class);

                intent.putExtra("mobileEt",list.get(position+1).get("Mobile").toString());
                intent.putExtra("name",list.get(position+1).get("Name").toString());
                intent.putExtra("designation",list.get(position+1).get("Designation").toString());
                intent.putExtra("sex",list.get(position+1).get("Sex").toString());
                startActivity(intent);
            }
        });

        return rootView;
    }

}
