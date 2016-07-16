package com.example.huff6.clientbox;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
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
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    //We need a thread singleton object here
    //DatabaseHelper myDb;

    public static final String MAIN_ACTIVITY          = "com.example.huff6.clientbox.MainActivity";
    public static final String ADD_CLIENT_ACTIVITY    = "com.example.huff6.clientbox.AddClientActivity";
    public static final String TIMER_ACTIVITY         = "com.example.huff6.clientbox.TimerActivity";
    public static final String MANUAL_ENTRY_ACTIVITY  = "com.example.huff6.clientbox.ManualEntryActivity";
    public static final String CLIENT_LOOKUP_ACTIVITY = "com.example.huff6.clientbox.ClientLookupActivity";
    public static final String preferences = "MyPrefsFile";
    protected ClientBoxApplication app;
    String check;
    long numClients;
    List<Client> clientList;
    ArrayList<com.example.huff6.clientbox.Log> phoneLog;

    Button syncr;

    boolean stillReading;

    SimpleDateFormat timeStamp = new SimpleDateFormat("yyyy/MM/dd 'at' HH:mm:ss z");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //myDb = new DatabaseHelper(this);
        clientList = new ArrayList<>();

        syncr = (Button) findViewById(R.id.button4);
        syncr.setVisibility(View.INVISIBLE);

        stillReading = false;

        //new thread

//        Looper.prepare();
        new loadTask().execute();

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

    void updateCallInfo() throws ParseException {
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
        phoneLog = new ArrayList<com.example.huff6.clientbox.Log>();
        com.example.huff6.clientbox.Log tempLog = new com.example.huff6.clientbox.Log();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy kk:mm:ss z", Locale.US);

        Date checkDate = timeStamp.parse(getPreferences());
        while (managedCursor.moveToNext()) {
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            check = phNumber.substring(2);

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

            //phoneLog.add(check);
            //https://www.mkyong.com/java/how-to-compare-dates-in-java/
            if(checkDate.compareTo(callDayTime)<0) {
                System.out.println("CALL AFTER TIME");
                // ADD TO TABLE
                if (!dir.equals("MISSED")) {

                    check = phNumber.substring(2);

                    long l = callDayTime.getTime() + Integer.parseInt(callDuration)*1000;
                    Date d = new Date(l);
                    System.out.println();
                    tempLog.setLog(
                            sdf.format(callDayTime),
                            sdf.format(d),
                            Long.parseLong(callDuration),
                            check);
                    phoneLog.add(tempLog);
                }
            }
        }
        managedCursor.close();
    }

    public void synchronize(View v) throws ParseException {

            phoneLog.clear();
            syncr.setVisibility(View.INVISIBLE);
            // call update function
            //updateCallInfo();

            //new thread
            new sync().execute();
            stillReading = false;
            //set the preferences here

            String currentDateandTime = timeStamp.format(new Date());
            //setPreferences(currentDateandTime);

            //timeStamp.parse(getPreferences()).toString()
    }

    private String getNumbers()
    {
        //clientList = new ArrayList<>();
        String numbers = "";
        List<Integer> temp = new ArrayList<>();
        System.out.println("BEFORE CHECKING PHONE NUMS");
        for (int i = 0; i < phoneLog.size(); i++){
            int num = checkNumbers(phoneLog.get(i).getNotes());
            if (num > -1) {

                //add to the database
                com.example.huff6.clientbox.Log tempLog = phoneLog.get(i);
                temp.add(num);
                String phoneNumber = tempLog.getNotes();
                tempLog.setNotes("phone call");
                app.clientRef.child(phoneNumber).child("Logs").push().setValue(tempLog);
            }
        }

        for (int i = 0; i < temp.size(); i++){
            numbers += clientList.get(temp.get(i)).getName() + " " + clientList.get(temp.get(i)).getNum() + "\n";
        }
        return numbers;
    }


    private int checkNumbers(String number) {
        //System.out.println("CHECK NUMBERES");
        for (int i = 0; i < clientList.size(); i++) {
            if (clientList.get(i).getNum().equals(number)) {
                System.out.println("FOUND");
                return i;
            }
        }
        return -1;
    }

    private void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

//http://stackoverflow.com/questions/23024831/android-shared-preferences-example
    private void setPreferences(String date) {
        SharedPreferences.Editor editor = getSharedPreferences(preferences, MODE_PRIVATE).edit();
        editor.putString("name", date);
        editor.commit();
    }

    private String getPreferences() {

        SharedPreferences prefs = getSharedPreferences(preferences, MODE_PRIVATE);
        String restoredText = prefs.getString("name", null);
        if (restoredText != null) {
            return restoredText;
        }
        else {
            return "none";
        }
    }


    // Read from the database
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

        ValueEventListener numListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numClients = dataSnapshot.getValue(Long.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        app.numClientRef.addValueEventListener(numListener);
    }




    //async task..


    class sync extends  loadTask{
        @Override
        protected String doInBackground(Void... params) {

            //
            //Looper.prepare();

            try {
                updateCallInfo();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            publishProgress("");
            stillReading = true;

            return "syncing";
        }

        @Override
        protected void onPostExecute(String result) {

            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            if (phoneLog.size() == 0) {
                showMessage("last synced\n" + getPreferences(), "no new client call logs");
            } else {
                showMessage("last synced\n" + getPreferences(), getNumbers());
            }

            String currentDateandTime = timeStamp.format(new Date());
            setPreferences(currentDateandTime);

            syncr.setVisibility(View.VISIBLE);
        }
    }
    //load task AsyncTask method
    class loadTask extends AsyncTask<Void,String,String> {

        @Override
        protected void onPreExecute() {
           //progress bar
            /*adapter = (ArrayAdapter) lv.getAdapter();

            // set progress bar
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setMax(10);
            progressBar.setProgress(0);
            progressBar.setVisibility(View.VISIBLE);
            count = 0;*/
            app = (ClientBoxApplication)getApplication();
            check = "no new updates";



        }

        @Override
        protected String doInBackground(Void... params) {

            //
//            Looper.prepare();
            readFromDatabase();
            try {
                updateCallInfo();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            getNumbers();

            publishProgress("");

            return "updated";
        }

        @Override
        protected void onProgressUpdate(String... values) {

            //progress bar
            /*
            adapter.add(values[0]);
            count++;
            progressBar.setProgress(count);
            */
        }

        @Override
        protected void onPostExecute(String result) {

            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            stillReading = true;

            syncr.setVisibility(View.VISIBLE);
            String currentDateandTime = timeStamp.format(new Date());
            setPreferences(currentDateandTime);
            //stillReading = true;
            //progressBar.setVisibility(View.GONE);
        }
    }
}