package com.example.huff6.clientbox;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.util.Log;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    //We need a thread singleton object here
    DatabaseHelper myDb;

    public static final String MAIN_ACTIVITY          = "com.example.huff6.clientbox.MainActivity";
    public static final String ADD_CLIENT_ACTIVITY    = "com.example.huff6.clientbox.AddClientActivity";
    public static final String TIMER_ACTIVITY         = "com.example.huff6.clientbox.TimerActivity";
    public static final String MANUAL_ENTRY_ACTIVITY  = "com.example.huff6.clientbox.ManualEntryActivity";
    public static final String CLIENT_LOOKUP_ACTIVITY = "com.example.huff6.clientbox.ClientLookupActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDb = new DatabaseHelper(this);
        updateCallInfo();
    }

    // go to add client page
    public void goToAddClient(View v) {
        try {
            Intent intent = new Intent(this, AddClientActivity.class);
            intent.putExtra(ADD_CLIENT_ACTIVITY, "");
            startActivity(intent);
            Log.e("MainActivity", "Cannot go to add client screen");
        } catch(Exception e) {
            Log.e("MainActivity", "Cannot go to add client screen");
        }
    }

    //go to add client page
    public void goToTimer(View v) {
        try {
            Intent intent = new Intent(this, TimerActivity.class);
            intent.putExtra(TIMER_ACTIVITY, "");
            startActivity(intent);
            Log.i("MainActivity", "Timer Activity Button Pressed");
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // go to add client page
    public void goToManualEntry(View v) {
        try {
            Intent intent = new Intent(this, ManualEntryActivity.class);
            intent.putExtra(MANUAL_ENTRY_ACTIVITY, "");
            startActivity(intent);
            Log.i("MainActivity", "Manual Entry Button Pressed");
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //go to add client page
    public void goToClientLookup(View v) {
        try {
            Intent intent = new Intent(this, ClientLookupActivity.class);
            intent.putExtra(MainActivity.CLIENT_LOOKUP_ACTIVITY, "");
            startActivity(intent);
            Log.i("MainActivity", "Client Lookup Button Pressed");
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    void updateCallInfo(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            Log.w("MainActivity","No permissions");
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
            String dir = "";
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
            if (!dir.equals("MISSED")) {
                myDb.insertData("UNKNOWN", phNumber, callDayTime.toString(),
                        "SOME TIME", callDuration, dir);
            }
        }
        managedCursor.close();
    }
}