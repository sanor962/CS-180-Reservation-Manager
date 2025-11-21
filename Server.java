import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * The Server class handles all client connections
 *
 * Port Number: 6767
 *
 * @author Kunj Arora (arora271)
 * @version November 21, 2025
 */


public class Server implements ServerInterface {
    private Database database;

    public Server() {
        database = new Database();
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
                        writer.println(accountID);
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
                    }
                } else if (command.equals("getAvailableSeats")) {
                    ArrayList<Seat> seats = getAvailableSeats();
                    writer.println(seats.size());
                    for (Seat seat : seats) {
                        writer.println(seat.writingInFile());
                    }
                } else if (command.equals("makeReservation")) {
                    String user = reader.readLine();
                    String showID = reader.readLine();
                    int numSeats = Integer.parseInt(reader.readLine());
                    List<String> seatIDs = new ArrayList<>();
                    for (int i = 0; i < numSeats; i++) seatIDs.add(reader.readLine());
                    String date = reader.readLine();
                    double totalPrice = Double.parseDouble(reader.readLine());

                    int reservationID = createReservation(user, showID, seatIDs, date, "", totalPrice);
                    if (reservationID != -1) {
                        writer.println("success");
                        writer.println(reservationID);
                    } else {
                        writer.println("fail");
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
                    for (Reservations r : reservations) {
                        writer.println(r.toString());
                    }
                } else if (command.equals("deleteAccount")) {
                    String user = reader.readLine();
                    String password = reader.readLine();
                    String id = reader.readLine();
                    if (deleteAccount(id, user, password)) {
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
    public int createReservation(String accountID, String showID, List<String> seatIDs,
                                 String date, String time, double totalPrice) {
        Account account = database.getAccount(accountID, "");

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
    public ArrayList<Seat> getAvailableSeats() {
        ArrayList<Seat> allSeats = new ArrayList<>();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("seats.txt"));
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

    public static void main(String[] args) {
        Server server = new Server();
        try {
            ServerSocket serverSocket = new ServerSocket(6767);
            System.out.println("Server running on port 6767...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());
                new Thread(() -> server.handleClient(clientSocket)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
