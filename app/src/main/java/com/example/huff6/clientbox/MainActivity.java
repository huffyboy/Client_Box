package com.example.huff6.clientbox;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    //We need a thread singleton object here
    DatabaseHelper myDb;

    public static final String MESSAGE = "com.example.huff6.clientbox.AddClientActivity";
    public static final String MESSAGE2 = "com.example.huff6.clientbox.TimerActivity";
    public static final String MESSAGE3 = "com.example.huff6.clientbox.ManualEntryActivity";
    public static final String MESSAGE4 = "com.example.huff6.clientbox.ClientLookupActivity";

    TextView call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //eye h8 meyecall <3
        // T~T < so mean )
        //View temp = findViewById(R.id.awesome);


        Intent intent = getIntent();
        String message = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);

        call = (TextView) findViewById(R.id.call);
        updateCallInfo();
    }

    public void goToAddClient(View v){
        //go to add client page
        try {
            // move on to the main page
            Intent intent = new Intent(this, AddClientActivity.class);
            intent.putExtra(MESSAGE, "");
            startActivity(intent);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void goToTimer(View v){
        //go to add client page
        try {
            // move on to the main page
            Intent intent = new Intent(this, TimerActivity.class);
            intent.putExtra(MESSAGE2, "");
            startActivity(intent);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void goToManualEntry(View v){
        //go to add client page
        try {
            // move on to the main page
            Intent intent = new Intent(this, ManualEntryActivity.class);
            intent.putExtra(MESSAGE3, "");
            startActivity(intent);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void goToClientLookup(View v){
        //go to add client page
        try {
            // move on to the main page
            Intent intent = new Intent(this, ClientLookupActivity.class);
            intent.putExtra(MESSAGE4, "");
            startActivity(intent);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    void updateCallInfo(){
        StringBuilder sb = new StringBuilder();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("No permissions");
            return;
        }
        Cursor managedCursor = getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null, null, null, null);

        assert managedCursor != null;
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);

        while (managedCursor.moveToNext()) {
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }
            // ADD TO TABLE
            myDb.insertData("UNKNOWN", phNumber, callDayTime.toString(),
                    "SOME TIME", callDuration, dir);
        }
        managedCursor.close();
    }
}