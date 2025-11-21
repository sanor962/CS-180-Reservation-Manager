import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Handles the user interface (Client Class) and communicates with the server
 *
 * Port Number: 6767
 * Host Name: localhost
 *
 * @author Saanvi Verma (verma279)
 * @version November 17, 2025
 */

public class Client implements ClientInterface {
    //Variables
    private boolean run;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private Scanner scanner = new Scanner(System.in);
    private String account;
    private String host;
    private int port;

    //Constructor
    public Client(String host, int port) {
        this.host = host;
        this.port = port;
        this.run = false;
        this.account = null;

    }

    public static void main(String[] args) {
        //Sets the client to localhost and port 6767
        Client client = new Client("localhost", 6767);
        if (client.connect()) {
            /*Thread thread = new Thread(client);
            thread.start();*/
            client.start();
        } else {
            System.out.println("Could not connect.");
        }
    }

    //Tries to connect with the server
    public boolean connect() {
        try {
            socket = new Socket(host, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Connected");
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    //Starts the program
    public void start() {
        run = true;
        System.out.println("Concert System");
        System.out.println();

        while (run) {
            if (account == null) {
                //Menu #1
                System.out.println("1) Login");
                System.out.println("2) Create Account");
                System.out.println("3) Exit");

                //Making sure they are choosing an option that is valid
                String choice = "";
                while (true) {
                    System.out.println("Choose an option: ");
                    choice = scanner.nextLine().trim();
                    if (choice.equals("1") || choice.equals("2") || choice.equals("3")) {
                        break;
                    } else {
                        System.out.println("Please enter 1, 2, or 3.");
                    }
                }

                //Logs the user in
                if (choice.equals("1")) {
                    System.out.println("Username: ");
                    String username = scanner.nextLine().trim();
                    System.out.println("Password: ");
                    String password = scanner.nextLine().trim();

                    try {
                        writer.write("login\n");
                        writer.write(username + "\n");
                        writer.write(password + "\n");
                        writer.flush();
                        String response = reader.readLine();
                        if (response.equals("s")) {
                            account = reader.readLine();
                            System.out.println("Login successful. Welcome, " + username + "!");
                        } else {
                            System.out.println("Login failed. Invalid username or password.");
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                } else if (choice.equals("2")) {
                    //Makes an account
                    System.out.println("First Name: ");
                    String firstName = scanner.nextLine().trim();
                    System.out.println("Last Name: ");
                    String lastName = scanner.nextLine().trim();

                    //Making sure they are inputting a correct age
                    String age = "";
                    while (true) {
                        System.out.println("Age: ");
                        age = scanner.nextLine().trim();
                        try {
                            int ageNum = Integer.parseInt(age);
                            if (ageNum > 0 && ageNum < 100) {
                                break;
                            } else {
                                System.out.println("Please enter a valid age.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a valid age.");
                        }
                    }

                    //Making sure they are inputting a correct username
                    String username = "";
                    while (true) {
                        System.out.println("Username: ");
                        username = scanner.nextLine().trim();
                        if (username.length() <= 5) {
                            System.out.println("Please enter a valid username.");
                        } else if (username.isEmpty() || username == null) {
                            System.out.println("Please enter a valid username.");
                        } else {
                            break;
                        }
                    }

                    //Making sure they are inputting a correct password
                    String password = "";
                    while (true) {
                        System.out.println("Password: ");
                        password = scanner.nextLine().trim();
                        if (password.length() <= 8) {
                            System.out.println("Please enter a valid password.");
                        } else if (password.isEmpty() || password == null) {
                            System.out.println("Please enter a valid password.");
                        } else {
                            break;
                        }
                    }

                    //Making sure they are inputting a correct email
                    String email = "";
                    while (true) {
                        System.out.println("Email: ");
                        email = scanner.nextLine().trim();
                        if (!email.contains("@")) {
                            System.out.println("Please enter a valid email.");
                        } else {
                            break;
                        }
                    }

                    //Making sure they are inputting a correct phone number
                    String phone = "";
                    while (true) {
                        System.out.println("Phone Number: ");
                        phone = scanner.nextLine().trim();
                        if (phone.length() != 10) {
                            System.out.println("Please enter a valid phone number.");
                        } else {
                            break;
                        }
                    }

                    //Creates the account
                    try {
                        writer.write("createAccount\n");
                        writer.write(firstName + "\n");
                        writer.write(lastName + "\n");
                        writer.write(age + "\n");
                        writer.write(username + "\n");
                        writer.write(password + "\n");
                        writer.write(email + "\n");
                        writer.write(phone + "\n");
                        writer.flush();

                        String response = reader.readLine();
                        if (response.equals("s")) {
                            System.out.println("Account created! You can now login.");
                        } else {
                            String couldNotCreate = reader.readLine();
                            System.out.println(couldNotCreate);
                        }
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }

                } else if (choice.equals("3")) {
                    run = false;
                    System.out.println("Bye bye");
                }

            } else {
                //Menu 2
                System.out.println("1) View Available Seats for a Concert");
                System.out.println("2) Make Reservation");
                System.out.println("3) Cancel Reservation");
                System.out.println("4) View My Reservations");
                System.out.println("5) Delete Account");
                System.out.println("6) Logout");

                //Making sure they are choosing an option that is valid
                String choice = "";
                while (true) {
                    System.out.println("Choose an option: ");
                    choice = scanner.nextLine().trim();

                    if (choice.equals("1") || choice.equals("2") || choice.equals("3") || choice.equals("4") || choice.equals("5") || choice.equals("6")) {
                        break;
                    } else {
                        System.out.println("Invalid option. Please enter a number from 1-6.");
                    }
                }

                //Gets avaliable seats in a concert by the ID
                if (choice.equals("1")) {
                    System.out.println("Concert ID: ");
                    String showID = scanner.nextLine().trim();

                    String date = "";
                    int num = 0;
                    String d = "";
                    String m = "";
                    String y = "";
                    while (true) {
                        //Checking to make sure that the date is valid
                        num = 0;
                        System.out.println("Date (DD/MM/YYYY) (For example March 3, 2025 will be 03/03/2025): ");
                        date = scanner.nextLine().trim();
                        for (int i = 0; i < date.length(); i++) {
                            if (date.charAt(i) == '/') {
                                num++;
                            }
                        }
                        if (num != 2) {
                            System.out.println("Please enter a valid date.");
                            try {
                                d = date.substring(0);
                                m = d.substring(d.indexOf("/") + 1);
                                d = d.substring(0, d.indexOf("/"));
                                y = m.substring(m.indexOf("/") + 1);
                                m = m.substring(0, m.indexOf("/"));
                                if (Integer.parseInt(d) > 31 || d.length() > 2) {
                                    System.out.println("Please enter a valid date.");
                                }
                                if (Integer.parseInt(m) > 12 || m.length() > 2) {
                                    System.out.println("Please enter a valid date.");
                                }
                                if (y.length() > 4) {
                                    System.out.println("Please enter a valid date.");
                                }
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        } else {
                            break;
                        }

                    }

                    //Gets the avaliable seats in the concert
                    try {
                        writer.write("getAvailableSeats\n");
                        writer.write(showID + "\n");
                        writer.write(date + "\n");
                        writer.flush();

                        String count = reader.readLine();
                        int numOfSeats = Integer.parseInt(count);

                        if (numOfSeats == 0) {
                            System.out.println("No available seats.");
                        } else {
                            System.out.println("Available Seats (" + numOfSeats + " total):");
                            for (int i = 0; i < numOfSeats; i++) {
                                String seat = reader.readLine();
                                System.out.println(seat);
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                } else if (choice.equals("2")) {
                    //Books a reservation
                    System.out.println("Show ID: ");
                    String showID = scanner.nextLine().trim();
                    String date = "";
                    int num = 0;
                    String d = "";
                    String m = "";
                    String y = "";

                    //Checks to make sure the date is correct
                    while (true) {
                        num = 0;
                        System.out.println("Date (DD/MM/YYYY) (For example March 3, 2025 will be 03/03/2025): ");
                        date = scanner.nextLine().trim();
                        for (int i = 0; i < date.length(); i++) {
                            if (date.charAt(i) == '/') {
                                num++;
                            }
                        }
                        if (num != 2) {
                            System.out.println("Please enter a valid date.");
                            try {
                                d = date.substring(0);
                                m = d.substring(d.indexOf("/") + 1);
                                d = d.substring(0, d.indexOf("/"));
                                y = m.substring(m.indexOf("/") + 1);
                                m = m.substring(0, m.indexOf("/"));
                                if (Integer.parseInt(d) > 31 || d.length() > 2) {
                                    System.out.println("Please enter a valid date.");
                                }
                                if (Integer.parseInt(m) > 12 || m.length() > 2) {
                                    System.out.println("Please enter a valid date.");
                                }
                                if (y.length() > 4) {
                                    System.out.println("Please enter a valid date.");
                                }
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        } else {
                            break;
                        }

                    }

                    int numOfSeats = 0;
                    while (true) {
                        System.out.println("How many seats? ");
                        String num1 = scanner.nextLine().trim();
                        try {
                            numOfSeats = Integer.parseInt(num1);
                            if (numOfSeats > 0) {
                                break;
                            } else {
                                System.out.println("Please enter a number bigger than 0.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a valid number.");
                        }
                    }

                    ArrayList<String> seatIDs = new ArrayList<>();
                    for (int i = 0; i < numOfSeats; i++) {
                        int a = i + 1;
                        System.out.println("Seat ID #" + a + ": ");
                        seatIDs.add(scanner.nextLine().trim());
                    }

                    double totalPrice = 0;
                    while (true) {
                        System.out.println("Total Price: $");
                        String priceStr = scanner.nextLine().trim();
                        try {
                            totalPrice = Double.parseDouble(priceStr);
                            if (totalPrice >= 0) {
                                break;
                            } else {
                                System.out.println("Price cannot be negative.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a valid price.");
                        }
                    }

                    try {
                        writer.write("makeReservation\n");
                        writer.write(account + "\n");
                        writer.write(showID + "\n");
                        writer.write(numOfSeats + "\n");
                        for (int i = 0; i < seatIDs.size(); i++) {
                            writer.write(seatIDs.get(i) + "\n");
                        }
                        writer.write(date + "\n");
                        writer.write(totalPrice + "\n");
                        writer.flush();

                        String r = reader.readLine();
                        if (r.equals("s")) {
                            String reservationID = reader.readLine();
                            System.out.println("Reservation was successful. Your reservation ID is: " + reservationID);
                        } else {
                            System.out.println("Reservation was not successful.");
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                } else if (choice.equals("3")) {
                    //Cancels reservation by ID
                    System.out.println("Reservation ID: ");
                    String reservationID = scanner.nextLine().trim();
                    String confirm = "";
                    while (true) {
                        System.out.println("Confirm? (y/n): ");
                        confirm = scanner.nextLine().trim();
                        confirm = confirm.toLowerCase();
                        if (confirm.equals("y") || confirm.equals("n")) {
                            break;
                        } else {
                            System.out.println("Please enter 'y' for yes or 'n' for no.");
                        }
                    }

                    //Confirms the cancelation
                    if (confirm.equals("y")) {
                        try {
                            writer.write("cancelReservation\n");
                            writer.write(reservationID + "\n");
                            writer.flush();
                            String response = reader.readLine();

                            if (response.equals("s")) {
                                System.out.println("Your reservation has been cancelled.");
                            } else {
                                System.out.println("Cancellation failed. Please try again.");
                            }
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                } else if (choice.equals("4")) {
                    //Gets all the reservations by the account
                    try {
                        writer.write("getReservations\n");
                        writer.write(account + "\n");
                        writer.flush();

                        String count = reader.readLine();
                        int num = Integer.parseInt(count);

                        if (num == 0) {
                            System.out.println("You have no reservations.");
                        } else {
                            if (num > 1) {
                                System.out.println("You have " + num + " reservations:");
                            } else {
                                System.out.println("You have " + num + " reservation:");
                            }
                            for (int i = 0; i < num; i++) {
                                String reservationSetails = reader.readLine();
                                System.out.println(reservationSetails);
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }

                } else if (choice.equals("5")) {
                    //Deletes account by password and accountID
                    System.out.println("Confirm password: ");
                    String password = scanner.nextLine().trim();
                    System.out.println("Confirm accountID: ");
                    String acountID = scanner.nextLine().trim();

                    String confirm = "";
                    while (true) {
                        System.out.println("Are you sure? (y/n): ");
                        confirm = scanner.nextLine().trim().toLowerCase();

                        if (confirm.equals("y") || confirm.equals("n")) {
                            break;
                        } else {
                            System.out.println("Please enter 'y' or 'n'.");
                        }
                    }

                    //Confirms cancellation
                    if (confirm.equals("y")) {
                        try {
                            writer.write("deleteAccount\n");
                            writer.write(account + "\n");
                            writer.write(password + "\n");
                            writer.write(acountID + "\n");
                            writer.flush();
                            String r = reader.readLine();
                            if (r.equals("s")) {
                                System.out.println("Account deleted.");
                                account = null;
                            } else {
                                System.out.println("Account deletion failed. Please try again.");
                            }
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }

                } else if (choice.equals("6")) {
                    //Logs the user out
                    account = null;
                    System.out.println("Logged-out");
                }
            }
        }

        disconnect();
    }

    public void disconnect() {
        //Disconnects from the server
        try {
            if (socket != null && !socket.isClosed()) {
                writer.write("disconnect\n");
                writer.flush();
                socket.close();
                System.out.println("Disconnected.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    //Getter methods
    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getAccount() {
        return account;
    }

    public boolean isRunning() {
        return run;
    }

    //Setter methods
    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
