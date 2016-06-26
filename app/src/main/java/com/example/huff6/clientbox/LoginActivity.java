package com.example.huff6.clientbox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

public class LoginActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    CheckBox stayLoggedIn;
    protected ClientBoxApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        app = (ClientBoxApplication)getApplication();


    }

    public void onLogin(View v) {


        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        stayLoggedIn = (CheckBox) findViewById(R.id.checkBox);

        //Simply write a test string to the DB - this should be where authentication goes
        String test = "Testing the DB";
        app.firebaseDb.push().setValue(test);
        //if not valid display error message here

        //it valid connect to database and move on to main page
        try {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(MainActivity.MAIN_ACTIVITY, "");
            startActivity(intent);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
