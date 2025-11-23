import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
/**
 * The Server class handles all client connections
 * Port Number: 6767
 *
 * @author Kunj Arora (arora271), Saanvi Verma (verma279), and Shalini Murthula (smurthul)
 * @version November 21, 2025
 */
public class Server implements ServerInterface, Runnable {
    private final Database database;
    private Socket socket;
    private PaymentManager paymentManager = new PaymentManager();

    public Server() {
        database = new Database();
    }

    public Server(Socket socket) {
        this.socket = socket;
        this.database = new Database();
        this.paymentManager = new PaymentManager();
    }


    private void handleClient(Socket socket) {
        BufferedReader reader = null;
        PrintWriter writer = null;

        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);

            boolean run = true;
            String accountID = null;

            while (run) {
                String command = reader.readLine();
                if (command == null) {
                    break;
                }

                if (command.equals("login")) {
                    String username = reader.readLine();
                    String password = reader.readLine();
                    if (login(username, password)) {
                        accountID = username;
                        writer.println("success");
                    } else {
                        writer.println("fail");
                    }
                } else if (command.equals("createAccount")) {
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
                    //Need to figure out what tf we are doing for this
                    String showID = reader.readLine();
                    String date = reader.readLine();
                    ArrayList<Seat> seats = getAvailableSeats(showID, date);
                    writer.println(seats.size());

                    for (int i = 0; i < seats.size(); i++) {
                        writer.println(seats.get(i).writingInFile());
                    }
                } else if (command.equals("makeReservation")) {
                    String user = reader.readLine();
                    String password = reader.readLine();
                    String showID = reader.readLine();
                    int numSeats = Integer.parseInt(reader.readLine());
                    List<String> seatIDs = new ArrayList<>();

                    for (int i = 0; i < numSeats; i++) {
                        seatIDs.add(reader.readLine());
                    }

                    String date = reader.readLine();
                    double totalPrice = 0;
                    for (int i = 0; i < seatIDs.size(); i++) {
                        Seat s = database.getSeat(seatIDs.get(i));
                        if (s == null || !s.isAvailable()) {
                            writer.println("Seat " + seatIDs.get(i) + " is unavailable.");
                            return;
                        }
                        totalPrice = totalPrice + s.getPrice();
                    }
                    writer.println(totalPrice);

                    String payCommand = reader.readLine();
                    if (!"pay".equals(payCommand)) {
                        writer.println("cancelled");
                        return;
                    }

                    String time = getTime(showID);

                    int reservationID = createReservation(user, password, showID, seatIDs, date, time, totalPrice);
                    boolean success = paymentManager.processPayment(reservationID, totalPrice);

                    if (!success) {
                        writer.println("fail");
                        return;
                    }

                    if (reservationID != -1) {
                        writer.println("success");
                        writer.println(reservationID);
                    } else {
                        writer.println("fail");
                    }

                    for (String seatID : seatIDs) {
                        database.updateSeatAvailability(seatID, false);
                    }
                } else if (command.equals("cancelReservation")) {
                    int reservationID = Integer.parseInt(reader.readLine());
                    if (cancelReservation(reservationID)) {
                        writer.println("success");
                    } else {
                        writer.println("fail");
                    }
                } else if (command.equals("getReservations")) {
                    String user = reader.readLine();
                    ArrayList<Reservations> reservations = getReservationsByAccount(user);
                    writer.println(reservations.size());

                    for (int i =0; i < reservations.size(); i++) {
                        writer.println(reservations.get(i).toString());
                    }
                } else if (command.equals("deleteAccount")) {
                    String id = reader.readLine();
                    String password = reader.readLine();
                    String user = reader.readLine();
                    if (deleteAccount(id, user, password)) {
                        writer.println("success");
                    } else {
                        writer.println("fail");
                    }
                } else if (command.equals("getALlConcerts")) {
                    ArrayList<String> concerts = getAllConcerts();
                    writer.println(concerts.size());

                    for (int i = 0; i < concerts.size(); i++) {
                        writer.println(concerts.get(i));
                    }
                } else if (command.equals("createConcert")) {
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

                socket.close();
                System.out.println("Client disconnected");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean login(String username, String password) {
        return database.loginIntoAccount(username, password);
    }

    @Override
    public String getTime(String concertID) {
        return database.getTime(concertID);
    }

    @Override
    public ArrayList<String> getAllConcerts() {
        return database.getAllConcerts();
    }

    @Override
    public boolean createConcert(String name, String date, String time) {
        return database.createConcert(name, date, time);
    }

    @Override
    public boolean createAccount(String firstName, String lastName, int age, String username,
                                 String password, String email, String phoneNumber) {
        return database.createAccount(firstName, lastName, age, username, password, email, phoneNumber);
    }

    @Override
    public boolean deleteAccount(String accountID, String username, String password) {
        return database.deleteAccount(accountID, username, password);
    }

    @Override
    public Account getAccount(String accountID, String password) {
        return database.getAccount(accountID, password);
    }

    @Override
    public int createReservation(String accountID, String password, String showID, List<String> seatIDs,
                                 String date, String time, double totalPrice) {
        Account account = database.getAccount(accountID, password);

        if (account == null) {
            return -1;
        }

        return database.createReservation(account, showID, seatIDs, date, time, totalPrice);
    }

    @Override
    public boolean cancelReservation(int reservationID) {
        return database.cancelReservation(reservationID);
    }

    @Override
    public ArrayList<Reservations> getReservationsByAccount(String accountID) {
        return database.getReservationsByAccount(accountID);
    }

    @Override
    public Reservations getReservationByID(int reservationID) {
        return database.getReservationByID(reservationID);
    }

    @Override
    public Seat getSeat(String seatID) {
        return database.getSeat(seatID);
    }

    @Override
    public boolean reserveSeat(String seatID) {
        Seat seat = database.getSeat(seatID);

        if (seat != null && seat.isAvailable()) {
            database.updateSeatAvailability(seatID, false);
            return true;
        }

        return false;
    }

    @Override
    public boolean cancelSeat(String seatID) {
        Seat seat = database.getSeat(seatID);

        if (seat != null && !seat.isAvailable()) {
            database.updateSeatAvailability(seatID, true);
            return true;
        }

        return false;
    }

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

    public void run() {
        handleClient(socket);
    }

    public static void main(String[] args) {
        Server server = new Server();
        try {
            ServerSocket serverSocket = new ServerSocket(6767);
            System.out.println("Server running on port 6767...");

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
