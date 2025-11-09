import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.*;
/**
 * JUnit test cases for the Account class
 * Tests cover all constructors, getters, setters, toString(), and the createID() logic
 *
 * @author Shalini Murthula, Saanvi Verma
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
    private final String EXPECTED_ID = FIRST_NAME + "," + LAST_NAME + "," + AGE + "," + USERNAME + "," + PASSWORD + "," + EMAIL + "," + PHONE_NUMBER + "," + EXPECTED_GENERATED_ID;

    @Test
    // test the main constructor and all getter methods
    public void TestConstructorAndGetters() {
        Account account = new Account(FIRST_NAME, LAST_NAME, AGE, USERNAME, PASSWORD, EMAIL, PHONE_NUMBER);
        assertEquals("First name should match input.", FIRST_NAME, account.getFirstName());
        assertEquals("Last name should match input.", LAST_NAME, account.getLastName());
        assertEquals("Age should match input.", AGE, account.getAge());
        assertEquals("User name should match input.", USERNAME, account.getUserName());
        assertEquals("Password should match input.", PASSWORD, account.getPassword());
        assertEquals("Email should match input.", EMAIL, account.getEmail());
        assertEquals("Phone number should match input.", PHONE_NUMBER, account.getPhoneNumber());
        assertNull("Initial UserID should be null before calling createID.", account.getID());
    }

    @Test
    // test the constructor that takes a string line (for file reading)
    public void testStringConstructor() {
        Account account = new Account(EXPECTED_ID);
        assertEquals("First name should be parsed correctly.", FIRST_NAME, account.getFirstName());
        assertEquals("Last name should be parsed correctly.", LAST_NAME, account.getLastName());
        assertEquals("Age should be parsed correctly.", AGE, account.getAge());
        assertEquals("Phone number should be parsed correctly.", PHONE_NUMBER, account.getPhoneNumber());
        assertEquals("UserID should be parsed correctly.", EXPECTED_GENERATED_ID, account.getID());
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

        assertEquals("Email should be updated.", newEmail, account.getEmail());
        assertEquals("Phone number should be updated.", newPhone, account.getPhoneNumber());
        assertEquals("Password should be updated.", newPassword, account.getPassword());
        assertEquals("Username should be updated.", newUserName, account.getUserName());
        assertEquals("ID should be updated.", newID, account.getID());
    }

    @Test
    // test the createID method with standard data
    public void testCreateID() {
        Account account = new Account(FIRST_NAME, LAST_NAME, AGE, USERNAME, PASSWORD, EMAIL, PHONE_NUMBER);
        String id = account.createID();
        assertEquals("The generated ID should match the logic after filtering.", EXPECTED_GENERATED_ID, id);
        assertEquals("The stored ID should match the returned ID.", EXPECTED_GENERATED_ID, account.getID());

    }

    @Test
    // test the createID method with data specifically containing '6' and '7' in all ID parts
    public void testCreateIDFilterCase() {
        Account account = new Account("Six", "Seven", 30, "s", "p", "s@s.com", "1234567676");
        String id = account.createID();

        assertEquals("The ID should be 'SVII-' after removing all 6's and 7's.", "SVII-", id);
    }

    @Test
    // test the createID method with minimum length names
    public void testCreateIDBoundaryCase() {
        Account account = new Account("An", "Xyz", 40, "ax", "p", "a@x.com", "1234567890");
        String id = account.createID();

        assertEquals("The ID should be 'XZNA-890' for minimum length inputs.", "XZNA-890", id);
    }

    @Test
    // used for writing to file
    public void testToString() {
        final String EXPECTED_ID_NULL = FIRST_NAME + "," + LAST_NAME + "," + AGE + "," + USERNAME + "," + PASSWORD + "," + EMAIL + "," + PHONE_NUMBER + "," + "null";
        Account account = new Account(FIRST_NAME, LAST_NAME, AGE, USERNAME, PASSWORD, EMAIL, PHONE_NUMBER);
        System.out.println(account.writingInFile());
        assertEquals("toString should match the expected format with 'null' ID before generation.", EXPECTED_ID_NULL, account.writingInFile());

        account.createID();
        assertEquals("toString should match the expected format with the generated ID.", EXPECTED_ID, account.writingInFile());
    }
}
