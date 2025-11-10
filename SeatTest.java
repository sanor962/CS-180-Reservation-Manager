import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
/**
 * JUnit test cases for the Seat class
 * Tests cover all constructors, all getters and setters, and the toString() method
 *
 * @author Shalini Murthula (smurthul)
 * @version November 8, 2025
 */
public class SeatTest {

    // sample data for a valid seat
    private final String seatId = "C15";
    private final String row = "C";
    private final int number = 15;
    private final double price = 45.99;
    private final double newPrice = 99.99;

    // data line format
    private final String avaliable = "A1,A,true,1,25.50";
    private final String unavaliable = "B5,B,false,5,100.00";

    // this is the expected string output for a default-constructed seat
    private final String expectedString = "Seat ID: " + seatId + "\n Available: true\n Price: $" + price;

    @Test
    // test the standard constructor via getters
    public void testConstructor() {
        Seat seat = new Seat(seatId, row, number, price);

        assertEquals(seatId, seat.getSeatID(), "Seat ID must match the input.");
        assertEquals(row, seat.getRow(), "Row must match the input.");
        assertEquals(number, seat.getNumber(), "Number must match the input.");
        assertEquals(price, seat.getPrice(), "Price must match the input.");
        assertTrue(seat.isAvailable(), "New seats must be available by default.");
    }

    @Test
    // test the constructor that parses data from a file line
    public void testStringConstructorParsing() {
        // test an available seat
        Seat availableSeat = new Seat(avaliable);
        assertEquals("A1", availableSeat.getSeatID());
        assertEquals("A", availableSeat.getRow());
        assertEquals(1, availableSeat.getNumber());
        assertEquals(25.50, availableSeat.getPrice());
        assertTrue(availableSeat.isAvailable());

        // test an unavailable seat
        Seat unavailableSeat = new Seat(unavaliable);
        assertEquals("B5", unavailableSeat.getSeatID());
        assertEquals("B", unavailableSeat.getRow());
        assertEquals(5, unavailableSeat.getNumber());
        assertEquals(100.00, unavailableSeat.getPrice());
        assertFalse(unavailableSeat.isAvailable());
    }

    @Test
    // test the setter methods for price and availability
    public void testSetters() {
        Seat seat = new Seat(seatId, row, number, price);

        // test setPrice
        seat.setPrice(newPrice);
        assertEquals(newPrice, seat.getPrice(), "Price should be updated to the new value.");

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
        Seat seat = new Seat(seatId, row, number, price);

        assertEquals(expectedString, seat.toString(), "toString() must match specification for available seat.");

        seat.setAvailable(false);
        String expectedUnavailable = "Seat ID: C15\n Available: false\n Price: $" + price;
        assertEquals(expectedUnavailable, seat.toString(), "toString() must match specifcation for unavailable seat.");
    }
}
