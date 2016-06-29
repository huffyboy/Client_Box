package com.example.huff6.clientbox;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.*;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ClientLookupActivity extends AppCompatActivity {

    public static final String TAG = "Client Lookup Activity";
    protected ClientBoxApplication app;
    //Client client;
    List<Client> clientList;
    private long numClients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_lookup);

        app = (ClientBoxApplication)getApplication();
        clientList = new ArrayList<>();
        readFromDatabase();
    }

    public void onClickView(View v) {
        numClients = clientList.size();
        app.numClientRef.setValue(numClients);

        StringBuilder buffer = new StringBuilder();

        buffer.append("Number of Clients: " ).append(numClients).append("\n\n");
        for (Client client : clientList) {
            buffer.append("Name     : " ).append(client.getName()).append("\n");
            buffer.append("Number   : " ).append(client.getNum()).append("\n\n");
        }
        showMessage("Data", buffer.toString());
    }

    public void readFromDatabase() {
        // Read from the database

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                android.util.Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

                // A new client has been added, add it to the displayed list
                Client theClient = dataSnapshot.getValue(Client.class);
                clientList.add(theClient);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                android.util.Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A client has changed, use the key to determine if we are displaying this
                // client and if so displayed the changed comment.
                Client newClient = dataSnapshot.getValue(Client.class);
                String clientKey = dataSnapshot.getKey();

                // ... ??
                //clientList.set(Integer.parseInt(clientKey), newClient);

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                android.util.Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                // A client has changed, use the key to determine if we are displaying this
                // client and if so remove it.
                String clientKey = dataSnapshot.getKey();

                // ... ??
                //clientList.remove(Integer.parseInt(clientKey));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                android.util.Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

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
                Toast.makeText(ClientLookupActivity.this, "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        app.clientRef.addChildEventListener(childEventListener);


        // TO READ THE NUMBER OF ITEMS      !!!!!!!!!!!

        ValueEventListener numListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numClients = dataSnapshot.getValue(Long.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                android.util.Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        app.numClientRef.addValueEventListener(numListener);
    }










    private void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);

        builder.show();
    }


    //create a public void class that passes the information of the listView
    //onClick will be executed by the listView so I imagine just pass in 'this'
    void listViewOnClick(){
        //adds a client and then returns to main menu?
    }
}
