package com.example.huff6.clientbox;

/**
 * The log object for all data stored for specific calls or client meetings
 */
public class Log {
    private String startTime;
    private String stopTime;
    private long duration;
    private String notes;

    public Log () {
        startTime = "";
        stopTime  = "";
        duration  = 0;
        notes     = "";
    }

    /**
     * Sett all times in the log
     *
     * @param sTime the start time
     * @param eTime the end time
     * @param dur the duration in seconds
     * @param inputNotes the notes for the log
     */
    public void setLog(String sTime, String eTime, long dur, String inputNotes) {
        startTime = sTime;
        stopTime = eTime;
        duration = dur;
        notes = inputNotes;
    }

    public String getStartTime() {
        return startTime;
    }

    /**
     * Stop time getter
     *
     * @return the stop time
     */
    public String getStopTime() {
        return stopTime;
    }

    /**
     * Duration getter
     *
     * @return the duration
     */
    public long getDuration() {
        return duration;
    }

    /**
     * Notes getter
     *
     * @return the notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Start time setter
     *
     * @param input the start time
     */
    public void setStartTime(String input) {
        startTime = input;
    }

    /**
     * Stop time setter
     *
     * @param input the stop time
     */
    public void setStopTime(String input) {
        stopTime = input;
    }

    /**
     * Duration setter
     *
     * @param input the duration
     */
    public void setDuration(int input) {
        duration = input;
    }

    /**
     * Notes setter
     *
     * @param input the notes
     */
    public void setNotes(String input) {
        notes = input;
    }

}
