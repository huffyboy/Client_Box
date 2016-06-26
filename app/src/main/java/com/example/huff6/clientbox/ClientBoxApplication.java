package com.example.huff6.clientbox;

import android.app.Application;
import android.os.Bundle;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
/**
 * Created by Tyler on 6/25/2016.
 * Info on why we use an application class is here
 * https://www.youtube.com/watch?v=guzAFDRSpB0
 */

//further help here http://stackoverflow.com/questions/30138017/where-to-put-firebase-setandroidcontext-function
public class ClientBoxApplication extends Application {

    //set up a path to the url here
    private static final String FIREBASE_URL = "https://proof-of-concept-b3c8b.firebaseio.com/";
    //connect to the firebase
    Firebase firebaseDb;

    @Override
    public void onCreate() {
        super.onCreate();
        //First thing that you have to do is set the android context
        Firebase.setAndroidContext(this);//use this to make things easier
        //Connect set here
        firebaseDb = new Firebase(FIREBASE_URL);
    }
}
