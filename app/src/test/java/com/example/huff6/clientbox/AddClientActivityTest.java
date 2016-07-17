package com.example.huff6.clientbox;

import android.test.AndroidTestCase;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * I believe this test would work, other than the fact that there's some weird errors with
 *  Firebase and not working here. So I guess that concludes our attempt to unit test
 *  for Firebase associated functions.
 *
public class AddClientActivityTest extends AndroidTestCase {

    private AddClientActivity addClientActivity;
    private Generator gen;
    private List<Client> clientList;
    private DatabaseReference clientRef;

    // CLASS to generate random strings of certain lengths
    class Generator {
        String generate(int length) {
            Random rand = new Random();
            String returnString = "";
            Integer number;

            while (returnString.length() < length) {
                number = rand.nextInt();
                returnString += number.toString();
            }

            return returnString;
        }
    }


    void readFromDatabase() {
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
                // do nothing for test
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // do nothing for test
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                // do nothing for test
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // do nothing for test
            }
        };
        clientRef.addChildEventListener(childEventListener);
    }


    @Before
    public void setUp() throws Exception {
        super.setUp();
        addClientActivity = new AddClientActivity();
        gen = new Generator();
        clientList = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        clientRef = database.getReference("Clients");
        readFromDatabase();
    }


    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }


    @Test
    public void insertDataTest() {
        // SETUP
        String name  = gen.generate(15);
        String phone = gen.generate(10);
        Client clientOne = new Client(name, phone);
        name  = gen.generate(15);
        phone = gen.generate(10);
        Client clientTwo = new Client(name, phone);

        View v = addClientActivity.findViewById(android.R.id.content);
        EditText nameText = (EditText) addClientActivity.findViewById(R.id.name);
        EditText phoneText = (EditText) addClientActivity.findViewById(R.id.phone);
        assert nameText != null;
        assert phoneText != null;

        // ACTUAL TEST
        nameText.setText(clientOne.getName());
        phoneText.setText(clientOne.getNum());
        addClientActivity.addClient(v);
        nameText.setText(clientTwo.getName());
        phoneText.setText(clientTwo.getNum());
        addClientActivity.addClient(v);

        int indexOne = clientList.indexOf(clientOne);
        int indexTwo = clientList.indexOf(clientOne);

        assertTrue(indexOne != -1);
        assertTrue(indexTwo != -1);
    }
}
 */