import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
/**
 * JUnit test cases for the Account class
 * Tests cover all constructors, getters, setters, toString(), and the createID() logic
 *
 * @author Shalini Murthula
 * @version November 8, 2025
 */
// JUnit test cases for the Account class
public class AccountTest {

    // sample data for a valid account
    private final String FIRST_NAME = "Test";
    private final String LAST_NAME = "User";
    private final int AGE = 25;
    private final String USERNAME = "testuser123";
    private final String PASSWORD = "strongPassword";
    private final String EMAIL = "test.user@example.com";
    private final String PHONE_NUMBER = "1234567890";
    private final String EXPECTED_GENERATED_ID = "UEES-890";
    private final String EXPECTED_ID = FIRST_NAME + ", " + LAST_NAME + ", " + USERNAME + ", " + PASSWORD + ", " + EMAIL + ", " + PHONE_NUMBER + ", " + EXPECTED_GENERATED_ID;

    @Test
    // test the main constructor and all getter methods
    public void TestConstructorAndGetters() {
        Account account = new Account(FIRST_NAME, LAST_NAME, AGE, USERNAME, PASSWORD, EMAIL, PHONE_NUMBER);
        assertEquals(FIRST_NAME, account.getFirstName(), "First name should match input.");
        assertEquals(LAST_NAME, account.getLastName(), "Last name should match input.");
        assertEquals(AGE, account.getAge(), "Age should match input.");
        assertEquals(USERNAME, account.getUserName(), "User name should match input.");
        assertEquals(PASSWORD, account.getPassword(), "Password should match input.");
        assertEquals(EMAIL, account.getEmail(), "Email should match input.");
        assertEquals(PHONE_NUMBER, account.getPhoneNumber(), "Phone number should match input.");
        assertNull(account.getID(), "Initial UserID should be null before calling createID.");
    }

    @Test
    // test the constructor that takes a string line (for file reading)
    public void testStringConstructor() {
        Account account = new Account(EXPECTED_ID);
        assertEquals(FIRST_NAME, account.getFirstName(), "First name should be parsed correctly.");
        assertEquals(LAST_NAME, account.getLastName(), "Last name should be parsed correctly.");
        assertEquals(AGE, account.getAge(), "Age should be parsed correctly.");
        assertEquals(PHONE_NUMBER, account.getPhoneNumber(), "Phone number should be parsed correctly.");
        assertEquals(EXPECTED_GENERATED_ID, account.getID(), "UserID should be parsed correctly.");
    }

    @Test
    // test the setter methods to check for updatable fields
    public void testSetters() {
        Account account = new Account(FIRST_NAME, LAST_NAME, AGE, USERNAME, PASSWORD, EMAIL, PHONE_NUMBER);
        String newEmail = "new.email@test.com";
        String newPhone = "0987654321";
        String newPassword = "newStrongPass";
        String newUserName = "newUserName";
        String newID = "XYZ-123";

        account.setEmail(newEmail);
        account.setPhoneNumber(newPhone);
        account.setPassword(newPassword);
        account.setUserName(newUserName);
        account.setID(newID);

        assertEquals(newEmail, account.getEmail(), "Email should be updated.");
        assertEquals(newPhone, account.getPhoneNumber(), "Phone number should be updated.");
        assertEquals(newPassword, account.getPassword(), "Password should be updated.");
        assertEquals(newUserName, account.getUserName(), "Username should be updated.");
        assertEquals(newID, account.getID(), "ID should be updated.");
    }

    @Test
    // test the createID method with standard data
    public void testCreateID() {
        Account account = new Account(FIRST_NAME, LAST_NAME, AGE, USERNAME, PASSWORD, EMAIL, PHONE_NUMBER);
        String id = account.createID();
        assertEquals(EXPECTED_GENERATED_ID, id, "The generated ID should match the logic after filtering.");
        assertEquals(EXPECTED_GENERATED_ID, account.getID(), "The stored ID should match the returned ID.");

    }

    @Test
    // test the createID method with data specifically containing '6' and '7' in all ID parts
    public void testCreateIDFilterCase() {
        Account account = new Account("Six", "Seven", 30, "s", "p", "s@s.com", "1234567676");
        String id = account.createID();

        assertEquals("SVII--", id, "The ID should be 'SVII--' after removing all 6's and 7's.");
    }

    @Test
    // test the createID method with minimum length names
    public void testCreateIDBoundaryCase() {
        Account account = new Account("An", "Xyz", 40, "ax", "p", "a@x.com", "1234567890");
        String id = account.createID();

        assertEquals("XZNN-890", id, "The ID should be 'XZNN-890' for minimum length inputs.");
    }

    @Test
    // used for writing to file
    public void testToString() {
        final String EXPECTED_ID_NULL = FIRST_NAME + ", " + LAST_NAME + ", " + USERNAME + ", " + PASSWORD + ", " + EMAIL + ", " + PHONE_NUMBER + ", " + "null";
        Account account = new Account(FIRST_NAME, LAST_NAME, AGE, USERNAME, PASSWORD, EMAIL, PASSWORD);
        assertEquals(EXPECTED_ID_NULL, account.toString(), "toString should match the expected format with 'null' ID before generation.");

        account.createID();
        assertEquals(EXPECTED_ID, account.toString(), "toString should match the expected format with the generated ID.");
    }
}
