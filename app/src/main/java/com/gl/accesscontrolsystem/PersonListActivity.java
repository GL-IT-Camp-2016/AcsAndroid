package com.gl.accesscontrolsystem;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.gl.accesscontrolsystem.pojo.PersonList;

import java.util.ArrayList;

public class PersonListActivity extends AppCompatActivity {

    private PersonList personList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_list);


        AcsApplication.getInstance().simpleRequest(EndPoints.PERSON_LIST, Request.Method.GET, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response);
            }
        });

        getPersons();

        // adapter = new ArrayAdapter<String>(this,R.layout.activity_person_list,personList);
        ArrayList<String> listItems = new ArrayList<String>();

        for (Object object : personList) {
            // do something with object
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        ListView lvPersons = (ListView) findViewById(R.id.listView);
        lvPersons.setAdapter(adapter);
        //lvPersons.setAdapter(adapter);
    }

    public void getPersons() {

        AcsApplication.getInstance().simpleRequest(EndPoints.PERSON_LIST, Request.Method.GET, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                String users = (String) response;
                personList = AcsApplication.getInstance().getmGson().fromJson(users, PersonList.class);

            }
        });
    }
}
