import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import com.aestheticsclub.services.Activity;
import com.aestheticsclub.services.Booking;
import com.aestheticsclub.services.Event;
import com.aestheticsclub.services.Facility;

import junit.framework.TestCase;

public class TestServices extends TestCase {

    @org.junit.Test
    public static void testViewAllActivities() {
        String result;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.out;
        System.setOut(ps);

        Activity.viewAllActivities();

        System.out.flush();
        System.setOut(old);

        result = baos.toString();

        assertFalse("Console output should not be empty", result.isEmpty());
    }

    @org.junit.Test
    public static void testViewAllBookings() {
        String result;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.out;
        System.setOut(ps);

        Booking.viewAllBookings();

        System.out.flush();
        System.setOut(old);

        result = baos.toString();

        assertFalse("Console output should not be empty", result.isEmpty());
    }

    @org.junit.Test
    public static void testViewAllEventsAndWebinars() {
        String result;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.out;
        System.setOut(ps);

        Event.viewAllEventsAndWebinars();

        System.out.flush();
        System.setOut(old);

        result = baos.toString();

        assertFalse("Console output should not be empty", result.isEmpty());
    }

    @org.junit.Test
    public static void testViewAllFacilities() {
        String result;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.out;
        System.setOut(ps);

        Facility.viewAllFacilities();

        System.out.flush();
        System.setOut(old);

        result = baos.toString();

        assertFalse("Console output should not be empty", result.isEmpty());
    }
}