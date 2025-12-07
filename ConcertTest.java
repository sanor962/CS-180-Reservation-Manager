import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
/**
 * JUnit test cases for the Concert class
 * Checks the main functionality of the Concert object,
 * including constructors, getters/setters, toString(),
 * writingInFile(), and basic error handling
 *
 * @author Shalini Murthula (smurthul)
 * @version November 23, 2025
 */
public class ConcertTest {

    // test to verify all fields are stored correctly
    @Test
    public void testConstructorWithParameters() {
        Concert c = new Concert("Rock Night", "2025-12-12", "19:00", 101);

        assertEquals("Rock Night", c.getName());
        assertEquals("2025-12-12", c.getDate());
        assertEquals("19:00", c.getTime());
        assertEquals(101, c.getID());
    }

    // tests to see the string is split correctly into four fields
    @Test
    public void testConstructorWithLine() {
        Concert c = new Concert("Jazz Fest", "2025-11-10", "20:30", 202);

        assertEquals("Jazz Fest", c.getName());
        assertEquals("2025-11-10", c.getDate());
        assertEquals("20:30", c.getTime());
        assertEquals(202, c.getID());
    }

    // test setter methods
    @Test
    public void testSetters() {
        Concert c = new Concert("Any Concert", "2025-01-01", "00:00", 1);

        c.setName("Holiday Gala");
        c.setDate("2025-12-24");
        c.setTime("18:00");
        c.setID(300);

        assertEquals("Holiday Gala", c.getName());
        assertEquals("2025-12-24", c.getDate());
        assertEquals("18:00", c.getTime());
        assertEquals(300, c.getID());
    }

    // test toString() method
    @Test
    public void testToString() {
        Concert c = new Concert("Winter Concert", "2025-12-01", "17:00", 400);

        String expected = "Name: Winter Concert\n" +
                "Date: 2025-12-01\n" + "Time: 17:00\n" + "ID: 400";

        assertEquals(expected, c.toString());
    }

    // test to see if output string is formatted correctly for saving to a file
    @Test
    public void testWritingInFile() {
        Concert c = new Concert("Coachella", "2025-04-15", "16:00", 501);

        assertEquals("Coachella,2025-04-15,16:00,501", c.writingInFile());
    }

    // test to see if constructor throws an exception
    @Test
    public void testConstructorWithMalformedString() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Concert("Two Fields,2025-03-05"); // missing time and id
        });
    }

    // test to see if constructor throws a NullPointerException
    @Test
    public void testConstructorWithNullLine() {
        assertThrows(NullPointerException.class, () -> {
            new Concert((String) null);
        });
    }
}
