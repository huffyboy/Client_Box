package com.example.huff6.clientbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AddClientActivity extends AppCompatActivity {

    EditText name;
    EditText phone;


    List<Client> clientList;
    protected ClientBoxApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);
        app = (ClientBoxApplication)getApplication();
    }

    /**
     * add Client allows for the user to input information
     * about a new client into a database
     *
     * @param v
     */
    public void addClient(View v) {
        name = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.phone);

        //add client to database

        //if added:
        Toast.makeText(AddClientActivity.this, "client added", Toast.LENGTH_SHORT).show();

        //go back to main page
        try {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(MainActivity.MAIN_ACTIVITY, "");
            startActivity(intent);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

//not used
    public void onChangeValidateName()
    {
    }
//not used
    public void onChangeValidateNumber()
    {
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
                Toast.makeText(AddClientActivity.this, "Failed to load comments.",
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
}