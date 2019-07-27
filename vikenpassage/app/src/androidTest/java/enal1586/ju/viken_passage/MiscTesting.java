package enal1586.ju.viken_passage;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import enal1586.ju.viken_passage.controllers.LogInActivity;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MiscTesting {

    @Test
    public void regExMacAddress() {
        String macAddress = "FF:FF:FF:FF:FF:FF";
        String macAddressNull = "";
        String macAddressFaulty = "FF:FF:F12F:FF";


        TestHelper testHelper = new TestHelper();



        assertEquals(testHelper.isValidMacAddress(macAddress),true);
        assertEquals(testHelper.isValidMacAddress(macAddressNull),false);
        assertEquals(testHelper.isValidMacAddress(macAddressFaulty),false);
    }
    @Test
    public void TimeDifferenceTest(){
        LocalDateTime first = LocalDateTime.of(2016,6,1,1,1,1);
        LocalDateTime second = LocalDateTime.of(2016,6,1,2,1,1);

        TestHelper testHelper = new TestHelper();

        String value = testHelper.updateTimeLeft(first,second);

        assertEquals(value,"01:00:00");
    }
    @Test
    public void TimeDifferenceTestLeapYear(){
        //Testing leap year
        LocalDateTime first = LocalDateTime.of(2016,2,28,23,0,0);
        LocalDateTime second = LocalDateTime.of(2016,3,1,0,0,0);

        TestHelper testHelper = new TestHelper();

        String value = testHelper.updateTimeLeft(first,second);

        assertEquals(value,second.toString());
    }
    @Test
    public void TimeDifferenceTestLeapYear2(){
        //Testing leap year
        LocalDateTime first = LocalDateTime.of(2016,2,29,23,0,0);
        LocalDateTime second = LocalDateTime.of(2016,3,1,0,0,0);

        TestHelper testHelper = new TestHelper();

        String value = testHelper.updateTimeLeft(first,second);

        assertEquals(value, "01:00:00");
    }
    @Test
    public void TimeDifferenceTestWrongTime(){
        //Testing first to be one month ahead
        LocalDateTime first = LocalDateTime.of(2016,2,28,23,1,0);
        LocalDateTime second = LocalDateTime.of(2016,2,28,23,0,0);

        TestHelper testHelper = new TestHelper();

        String value = testHelper.updateTimeLeft(first,second);

        assertEquals(value,"00:-1:00");
    }

}
