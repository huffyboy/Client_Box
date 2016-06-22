package com.example.huff6.clientbox;



/**
 * Created by joshu on 6/6/2016
 */
public class Log {
    private String startTime;
    private String stopTime;
    private int duration;
    private String notes;

    Log () {
        startTime = "";
        stopTime  = "";
        duration  = 0;
        notes     = "";
    }

    public String getStartTime() {
        return startTime;
    }

    public String getStopTime() {
        return stopTime;
    }

    public int getDuration() {
        return duration;
    }

    public String getNotes() {
        return notes;
    }

    public void setStartTime(String input) {
        startTime = input;
    }

    public void setStopTime(String input) {
        stopTime = input;
    }

    public void setDuration(int input) {
        duration = input;
    }

    public void setNotes(String input) {
        notes = input;
    }

}
