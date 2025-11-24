import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * Database class
 *
 * @author Saanvi Verma (verma279), Kunj Arora (arora271),
 * Arav Nair (nair234), and Shalini Murthula (smurthul)
 * @version November 6, 2025
 */
public class Database {
    //Files to store data in
    private static final String ACCOUNTFILE = "accounts.txt";
    private static final String SEATSFILE = "seats.txt";
    private static final String RESERVATIONSFILE = "reservations.txt";
    private static final String CONCERTFILE = "concert.txt";

    private final AtomicInteger nextID = new AtomicInteger(1);

    private File fileA;
    private File fileS;
    private File fileR;
    private File fileC;

    //Thread objects
    private final Object accountO = new Object();
    private final Object seatO = new Object();
    private final Object reservationO = new Object();
    private final Object concertO = new Object();

    //Constructor
    public Database() {
        try {
            fileA = new File(ACCOUNTFILE);
            fileS = new File(SEATSFILE);
            fileR = new File(RESERVATIONSFILE);
            fileC = new File(CONCERTFILE);

            //creates files if they don't exist
            if (!fileA.exists()) {
                fileA.createNewFile();
            }
            if (!fileS.exists()) {
                fileS.createNewFile();
            }
            if (!fileR.exists()) {
                fileR.createNewFile();
            }
            if (!fileC.exists()) {
                fileC.createNewFile();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        int id = 0;
        ArrayList<String> res = new ArrayList<>();
        try (BufferedReader brR = new BufferedReader(new FileReader(fileR))) {
            while (true) {
                String line = brR.readLine();
                if (line == null) {
                    break;
                }
                res.add(line);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < res.size(); i++) {
            Reservations reservations = new Reservations(res.get(i));
            if (reservations.getReservationID() > id) {
                id = reservations.getReservationID();
            }
        }
        nextID.set(id + 1);
    }

    // ACCOUNT METHODS

    //Creating each account
    public boolean createAccount(String firstName, String lastName, int age, String userName,
                                 String password, String email, String phoneNumber) {
        synchronized (accountO) {
            // input validation
            if (firstName == null || firstName.trim().isEmpty() ||
                    lastName == null || lastName.trim().isEmpty() ||
                    userName == null || userName.trim().isEmpty() ||
                    age < 0) { // assuming age validation is also needed
                return false;
            }
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
                bufferedWriter.write(account.writingInFile() + "\n");
            } catch (IOException e) {
                System.out.println("Error adding account " + fileA + e.getMessage());
            }
            return true;
        }

    }

    //Logging into your account
    public boolean loginIntoAccount(String username, String password) {
        synchronized (accountO) {
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

    }

    //Deleting your account
    public boolean deleteAccount(String accountID, String userName, String password) {
        synchronized (accountO) {
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
                if (!(passWord.equals(password)) || !(userName1.equals(userName)) || !(id.equals(accountID))) {
                    newLines.add(lines.get(i));
                } else {
                    boolean isAccountToDelete = userName1.equals(userName) && passWord.equals(password) && id.equals(accountID);

                    if (isAccountToDelete) {
                        found = true;
                    } else {
                        newLines.add(lines.get(i));
                    }
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
    }

    //Getting your Account using ID and Password
    public Account getAccount(String accountID, String password) {
        synchronized (accountO) {
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
    }

    //Helper method for reading reservations from file
    private List<Reservations> readReservationsFromFile() {
        List<Reservations> reservations = new ArrayList<>();
        try (BufferedReader br = new BufferedReader((new FileReader(fileR)))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    reservations.add(new Reservations(line));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading reservations: " + e.getMessage());
        }
        return reservations;
    }

    // RESERVATION METHODS

    //Helper method for writing reservations to file
    private void writeReservationsToFile(List<Reservations> reservations) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileR, false))) {
            for (Reservations reservation : reservations) {
                bw.write(reservation.toString());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing reservations: " + e.getMessage());
        }
    }

    //Creating a reservation using Account object
    public int createReservation(Account account, String showID, List<String> seatIDs,
                                 String date, String time, double totalPrice) {

        synchronized (reservationO) {
            if (account == null) {
                throw new IllegalArgumentException("Account cannot be null when creating a reservation.");
            }

            synchronized (seatO) {
                for (int i = 0; i < seatIDs.size(); i++) {
                    if (!updateSeatAvailability(showID, seatIDs.get(i), false)) {
                        return -1;
                    }
                }
            }

            // Create the reservation object
            int id = nextID.getAndIncrement();
            Reservations reservation = new Reservations(id, account, showID, seatIDs, date, time, totalPrice);

            // Append the reservation to the file
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileR, true))) {
                bw.write(reservation.toString());
                bw.newLine();
            } catch (IOException e) {
                System.out.println("Error adding reservation: " + e.getMessage());
                return -1; // or throw an exception
            }

            // Return the reservation ID as int
            return reservation.getReservationID();
        }
    }


