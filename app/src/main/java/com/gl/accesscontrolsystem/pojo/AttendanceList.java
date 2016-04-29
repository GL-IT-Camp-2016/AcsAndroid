package com.gl.accesscontrolsystem.pojo;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Martin Noga on 29. 4. 2016.
 */
public class AttendanceList {

    ALResult result;
    String success;

    public String toString(){
        return "Attendance list: "+success;
    }

    public boolean getArrivalForDateSelected(Calendar c){
        Calendar cal = Calendar.getInstance();
        for(Attendance att:result.attendances){
            cal.setTimeInMillis(att.timestamp);
            System.out.println(cal.getTime());
            if(cal.get(Calendar.YEAR) == c.get(Calendar.YEAR) && cal.get(Calendar.DATE) == c.get(Calendar.DATE) && cal.get(Calendar.MONTH) == c.get(Calendar.MONTH) ){
                return true;
            }
        }
        return false;
    }

}

class ALResult{

    ArrayList<Attendance> attendances;

}

class Attendance{

    boolean is_arrival;
    long timestamp;
}