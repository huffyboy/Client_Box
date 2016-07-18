package com.example.huff6.clientbox;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Main menu activity
 */
public class MainActivity extends AppCompatActivity {

    public static final String MAIN_ACTIVITY          = "com.example.huff6.clientbox.MainActivity";
    public static final String ADD_CLIENT_ACTIVITY    = "com.example.huff6.clientbox.AddClientActivity";
    public static final String TIMER_ACTIVITY         = "com.example.huff6.clientbox.TimerActivity";
    public static final String MANUAL_ENTRY_ACTIVITY  = "com.example.huff6.clientbox.ManualEntryActivity";
    public static final String CLIENT_LOOKUP_ACTIVITY = "com.example.huff6.clientbox.ClientLookupActivity";
    public static final String preferences = "MyPrefsFile";

    protected ClientBoxApplication app;
    private String callNotes;
    private List<Client> clientList;
    private ArrayList<com.example.huff6.clientbox.Log> phoneLog;
    private Button syncr;
    SimpleDateFormat timeStamp;
    SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app = (ClientBoxApplication)getApplication();
        timeStamp = new SimpleDateFormat("yyyy/MM/dd 'at' HH:mm:ss z", Locale.US);
        clientList = new ArrayList<>();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy kk:mm:ss z", Locale.US);

        syncr = (Button) findViewById(R.id.button4);
        assert syncr != null;
        syncr.setVisibility(View.INVISIBLE);

