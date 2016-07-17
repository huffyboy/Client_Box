 package com.example.huff6.clientbox;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

 public class TimerActivity extends AppCompatActivity {

     private Chronometer chronometer;
     private Button startStop;
     private long time;
     private Boolean start;
     private Date startDate;
     private Date stopDate;
     private String startString;
     private String stopString;
     private SimpleDateFormat dateFormat;


     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_timer);
         chronometer = (Chronometer) findViewById(R.id.chronometer);
         startStop   = (Button)      findViewById(R.id.btn_toggle_start_stop);
         dateFormat = new SimpleDateFormat("MM/dd/yyyy kk:mm:ss z", Locale.US);
         startString = stopString = "";
         start = false;
         time = 0;
     }

     /**
      * get the time
      *
      * @return timeValue
      */
     public long getTime(){
        return time;
     }

     /**
      * Stops the timer
      *
      * @param v the view for XML reference
      */
     public void onClickStartStop(View v){
        if (!start) {
            startDate = new Date();
            chronometer.setBase(SystemClock.elapsedRealtime() + time);
            chronometer.start();
            startStop.setText("Stop");
            startString = setDateString(startDate);
        } else {
            time = chronometer.getBase() - SystemClock.elapsedRealtime();
            chronometer.stop();
            startStop.setText("Start");
            stopDate = new Date();
            stopString = setDateString(stopDate);
        }
        start = !start;
    }

     /**
      * Reset the timer
      *
      * @param v the view to allow XML reference
      */
     public void onClickReset(View v){
         chronometer.setBase(SystemClock.elapsedRealtime());
         time = 0;
     }

     /**
      * Forwards to the ManualActivity
      *
      * @param v the view to allow XML reference
      */
     public void goToManualEntry(View v){
         try {
             chronometer.stop();
             //if the timer is running, then
             if (start){
                 stopDate = new Date();
                 stopString = setDateString(stopDate);
             } else if (startString.equals("")){
                 Toast.makeText(TimerActivity.this, "Please start your entry.", Toast.LENGTH_SHORT).show();
             }
             Intent intent = new Intent(this, ManualEntryActivity.class);
             intent.putExtra("startString", startString);
             intent.putExtra("stopString", stopString);
             startActivity(intent);
         } catch(Exception e) {
             System.out.println(e.getMessage());
         }
     }

     /**
      * turns the date into a string format
      *
      * @param date input
      * @return the date in string formate
      */
     public String setDateString(Date date){
         //https://www.mkyong.com/java/java-how-to-get-current-date-time-date-and-calender/
        return dateFormat.format(date);
     }
}
