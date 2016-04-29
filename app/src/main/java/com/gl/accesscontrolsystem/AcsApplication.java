package com.gl.accesscontrolsystem;

import android.app.Application;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Patrik Obertik on 28. 4. 2016.
 */
public class AcsApplication extends Application {

    private static AcsApplication sInstance;
    private RequestQueue mQueue;
    private static Gson mGson;
    private String mUrl = "http://itc16.herokuapp.com/";

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        mQueue = Volley.newRequestQueue(this);

        GsonBuilder gsonBuilder = new GsonBuilder();
        mGson = gsonBuilder.create();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mQueue != null) {
            mQueue.cancelAll(this);
        }
    }

    public static AcsApplication getInstance() {
        return sInstance;
    }

    public RequestQueue getmQueue() {
        return mQueue;
    }

    public void setmQueue(RequestQueue mQueue) {
        this.mQueue = mQueue;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    /*!--
     *   Simple Rest client
     *   http://developer.android.com/training/volley/index.html
     *
     *   REST:
     *   http://itc16.herokuapp.com/
     */

    public void simpleRequest(String endPoint, int method, Response.Listener response) {
        // GET  Request.Method.GET
        // POST Request.Method.POST

        StringRequest stringRequest = new StringRequest(method, mUrl + endPoint,
                response,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(this.getClass().getSimpleName(), "REST error: " + error.getMessage());
                    }
                });

        mQueue.add(stringRequest);
    }

/*
    GET person
    list:

    http://itc16.herokuapp.com/person/list/

    result:

    {
        "result":{
        "persons":[
        {
            "name":"aaa"
        }
        ]
    },
        "success":true
    }


    GET attendance list:
    http://itc16.herokuapp.com/attendance/list/?person_name=aaa

    {
        "result":{
        "attendances":[
        {
            "is_arrival":true,
                "timestamp":2165456
        },
        {
            "is_arrival":false,
                "timestamp":2165499
        }
        ]
    },
        "success":true
    }

    POST arrival
    http://itc16.herokuapp.com/attendance/store/arrival/

    form params    :
    person_name
    timestamp

    result:

    {
        "success":true
    }

    DEPARTURE:
    POST http://itc16.herokuapp.com/attendance/store/departure/

    form params    :
    person_name
    timestamp
    result

    {
        "success":true
    }
*/

}