    //Canceling the Reservation
    public boolean cancelReservation(int reservationID) {
        synchronized (reservationO) {
            //Reading in file
            ArrayList<String> lines = new ArrayList<>();
            try (BufferedReader brR = new BufferedReader(new FileReader(fileR))) {
                String line;
                while ((line = brR.readLine()) != null) {
                    if (!line.trim().isEmpty()) {
                        lines.add(line);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            List<String> newLines = new ArrayList<>();
            Reservations cancelledReservation = null;

            //Finding the reservation and adding every other reservation to the newLines
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).trim().isEmpty()) {
                    continue;
                }
                Reservations reservations = new Reservations(lines.get(i));
                if (reservations.getReservationID() == reservationID) {
                    cancelledReservation = reservations;
                } else {
                    newLines.add(lines.get(i));
                }
            }

            //Updating the seats used and added the new lines without the reservation to the file
            if (cancelledReservation != null) {
                for (int i = 0; i < cancelledReservation.getSeatIDs().size(); i++) {
                    //This can cause a deadlock so make sure to always have reservationO then seatO
                    updateSeatAvailability(cancelledReservation.getShowID(), cancelledReservation.getSeatIDs().get(i), true);
                }
                try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileR))) {
                    for (int i = 0; i < newLines.size(); i++) {
                        bufferedWriter.write(newLines.get(i) + "\n");
                    }
                } catch (IOException e) {
                    System.out.println("Error adding reservation " + fileR + e.getMessage());
                }
                return true;
            }
            return false;
        }

    }

    //Updates the availability seat with the seatID to the boolean
    public boolean updateSeatAvailability(String show, String seatID, boolean available) {
        synchronized (seatO) {
            String f = "Concert" + show;
            File file = new File(f);
            if (!file.exists()) {
                file = fileS;
            }
            ArrayList<String> lines = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line = br.readLine();
                while (line != null) {
                    lines.add(line);
                    line = br.readLine();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            boolean seatFound = false;
            for (int i = 1; i < lines.size(); i++) {
                //String[] parts = lines.get(i).split(",");
                Seat seat = new Seat(lines.get(i));
            /*if (parts[0].equals(seatID)) {
                parts[1] = String.valueOf(available);
                lines.set(i, parts[0] + "," + parts[1]);
                break;
            }*/
                if (seat.getSeatID().equals(seatID)) {
                    seat.setAvailable(available);
                    lines.set(i, seat.writingInFile());
                    seatFound = true;
                    break;
                }
            }
            if (!seatFound) {
                return false;
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                for (int i = 0; i < lines.size(); i++) {
                    bw.write(lines.get(i) + "\n");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return true;
        }
    }

    //Getting the reservations by the user account
    public ArrayList<Reservations> getReservationsByAccount(String accountID) {
        synchronized (reservationO) {
            //Reading in the file
            ArrayList<String> lines = new ArrayList<>();
            try (BufferedReader brR = new BufferedReader(new FileReader(fileR))) {
                while (true) {
                    String line = brR.readLine();
                    if (line == null) {
                        break;
                    }
                    if (line.trim().isEmpty()) {
                        continue;
                    }
                    lines.add(line);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            //Searching for the different reservations and returning them
            ArrayList<Reservations> accountReservations = new ArrayList<>();
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).trim().isEmpty()) {
                    continue;
                }
                Reservations reservation = new Reservations(lines.get(i));
                if (reservation.getUserID().equals(accountID)) {
                    accountReservations.add(reservation);
                }
            }
            return accountReservations;
        }

    }

    //Getting your reservation by the ID
    public Reservations getReservationByID(int reservationID) {
        synchronized (reservationO) {
            //Reading in the reservations
            ArrayList<String> lines = new ArrayList<>();
            try (BufferedReader brR = new BufferedReader(new FileReader(fileR))) {
                while (true) {
                    String line = brR.readLine();
                    if (line == null) {
                        break;
                    }
                    if (line.trim().isEmpty()) {
                        continue;
                    }
                    lines.add(line);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            //Comparing reservations IDs and returning the reservation with the same ID
            for (int i = 0; i < lines.size(); i++) {
                if (lines.get(i).trim().isEmpty()) {
                    continue;
                }
                Reservations reservation = new Reservations(lines.get(i));
                if (reservation.getReservationID() == reservationID) {
                    return reservation;
                }
            }
            return null;
        }

    }

    //Getting the users seat by their ID
    public Seat getSeat(String show, String seatID) {
        synchronized (seatO) {
            String name = "Concert" + show;
            File file = new File(name);
            if (!file.exists()) {
                return null;
            }

            ArrayList<String> lines = new ArrayList<>();
            try (BufferedReader brS = new BufferedReader(new FileReader(file))) {
                String header = brS.readLine();
                if (header == null) {
                    return null;
                }
                String line;
                while (true) {
                    line = brS.readLine();
                    if (line == null) {
                        break;
                    }
                    if (line.trim().isEmpty()) {

                    } else {
                        lines.add(line);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (int i = 0; i < lines.size(); i++) {
                Seat seat = new Seat(lines.get(i));
                if (seat.getSeatID().equals(seatID)) {
                    return seat;
                }
            }
            return null;
        }

    }

    public boolean createConcert(String name, String date, String time) {
        synchronized (concertO) {
            ArrayList<String> lines = new ArrayList<>();
            try (BufferedReader bwC = new BufferedReader(new FileReader(fileC))) {
                while (true) {
                    String line = bwC.readLine();
                    if (line == null) {
                        break;
                    }
                    lines.add(line);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            for (int i = 0; i < lines.size(); i++) {
                String[] parts = lines.get(i).split(",");
                if (parts[1].equals(date) && parts[2].equals(time)) {
                    return false;
                }
            }
            int num = lines.size() + 1;
            Concert concert = new Concert(name, date, time, num);

            String nameOfFile = "Concert" + num;
            File file = new File(nameOfFile);

            ArrayList<String> seats = new ArrayList<>();
            try (BufferedWriter bC = new BufferedWriter(new FileWriter(file))) {
                bC.write(concert.getID());
                bC.newLine();
                try (BufferedReader bwS = new BufferedReader(new FileReader(fileS))) {
                    while (true) {
                        String line = bwS.readLine();
                        if (line == null) {
                            break;
                        }
                        seats.add(line);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                for (int i = 0; i < seats.size(); i++) {
                    bC.write(seats.get(i));
                    bC.newLine();
                }
            } catch (IOException e) {
                return false;
            }

            try (BufferedWriter bC = new BufferedWriter(new FileWriter(fileC, true))) {
                bC.write(concert.writingInFile());
                bC.newLine();
            } catch (IOException e) {
                return false;
            }

            return true;
        }
    }

    public ArrayList<String> getAllConcerts() {
        synchronized (concertO) {
            ArrayList<String> concerts = new ArrayList<>();
            String line = "";
            try (BufferedReader brC = new BufferedReader(new FileReader(fileC))) {
                while (true) {
                    line = brC.readLine();
                    if (line == null) {
                        break;
                    }
                    concerts.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return concerts;
        }
    }

    public String getTime(String concertID) {
        synchronized (concertO) {
            ArrayList<String> concerts = new ArrayList<>();
            String line = "";
            try (BufferedReader brC = new BufferedReader(new FileReader(fileC))) {
                while (true) {
                    line = brC.readLine();
                    if (line == null) {
                        break;
                    }
                    concerts.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            String ID = "";
            String time = "";
            for (int i = 0 ; i < concerts.size(); i++) {
                String[] partsOfConcert = concerts.get(i).split(",");
                ID = (partsOfConcert[3]);
                if (concertID.equals(ID)) {
                    time = partsOfConcert[2];
                }
            }
            return time;
        }
    }
}
