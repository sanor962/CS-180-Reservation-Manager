import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class ReservationsTest {

    // Helper to create a test Account
    private Account createTestAccount(String firstName, String lastName, String phone) {
        Account acc = new Account(firstName, lastName, 25, firstName.toLowerCase(), "pass123",
                firstName.toLowerCase() + "@gmail.com", phone);
        acc.createID();
        return acc;
    }

    //Testing Reservations constructors and getter methods
    @Test(timeout = 1000)
    public void testConstructorAndGetters() {
        Account acc = createTestAccount("John", "Doe", "1234567890");
        List<String> seats = Arrays.asList("A1", "A2");
        Reservations r = new Reservations(acc, "S001", seats, "2025-11-10", "19:00", 40.0);

        assertTrue(r.getReservationID() > 0);
        assertEquals(acc.getID(), r.getUserID());
        assertEquals("S001", r.getShowID());
        assertEquals(seats, r.getSeatIDs());
        assertEquals(2, r.getNumSeats());
        assertEquals("2025-11-10", r.getDate());
        assertEquals("19:00", r.getTime());
        assertEquals(40.0, r.getTotalPrice(), 0.001);
    }

    //Testing constructor null input handling
    @Test(timeout = 1000)
    public void testConstructorHandlesNullInputs() {
        Reservations r = new Reservations((Account) null, null, null, null, null, 0.0);

        assertTrue(r.getReservationID() > 0);
        assertEquals("", r.getUserID());
        assertEquals("", r.getShowID());
        assertTrue(r.getSeatIDs().isEmpty());
        assertEquals(0, r.getNumSeats());
        assertEquals("", r.getDate());
        assertEquals("", r.getTime());
        assertEquals(0.0, r.getTotalPrice(), 0.001);
    }

    //Testing Reservations setter methods
    @Test(timeout = 1000)
    public void testSetterUpdateValues() {
        Account acc = createTestAccount("John", "Doe", "1234567890");
        Reservations r = new Reservations(acc, "S002", Arrays.asList("B1"), "2025-12-01", "18:30", 15.0);

        r.setUserID("U010"); //manually overrides userID
        r.setShowID("S010");
        r.setSeatIDs(Arrays.asList("B1", "B2", "B3"));
        r.setDate("2025-12-02");
        r.setTime("20:00");
        r.setTotalPrice(45.0);

        assertEquals("U010", r.getUserID());
        assertEquals("S010", r.getShowID());
        assertEquals(Arrays.asList("B1", "B2", "B3"), r.getSeatIDs());
        assertEquals(3, r.getNumSeats());
        assertEquals("2025-12-02", r.getDate());
        assertEquals("20:00", r.getTime());
        assertEquals(45.0, r.getTotalPrice(), 0.001);
    }

    //Testing Constructor that takes from reservations.txt File
    @Test(timeout = 1000)
    public void testFileLineConstructor() {
        //Create a sample line as it would appear in reservations.txt
        String line = "3,U003,S003,A1|A2|A3,2025-11-15,21:00,60.0";
        Reservations r = new Reservations(line);

        assertEquals(3, r.getReservationID());
        assertEquals("U003", r.getUserID());
        assertEquals("S003", r.getShowID());
        assertEquals(Arrays.asList("A1", "A2", "A3"), r.getSeatIDs());
        assertEquals(3, r.getNumSeats());
        assertEquals("2025-11-15", r.getDate());
        assertEquals("21:00", r.getTime());
        assertEquals(60.0, r.getTotalPrice(), 0.001);
    }

    //Testing parsing when the line is blank, null, or missing fields
    @Test(timeout = 1000)
    public void testLineConstructorEdgeCases() {
        Reservations blank = new Reservations("");
        Reservations nullLine = new Reservations(null);
        Reservations shortLine = new Reservations("10,U010");

        for (Reservations r : Arrays.asList(blank, nullLine, shortLine)) {
            assertTrue(r.getReservationID() > 0);
            assertTrue(r.getSeatIDs().isEmpty());
            assertEquals(0, r.getNumSeats());
            assertEquals("", r.getDate());
            assertEquals("", r.getTime());
            assertEquals(0.0, r.getTotalPrice(), 0.001);
        }
    }

    //Testing invalid numeric totalPrice (should default to 0.0)
    @Test(timeout = 1000)
    public void testInvalidPriceParsing() {
        String line = "11,U011,S011,A1|A2,2025-11-20,18:00,INVALID";
        Reservations r = new Reservations(line);
        assertEquals(0.0, r.getTotalPrice(), 0.001);
    }

    //Testing toString formatting
    @Test(timeout = 1000)
    public void testToStringFormat() {
        Account acc = createTestAccount("John", "Doe", "1234567890");
        Reservations r = new Reservations(acc, "S004", Arrays.asList("C1", "C2"), "2025-12-10", "17:00", 25.0);
        String expected = r.getReservationID() + "," + acc.getID() + ",S004,C1|C2,2025-12-10,17:00,25.0";
        assertEquals(expected, r.toString());
    }

    //Testing numSeats consistency
    @Test(timeout = 1000)
    public void testNumSeatsConsistency() {
        Account acc = createTestAccount("John", "Doe", "1234567890");
        Reservations r = new Reservations(acc, "S007", Arrays.asList("E1"), "2025-12-25", "20:00", 20.0);
        assertEquals(1, r.getNumSeats());

        r.setSeatIDs(Arrays.asList("E1", "E2", "E3", "E4"));
        assertEquals(4, r.getNumSeats());

        r.setSeatIDs(new ArrayList<>());
        assertEquals(0, r.getNumSeats());
    }

    //Testing empty seat list from constructor
    @Test(timeout = 1000)
    public void testEmptySeatsFromLine() {
        String line = "9,U009,S009,,2025-12-31,20:00,50.0";
        Reservations r = new Reservations(line);
        assertEquals(9, r.getReservationID());
        assertEquals("U009", r.getUserID());
        assertEquals("S009", r.getShowID());
        assertEquals(0, r.getNumSeats());
        assertTrue(r.getSeatIDs().isEmpty());
        assertEquals("2025-12-31", r.getDate());
        assertEquals("20:00", r.getTime());
        assertEquals(50.0, r.getTotalPrice(), 0.001);
    }

    //Testing totalPrice edge cases
    @Test(timeout = 1000)
    public void testTotalPriceEdgeCases() {
        Account acc = createTestAccount("John", "Doe", "1234567890");
        Reservations r = new Reservations(acc, "S009", Arrays.asList("F1"), "2025-12-15", "19:00", -50.0);
        assertEquals(-50.0, r.getTotalPrice(), 0.001);

        r.setTotalPrice(0);
        assertEquals(0, r.getTotalPrice(), 0.001);

        r.setTotalPrice(1_000_000);
        assertEquals(1_000_000, r.getTotalPrice(), 0.001);
    }

    //Testing if Reservations properly saves and reloads data
    @Test(timeout = 1000)
    public void testPersistentDataStringMatch() {
        Account acc = createTestAccount("John", "Doe", "1234567890");
        //First create a reservation object
        Reservations original = new Reservations(acc, "S006", Arrays.asList("D1", "D2"), "2025-12-20", "19:30", 30.0);

        //Second convert reservation to string (simulates saving to a file)
        String savedLine = original.toString();

        //Third create new reservation from that string (simulated reading from a file)
        Reservations loaded = new Reservations(savedLine);

        //Fourth verify that important fields are preserved
        assertEquals(original.getReservationID(), loaded.getReservationID());
        assertEquals(original.getSeatIDs(), loaded.getSeatIDs());
        assertEquals(original.getTotalPrice(), loaded.getTotalPrice(), 0.001);

    }

}
