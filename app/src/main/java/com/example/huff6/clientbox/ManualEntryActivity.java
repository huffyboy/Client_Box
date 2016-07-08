package com.example.huff6.clientbox;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;


public class ManualEntryActivity extends AppCompatActivity {

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy kk:mm");

    String start;
    String stop;
    String date1;
    String time1;
    String notes;
    Client client;
    boolean isValid;
    boolean check;
    LocalConnection localConnection;
    //Client client;
    List<Client> clientList;
    List<String> clientString;

    TextView textView;

    //database variables
    ListView modeList;
    ListView items;
    String clientReference;
    String description;

    protected ClientBoxApplication app;

//////////////////////////////////
    private static Button date, time;
    private static TextView set_date, set_time;
    private static final int Date_id = 0;
    private static final int Time_id = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_entry);
        app = (ClientBoxApplication)getApplication();

        check = false;

        Intent intent = getIntent();
        //either recieve from the main of recieve from the timer
        //we need to figure out how to switch between different intents
        String message = intent.getStringExtra(MainActivity.MANUAL_ENTRY_ACTIVITY);



        time = (Button) findViewById(R.id.selecttime);
        set_time = (TextView) findViewById(R.id.set_time);

        //set database vars
        textView = (TextView) findViewById(R.id.textView);
        EditText et = (EditText) findViewById(R.id.editText3);
        description = et.getText().toString();

        clientList = new ArrayList<>();
        clientString = new ArrayList<>();
        readFromDatabase();

    }

    public void startTimeSetter(View v) {
        // Show time dialog
        set_time = (TextView) findViewById(R.id.set_time);
        showDialog(Time_id);
        //set_date = (TextView) findViewById(R.id.set_date);
        showDialog(Date_id);
    }

    public void endTimeSetter(View v) {
        // Show time dialog
        set_time = (TextView) findViewById(R.id.set_time2);
        showDialog(Time_id);
        //set_date = (TextView) findViewById(R.id.set_date2);
        showDialog(Date_id);
    }



    /**
     * Submits the manual entry (when button pressed
     * to a database and the passes an intent back to MainActivity.
     * It will throw an exception
     * @param v
     */
    public void onClickSubmitManualEntry(View v) {
        //submit info to database
        if (check) {

            EditText et = (EditText) findViewById(R.id.editText3);
            description = et.getText().toString();


            Log tempLog = new Log();
            tempLog.setLog("startTime", "endTime", 00, description);

            //Client client = new Client(name, phoneNumber);
            app.clientRef.child(clientReference).child("Logs").push().setValue(tempLog);

            //app.database.setValue("client 00").push(tem);
            // we may want to increment number of logs per user?
            //numClients++;
            //numClientsRef.setValue(numClients);


            //if added:
            Toast.makeText(ManualEntryActivity.this, "submitted successfully", Toast.LENGTH_SHORT).show();

            //go back to main page
            try {
                // move on to the main page
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(MainActivity.MAIN_ACTIVITY, "");
                startActivity(intent);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        else{
            Toast.makeText(ManualEntryActivity.this, "please select a client", Toast.LENGTH_SHORT).show();
        }
    }

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

    // Date picker dialog
    DatePickerDialog.OnDateSetListener date_listener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            // store the data in one string and set it to text
            date1 = String.valueOf(month + 1) + "/" + String.valueOf(day)
                    + "/" + String.valueOf(year);
            set_time.setText(date1);
        }
    };
    TimePickerDialog.OnTimeSetListener time_listener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {
            // store the data in one string and set it to text
            String min = String.valueOf(minute);
            if (minute < 10)
                min = "0" + String.valueOf(minute);
            time1 = String.valueOf(hour) + ":" + min;
            set_time.setText(date1 + " " + time1);
        }
    };

//////////////////////////////////
//

    /**update start and stop inputs
     */
    public void update(String startInput, String stopInput) {
        start = startInput;
        stop = stopInput;
    }


    /**go to add client page to select client
     */
    public void showClients(View v) {

//http://stackoverflow.com/questions/2874191/is-it-possible-to-create-listview-inside-dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Client");

        populateListView();
        //String[] stringArray = new String[]{"Bright Mode", "Normal Mode"};
        //ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, stringArray);
        //modeList.setAdapter(modeAdapter);

        builder.setView(modeList);
        final Dialog dialog = builder.create();

        dialog.show();

        // onclick listener for the listview that has been created
        modeList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>adapter,View v, int position,long l){

                //set textView
                String text = (String) (modeList.getItemAtPosition(position));
                textView.setText(text);

                //close dialog
                dialog.cancel();
                clientReference = (String) (items.getItemAtPosition(position));

                check = true;
            }


        });

    }



    public void fromToClientLookup(View v) {

        populateListView();

    }
    public void validateTime() {
    }

    public boolean getIsValid() {
        return isValid;
    }



    public void readFromDatabase() {
        // Read from the database

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                android.util.Log.d(ClientLookupActivity.TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new client has been added, add it to the displayed list
                Client theClient = dataSnapshot.getValue(Client.class);
                theClient.setNum(dataSnapshot.getKey());
                clientList.add(theClient);
            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                android.util.Log.d(ClientLookupActivity.TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A client has changed, use the key to determine if we are displaying this
                // client and if so displayed the changed comment.
                Client newClient = dataSnapshot.getValue(Client.class);
                String clientKey = dataSnapshot.getKey();

                // ... ??
                //clientList.set(Integer.parseInt(clientKey), newClient);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                android.util.Log.d(ClientLookupActivity.TAG, "onChildRemoved:" + dataSnapshot.getKey());

                // A client has changed, use the key to determine if we are displaying this
                // client and if so remove it.
                String clientKey = dataSnapshot.getKey();

                // ... ??
                //clientList.remove(Integer.parseInt(clientKey));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                android.util.Log.d(ClientLookupActivity.TAG, "onChildMoved:" + dataSnapshot.getKey());

                // A client has changed position, use the key to determine if we are
                // displaying this client and if so move it.
                //      Client movedClient = dataSnapshot.getValue(Client.class);
                //      String clientKey = dataSnapshot.getKey();

                // ...
                // ???
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Toast.makeText(ManualEntryActivity.this, "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        app.clientRef.addChildEventListener(childEventListener);


        // TO READ THE NUMBER OF ITEMS      !!!!!!!!!!!

        ValueEventListener numListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //numClients = dataSnapshot.getValue(Long.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                android.util.Log.w(ClientLookupActivity.TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        app.numClientRef.addValueEventListener(numListener);
    }

    void populateListView(){


        modeList = new ListView(this);
        items = new ListView(this);


        List<String> data = new ArrayList<>();
        List<String> dataOther = new ArrayList<>();
        for (Client client : clientList) {
            data.add(client.getName() + "\n"
                    + client.getNum());

            dataOther.add(client.getNum());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                data );

        modeList.setAdapter(arrayAdapter);

        arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                dataOther );

        items.setAdapter(arrayAdapter);




    }


}
