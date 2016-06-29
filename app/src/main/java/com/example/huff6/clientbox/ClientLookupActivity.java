package com.example.huff6.clientbox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

public class ClientLookupActivity extends AppCompatActivity {

    Client client;
    ArrayAdapter<Client> clientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_lookup);
    }

    void onClickView(View v) {

    }

    //create a public void class that passes the information of the listView
    //onClick will be executed by the listView so I imagine just pass in 'this'
    void listViewOnClick(){
        //adds a client and then returns to main menu?
    }
}
