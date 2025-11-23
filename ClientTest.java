import org.junit.jupiter.api.*;
import java.io.*;
import java.util.concurrent.Executors;
import static org.junit.jupiter.api.Assertions.*;
/**
 * JUnit test cases for the Client class
 * Tests verify functionality of the class, which represents a client
 * in a concert reservation system, using simulations of server interactions
 *
 * @author Shalini Murthula (smurthul)
 * @version November 22, 2025
 */
public class ClientTest {

    private PipedInputStream clientIn;
    private PipedOutputStream clientOut;
    private PipedInputStream serverIn;
    private PipedOutputStream serverOut;
    private Client client;

    @BeforeEach
    public void setup() throws IOException {
        // create piped streams to simulate a client-server connection
        clientIn = new PipedInputStream();
        clientOut = new PipedOutputStream();
        serverIn = new PipedInputStream(clientOut);
        serverOut = new PipedOutputStream(clientIn);

        // create client with overridden connect() to use piped streams
        client = new Client("localhost", 6767) {
            @Override
            public boolean connect() {
                try {
                    this.reader = new BufferedReader(new InputStreamReader(clientIn));
                    this.writer = new PrintWriter(clientOut, true);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        };
    }

    @AfterEach
    public void teardown() throws IOException {
        clientIn.close();
        clientOut.close();
        serverIn.close();
        serverOut.close();
    }

    // helper to start a "mock server" for the tests
    private void mockServer(String[] commands, String[] responses) {
        Executors.newSingleThreadExecutor().submit(() -> {
            try (BufferedReader sr = new BufferedReader(new InputStreamReader(serverIn));
                 BufferedWriter sw = new BufferedWriter(new OutputStreamWriter(serverOut))) {
                for (int i = 0; i < commands.length; i++) {
                    String command = sr.readLine();
                    assertEquals(commands[i], command); // check if client sent expected command

                    if (responses != null && i < responses.length && responses[i] != null) {
                        sw.write(responses[i] + "\n");
                        sw.flush();
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Test
    public void testConstructorAndSetters() {
        // check default constructor values
        assertEquals("localhost", client.getHost());
        assertEquals(6767, client.getPort());
        assertNull(client.getAccount());
        assertFalse(client.isRunning());

        // check setters
        client.setHost("127.0.0.1");
        client.setPort(1234);
        assertEquals("127.0.0.1", client.getHost());
        assertEquals(1234, client.getPort());
    }

    @Test
    public void testConnectAndDisconnect() {
        // client should connect successfully
        assertTrue(client.connect());
        // disconnecting shouldn't throw any exceptions
        assertDoesNotThrow(client::disconnect);
    }

    @Test
    public void testLoginFlow() {
        // simulate user input for login (menu option 1)
        String input = "1\nuser_test\npass_test\n";
        client.scanner = new java.util.Scanner(new ByteArrayInputStream(input.getBytes()));

        // mock server expects login and sends success
        mockServer(new String[]{"login", "user_test", "pass_test"}, new String[]{"success"});

        client.connect();
        client.start();

        assertEquals("user_test", client.getAccount());
    }

    @Test
    public void testCreateAccountFlow() {
        String input = "2\nJohn\nDoe\n25\njohnDoe123\npassword123\njohn@example.com\n1234567890\n3\n";
        client.scanner = new java.util.Scanner(new ByteArrayInputStream(input.getBytes()));

        // server expects createAccount command
        mockServer(new String[]{"createAccount", "John", "Doe", "25", "johnDoe123", "password123", "john@example.com", "1234567890"},
                new String[]{"success", "null", "null", "null", "null", "null", "null", "null"});

        client.connect();
        client.start();

        // account not set yet
        assertNull(client.getAccount());
    }

    @Test
    public void testDeleteAccountFlow() {
        client.account = "user_test";
        client.scanner = new java.util.Scanner(new ByteArrayInputStream("7\nuser_test\npass_test\ny\n3\n".getBytes()));

        mockServer(new String[]{"deleteAccount", "user_test", "pass_test", "user_test"}, new String[]{"success", null, null, null});

        client.connect();
        client.start();

        assertNull(client.getAccount());
    }

    @Test
    public void testMakeReservationFlow() {
        // set client as logged in
        client.account = "user_test";

        String input = "2\npass_test\nshow123\n03/03/2025\n2\nA1\nA2\ny\n8\n";
        client.scanner = new java.util.Scanner(new ByteArrayInputStream(input.getBytes()));

        // server expects reservation flow
        mockServer(new String[]{"makeReservation", "user_test", "pass_test", "show123", "2", "A1", "A2", "03/03/2025", "pay"},
                new String[]{"150.00", null, null, null, null, null, null, null, "success\nRES123"});

        client.connect();
        client.start();
    }

    @Test
    public void testCancelReservationFlow() {
        client.account = "user_test";
        String input = "3\nRES123\ny\n8\n3\n";
        client.scanner = new java.util.Scanner(new ByteArrayInputStream(input.getBytes()));

        mockServer(new String[]{"cancelReservation", "RES123"}, new String[]{"success"});

        client.connect();
        client.start();
    }

    @Test
    public void testViewReservationFlow() {
        client.account = "user_test";
        String input = "4\n8\n";
        client.scanner = new java.util.Scanner(new ByteArrayInputStream(input.getBytes()));

        mockServer(new String[]{"getReservations", "user_test"},
                new String[]{"\"2\\nRES001,Show1,03/03/2025,A1\\nRES002,Show2,04/03/2025,B1\""});

        client.connect();
        client.start();
    }

    @Test
    public void testLoginFailure() {
        client.scanner = new java.util.Scanner(new ByteArrayInputStream("1\nuser_fail\nwrong_pass\n3\n".getBytes()));
        mockServer(new String[]{"login", "user_fail", "wrong_pass"}, new String[]{"failure"});

        client.connect();
        client.start();

        assertNull(client.getAccount());
    }

    @Test
    public void testCreateAccountFailure() {
        client.scanner = new java.util.Scanner(new ByteArrayInputStream(
                "2\nJohn\nDoe\n25\njohnFail\npassFail\njohn@example.com\n1234567890\n3\n".getBytes()));

        mockServer(new String[]{"createAccount", "John", "Doe", "25", "johnFail", "passFail", "john@example.com", "1234567890"},
                new String[]{"failure", null, null, null, null, null, null, null});

        client.connect();
        client.start();

        assertNull(client.getAccount());
    }

    @Test
    public void testMakeReservationFailure() {
        client.account = "user_test";
        client.scanner = new java.util.Scanner(new ByteArrayInputStream("2\npass_test\nshowX\n03/03/2025\n1\nA1\ny\n8\n3\n".getBytes()));

        mockServer(new String[]{"makeReservation", "user_test", "pass_test", "showX", "1", "A1", "03/03/2025", "pay"},
                new String[]{"failure", null, null, null, null, null, "failure"});

        client.connect();
        client.start();
    }

    @Test
    public void testDisconnectDuringSession() throws IOException {
        client.account = "user_test";
        client.scanner = new java.util.Scanner(new ByteArrayInputStream("4\n8\n".getBytes()));

        serverOut.close(); // simulate disconnect
        client.connect();
        assertDoesNotThrow(client::start);
    }
}