        new startupSync().execute();
    }

    /**
     * Go to the add client page
     *
     * @param v the view for XML reference
     */
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

    /**
     * Go to Timer activity
     *
     * @param v the view for XML reference
     */
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

    /**
     * Go to Manual Entry Activity
     *
     * @param v the view for the XML reference
     */
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

    /**
     * Go to Client Lookup Activity
     *
     * @param v the view for XML reference
     */
    public void goToClientLookup(View v) {
        try {
            Intent intent = new Intent(this, ClientHistoryActivity.class);
            intent.putExtra(MainActivity.CLIENT_LOOKUP_ACTIVITY, "");
            startActivity(intent);
            Log.i("MainActivity", "Client Lookup Button Pressed");
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Gets the phone logs from the phone and stores the good ones in
     * the phoneLog list
     *
     * @throws ParseException
     */
    void extractPhoneLogs() throws ParseException {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
                != PackageManager.PERMISSION_GRANTED) {
            Log.w("MainActivity","No permissions");
            Toast.makeText(MainActivity.this, "Need permissions for reading your call history",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        Cursor managedCursor = getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null, null, null, null);

        assert managedCursor != null;
        int number   = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type     = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date     = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        phoneLog = new ArrayList<>();
        com.example.huff6.clientbox.Log tempLog = new com.example.huff6.clientbox.Log();
        Date checkDate = timeStamp.parse(getLastSyncTime());

        // extract information out of the phone logs stored in the phone
        while (managedCursor.moveToNext()) {
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));

            // store phone number in call notes temporarily
            callNotes = phNumber.substring(2);
            String callDuration = managedCursor.getString(duration);

            // decipher what kind of call it was
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

            // only add calls that were after the last synced time and calls that weren't missed
            //      date comparison copied from
            //      https://www.mkyong.com/java/how-to-compare-dates-in-java/
            if(checkDate.compareTo(callDayTime) < 0) {
                if (!dir.equals("MISSED")) {
                    long l = callDayTime.getTime() + Integer.parseInt(callDuration) * 1000;
                    Date d = new Date(l);
                    tempLog.setLog(
                            dateFormat.format(callDayTime),
                            dateFormat.format(d),
                            Long.parseLong(callDuration),
                            callNotes);
                    phoneLog.add(tempLog);
                }
            }
        }
        managedCursor.close();
    }

    /**
     * Syncronize button function to push logs to the database
     *
     * @param v the view to allow XML access
     * @throws ParseException
     */
    public void synchronize(View v) throws ParseException {
        phoneLog.clear();
        syncr.setVisibility(View.INVISIBLE);
        new sync().execute();
    }

    /**
     * Pushes the client logs from phoneLog to the database
     *
     * @return a string containing a list of all calls pushed to the database
     */
    private String pushLogsToDatabase() {
        String numbers = "";
        List<Integer> temp = new ArrayList<>();

        for (int i = 0; i < phoneLog.size(); i++){
            int index = findNumberInDatabase(phoneLog.get(i).getNotes());
            // add the the database if the phone number is a client
            if (index > -1) {
                com.example.huff6.clientbox.Log tempLog = phoneLog.get(i);
                temp.add(index);
                String phoneNumber = tempLog.getNotes();
                tempLog.setNotes("phone call");
                app.clientRef.child(phoneNumber).child("Logs").push().setValue(tempLog);
            }
        }
        // create the string of logs that were pushed ot the database
        for (int i = 0; i < temp.size(); i++){
            numbers += clientList.get(temp.get(i)).getName() + " " +
                    clientList.get(temp.get(i)).getNum() + "\n";
        }
        return numbers;
    }

    /**
     * Checks if the phone number is a client in the database
     *
     * @param number the phone number you want to lookup in the database
     * @return the position in the database, or -1 if not found
     */
    private int findNumberInDatabase(String number) {
        for (int i = 0; i < clientList.size(); i++) {
            if (clientList.get(i).getNum().equals(number)) {
                System.out.println("FOUND");
                return i;
            }
        }
        return -1;
    }

    /**
     * Create a popup message to the screen
     * copied from tutorial given by
     *      https://www.youtube.com/channel/UCs6nmQViDpUw0nuIx9c_WvA
     *
     * @param title the title of the popup
     * @param Message the popup text
     */
    private void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    /**
     * Set the last synced time
     *
     * @param date a date in string format
     */
    private void setLastSyncTime(String date) {
        //http://stackoverflow.com/questions/23024831/android-shared-preferences-example
        SharedPreferences.Editor editor = getSharedPreferences(preferences, MODE_PRIVATE).edit();
        editor.putString("name", date);
        editor.apply();
    }

    /**
     * Get the last synced time
     *
     * @return a date in string format of last time phone synced to the database
     */
    private String getLastSyncTime() {
        SharedPreferences prefs = getSharedPreferences(preferences, MODE_PRIVATE);
        String lastSyncTime = prefs.getString("name", null);
        if (lastSyncTime != null) {
            return lastSyncTime;
        } else {
            return "none";
        }
    }

    /**
     * Read the clients from the database
     */
    public void readFromDatabase() {
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                    // A new client has been added, add it to the displayed list
                    Client theClient = dataSnapshot.getValue(Client.class);
                    theClient.setNum(dataSnapshot.getKey());
                    clientList.add(theClient);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        app.clientRef.addChildEventListener(childEventListener);
    }

    /**
     * Background startup automatic syncing
     */
    class startupSync extends AsyncTask<Void,String,String> {

        @Override
        protected void onPreExecute() {
            callNotes = "no new updates";
        }

        @Override
        protected String doInBackground(Void... params) {
            readFromDatabase();
            try {
                extractPhoneLogs();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            pushLogsToDatabase();
            publishProgress("");
            return "updated";
        }

        @Override
        protected void onProgressUpdate(String... values) {
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            syncr.setVisibility(View.VISIBLE);
            String currentDateandTime = timeStamp.format(new Date());
            setLastSyncTime(currentDateandTime);
        }
    }

    /**
     * Manual background syncing
     */
    class sync extends startupSync {
        @Override
        protected String doInBackground(Void... params) {
            try {
                extractPhoneLogs();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            publishProgress("");

            return "syncing";
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            if (phoneLog.size() == 0) {
                showMessage("last synced\n" + getLastSyncTime(), "no new client call logs");
            } else {
                showMessage("last synced\n" + getLastSyncTime(), pushLogsToDatabase());
            }

            // save the time we last synced to only store new logs in the database
            String currentDateandTime = timeStamp.format(new Date());
            setLastSyncTime(currentDateandTime);

            syncr.setVisibility(View.VISIBLE);
        }
    }
}