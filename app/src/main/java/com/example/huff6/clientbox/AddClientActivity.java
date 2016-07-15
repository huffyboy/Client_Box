package com.example.huff6.clientbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AddClientActivity extends AppCompatActivity {

    EditText name;
    EditText phone;
    List<Client> clientList;
    protected ClientBoxApplication app;
    long numClients;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);
        app = (ClientBoxApplication) getApplication();

        readFromDatabase();
    }


    /**
     * add Client allows for the user to input information
     * about a new client into a database
     *
     * @param v the view to allow XML reference
     */
    public void addClient(View v) {
        name = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.phone);
        if (phone.getText().toString().matches("") || name.getText().toString().matches("")) {
            Toast.makeText(AddClientActivity.this, "please fill name and number", Toast.LENGTH_SHORT).show();
        } else if (phone.getText().length() < 10) {
            Toast.makeText(AddClientActivity.this, "please fill out 10 digits of number", Toast.LENGTH_SHORT).show();
        }
        else{
            //add client to database
            app.clientRef.child(phone.getText().toString()).child("name").setValue(name.getText().toString());
            app.numClientRef.setValue(numClients + 1);
            //if added:
            Toast.makeText(AddClientActivity.this, "client added", Toast.LENGTH_SHORT).show();

            //go back to main page
            try {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(MainActivity.MAIN_ACTIVITY, "");
                startActivity(intent);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }



    private void readFromDatabase(){
        ValueEventListener numListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                numClients = dataSnapshot.getValue(Long.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        app.numClientRef.addValueEventListener(numListener);
    }

    //not used
    public void onChangeValidateName()
    {
    }


    //not used
    public void onChangeValidateNumber()
    {
    }

}