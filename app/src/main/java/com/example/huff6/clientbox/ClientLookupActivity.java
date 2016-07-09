package com.example.huff6.clientbox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

public class ClientLookupActivity extends AppCompatActivity {

    public static final String TAG = "Client Lookup Activity";
    protected ClientBoxApplication app;
    List<Client> clientList;
    //List<String> clientString;
    //private long numClients;
    ListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_lookup);

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


    public void onClickView(View v) {
        populateListView();
        /*
        numClients = clientList.size();
        app.numClientRef.setValue(numClients);

        StringBuilder buffer = new StringBuilder();

        String[] clients = new String[clientList.size()];
        buffer.append("Number of Clients: " ).append(numClients).append("\n\n");
        for (Client client : clientList) {
            buffer.append("Name     : " ).append(client.getName()).append("\n");
            buffer.append("Number   : " ).append(client.getNum()).append("\n\n");
            clientString.add("Name   : " + client.getName() +"\n"
                           + "Number : " + client.getNum() + "\n");
        }
        //clients = clientList.toArray(clients);

        ArrayAdapter adapter = new ArrayAdapter<>(this,R.layout.activity_client_lookup,clientString);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        showMessage("Data", buffer.toString());*/
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
                android.util.Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                // A client has changed, use the key to determine if we are displaying this
                // client and if so displayed the changed comment.

                //Client newClient = dataSnapshot.getValue(Client.class);
                //String clientKey = dataSnapshot.getKey();

                // do nothing for now
                //clientList.set(Integer.parseInt(clientKey), newClient);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                android.util.Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());

                // A client has changed, use the key to determine if we are displaying this
                // client and if so remove it.

                //String clientKey = dataSnapshot.getKey();

                // do nothing for now
                //clientList.remove(Integer.parseInt(clientKey));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                android.util.Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

                // A client has changed position, use the key to determine if we are
                // displaying this client and if so move it.
                //      Client movedClient = dataSnapshot.getValue(Client.class);
                //      String clientKey = dataSnapshot.getKey();

                // do nothing for now
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Log.w(TAG, "postComments:onCancelled", databaseError.toException());
                Toast.makeText(ClientLookupActivity.this, "Failed to load comments.",
                        Toast.LENGTH_SHORT).show();
            }
        };
        app.clientRef.addChildEventListener(childEventListener);

        // HERE WE READ THE NUMBER OF ITEMS
        /*
        ValueEventListener numListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //numClients = dataSnapshot.getValue(Long.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                android.util.Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        app.numClientRef.addValueEventListener(numListener);
        */
    }

    /*private void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);

        builder.show();
    }*/

}
