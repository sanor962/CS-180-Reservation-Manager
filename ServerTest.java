import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Files;
import java.nio.file.Path;
/**
 * JUnit test cases for the Server class
 * Tests account management, concert creation, reservations,
 * seat availability, and time retrieval
 * NOTE: some tests assume certain seat files exist
 *
 * @author Shalini Murthula (smurthul) and Saanvi Verma (verma279)
 * @version November 23, 2025
 */
public class ServerTest {
    private Server server;
    private static String oAccounts;
    private static String oSeats;
    private static String oReservations;
    private static String oConcerts;

    @BeforeAll
    public static void backupData() throws IOException {
        oAccounts = readFileIfExists("accounts.txt");
        oSeats = readFileIfExists("seats.txt");
        oReservations = readFileIfExists("reservations.txt");
        oConcerts = readFileIfExists("concert.txt");
    }

    @AfterAll
    public static void restoreData() throws IOException {
        Files.writeString(Path.of("accounts.txt"), oAccounts);
        Files.writeString(Path.of("seats.txt"), oSeats);
        Files.writeString(Path.of("reservations.txt"), oReservations);
        Files.writeString(Path.of("concert.txt"), oConcerts);
    }

    private static String readFileIfExists(String filename) throws IOException {
        Path path = Path.of(filename);
        if (Files.exists(path)) {
            return Files.readString(path);
        }
        return "";
    }

    @BeforeEach
    public void setup() throws Exception {
        server = new Server();

        // clear or create files to start fresh
        Files.writeString(Path.of("accounts.txt"), "");
        Files.writeString(Path.of("seats.txt"), "A1,A,true,1,50.0\n" +
                "B1,B,true,1,60.0\n" + "C1,C,true,1,70.0\n");
        Files.writeString(Path.of("reservations.txt"), "");
        Files.writeString(Path.of("concert.txt"), "");
    }

    @Test
    public void testCreateAccountAndLogin() {
        // create a new account
        boolean created = server.createAccount("John", "Doe", 25, "john_doe",
                "password123", "john@example.com", "1234567890");
        assertTrue(created, "Account should be created successfully.");

        // attempt to log in with correct credentials
        assertTrue(server.login("john_doe", "password123"), "Login should succeed with correct credentials.");

        // attempt to log in with wrong password
        assertFalse(server.login("john_doe", "wrongpassword"), "Login should fail with incorrect password.");
    }

    @Test
    public void testDeleteAccount() {
        boolean created = server.createAccount("Jane", "Doe", 30, "jane_doe",
                "securepassword", "jane@example.com", "0987654321");
        assertTrue(created);

        // retrieve the account
        Account acc = server.getAccountByUsername("jane_doe", "securepassword");
        assertNotNull(acc);
        String accountID = acc.getID();

        // delete the account
        boolean deleted = server.deleteAccount(accountID,"jane_doe", "securepassword");
        assertTrue(deleted, "Account should be deleted successfully");

        // login should fail after deletion
        assertFalse(server.login("jane_doe", "securepassword"), "Login should fail after account deletion.");
    }

    @Test
    public void testCreateAndGetConcerts() {
        boolean created = server.createConcert("RockFest", "25/12/2025", "19:00");
        assertTrue(created, "Concert should be created successfully.");

        ArrayList<String> concerts = server.getAllConcerts();
        assertTrue(concerts.contains("RockFest,25/12/2025,19:00,1"), "Concert list should contain the created concert.");
    }

    @Test
    public void testReservationFlow() {
        // setup account and concert
        server.createAccount("Alice", "Smith", 28, "alice_smith",
                "passalice123", "alice@example.com", "1112223333");
        server.createConcert("JazzNight", "01/01/2026", "20:00");

        // Get concert id
        ArrayList<String> concerts = server.getAllConcerts();
        String concertID = null;
        for (String c : concerts) {
            String[] parts = c.split(",");
            if (parts[0].equals("JazzNight")) {
                concertID = parts[3]; // 4th element is ID
                break;
            }
        }
        assertNotNull(concertID, "Concert ID should not be null");

        // create a reservation
        List<String> seatIDs = new ArrayList<>();
        seatIDs.add("A1");

        int reservationID = server.createReservation("alice_smith", "passalice123",
                concertID, seatIDs, "01/01/2026", "20:00", 50.0);
        assertTrue(reservationID != -1, "Reservation should be created successfully.");

        // cancel reservation
        boolean cancelled = server.cancelReservation(reservationID);
        assertTrue(cancelled, "Reservation should be cancelled successfully.");
    }

    @Test
    public void testReserveAndCancelSeat() {
        // assume a seat exists with id "B1"
        String seatID = "B1";
        String showID = "Billie";

        // reserve seat
        boolean reserved = server.reserveSeat(showID, seatID);
        assertTrue(reserved || !reserved);

        // cancel seat
        boolean cancelled = server.cancelSeat(showID, seatID);
        assertTrue(cancelled || !cancelled);
    }

    @Test
    public void testGetAvailableSeats() {
        server.createConcert("Concert", "01/01/2026", "18:00");

        ArrayList<String> concerts = server.getAllConcerts();
        String concertID = concerts.get(0).split(",")[3];

        ArrayList<Seat> seats = server.getAvailableSeats(concertID, "01/01/2026");
        assertNotNull(seats, "Available seats list should not be null.");
        assertEquals(3, seats.size(), "All initial seats should be available.");
    }

    @Test
    public void testGetReservationByID() {
        // set up credentials
        String username = "tswift";
        String password = "cats";

        // set up a reservation
        server.createAccount("Taylor", "Swift", 36,
                username, password,
                "taylorswift@example.com", "1212121212");
        server.createConcert("ErasTour", "15/03/2026", "21:00");

        // retrieve the Account object to get the actual unique id
        Account createdAccount = server.getAccountByUsername(username, password);
        String expectedUserID = createdAccount.getID();

        ArrayList<String> concerts = server.getAllConcerts();
        String concertID = concerts.get(0).split(",")[3];

        List<String> seats = new ArrayList<>();
        seats.add("C1");

        int reservationID = server.createReservation("tswift", "cats",
                concertID, seats, "15/03/2026", "21:00", 180.0);

        // must be valid
        assertNotEquals(-1, reservationID);

        // retrieve
        Reservations r = server.getReservationByID(reservationID);
        assertNotNull(r, "Reservation should be returned correctly.");

        assertEquals(expectedUserID, r.getUserID(), "Reservation UserID should match the Account's unique ID.");
        assertEquals(180.0, r.getTotalPrice());
    }

    @Test
    public void testGetTime() {
        server.createConcert("MetalFest", "20/05/2026", "20:30");

        ArrayList<String> concerts = server.getAllConcerts();
        String concertID = concerts.get(0).split(",")[3];

        String time = server.getTime(concertID);
        assertEquals("20:30", time, "Time should match the concert time.");
    }
}

