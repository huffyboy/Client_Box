package com.example.huff6.clientbox;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

/**
 * The login activity
 */
public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "Login Activity";
    EditText editUsername, editPassword;
    protected ClientBoxApplication app;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        app = (ClientBoxApplication)getApplication();

        editUsername = (EditText) findViewById(R.id.username);
        editPassword = (EditText) findViewById(R.id.password);
    }


    @Override
    public void onStart() {
        super.onStart();
        app.auth.addAuthStateListener(app.authListener);
    }


    @Override
    public void onStop() {
        super.onStop();
        app.auth.removeAuthStateListener(app.authListener);
    }


    /**
     * on login allows for the user to request access to the database
     * and move on to the main activity
     *
     * @param v the view is needed for XML access
     */
    public void onLogin(View v) {
        String username = editUsername.getText().toString();
        String password = editPassword.getText().toString();

        // to avoid crashing, we have to pass something
        if (username.equals("")) {
            username = "blank";
        }
        if (password.equals("")) {
            password = "blank";
        }

        app.auth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            android.util.Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }
}
