package com.example.huff6.clientbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    public static final String MESSAGE = "com.example.huff6.clientbox.AddClientActivity";
    public static final String MESSAGE2 = "com.example.huff6.clientbox.TimerActivity";
    public static final String MESSAGE3 = "com.example.huff6.clientbox.ManualEntryActivity";
    public static final String MESSAGE4 = "com.example.huff6.clientbox.ClientLookupActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //eye h8 meyecall <3
        //View temp = findViewById(R.id.awesome);


        Intent intent = getIntent();
        String message = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);
    }

    public void goToAddClient(View v){
        //go to add client page
        try {
            // move on to the main page
            Intent intent = new Intent(this, AddClientActivity.class);
            intent.putExtra(MESSAGE, "");
            startActivity(intent);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void goToTimer(View v){
        //go to add client page
        try {
            // move on to the main page
            Intent intent = new Intent(this, TimerActivity.class);
            intent.putExtra(MESSAGE2, "");
            startActivity(intent);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void goToManualEntry(View v){
        //go to add client page
        try {
            // move on to the main page
            Intent intent = new Intent(this, ManualEntryActivity.class);
            intent.putExtra(MESSAGE3, "");
            startActivity(intent);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void goToClientLookup(View v){
        //go to add client page
        try {
            // move on to the main page
            Intent intent = new Intent(this, ClientLookupActivity.class);
            intent.putExtra(MESSAGE4, "");
            startActivity(intent);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }


}
