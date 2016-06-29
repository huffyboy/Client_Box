package com.example.huff6.clientbox;

import android.app.Application;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Tyler on 6/25/2016.
 * Info on why we use an application class is here
 * https://www.youtube.com/watch?v=guzAFDRSpB0
 */

//further help here http://stackoverflow.com/questions/30138017/where-to-put-firebase-setandroidcontext-function
public class ClientBoxApplication extends Application {

    public static final String TAG = "Application class";

    //set up a path to the url here
    //private static final String FIREBASE_URL = "https://proof-of-concept-b3c8b.firebaseio.com/";
    //connect to the firebase
    //Firebase firebaseDb;
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authListener;

    @Override
    public void onCreate() {
        super.onCreate();
        //First thing that you have to do is set the android context
        //Firebase.setAndroidContext(this);//use this to make things easier
        //Connect set here
        //firebaseDb = new Firebase(FIREBASE_URL);

        auth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    android.util.Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    android.util.Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

    }
}
