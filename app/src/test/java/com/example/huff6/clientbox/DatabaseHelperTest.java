package com.example.huff6.clientbox;

import android.database.Cursor;
import android.test.AndroidTestCase;

import com.example.huff6.clientbox.deprecated.DatabaseHelper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

/**
 * Created by huff6 on 6/18/2016
 */
public class DatabaseHelperTest extends AndroidTestCase {

    public static final int NUM_ITEMS_TO_TEST = 25;
    private String[] name, phone, startTime, endTime, duration, notes;
    private DatabaseHelper databaseHelper;
    private Generator gen;

    // CLASS to generate random strings of certain lengths
    class Generator {
        String generate(int length) {
            Random rand = new Random();
            String returnString = "";
            Integer number;

            while (returnString.length() < length) {
                number = rand.nextInt();
                returnString += number.toString();
            }

            return returnString;
        }
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        databaseHelper = new DatabaseHelper(mContext);

        gen = new Generator();

        name      = new String[25];
        phone     = new String[25];
        startTime = new String[25];
        endTime   = new String[25];
        duration  = new String[25];
        notes     = new String[25];
    }

    @After
    public void tearDown() throws Exception {
        databaseHelper.close();
        super.tearDown();
    }

    @Test
    public void insertDataTest() {
        // SETUP
        name[0]      = gen.generate(15);
        phone[0]     = gen.generate(10);
        startTime[0] = gen.generate(6);
        endTime[0]   = gen.generate(6);
        duration[0]  = gen.generate(4);
        notes[0]     = gen.generate(300);


        // ACTUAL TEST
        //assertTrue(databaseHelper != null);
        boolean isInserted = databaseHelper.insertData(
                name[0], phone[0], startTime[0], endTime[0], duration[0], notes[0]);

        assertTrue(isInserted);
    }


    @Test
    public void getAllDataTest() {
        // SETUP
        for (int i = 0; i < NUM_ITEMS_TO_TEST; i++) {
            name[i]      = gen.generate(15);
            phone[i]     = gen.generate(10);
            startTime[i] = gen.generate(6);
            endTime[i]   = gen.generate(6);
            duration[i]  = gen.generate(4);
            notes[i]     = gen.generate(300);

            databaseHelper.insertData(
                    name[i], phone[i], startTime[i], endTime[i], duration[i], notes[i]);
        }


        // ACTUAL TEST
        Cursor resultCursor = databaseHelper.getAllData();
        assertTrue(resultCursor.getCount() != 0);

        //int i = 0;
        //while (resultCursor.moveToNext()) {
        for (int i = 0; resultCursor.moveToNext(); i++) {
            resultCursor.getString(0);
            assertTrue(resultCursor.getString(1).equals(name[i]      ));
            assertTrue(resultCursor.getString(2).equals(phone[i]     ));
            assertTrue(resultCursor.getString(3).equals(startTime[i] ));
            assertTrue(resultCursor.getString(4).equals(endTime[i]   ));
            assertTrue(resultCursor.getString(5).equals(duration[i]  ));
            assertTrue(resultCursor.getString(6).equals(notes[i]     ));
            // i++
        }
    }

    @Test
    public void deleteAllDataTest() {
        // SETUP
        for (int i = 0; i < NUM_ITEMS_TO_TEST; i++) {
            name[i]      = gen.generate(15);
            phone[i]     = gen.generate(10);
            startTime[i] = gen.generate(6);
            endTime[i]   = gen.generate(6);
            duration[i]  = gen.generate(4);
            notes[i]     = gen.generate(300);

            databaseHelper.insertData(
                    name[i], phone[i], startTime[i], endTime[i], duration[i], notes[i]);
        }

        // ACTUAL TEST
        databaseHelper.deleteAllData();
        Cursor resultCursor = databaseHelper.getAllData();
        assertTrue(resultCursor.getCount() == 0);
    }
}
