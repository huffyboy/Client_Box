package com.example.huff6.clientbox.deprecated;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.huff6.clientbox.R;

import java.util.Date;

public class TempActivity extends AppCompatActivity {
    //LocalSingleton localConnection;
    DatabaseHelper myDb;
    EditText textName, textPhone, textStartTime, textEndTime, textDuration, textNotes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);
        //localConnection = new LocalSingleton(this);
        myDb = new DatabaseHelper(this);

        textName      = (EditText) findViewById(R.id.editText_name);
        textPhone     = (EditText) findViewById(R.id.editText_phone);
        textStartTime = (EditText) findViewById(R.id.editText_startTime);
        textEndTime   = (EditText) findViewById(R.id.editText_endTime);
        textDuration  = (EditText) findViewById(R.id.editText_duration);
        textNotes     = (EditText) findViewById(R.id.editText_notes);

        getCallData();
    }

    public void getCallData() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("No permissions");
            return;
        }
        Cursor managedCursor = getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null, null, null, null);

        assert managedCursor != null;
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);

        while (managedCursor.moveToNext()) {
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString(duration);
            String dir = null;
            int dircode = Integer.parseInt(callType);
            switch (dircode) {
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;

                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;

                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;
            }
            // ADD TO TABLE
            myDb.insertData("UNKNOWN", phNumber, callDayTime.toString(),
                    "SOME TIME", callDuration, dir);
        }
        managedCursor.close();
    }

    public void onClickAddData(View v) {
        //boolean isInserted = localConnection.insertRow(
        boolean isInserted = myDb.insertData(
                textName.getText().toString(),
                textPhone.getText().toString(),
                textStartTime.getText().toString(),
                textEndTime.getText().toString(),
                textDuration.getText().toString(),
                textNotes.getText().toString()
        );

        if (isInserted) {
            Toast.makeText(TempActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(TempActivity.this, "Data NOT Inserted", Toast.LENGTH_LONG).show();
        }

    }

    public void onClickViewData(View v) {
        //Cursor resultCursor = localConnection.getAllRows();
        Cursor resultCursor = myDb.getAllData();
        System.out.println("Before get count");
        if (resultCursor.getCount() == 0) {
            System.out.println("empty list");
            showMessage("Error", "Nothing found");
            return;
        }

        // ELSE //////
        System.out.println("before string builder");
        System.out.println("COUNT IS " + resultCursor.getCount());
        StringBuilder buffer = new StringBuilder();
        while (resultCursor.moveToNext()) {
            System.out.println("inside loop");
            buffer.append("Id       : " ).append(resultCursor.getString(0)).append("\n");
            buffer.append("Name     : " ).append(resultCursor.getString(1)).append("\n");
            buffer.append("Number   : " ).append(resultCursor.getString(2)).append("\n");
            buffer.append("S Time   : " ).append(resultCursor.getString(3)).append("\n");
            buffer.append("E Time   : " ).append(resultCursor.getString(4)).append("\n");
            buffer.append("Duration : " ).append(resultCursor.getString(5)).append("\n");
            buffer.append("Notes    : " ).append(resultCursor.getString(6)).append("\n\n");
        }

        System.out.println("out of loop");

        // show all data
        showMessage("Data", buffer.toString());
    }
    public void onClickUpdateQueue(View v) {
        // DO NOTHING FOR NOW
    }

    public void onClickSyncDatabase(View v) {
        //localConnection.clearContents();
        myDb.deleteAllData();
    }

    public void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);

        builder.show();
    }
}
