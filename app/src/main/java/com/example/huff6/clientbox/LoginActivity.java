package com.example.huff6.clientbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.huff6.clientbox.MainActivity";
    EditText username;
    EditText password;
    CheckBox stayLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onLogin(View v) {


        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        stayLoggedIn = (CheckBox) findViewById(R.id.checkBox);

        //check against database here

        //if not valid display error message here

        //it valid connect to database and move on to main page


        try {
            // move on to the main page
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(EXTRA_MESSAGE, "");
            startActivity(intent);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
