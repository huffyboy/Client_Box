package com.example.huff6.clientbox;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.SynchronousQueue;

public class ClientHistoryActivity extends AppCompatActivity {

    public static final String TAG = "Client Lookup Activity";
    protected ClientBoxApplication app;
    List<Client> clientList;
    List<com.example.huff6.clientbox.Log> logList;
    //List<String> clientString;
    //private long numClients;
    ListView lv;
    ListView modeList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_history);
        clientList = new ArrayList<>();
        logList = new ArrayList<>();

        new loadTask().execute();
        //populateListView();

        setList();
        //populateLog();
    }

    private void populateLog() {
        List<String> data = new ArrayList<>();
        for (Log log : logList) {
            data.add( "\nTime : " + log.getStartTime() + "\n"
                    //+ "End Time   : " + log.getStartTime() + "\n"
                    + "Duration : " + log.getDuration() + " seconds\n"
                    + "Notes : " + log.getNotes() + "\n");
        }
        if (data.isEmpty()){
            data.add("\nthis client has no items\n");
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                data );

        modeList.setAdapter(arrayAdapter);
    }

    private void setList(){
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>adapter,View v, int position,long l){
                // set textView

                String[] title = (lv.getItemAtPosition(position)).toString().replace("Name   : ","").split("\\\n");
                String phoneNumber = (lv.getItemAtPosition(position)).toString().replaceAll("\\D","");
                System.out.println(phoneNumber);
                logList.clear();
                readFromDatabase(true, phoneNumber);
                AlertDialog.Builder builder = new AlertDialog.Builder(ClientHistoryActivity.this);
                builder.setTitle(title[0]);


                modeList = new ListView(ClientHistoryActivity.this);
                builder.setView(modeList);
                final Dialog dialog = builder.create();
                dialog.show();
                new loadLogsTask().execute();
                //dialog.cancel();
                //clientPhone = (String) (items.getItemAtPosition(position));

            }
        });


    }


    //this function is to populate the listview with the
    //database information
    void populateListView(){
        List<String> data = new ArrayList<>();
        for (Client client : clientList) {
            data.add("Name   : " + client.getName() + "\n"
                    + "Number : " + client.getNum());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                data );

        lv.setAdapter(arrayAdapter);
    }

    // Read from the database
    public void readFromDatabase(final boolean readLogs, String number) {
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                //android.util.Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                if (!readLogs) {
                    // A new client has been added, add it to the displayed list
                    Client theClient = dataSnapshot.getValue(Client.class);
                    theClient.setNum(dataSnapshot.getKey());
                    clientList.add(theClient);
                } else {
                    com.example.huff6.clientbox.Log theLog =
                            dataSnapshot.getValue(com.example.huff6.clientbox.Log.class);
                    logList.add(theLog);
                }
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
        if (!readLogs) {
            app.clientRef.addChildEventListener(childEventListener);
        } else {
            app.clientRef.child(number).child("Logs").addChildEventListener(childEventListener);
        }


    }




    //load task AsyncTask method
    class loadTask extends AsyncTask<Void,String,String> {
        @Override
        protected void onPreExecute() {

            app = (ClientBoxApplication)getApplication();
            lv = (ListView) findViewById(R.id.listView);
        }

        @Override
        protected String doInBackground(Void... params) {
            readFromDatabase(false, "");
            return "done";
        }

        @Override
        protected void onProgressUpdate(String... values) {
        }

        @Override
        protected void onPostExecute(String result) {
            populateListView();
        }
    }


    //load task AsyncTask method
    class loadLogsTask extends loadTask {

        //private ListView logsView;

        //public loadLogsTask(ListView inputView) {
        //    logsView = inputView;
        //}

        @Override
        protected String doInBackground(Void... params) {
            //readFromDatabase(false, "");
            return "done";
        }

        @Override
        protected void onPostExecute(String result) {
            populateLog();
        }
    }
}
