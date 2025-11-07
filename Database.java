import java.io.*;
import java.util.*;

/**
 * Database class
 *
 * @author Saanvi Verma
 * @version November 6, 2025
 */

public class Database {
    //Files to store data in
    private static final String accountFile = "accounts.txt";
    private static final String seatsFile = "seats.txt";
    private static final String reservationFile = "reservations.txt";

    private File fileA;
    private File fileS;
    private File fileR;

    //Thread objects
    //private final Object accountO = new Object();
    //private final Object seatO = new Object();
    //private final Object reservationO = new Object();

    //Constructor
    public Database() {
        try {
            fileA = new File(accountFile);
            fileS = new File(seatsFile);
            fileR = new File(reservationFile);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Creating each account
    public boolean createAccount(String firstName, String lastName, int age, String userName, String password, String email, String phoneNumber) {
        //Making sure the username doesn't exist
        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader bwA = new BufferedReader(new FileReader(fileA))) {
            while (true) {
                String line = bwA.readLine();
                if (line == null) {
                    break;
                }
                lines.add(line);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < lines.size(); i++) {
            String[] lines1 = lines.get(i).split(",");
            String userName1 = lines1[3];
            if (userName1.equals(userName)) {
                return false;
            }
        }

        //Creating a new account and adding to the file
        Account account = new Account(firstName, lastName, age, userName, password, email, phoneNumber);
        account.createID();
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileA, true))) {
            bufferedWriter.write(account.toString() + "\n");
        } catch (IOException e) {
            System.out.println("Error adding account " + fileA + e.getMessage());
        }
        return true;
    }

    //Logging into your account
    public boolean loginIntoAccount(String username, String password) {
        //Reading the file
        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader brA = new BufferedReader(new FileReader(fileA))) {
            while (true) {
                String line = brA.readLine();
                if (line == null) {
                    break;
                }
                lines.add(line);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        //Checking and logging the user into their account
        for (int i = 0; i < lines.size(); i++) {
            String[] lines1 = lines.get(i).split(",");
            String userName1 = lines1[3];
            String passWord = lines1[4];
            if (userName1.equals(username) && passWord.equals(password)) {
                return true;
            }
        }
        return false;
    }

    //Deleting your account
    public boolean deleteAccount(String accountID, String userName, String password) {
        //Reading the file
        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader brA = new BufferedReader(new FileReader(fileA))) {
            while (true) {
                String line = brA.readLine();
                if (line == null) {
                    break;
                }
                lines.add(line);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        ArrayList<String> newLines = new ArrayList<>();
        boolean found = false;

        //Finding the account and adding every other account to the newLines
        for (int i = 0; i < lines.size(); i++) {
            String[] lines1 = lines.get(i).split(",");
            String userName1 = lines1[3];
            String passWord = lines1[4];
            String id = lines1[7];
            if (!(passWord.equals(password)) && !(userName1.equals(userName)) && !(id.equals(accountID))) {
                newLines.add(lines.get(i));
            } else {
                found = true;
            }
        }

        //Replacing the lines with the new lines (without the account)
        if (found) {
            try (BufferedWriter bwA = new BufferedWriter(new FileWriter(fileA))) {
                for (int i = 0; i < newLines.size(); i++) {
                    bwA.write(newLines.get(i) + "\n");
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return true;
        }
        return false;
    }

    //Getting your Account using ID and Password
    public Account getAccount(String accountID, String password) {
        //Reading the file
        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader brA = new BufferedReader(new FileReader(fileA))) {
            while (true) {
                String line = brA.readLine();
                if (line == null) {
                    break;
                }
                lines.add(line);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //Returning the account
        for (int i = 0; i < lines.size(); i++) {
            Account account = new Account(lines.get(i));
            if (account.getID().equals(accountID) && account.getPassword().equals(password)) {
                return account;
            }
        }
        return null;
    }

    //Creating a reservation
    public String createReservation(String accountID, String eventDate, String eventTime, ArrayList<String> seatIDs, ArrayList<Integer> whatNumbers, double totalPrice) {
        //Checking for available seats
        //Assuming getSeatID(), isAvailable(), and the constructor takes that in because I don't have the Seat class yet
        ArrayList<String> availableSeats = new ArrayList<>();
        for (int i = 0; i < seatIDs.size(); i++) {
            Seat seat = new Seat(seatIDs.get(i));
            if (seat.isAvailable()) {
                availableSeats.add(seatIDs.get(i).getSeatID());
            }
        }

        //Reading in all the reservations
        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader brR = new BufferedReader(new FileReader(fileR))) {
            while (true) {
                String line = brR.readLine();
                if (line == null) {
                    break;
                }
                lines.add(line);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //Finding max id
        int max = 0;
        for (int i = 0; i < lines.size(); i++) {
            Reservations reservation = new Reservations(lines.get(i));
            if (Integer.parseInt(reservation.getReservationID()) > max) {
                max = Integer.parseInt(reservation.getReservationID());
            }
        }
        max = max + 1;
        String reservationID = max + "";

        //Creating a reservation and adding it into the reservations file
        Reservations reservation = new Reservations(reservationID, accountID, eventTime, availableSeats, eventDate, eventTime, totalPrice);
        reservation.setNumSeats(seatIDs.size());
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileR, true))) {
            bufferedWriter.write(reservation.toString() + "\n");
        } catch (IOException e) {
            System.out.println("Error adding reservation " + fileR + e.getMessage());
        }
        return reservationID;
    }

    //Canceling the Reservation
    public boolean cancelReservation(String reservationID) {
        //Reading in file
        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader brR = new BufferedReader(new FileReader(fileR))) {
            while (true) {
                String line = brR.readLine();
                if (line == null) {
                    break;
                }
                lines.add(line);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        List<String> newLines = new ArrayList<>();
        Reservations cancelledReservation = null;

        //Finding the reservation and adding every other reservation to the newLines
        for (int i = 0; i < lines.size(); i++) {
            Reservations reservations = new Reservations(lines.get(i));
            if (reservations.getReservationID().equals(reservationID)) {
                cancelledReservation = reservations;
            } else {
                newLines.add(lines.get(i));
            }
        }

        //Updating the seats used and added the new lines without the reservation to the file
        //I'm currently assuming that updateSeatAvailability() exists at the moment waiting for Arav to write that
        if (!(cancelledReservation == null)) {
            for (int i = 0; i < cancelledReservation.getSeatIDs().size(); i++) {
                updateSeatAvailability(cancelledReservation.getSeatIDs().get(i), true);
            }
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileR, true))) {
                for (int i = 0; i < newLines.size(); i++) {
                    bufferedWriter.write(newLines + "\n");
                }
            } catch (IOException e) {
                System.out.println("Error adding reservation " + fileR + e.getMessage());
            }
            return true;
        }
        return false;
    }

    //Getting the reservations by the user account
    public ArrayList<Reservations> getReservationsByAccount(String accountID) {
        //Reading in the file
        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader brR = new BufferedReader(new FileReader(fileR))) {
            while (true) {
                String line = brR.readLine();
                if (line == null) {
                    break;
                }
                lines.add(line);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        
        //Searching for the different reservations and returning them
        ArrayList<Reservations> accountReservations = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            Reservations reservation = new Reservations(lines.get(i));
            if (reservation.getUserID().equals(accountID)) {
                accountReservations.add(reservation);
            }
        }
        return accountReservations;
    }

    //Getting your reservation by the ID
    public Reservations getReservationByID(String reservationID) {
        //Reading in the reservations
        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader brR = new BufferedReader(new FileReader(fileR))) {
            while (true) {
                String line = brR.readLine();
                if (line == null) {
                    break;
                }
                lines.add(line);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //Comparing reservations IDs and returning the reservation with the same ID
        for (int i = 0; i < lines.size(); i++) {
            Reservations reservation = new Reservations(lines.get(i));
            if (reservation.getReservationID().equals(reservationID)) {
                return reservation;
            }
        }
        return null;
    }

}
