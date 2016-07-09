package com.example.huff6.clientbox;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by huff6 on 6/18/2016
 */
public class LocalSingleton {
    DatabaseHelper myDb;


    LocalSingleton(Context context) {
        myDb = new DatabaseHelper(context);
    }

    // INSERT ROW
    boolean insertRow(String name, String phone, String startTime,
                          String endTime, String duration, String notes) {

        return myDb.insertData(name, phone, startTime, endTime, duration, notes);
    }

    // GET ALL ROWS
    Cursor getAllRows() {
        return myDb.getAllData();
    }

    // DELETE ALL ROWS
    void clearContents() {
        myDb.deleteAllData();
    }

}
