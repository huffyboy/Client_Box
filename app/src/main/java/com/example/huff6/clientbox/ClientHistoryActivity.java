package com.example.huff6.clientbox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class ClientHistoryActivity extends AppCompatActivity {

    public static final String TAG = "Client Lookup Activity";
    protected ClientBoxApplication app;
    List<Client> clientList;
    //List<String> clientString;
    //private long numClients;
    ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_history);

        app = (ClientBoxApplication)getApplication();
        clientList = new ArrayList<>();
        //clientString = new ArrayList<>();
        readFromDatabase();

        populateListView();
    }


    //this function is to populate the listview with the
    //database information
    void populateListView(){
        lv = (ListView) findViewById(R.id.listView);

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
    public void readFromDatabase() {
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                android.util.Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

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
                //Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Toast.makeText(ClientHistoryActivity.this, "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        app.clientRef.addChildEventListener(childEventListener);
    }
}
