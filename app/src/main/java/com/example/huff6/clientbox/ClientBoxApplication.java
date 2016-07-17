package com.example.huff6.clientbox;

import android.app.Application;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Tyler on 6/25/2016.
 * Info on why we use an application class is here
 * https://www.youtube.com/watch?v=guzAFDRSpB0
 */

//further help here http://stackoverflow.com/questions/30138017/where-to-put-firebase-setandroidcontext-function
public class ClientBoxApplication extends Application {

    public static final String TAG = "Application class";
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authListener;
    FirebaseDatabase database;
    DatabaseReference clientRef, numClientRef, logRef;


    @Override
    public void onCreate() {
        super.onCreate();
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
            }
        };

        database = FirebaseDatabase.getInstance();
        numClientRef = database.getReference("numClients");
        clientRef    = database.getReference("Clients");
        logRef       = database.getReference("client logs");
    }
}
