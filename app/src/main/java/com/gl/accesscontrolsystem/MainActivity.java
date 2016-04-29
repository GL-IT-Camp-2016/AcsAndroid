package com.gl.accesscontrolsystem;

import android.app.DatePickerDialog;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.gl.accesscontrolsystem.pojo.AttendanceList;
import com.gl.accesscontrolsystem.pojo.Attendances;
import com.gl.accesscontrolsystem.pojo.PostResult;
import com.gl.accesscontrolsystem.requests.JsonObjectPostRequest;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.JsonObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    JsonObjectPostRequest<PostResult> attendances;
    CheckBox arrivalChkbx;
    CheckBox departureChkbx;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        arrivalChkbx = (CheckBox) findViewById(R.id.arrivalCheckBox);
        departureChkbx = (CheckBox) findViewById(R.id.departureCheckBox);

//        AcsApplication.getInstance().simpleRequest(EndPoints.PERSON_LIST, Request.Method.GET,new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                System.out.println(response);
//            }
//        } );


    }


    public void onDateClick(View view) {
        TextView dateTextView = (TextView) findViewById(R.id.dateTextView);

        Calendar mcurrentDate = Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth = mcurrentDate.get(Calendar.MONTH);
        int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(final DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                System.out.println("On date set main activity");

                Map<String, String> headers = new HashMap<>();
                headers.put("person_name", "aaa");
                final Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR,selectedyear);
                cal.set(Calendar.MONTH,selectedmonth);
                cal.set(Calendar.DATE,selectedday);
                AcsApplication.getInstance().simpleRequest(EndPoints.ATTENDANCE_LIST + "?person_name=" + "aaa", Request.Method.GET, new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {

                        AttendanceList attendances = AcsApplication.getmGson().fromJson((String) response, AttendanceList.class);
                        arrivalChkbx.setChecked(attendances.getArrivalForDateSelected(cal));
                        departureChkbx.setChecked(attendances.getDepartureForDateSelected(cal));
                    }
                });
            }
        }, mYear, mMonth, mDay);
        mDatePicker.setTitle("Select date");
        mDatePicker.show();
    }


    public void onArrivalClick(View view) {
            Map<String,String> headers = new HashMap<>();
            headers.put("person_name","aaa");
            headers.put("timestamp", String.valueOf((Calendar.getInstance().getTimeInMillis()/1000)));
           attendances = new JsonObjectPostRequest<>(AcsApplication.getInstance().getmUrl()+EndPoints.ATTENDANCE_ARRIVAL, PostResult.class,headers,
                   new Response.Listener<PostResult>(){

                       @Override
                       public void onResponse(PostResult response) {
                           System.out.println(response.isResult());
                       }
                   },new Response.ErrorListener() {
               @Override
               public void onErrorResponse(VolleyError error) {
                   Log.e(this.getClass().getSimpleName(), "REST error: " + error.getMessage());
               }
           });
        AcsApplication.getInstance().getmQueue().add(attendances);
    }
}
