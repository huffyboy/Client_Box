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

     Chronometer chronometer;
     Button startStop;
     long time = 0;
     Boolean start = false;
     Date startDate;
     Date stopDate;
     String startString;
     String stopString;
     SimpleDateFormat dateFormat;



     @Override
     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        //      Intent intent = getIntent();
        //      String message = intent.getStringExtra(MainActivity.TIMER_ACTIVITY);
         chronometer = (Chronometer) findViewById(R.id.chronometer);
         startStop = (Button) findViewById(R.id.btn_toggle_start_stop);
         dateFormat = new SimpleDateFormat("dd/mm/yyyykk:mm", Locale.US);
         startString = stopString = "";
     }

     void onClickManualEntry(){
        // go to manual entry
        time -= 30;
     }

     long getTime(){
        return time;
     }

     /**
      * Stops the timer
      *
      * @param v the view for XML reference
      */
     public void onClickStartStop(View v){
        if (!start) {
            startDate = new Date(); //sets current date
            chronometer.setBase(SystemClock.elapsedRealtime()+time);
            chronometer.start();
            startStop.setText("Stop");
            startString = setDateString(startDate);
        }
        else {
            time = chronometer.getBase()-SystemClock.elapsedRealtime();
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
             //stop the chrono
             chronometer.stop();
             //if the timer is running, then
             if (start){
                 stopDate = new Date();
                 stopString = setDateString(stopDate);
             }
             else if (startString.equals("")){
                 Toast.makeText(TimerActivity.this, "Please start your entry.", Toast.LENGTH_SHORT).show();
             }
             Intent intent = new Intent(this, ManualEntryActivity.class);
             //passing intent - http://stackoverflow.com/questions/19286970/using-intents-to-pass-data-between-activities-in-android
             intent.putExtra("startString", startString);
             intent.putExtra("stopString", stopString);
             startActivity(intent);
         } catch(Exception e) {
             System.out.println(e.getMessage());
         }
     }

     public String setDateString(Date date){
         //get date to string
         //https://www.mkyong.com/java/java-how-to-get-current-date-time-date-and-calender/
        return dateFormat.format(date);
     }

}
