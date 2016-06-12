 package com.example.huff6.clientbox;

import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

 public class TimerActivity extends AppCompatActivity {

     public static final String EXTRA_MESSAGE = "com.example.huff6.clientbox.ManualEntryActivity";
    Chronometer chronometer;
     Button startStop;
    long time = 0;
    Boolean start = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.MESSAGE2);

        chronometer = (Chronometer) findViewById(R.id.chronometer);
        startStop = (Button) findViewById(R.id.btn_toggle_start_stop);

    }

    void onClickManualEntry(){
        time -= 30;//go to manual entry
    }
    long getTime(){
        return time;
    }

    public void onClickStartStop(View v){
        if (!start) {
            chronometer.setBase(SystemClock.elapsedRealtime()+time);
            chronometer.start();
            startStop.setText("Stop");
        }
        else {
            time = chronometer.getBase()-SystemClock.elapsedRealtime();
            chronometer.stop();
            startStop.setText("Start");
        }
        start = !start;
    }

     public void onClickReset(View v){
         chronometer.setBase(SystemClock.elapsedRealtime());
     }

     public void goToManualEntry(View v){
         try {
             // move on to the main page
             Intent intent = new Intent(this, ManualEntryActivity.class);
             intent.putExtra(EXTRA_MESSAGE, "");
             startActivity(intent);
         }
         catch(Exception e){
             System.out.println(e.getMessage());
         }
     }

}
