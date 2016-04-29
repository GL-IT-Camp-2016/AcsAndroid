package com.gl.accesscontrolsystem;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.gl.accesscontrolsystem.pojo.Attendances;
import com.gl.accesscontrolsystem.pojo.PersonList;

import java.util.ArrayList;

public class PersonListActivity extends AppCompatActivity {

    private PersonList personList;
    private ListView lvPersons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_list);

        lvPersons = (ListView) findViewById(R.id.listView);
        lvPersons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                String personName = (String) arg0.getItemAtPosition(arg2);

                Intent myIntent = new Intent(PersonListActivity.this, PersonDetailActivity.class);
                myIntent.putExtra("name", personName);
                PersonListActivity.this.startActivity(myIntent);

            }
        });

        getPersons();

    }

    public void getPersons() {

        AcsApplication.getInstance().simpleRequest(EndPoints.PERSON_LIST, Request.Method.GET, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                String users = (String) response;
                personList = AcsApplication.getInstance().getmGson().fromJson(users, PersonList.class);

                ArrayList<String> listItems = new ArrayList<String>();

                ArrayAdapter adapter = new ArrayAdapter<String>(PersonListActivity.this,
                        android.R.layout.simple_list_item_1,
                        personList.getPersonNames());
                lvPersons.setAdapter(adapter);

            }
        });
    }


    //public void onItemClick(View view) {}
}
