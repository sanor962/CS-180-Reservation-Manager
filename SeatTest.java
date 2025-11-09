import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
/**
 * JUnit test cases for the Seat class
 * Tests cover all constructors, all getters and setters, and the toString() method
 *
 * @author Shalini Murthula
 * @version November 8, 2025
 */
public class SeatTest {

    // sample data for a valid seat
    private final String SEAT_ID = "C15";
    private final String ROW = "C";
    private final int NUMBER = 15;
    private final double PRICE = 45.99;
    private final double NEW_PRICE = 99.99;

    // data line format
    private final String AVAILABLE = "A1,A,true,1,25.50";
    private final String UNAVAILABLE = "B5,B,false,5,100.00";

    // this is the expected string output for a default-constructed seat
    private final String EXPECTED_STRING = "Seat ID: " + SEAT_ID + "\n Available: true\n Price: $" + PRICE;

    @Test
    // test the standard constructor via getters
    public void testConstructor() {
        Seat seat = new Seat(SEAT_ID, ROW, NUMBER, PRICE);

        assertEquals(SEAT_ID, seat.getSeatID(), "Seat ID must match the input.");
        assertEquals(ROW, seat.getRow(), "Row must match the input.");
        assertEquals(NUMBER, seat.getNumber(), "Number must match the input.");
        assertEquals(PRICE, seat.getPrice(), "Price must match the input.");
        assertTrue(seat.isAvailable(), "New seats must be available by default.");
    }

    @Test
    // test the constructor that parses data from a file line
    public void testStringConstructorParsing() {
        // test an available seat
        Seat availableSeat = new Seat(AVAILABLE);
        assertEquals("A1", availableSeat.getSeatID());
        assertEquals("A", availableSeat.getRow());
        assertEquals(1, availableSeat.getNumber());
        assertEquals(25.50, availableSeat.getPrice());
        assertTrue(availableSeat.isAvailable());

        // test an unavailable seat
        Seat unavailableSeat = new Seat(UNAVAILABLE);
        assertEquals("B5", unavailableSeat.getSeatID());
        assertEquals("B", unavailableSeat.getRow());
        assertEquals(5, unavailableSeat.getNumber());
        assertEquals(100.00, unavailableSeat.getPrice());
        assertFalse(unavailableSeat.isAvailable());
    }

    @Test
    // test the setter methods for price and availability
    public void testSetters() {
        Seat seat = new Seat(SEAT_ID, ROW, NUMBER, PRICE);

        // test setPrice
        seat.setPrice(NEW_PRICE);
        assertEquals(NEW_PRICE, seat.getPrice(), "Price should be updated to the new value.");

        // test setAvailable (to false)
        seat.setAvailable(false);
        assertFalse(seat.isAvailable(), "Availability should be set to false.");

        // test setAvailable (back to true)
        seat.setAvailable(true);
        assertTrue(seat.isAvailable(), "Availability should be set back to true.");
    }

    @Test
    // test the toString() method
    public void testToString() {
        Seat seat = new Seat(SEAT_ID, ROW, NUMBER, PRICE);

        assertEquals(EXPECTED_STRING, seat.toString(), "toString() must match specification for available seat.");

        seat.setAvailable(false);
        String expectedUnavailable = "Seat ID: C15\n Available: false\n Price: $" + PRICE;
        assertEquals(expectedUnavailable, seat.toString(), "toString() must match specifcation for unavailable seat.");
    }
}
