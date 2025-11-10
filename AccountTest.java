import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * JUnit test cases for the Account class
 * Tests cover all constructors, getters, setters, toString(), and the createID() logic
 *
 * @author Shalini Murthula (smurthul), Saanvi Verma (verma279)
 * @version November 8, 2025
 */
// JUnit test cases for the Account class
public class AccountTest {

    // sample data for a valid account
    private final String firstName = "Test";
    private final String lastName = "User";
    private final int age = 25;
    private final String username = "testuser123";
    private final String password = "strongPassword";
    private final String email = "test.user@example.com";
    private final String phoneNumber = "1234567890";
    private final String expectedGeneratedId = "UEES-890";
    private final String expectedId = firstName + "," + lastName + "," + age + "," + username +
            "," + password + "," + email + "," + phoneNumber + "," + expectedGeneratedId;

    @Test
    // test the main constructor and all getter methods
    public void testConstructorAndGetters() {
        Account account = new Account(firstName, lastName, age, username, password, email, phoneNumber);
        assertEquals("First name should match input.", firstName, account.getFirstName());
        assertEquals("Last name should match input.", lastName, account.getLastName());
        assertEquals("Age should match input.", age, account.getAge());
        assertEquals("User name should match input.", username, account.getUserName());
        assertEquals("Password should match input.", password, account.getPassword());
        assertEquals("Email should match input.", email, account.getEmail());
        assertEquals("Phone number should match input.", phoneNumber, account.getPhoneNumber());
        assertNull("Initial UserID should be null before calling createID.", account.getID());
    }

    @Test
    // test the constructor that takes a string line (for file reading)
    public void testStringConstructor() {
        Account account = new Account(expectedId);
        assertEquals("First name should be parsed correctly.", firstName, account.getFirstName());
        assertEquals("Last name should be parsed correctly.", lastName, account.getLastName());
        assertEquals("Age should be parsed correctly.", age, account.getAge());
        assertEquals("Phone number should be parsed correctly.", phoneNumber, account.getPhoneNumber());
        assertEquals("UserID should be parsed correctly.", expectedGeneratedId, account.getID());
    }

    @Test
    // test the setter methods to check for updatable fields
    public void testSetters() {
        Account account = new Account(firstName, lastName, age, username, password, email, phoneNumber);
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
        Account account = new Account(firstName, lastName, age, username, password, email, phoneNumber);
        String id = account.createID();
        assertEquals("The generated ID should match the logic after filtering.", expectedGeneratedId, id);
        assertEquals("The stored ID should match the returned ID.", expectedGeneratedId, account.getID());

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
        final String expectedIdNull = firstName + "," + lastName + "," + age + "," +
                username + "," + password + "," + email + "," + phoneNumber + "," + "null";
        Account account = new Account(firstName, lastName, age, username, password, email, phoneNumber);
        System.out.println(account.writingInFile());
        assertEquals("toString should match the expected format with 'null' ID before generation.", 
                expectedIdNull, account.writingInFile());

        account.createID();
        assertEquals("toString should match the expected format with the generated ID.", 
                expectedId, account.writingInFile());
    }
}
