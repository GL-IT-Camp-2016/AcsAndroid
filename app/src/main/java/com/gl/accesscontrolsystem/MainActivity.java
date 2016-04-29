package com.gl.accesscontrolsystem;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.gl.accesscontrolsystem.pojo.AttendanceList;
import com.gl.accesscontrolsystem.pojo.Attendances;
import com.gl.accesscontrolsystem.pojo.PostResult;
import com.gl.accesscontrolsystem.requests.FormBodyPostRequest;
import com.gl.accesscontrolsystem.requests.JsonObjectPostRequest;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {

    FormBodyPostRequest arrdep;
    CheckBox arrivalChkbx;
    CheckBox departureChkbx;
    List<Geofence> geofences;
    PendingIntent geopendintent;
    GoogleApiClient gac;

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
        gac = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                this /* OnConnectionFailedListener */)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .build();
        gac.connect();
        geofences = new ArrayList<>();
        geofences.add(new Geofence.Builder()
                .setRequestId("ABCD")

                .setCircularRegion(
                        49.224771, 18.736787,
                        2
                )
                .setExpirationDuration(1000)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                        Geofence.GEOFENCE_TRANSITION_EXIT)
                .build());
        gac.connect();
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofences);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent() {
        if (geopendintent != null) {
            return geopendintent;
        }
        Intent intent = new Intent(this, MainActivity.class);
        return PendingIntent.getService(this, 0, intent, PendingIntent.
                FLAG_UPDATE_CURRENT);
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
        headers.put("person_name","Zuzana");
        headers.put("timestamp", String.valueOf((Calendar.getInstance().getTimeInMillis()/1000)));
        arrdep = new FormBodyPostRequest(AcsApplication.getInstance().getmUrl()+EndPoints.ATTENDANCE_ARRIVAL,headers,
                new Response.Listener<String>(){

                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(this.getClass().getSimpleName(), "REST error: " + error.getMessage());
            }
        });
        AcsApplication.getInstance().getmQueue().add(arrdep);


    }

    public void onDepartureClick(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.GeofencingApi.addGeofences(
                gac,
                getGeofencingRequest(),
                getGeofencePendingIntent()
        ).setResultCallback(this);


//        Map<String,String> headers = new HashMap<>();
//        headers.put("person_name","Zuzana");
//        headers.put("timestamp", String.valueOf((Calendar.getInstance().getTimeInMillis()/1000)));
//        arrdep = new FormBodyPostRequest(AcsApplication.getInstance().getmUrl()+EndPoints.ATTENDANCE_DEPARTURE,headers,
//                new Response.Listener<String>(){
//
//                    @Override
//                    public void onResponse(String response) {
//                        System.out.println(response);
//                    }
//                },new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(this.getClass().getSimpleName(), "REST error: " + error.getMessage());
//            }
//        });
//        AcsApplication.getInstance().getmQueue().add(arrdep);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        System.out.println("####Connection failed####");
    }

    @Override
    public void onResult(@NonNull Status status) {
        System.out.println("GeoFenceOnResult");
    }
}
