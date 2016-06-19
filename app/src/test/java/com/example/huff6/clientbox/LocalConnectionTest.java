package com.example.huff6.clientbox;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Tyler on 6/8/2016.
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class LocalConnectionTest {

    LocalConnection myObjectUnderTest = new LocalConnection();
//test
    @Test
    public void updateQueueTest() throws Exception {
        // ...when the int is returned from the object under test...
        assertTrue(myObjectUnderTest.getLogs().isEmpty());
    }


    @Test
    public void haveInternetConnectionTest() throws Exception {
        Boolean result = myObjectUnderTest.getServerConnection().haveInternetConnection();
        assertTrue(result);
    }
}
