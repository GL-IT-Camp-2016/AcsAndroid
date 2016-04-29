package com.gl.accesscontrolsystem;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.gl.accesscontrolsystem.pojo.AttendanceList;
import com.gl.accesscontrolsystem.pojo.Attendances;

import java.util.ArrayList;
import java.util.HashMap;

public class PersonDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_detail);

        Intent intent = getIntent();
        String personName = intent.getStringExtra("name");

        System.out.println("personname: "+personName);
        getPersonTimes(personName);

    }


    public void  getPersonTimes(final String name){

        AcsApplication.getInstance().simpleRequest(EndPoints.ATTENDANCE_LIST + "?person_name=" + name, Request.Method.GET, new Response.Listener() {
            @Override
            public void onResponse(Object response) {

                AttendanceList attendanceList = AcsApplication.getInstance().getmGson().fromJson((String) response, AttendanceList.class);

                ListView lvDetail = (ListView) findViewById(R.id.lvDetail);

                ArrayList<String> isArrivalList = attendanceList.getTypeDetails();
                ArrayList<String> timeList = attendanceList.getTimeDetails();

                ArrayList<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<isArrivalList.size(); i++){
                    map.put("Type", isArrivalList.get(i).toString());
                    map.put("Time", timeList.get(i).toString());
                    mylist.add(map);
                    System.out.println(isArrivalList.get(i).toString()+"//"+timeList.get(i).toString());
                }

                TextView textView = new TextView(getApplicationContext());
                textView.setText(name);
                textView.setTextSize(30);
                textView.setTextColor(Color.BLACK);
                textView.setPadding(0,0,0,10);
                lvDetail.addHeaderView(textView);

                SimpleAdapter mSchedule = new SimpleAdapter(PersonDetailActivity.this, mylist, R.layout.detail_item_layout,
                        new String[] {"Type", "Time"}, new int[] {R.id.TYPE_CELL, R.id.TIME_CELL});
                lvDetail.setAdapter(mSchedule);

            }
        });
    }
}
