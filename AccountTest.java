import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
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
        assertEquals(firstName, account.getFirstName(), "First name should match input.");
        assertEquals(lastName, account.getLastName(), "Last name should match input.");
        assertEquals(age, account.getAge(), "Age should match input.");
        assertEquals(username, account.getUserName(), "User name should match input.");
        assertEquals(password, account.getPassword(), "Password should match input.");
        assertEquals(email, account.getEmail(), "Email should match input.");
        assertEquals(phoneNumber, account.getPhoneNumber(), "Phone number should match input.");
        assertNull(account.getID(), "Initial UserID should be null before calling createID.");
    }

    @Test
    // test the constructor that takes a string line (for file reading)
    public void testStringConstructor() {
        Account account = new Account(expectedId);
        assertEquals(firstName, account.getFirstName(), "First name should be parsed correctly.");
        assertEquals(lastName, account.getLastName(), "Last name should be parsed correctly.");
        assertEquals(age, account.getAge(), "Age should be parsed correctly.");
        assertEquals(phoneNumber, account.getPhoneNumber(), "Phone number should be parsed correctly.");
        assertEquals(expectedGeneratedId, account.getID(), "UserID should be parsed correctly.");
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

        assertEquals(newEmail, account.getEmail(), "Email should be updated.");
        assertEquals(newPhone, account.getPhoneNumber(), "Phone number should be updated.");
        assertEquals(newPassword, account.getPassword(), "Password should be updated.");
        assertEquals(newUserName, account.getUserName(), "Username should be updated.");
        assertEquals(newID, account.getID(), "ID should be updated.");
    }

    @Test
    // test the createID method with standard data
    public void testCreateID() {
        Account account = new Account(firstName, lastName, age, username, password, email, phoneNumber);
        String id = account.createID();
        assertEquals(expectedGeneratedId, id, "The generated ID should match the logic after filtering.");
        assertEquals(expectedGeneratedId, account.getID(), "The stored ID should match the returned ID.");

    }

    @Test
    // test the createID method with data specifically containing '6' and '7' in all ID parts
    public void testCreateIDFilterCase() {
        Account account = new Account("Six", "Seven", 30, "s", "p", "s@s.com", "1234567676");
        String id = account.createID();

        assertEquals("SVII-", id, "The ID should be 'SVII-' after removing all 6's and 7's.");
    }

    @Test
    // test the createID method with minimum length names
    public void testCreateIDBoundaryCase() {
        Account account = new Account("An", "Xyz", 40, "ax", "p", "a@x.com", "1234567890");
        String id = account.createID();

        assertEquals("XZNA-890", id, "The ID should be 'XZNA-890' for minimum length inputs.");
    }

    @Test
    // used for writing to file
    public void testToString() {
        final String expectedIdNull = firstName + "," + lastName + "," + age + "," +
                username + "," + password + "," + email + "," + phoneNumber + "," + "null";
        Account account = new Account(firstName, lastName, age, username, password, email, phoneNumber);
        System.out.println(account.writingInFile());
        assertEquals(expectedIdNull, account.writingInFile(),
                "toString should match the expected format with 'null' ID before generation.");

        account.createID();
        assertEquals(expectedId, account.writingInFile(),
                "toString should match the expected format with the generated ID.");
    }
}
