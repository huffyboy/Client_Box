package com.example.huff6.clientbox;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Manual Entry Activity
 */
public class ManualEntryActivity extends AppCompatActivity {

    private static final int Date_id = 0;
    private static final int Time_id = 1;
    public static final String TAG = "Manual Entry Activity";
    SimpleDateFormat dateFormat;
    String start, stop, date1, clientPhone, description;
    boolean startId, clientSelected;
    Date startDate, stopDate;
    List<Client> clientList;
    List<String> clientString, phoneNumberList;
    TextView clientTextView, set_time;
    ListView clientSelectListView;
    protected ClientBoxApplication app;
    private static TextView startTimeText, stopTimeText;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_entry);
        clientTextView = (TextView) findViewById(R.id.client_textView);
        startTimeText  = (TextView) findViewById(R.id.start_time);
        stopTimeText   = (TextView) findViewById(R.id.end_time);
        app = (ClientBoxApplication)getApplication();
        phoneNumberList = new ArrayList<>();
        clientList   = new ArrayList<>();
        clientString = new ArrayList<>();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy kk:mm:ss z", Locale.US);
        startId        = true;
        clientSelected = false;

        // get the time from timer activity if user came from that activity
        intent = getIntent();
        if (intent.getStringExtra("startString") != null) {
            startTimeText.setText(intent.getStringExtra("startString"));
            stopTimeText.setText(intent.getStringExtra("stopString"));
            start = intent.getStringExtra("startString");
            stop = intent.getStringExtra("stopString");
        } else {
            start = "";
            stop = "";
        }

        EditText editText = (EditText) findViewById(R.id.editText3);
        assert editText != null;
        description = editText.getText().toString();
        readFromDatabase();
    }


    /**
     * Let the user select the time for start time
     *
     * @param v the view to allow xml reference
     */
    public void startTimeSetter(View v) {
        set_time = (TextView) findViewById(R.id.start_time);
        showDialog(Time_id);
        showDialog(Date_id);
    }

    /**
     * Let the user select the time for end time
     *
     * @param v the view to allow xml reference
     */
    public void endTimeSetter(View v) {
        set_time = (TextView) findViewById(R.id.end_time);
        showDialog(Time_id);
        showDialog(Date_id);
    }

    /**
     * Submits the manual entry to a database and the passes an intent back to MainActivity.
     * It will throw an exception
     *
     * @param v the view for xml access
     */
    public void onClickSubmitManualEntry(View v) {
        if (checkValidEntries()) {
            try {
                EditText et = (EditText) findViewById(R.id.editText3);
                assert et != null;
                description = et.getText().toString();

                // calc different between two dates
                //      code copied from
                //      http://www.javamadesoeasy.com/2015/07/difference-between-two-dates-in-days.html
                // create log and push to the database
                startDate = dateFormat.parse(start);
                stopDate  = dateFormat.parse(stop);
                GregorianCalendar calendar1 = new GregorianCalendar();
                GregorianCalendar calendar2 = new GregorianCalendar();
                calendar1.setTime(startDate);
                calendar2.setTime(stopDate);

                long duration = Math.abs((calendar2.getTimeInMillis() - calendar1.getTimeInMillis()));
                duration = duration / 1000;
                Log tempLog = new Log();
                tempLog.setLog(start, stop, duration, description);
                app.clientRef.child(clientPhone).child("Logs").push().setValue(tempLog);
            }
            catch (ParseException ex){
                android.util.Log.e(TAG, "ERROR date not in correct parsing format" + ex.getMessage());
            }
            Toast.makeText(ManualEntryActivity.this, "submitted successfully", Toast.LENGTH_SHORT).show();

            // go back to main page
            try {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(MainActivity.MAIN_ACTIVITY, "");
                startActivity(intent);
            } catch (Exception e) {
                android.util.Log.e(TAG, e.getMessage());
            }
        }
    }

    /**
     * Returns either the dialog for the time picker or the date picker
     *
     * @param id for time picker or date picker
     * @return Dialog to date or time picker
     */
    protected Dialog onCreateDialog(int id) {
        // Get the calander
        Calendar c = Calendar.getInstance();

        // From calander get the year, month, day, hour, minute
        int year   = c.get(Calendar.YEAR);
        int month  = c.get(Calendar.MONTH);
        int day    = c.get(Calendar.DAY_OF_MONTH);
        int hour   = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        switch (id) {
            case Date_id:
                // Open the datepicker dialog
                return new DatePickerDialog(this, date_listener, year, month, day);

            case Time_id:
                // Open the timepicker dialog
                return new TimePickerDialog(this, time_listener, hour, minute, false);
        }
        return null;
    }

    /**
     * Listener for the date being set from datepicker
     */
    DatePickerDialog.OnDateSetListener date_listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // store the data in one string and set it to text
            String monthData;
            if (month < 10){
                monthData = "0" + String.valueOf(month + 1);
            } else {
                monthData = String.valueOf(month + 1);
            }
            date1 = monthData + "/" + String.valueOf(day)
                    + "/" + String.valueOf(year);
            set_time.setText(date1);
        }
    };

    /**
     * Listener for the time being set from timepicker
     */
    TimePickerDialog.OnTimeSetListener time_listener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int inputHour, int inputMinute) {
            // compile date string from selected time
            String minute = String.valueOf(inputMinute);
            Date tempDate = new Date();
            SimpleDateFormat s = new SimpleDateFormat("z", Locale.US);
            String timeZone = s.format(tempDate);
            if (inputMinute < 10) {
                minute = "0" + String.valueOf(inputMinute);
            }
            String time = String.valueOf(inputHour) + ":" + minute + ":00 " + timeZone;

            // set the start or stop string accordingly
            if (startId) {
                start = date1 + " " + time;
                startId = false;
            } else {
                stop = date1 + " " + time;
            }
            String dateAndTime = date1 + " " + time;
            set_time.setText(dateAndTime);
        }
    };

    /**
     * create a popup to select a client
     */
    public void showClients(View v) {
        //      code copied from
        //      http://stackoverflow.com/questions/2874191/is-it-possible-to-create-listview-inside-dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Client");
        populateListView();
        builder.setView(clientSelectListView);
        final Dialog dialog = builder.create();
        dialog.show();

        // onclick listener for the listview that has been created
        clientSelectListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>adapter,View v, int position,long l){
                String text = (String) (clientSelectListView.getItemAtPosition(position));
                clientTextView.setText(text);
                clientSelected = true;
                dialog.cancel();
                clientPhone = phoneNumberList.get(position);
            }
        });
    }

    /**
     * Checks to make sure all entries are valid and pops up with any errors to the users
     *
     * @return true if all entries are valid
     */
    public boolean checkValidEntries() {
        //Throw appropriate toast if not valid
        if(!(clientSelected) || stop.equals("")|| start.equals("")) {
            String toastString = "";

            if(!(clientSelected)) {
                toastString = "client";
            } else if(start.equals("")) {
                toastString = "start time";
            } else if(stop.equals("")) {
                toastString = "stop time";
            }

            Toast.makeText(ManualEntryActivity.this, "Please select a " + toastString, Toast.LENGTH_SHORT).show();
            return false;
        } else return checkValidTimes();
    }

    /**
     * Compare the times to see if they are before or after each other
     *
     * @return true if times are valid
     */
    public boolean checkValidTimes() {
        try {
            startDate = dateFormat.parse(start);
            stopDate  = dateFormat.parse(stop);
        } catch (ParseException ex) {
            android.util.Log.e(TAG, "ERROR date not in correct parsing format" + ex.getMessage());
        }

        // code copied from
        //      http://stackoverflow.com/questions/1505496/should-i-use-calendar-compareto-to-compare-dates
        //      more info here -http://www.tutorialspoint.com/java/util/date_compareto.htm
        if (startDate.compareTo(stopDate) > 0) {
            Toast.makeText(ManualEntryActivity.this, "Start/Stop Times are not valid.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
            }
        };
        app.clientRef.addChildEventListener(childEventListener);
    }

    /**
     * Populate the listview with clients and collect the phone numbers too
     */
    void populateListView(){
        clientSelectListView = new ListView(this);
        List<String> data = new ArrayList<>();
        phoneNumberList = new ArrayList<>();
        for (Client client : clientList) {
            data.add(client.getName() + "\n"
                    + client.getNum());
            // collecting the phone numbers
            phoneNumberList.add(client.getNum());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                data );
        clientSelectListView.setAdapter(arrayAdapter);
    }
}