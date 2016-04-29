package com.gl.accesscontrolsystem.pojo;

import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

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
            if(att.is_arrival) {
                cal.setTimeInMillis(att.timestamp);
                System.out.println(cal.getTime());
                if (cal.get(Calendar.YEAR) == c.get(Calendar.YEAR) && cal.get(Calendar.DATE) == c.get(Calendar.DATE) && cal.get(Calendar.MONTH) == c.get(Calendar.MONTH)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean getDepartureForDateSelected(Calendar c){
        Calendar cal = Calendar.getInstance();
        for(Attendance att:result.attendances){
            if(!att.is_arrival) {
                cal.setTimeInMillis(att.timestamp);
                System.out.println(cal.getTime());
                if (cal.get(Calendar.YEAR) == c.get(Calendar.YEAR) && cal.get(Calendar.DATE) == c.get(Calendar.DATE) && cal.get(Calendar.MONTH) == c.get(Calendar.MONTH)) {
                    return true;
                }
            }
        }
        return false;
    }


    public ArrayList<String> getTypeDetails()
    {
        ArrayList<String> types = new ArrayList<>();
        for (Attendance a:result.attendances) {
            if(a.is_arrival)
                types.add("Arrival");
            else
                types.add("Departure");
        }
        return types;
    }

    public ArrayList<String> getTimeDetails()
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat fmtOut = new SimpleDateFormat("dd.MM.yyyy  HH:mm");

        ArrayList<String> times = new ArrayList<>();
        for (Attendance a:result.attendances) {
            cal.setTimeInMillis(a.timestamp*1000);
            times.add(fmtOut.format(cal.getTime()));
        }
        return times;
    }

}

class ALResult{

    ArrayList<Attendance> attendances;


}

class Attendance{

    boolean is_arrival;
    long timestamp;
}