import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
/**
 * The Server class handles all client connections
 * Port Number: 6767
 *
 * @author Kunj Arora (arora271), Saanvi Verma (verma279), Shalini Murthula (smurthul)
 * @version November 21, 2025
 */
public class Server implements ServerInterface, Runnable {
    private static final Database DATABASE = new Database();
    private Socket socket;
    private PaymentManager paymentManager = new PaymentManager();

    public Server() {
        //database = new Database();
    }

    //Sets it to a given socket
    public Server(Socket socket) {
        this.socket = socket;
        //this.database = new Database();
        this.paymentManager = new PaymentManager();
    }


    private void handleClient(Socket socket1) {
        BufferedReader reader = null;
        PrintWriter writer = null;

        try {
            reader = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
            writer = new PrintWriter(socket1.getOutputStream(), true);

            boolean run = true;
            String accountID = null;

            while (run) {
                String command = reader.readLine();
                if (command == null) {
                    break;
                }

                if (command.equals("login")) {
                    //Logs the user in
                    String username = reader.readLine();
                    String password = reader.readLine();
                    Account account = DATABASE.getAccountByUsername(username, password);
                    if (login(username, password)) {
                        //accountID = username;
                        writer.println("success");
                        writer.println(account.getID());
                    } else {
                        writer.println("fail");
                    }
                } else if (command.equals("createAccount")) {
                    //Creates the account
                    String firstName = reader.readLine();
                    String lastName = reader.readLine();
                    int age = Integer.parseInt(reader.readLine());
                    String username = reader.readLine();
                    String password = reader.readLine();
                    String email = reader.readLine();
                    String phone = reader.readLine();
                    if (createAccount(firstName, lastName, age, username, password, email, phone)) {
                        writer.println("success");
                    } else {
                        writer.println("fail");
                        writer.println("Account could not be created because username has already been taken.");
                    }
                } else if (command.equals("getAvailableSeats")) {
                    //Gets avaliable seats
                    String showID = reader.readLine();
                    String date = reader.readLine();
                    ArrayList<Seat> seats = getAvailableSeats(showID, date);
                    writer.println(seats.size());

                    for (int i = 0; i < seats.size(); i++) {
                        writer.println(seats.get(i).writingInFile());
                    }
                } else if (command.equals("makeReservation")) {
                    //Make reservation
                    String username = reader.readLine();
                    String password = reader.readLine();
                    Account currentUser = DATABASE.getAccountByUsername(username, password);
                    if (currentUser == null || !currentUser.getPassword().equals(password)) {
                        writer.println("invalid");
                        writer.flush();
                        continue;
                    }
                    writer.println("valid");
                    writer.flush();

                    String showID = reader.readLine();
                    int numSeats = Integer.parseInt(reader.readLine());
                    List<String> seatIDs = new ArrayList<>();

                    for (int i = 0; i < numSeats; i++) {
                        seatIDs.add(reader.readLine());
                    }

                    String date = reader.readLine();
                    double totalPrice = 0;
                    for (int i = 0; i < seatIDs.size(); i++) {
                        Seat s = DATABASE.getSeat(showID, seatIDs.get(i));
                        if (s == null || !s.isAvailable()) {
                            writer.println("Seat " + seatIDs.get(i) + " is unavailable.");
                            continue;
                        }
                        totalPrice = totalPrice + s.getPrice();
                    }
                    writer.println(totalPrice);

                    String payCommand = reader.readLine();
                    if (!"pay".equals(payCommand)) {
                        writer.println("cancelled");
                        continue;
                    }

                    String time = getTime(showID);

                    int reservationID = createReservation(username, password, showID, seatIDs, date, time, totalPrice);
                    boolean success = paymentManager.processPayment(reservationID, totalPrice);

                    if (!success) {
                        writer.println("fail");
                        continue;
                    }

                    if (reservationID != -1) {
                        writer.println("success");
                        writer.println(reservationID);
                    } else {
                        writer.println("fail");
                    }

                    for (String seatID : seatIDs) {
                        DATABASE.updateSeatAvailability(showID, seatID, false);
                    }
                } else if (command.equals("cancelReservation")) {
                    //Canceling reservation
                    int reservationID = Integer.parseInt(reader.readLine());
                    if (cancelReservation(reservationID)) {
                        writer.println("success");
                    } else {
                        writer.println("fail");
                    }
                } else if (command.equals("getReservations")) {
                    //Getting users reservation
                    String user = reader.readLine();
                    ArrayList<Reservations> reservations = getReservationsByAccount(user);
                    writer.println(reservations.size());

                    for (int i = 0; i < reservations.size(); i++) {
                        writer.println(reservations.get(i).toString());
                    }
                } else if (command.equals("deleteAccount")) {
                    //Deleting account
                    String id = reader.readLine();
                    String password = reader.readLine();
                    String user = reader.readLine();
                    if (deleteAccount(id, user, password)) {
                        writer.println("success");
                    } else {
                        writer.println("fail");
                    }
                } else if (command.equals("deleteAccountByCredentials")) {
                    //Deleting account using username and password only
                    String username = reader.readLine();
                    String password = reader.readLine();
                    // First, fetch the account using getAccountByUsername
                    Account account = DATABASE.getAccountByUsername(username, password);
                    if (account != null) {
                        // Get the userID from the account
                        String userID = account.getID();
                        // Now delete the account using the userID
                        boolean deleted = DATABASE.deleteAccount(userID, username, password);
                        if (deleted) {
                            writer.println("success");
                            writer.flush();
                        } else {
                            writer.println("fail");
                            writer.flush();
                        }
                    } else {
                        writer.println("fail");
                        writer.flush();
                    }
                } else if (command.equals("getALlConcerts")) {
                    //Getting all concerts
                    ArrayList<String> concerts = getAllConcerts();
                    writer.println(concerts.size());

                    for (int i = 0; i < concerts.size(); i++) {
                        writer.println(concerts.get(i));
                    }
                } else if (command.equals("createConcert")) {
                    //Creating a concert
                    String name = reader.readLine();
                    String date = reader.readLine();
                    String time = reader.readLine();
                    if (createConcert(name, date, time)) {
                        writer.println("success");
                    } else {
                        writer.println("fail");
                    }
                } else if (command.equals("disconnect")) {
                    run = false;
                } else {
                    writer.println("Unknown command: " + command);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
                if (writer != null) {
                    writer.close();
                }

                socket1.close();
                System.out.println("Client disconnected");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //Calls the database
    @Override
    public boolean login(String username, String password) {
        return DATABASE.loginIntoAccount(username, password);
    }

    //Calls the database
    @Override
    public String getTime(String concertID) {
        return DATABASE.getTime(concertID);
    }

    //Calls the database
    @Override
    public ArrayList<String> getAllConcerts() {
        return DATABASE.getAllConcerts();
    }

    //Calls the database
    @Override
    public boolean createConcert(String name, String date, String time) {
        return DATABASE.createConcert(name, date, time);
    }

    //Calls the database
    @Override
    public boolean createAccount(String firstName, String lastName, int age, String username,
                                 String password, String email, String phoneNumber) {
        return DATABASE.createAccount(firstName, lastName, age, username, password, email, phoneNumber);
    }

    //Calls the database
    @Override
    public boolean deleteAccount(String accountID, String username, String password) {
        return DATABASE.deleteAccount(accountID, username, password);
    }

    //Calls the database
    @Override
    public Account getAccount(String accountID, String password) {
        return DATABASE.getAccount(accountID, password);
    }

    //Calls the database
    @Override
    public int createReservation(String username, String password, String showID, List<String> seatIDs,
                                 String date, String time, double totalPrice) {
        Account account = DATABASE.getAccountByUsername(username, password);

        if (account == null) {
            return -1;
        }

        return DATABASE.createReservation(account, showID, seatIDs, date, time, totalPrice);
    }

    //Calls the database
    @Override
    public Account getAccountByUsername(String username, String password) {
        return DATABASE.getAccountByUsername(username, password);
    }

    //Calls the database
    @Override
    public boolean cancelReservation(int reservationID) {
        return DATABASE.cancelReservation(reservationID);
    }

    //Calls the database
    @Override
    public ArrayList<Reservations> getReservationsByAccount(String accountID) {
        return DATABASE.getReservationsByAccount(accountID);
    }

    //Calls the database
    @Override
    public Reservations getReservationByID(int reservationID) {
        return DATABASE.getReservationByID(reservationID);
    }

    //Calls the database
    @Override
    public Seat getSeat(String show, String seatID) {
        return DATABASE.getSeat(show, seatID);
    }

    //Calls the database
    @Override
    public boolean reserveSeat(String show, String seatID) {
        Seat seat = DATABASE.getSeat(show, seatID);

        if (seat != null && seat.isAvailable()) {
            DATABASE.updateSeatAvailability(show, seatID, false);
            return true;
        }

        return false;
    }

    //Calls the database
    @Override
    public boolean cancelSeat(String show, String seatID) {
        Seat seat = DATABASE.getSeat(show, seatID);

        if (seat != null && !seat.isAvailable()) {
            DATABASE.updateSeatAvailability(show, seatID, true);
            return true;
        }

        return false;
    }

    //Calls the database
    @Override
    public ArrayList<Seat> getAvailableSeats(String showID, String date) {
        ArrayList<Seat> allSeats = new ArrayList<>();
        BufferedReader br = null;
        String name = "Concert" + showID;
        try {
            br = new BufferedReader(new FileReader(name));
            String firstLine = br.readLine();
            if (firstLine == null) {
                return allSeats;
            }
            String line;
            while ((line = br.readLine()) != null) {
                Seat seat = new Seat(line);

                if (seat.isAvailable()) {
                    allSeats.add(seat);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return allSeats;
    }

    //Run method
    public void run() {
        handleClient(socket);
    }

    //Starts server
    public static void main(String[] args) {
        Server server = new Server();
        try {
            ServerSocket serverSocket = new ServerSocket(6767);
            System.out.println("Server running on port 6767");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected");

                Thread thread = new Thread(new Server(clientSocket));
                thread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
