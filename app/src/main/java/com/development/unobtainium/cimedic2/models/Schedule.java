package com.development.unobtainium.cimedic2.models;

/**
 * Created by unobtainium on 11/10/16.
 */
public class Schedule {
    private Integer id;
    private String start;
    private String end;

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
}
