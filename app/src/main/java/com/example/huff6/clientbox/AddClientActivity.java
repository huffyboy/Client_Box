package com.example.huff6.clientbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddClientActivity extends AppCompatActivity {

    EditText name;
    EditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_client);
    }

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


    public void onChangeValidateName()
    {
    }

    public void onChangeValidateNumber()
    {
    }

}