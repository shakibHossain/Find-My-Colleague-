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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class EmployeeInformation extends Fragment {
    private ArrayAdapter<String> listviewAdapter;
    static ArrayList<String> names,detailActivityList= new ArrayList<>(7);
    ArrayList<String> detail= new ArrayList<>();
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
        if(DrawerActivity.searchedNames!=null){
            names=DrawerActivity.searchedNames;
        }

        listviewAdapter = new ArrayAdapter<String>(
                getActivity(), // The current context (this activity)
                R.layout.list_item_info, // The name of the layout ID.
                R.id.list_item_textview, // The ID of the textview to populate.
                //        weekForecast);
                //     new ArrayList<String>());
                names);


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get a reference to the ListView, and attach this adapter to it.
        final ListView listView = (ListView) rootView.findViewById(R.id.listview_info);
        listView.setAdapter(listviewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //    String forecast = listviewAdapter.getItem(position);
                Toast.makeText(getActivity(), "position " + position, Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(getActivity(), DetailActivity.class).putExtra(Intent.EXTRA_TEXT, forecast);
                JSONObject object = new JSONObject();
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                try {
                    object = DrawerActivity.response.getJSONObject(position);

                    intent.putExtra("mobile", object.getString("mobile"));
                    intent.putExtra("Name", object.getString("Name"));
                    intent.putExtra("status", object.getString("status"));
                    String coords= object.getDouble("lat")+", "+object.getDouble("lon");
                    intent.putExtra("coordinates", coords);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return rootView;
    }

}
