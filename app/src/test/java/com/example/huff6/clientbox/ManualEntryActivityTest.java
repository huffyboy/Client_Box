package com.example.huff6.clientbox;



import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class ManualEntryActivityTest {

    @Test
    public void updateTest() {
        // update(Time, Time) : void

        String temp1 = "abc";
        String temp2 = "def";

        ManualEntryActivity MEA = new ManualEntryActivity();
        MEA.update(temp1, temp2);

        assertThat(temp1, is(temp1));
        assertThat(temp2 , is(temp2));
    }


    @Test
    public void validateTimeTest() {
        // validateTime( start Time, stop Time) : void

        ManualEntryActivity MEA = new ManualEntryActivity();
        MEA.validateTime();

        assertTrue(MEA.getIsValid());
    }

}
