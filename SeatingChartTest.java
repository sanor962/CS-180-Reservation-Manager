import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
/**
 * JUnit test cases for the SeatingChart class
 * Tests verify the functionality of managing a collection of seats,
 * including adding, retrieving, reserving, and filtering available seats
 * NOTE: assumes the Seat class and its methods work correctly
 *
 * @author Shalini Murthula
 * @version November 8, 2025
 */
class SeatingChartTest {

    private SeatingChart chart;
    private Seat seatA1;
    private Seat seatB2;
    private Seat seatC3;

    // initialize three distinct seat objects
    @BeforeEach
    public void setUp() {
        chart = new SeatingChart();
        seatA1 = new Seat("A1", "A", 1, 50.00);
        seatB2 = new Seat("B2", "B", 2, 75.00);
        seatC3 = new Seat("C3", "C", 3, 100.00);

        // add seats to the chart for tests
        chart.addSeat(seatA1);
        chart.addSeat(seatB2);
        chart.addSeat(seatC3);
    }

    // -- MANAGING SEATING --

    @Test
    public void testInitialStateAndAddSeat() {
        // verify initial size of the chart
        assertEquals(3, chart.getAllSeats().size(), "Chart should contain 3 seats after setup.");

        // add a fourth seat and check size again
        Seat seatD4 = new Seat("D4", "D", 4, 120.00);
        chart.addSeat(seatD4);
        assertEquals(4, chart.getAllSeats().size(), "Chart size should increment after adding a new seat.");
    }

    @Test
    public void testGetSeatFound() {
        // retrieve an existing seat
        Seat retrievedSeat = chart.getSeat("B2");
        assertNotNull(retrievedSeat, "getSeat should find an existing seat.");
        assertEquals("B2", retrievedSeat.getSeatID(), "The retrieved seat must have the correct ID.");
        assertEquals(seatB2, retrievedSeat, "The retrieved object should be the exact same instance.");
    }

    @Test
    public void testGetSeatNotFound() {
        // attempt to retrieve a non-existent seat
        Seat retrievedSeat = chart.getSeat("Z99");
        assertNull(retrievedSeat, "getSeat should return null for a non-existent seat ID.");
    }

    @Test
    public void testGetAllSeats() {
        List<Seat> allSeats = chart.getAllSeats();
        assertNotNull(allSeats, "getAllSeats should not return null.");
        assertEquals(3, allSeats.size(), "The list returned should contain all 3 seats.");
        assertTrue(allSeats.contains(seatA1), "The list must contain seat A1.");
        assertTrue(allSeats.contains(seatC3), "The list must contain seat C3.");
    }

    // -- RESERVATION AND CANCELLATION TESTS --

    @Test
    public void testReserveSeatSuccess() {
        assertTrue(seatA1.isAvailable(), "Seat A1 should start available");

        // reserve the seat
        boolean success = chart.reserveSeat("A1");

        assertTrue(success, "reserveSeat should return true on successful reservation.");
        assertFalse(seatA1.isAvailable(), "Seat A1 must be set to unavailable after reservation.");
    }

    @Test
    public void testReserveSeatFailureAlreadyReserved() {
        // first reserve the seat
        seatB2.setAvailable(false);

        // attempt to reserve it again
        boolean success = chart.reserveSeat("B2");

        assertFalse(success, "reserveSeat should return false when the seat is already reserved.");
        assertFalse(seatB2.isAvailable(), "Seat B2 availability status should remain false.");
    }

    @Test
    public void testReserveSeatFailureNonExistentID() {
        // this test relies on the original getSeat logic returning null for non-existent IDs.
        // NOTE: the current implementation of reserveSeat will throw a NullPointerException
        assertThrows(NullPointerException.class, () -> chart.reserveSeat("Z99"),
                "Reserving a non-existent seat must throw NullPointerException.");
    }

    @Test
    public void testCancelSeatSuccess() {
        // first reserve the seat so it can be cancelled
        seatC3.setAvailable(false);
        assertFalse(seatC3.isAvailable(), "Seat C3 should start unavailable for this cancellation test.");

        // cancel the seat
        boolean success = chart.cancelSeat("C3");

        assertTrue(success, "cancelSeat should return true on successful cancellation.");
        assertTrue(seatC3.isAvailable(), "Seat C3 must be set to available after cancellation.");
    }

    @Test
    public void testCancelSeatFailureAlreadyAvailable() {
        // seat A1 is available by default
        assertTrue(seatA1.isAvailable(), "Seat A1 should start available.");

        //attempt to cancel it
        boolean success = chart.cancelSeat("A1");

        assertFalse(success, "cancelSeat should return false when the seat is already available.");
        assertTrue(seatA1.isAvailable(), "Seat A1 availability status should remain true.");
    }

    // -- FILTERING --

    @Test
    public void testAvailableSeatsAllAvailable() {
        List<Seat> available = chart.getAvailableSeats();
        assertEquals(3, available.size(), "When all seats are available, the list size should be 3.");
        assertTrue(available.contains(seatA1));
        assertTrue(available.contains(seatB2));
        assertTrue(available.contains(seatC3));
    }

    @Test
    public void testGetAvailableSeatsMixedAvailability() {
        // make B2 unavailable
        seatB2.setAvailable(false);

        // reserve C3 using the chart method
        chart.reserveSeat("C3");

        List<Seat> available = chart.getAvailableSeats();

        assertEquals(1, available.size(), "Only seat A1 should be available.");
        assertTrue(available.contains(seatA1), "The only available seat should be A1.");
        assertFalse(available.contains(seatB2), "B2 should not be in the available list.");
        assertFalse(available.contains(seatC3), "C3 should not be in the available list.");
    }

    @Test
    public void testGetAvailableSeatsNoneAvailable() {
        // reserve all seats
        chart.reserveSeat("A1");
        chart.reserveSeat("B2");
        chart.reserveSeat("C3");

        List<Seat> available = chart.getAvailableSeats();

        assertTrue(available.isEmpty(), "When all seats are reserved, the available list should be empty.");
    }
}
