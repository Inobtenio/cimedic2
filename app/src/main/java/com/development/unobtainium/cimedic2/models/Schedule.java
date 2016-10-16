package com.development.unobtainium.cimedic2.models;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by unobtainium on 11/10/16.
 */
public class Schedule implements Comparator<Schedule>{
    private Integer id;
    private String start;
    private String end;
    public Boolean taken;

    public String hashKey(){
        Date date = new Date(Integer.valueOf(this.getStart())*1000L); // *1000 is to convert seconds to milliseconds
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        calendar.setTime(date);
        return "day" + String.valueOf(calendar.get(Calendar.DAY_OF_WEEK));
    }

    public String hoursString(){
        Date start = new Date(Integer.valueOf(this.getStart())*1000L);
        Date end = new Date(Integer.valueOf(this.getEnd())*1000L);
        Calendar startCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        Calendar endCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT-5"));
        endCalendar.setTime(end);
        startCalendar.setTime(start);
        return String.valueOf(startCalendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.valueOf(startCalendar.get(Calendar.MINUTE)) + " - " + String.valueOf(endCalendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.valueOf(endCalendar.get(Calendar.MINUTE));
    }

    public String fullHoursString(){
        Date date = new Date(Integer.valueOf(this.getStart())*1000L);
        String myFormat = "EEE, dd MMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        return sdf.format(date) + "  " + hoursString();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    @Override
    public int compare(Schedule lhs, Schedule rhs) {
        return Integer.getInteger(lhs.getStart()) - Integer.getInteger(rhs.getStart());
    }
}
