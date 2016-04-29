package com.gl.accesscontrolsystem.pojo;

import java.util.ArrayList;

/**
 * Created by Patrik Obertik on 29. 4. 2016.
 */
public class Attendances {

    Result result;
    boolean success;



    public class Result {
        ArrayList<Attendance> attendances;

        public class Attendance {

            boolean is_arrival;
            long timestamp;
        }
    }
}

