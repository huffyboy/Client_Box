package com.example.huff6.clientbox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;

public class ClientLookupActivity extends AppCompatActivity {

    Client client;
    ArrayAdapter<client> clientList;
    //previous view - i think this is handled in the intent that should be passed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_lookup);
    }


    //create a public void class that passes the information of the listView
    //onClick will be executed by the listView so I imagine just pass in 'this'
    void listViewOnClick(){
        //adds a client and then returns to main menu?
    }
}
