package com.example.huff6.clientbox;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

/**
 * The client history activity
 */
public class ClientHistoryActivity extends AppCompatActivity {

    protected ClientBoxApplication app;
    private List<Client> clientList;
    private List<Log> logList;
    private ListView listView, logsListView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_history);
        clientList = new ArrayList<>();
        logList    = new ArrayList<>();
        app = (ClientBoxApplication)getApplication();
        listView = (ListView) findViewById(R.id.listView);

        new readClientsTask().execute();
        makeListViewClickable();
    }

    /**
     * Make the listview a list of clickable clients
     */
    private void makeListViewClickable(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>adapter,View v, int position,long l){
                // get title
                String[] title = (listView.getItemAtPosition(position)).toString().replace("Name   : ","").split("\\\n");
                String phoneNumber = (listView.getItemAtPosition(position)).toString().replaceAll("\\D","");
                // reset log list
                logList.clear();
                readFromDatabase(true, phoneNumber);
                // create logs list pop up
                AlertDialog.Builder builder = new AlertDialog.Builder(ClientHistoryActivity.this);
                builder.setTitle(title[0]);
                logsListView = new ListView(ClientHistoryActivity.this);
                builder.setView(logsListView);
                final Dialog dialog = builder.create();
                dialog.show();

                new readLogsTask().execute();
            }
        });
    }

    /**
     * Populate the listview with clients from the database
     */
    void populateClientList(){
        List<String> data = new ArrayList<>();
        for (Client client : clientList) {
            data.add("\nName   : " + client.getName() + "\n"
                    + "Number : " + client.getNum() + "\n");
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                data );

        listView.setAdapter(arrayAdapter);
    }

    /**
     * Populate the listview with logs from the database for a client
     */
    private void populateLogList() {
        List<String> data = new ArrayList<>();
        for (Log log : logList) {
            data.add( "\nTime : " + log.getStartTime() + "\n"
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

        logsListView.setAdapter(arrayAdapter);
    }

    /**
     * Reads either all the clients or the logs of a specific client
     *
     * @param readLogs are you reading the logs?
     * @param number the number for the client you want to read logs from
     */
    public void readFromDatabase(final boolean readLogs, String number) {
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                if (!readLogs) {
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

    /**
     * Reload the client list on click if the page glitches
     * and doesn't load the data
     *
     * @param v the view for XML reference
     */
    public void redoClientList(View v){
        populateClientList();
    }

    /**
     * A class for reading clients in the background
     */
    class readClientsTask extends AsyncTask<Void,String,String> {
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(Void... params) {
            readFromDatabase(false, "");
            populateClientList();
            return "done";
        }

        @Override
        protected void onProgressUpdate(String... values) {
            populateClientList();
        }

        @Override
        protected void onPostExecute(String result) {
            populateClientList();
        }
    }

    /**
     * A class for reading logs in the background
     */
    class readLogsTask extends readClientsTask {

        @Override
        protected String doInBackground(Void... params) {
            return "done";
        }

        @Override
        protected void onPostExecute(String result) {
            populateLogList();
        }
    }
}
