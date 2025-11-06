import java.io.*;
import java.lang.reflect.Array;
import java.util.*;

/**
 * Database class
 *
 * @author Saanvi Verma
 * @version 11/6/2025
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
    private final Object accountO = new Object();
    private final Object seatO = new Object();
    private final Object reservationO = new Object();

    //Constructor
    public Database() {
        try {
            fileA = new File(accountFile);
            fileS = new File(seatsFile);
            fileR = new File(reservationFile);

            /*if (fileA.exists()) {
                BufferedWriter bwA = new BufferedWriter(new FileWriter(accountFile));
            } else {
                BufferedWriter bwA = new BufferedWriter(new FileWriter("accounts.txt"));
            }
            if (fileS.exists()) {
                BufferedWriter bwS = new BufferedWriter(new FileWriter(accountFile));
            }
            if (fileR.exists()) {
                BufferedWriter bwR = new BufferedWriter(new FileWriter(accountFile));
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean createAccount(String firstName, String lastName, int age, String userName, String password, String email, String phoneNumber) {
        synchronized (accountO) {
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

            // Create new user and add to the file
            Account account = new Account(firstName, lastName, age, userName, password, email, phoneNumber);
            account.createID();
            try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileA, true))) {
                bufferedWriter.write(account.toString() + "\n");
            } catch (IOException e) {
                System.out.println("Error adding account " + fileA + e.getMessage());
            }
            return true;
        }
    }

    public boolean loginIntoAccount(String username, String password) {
        synchronized (accountO) {
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
            for (int i = 0; i < lines.size(); i++) {
                /*String userName1 = lines.get(i).substring(lines.get(i).indexOf(",") + 1);
                String passWord = userName1.substring(userName1.indexOf(",") + 1);
                passWord = passWord.substring(0, passWord.indexOf(","));
                userName1 = userName1.substring(0, userName1.indexOf(","));*/
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

    public boolean deleteUser(String accountID, String userName, String password) {
        synchronized (accountO) {
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

            for (int i = 0; i < lines.size(); i++) {
                /*String id = lines.get(i).substring(0, lines.get(i).indexOf(","));
                String userName1 = lines.get(i).substring(lines.get(i).indexOf(",") + 1);
                String passWord = userName1.substring(userName1.indexOf(",") + 1);
                passWord = passWord.substring(0, passWord.indexOf(","));
                userName1 = userName1.substring(0, userName1.indexOf(","));*/
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

    public Account getAccount(String accountID, String password) {
        synchronized (accountO) {
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
            
            for (int i = 0; i < lines.size(); i++) {
                Account account = Account.fromFile(lines.get(i));
                if (account.getUserID().equals(accountID) && account.getPassword().equals(password)) {
                    return account;
                }
            }
            return null;
        }
    }
}
