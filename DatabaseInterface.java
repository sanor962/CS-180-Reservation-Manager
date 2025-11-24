import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseInterface - Interface for Database class
 *
 * @author Saanvi Verma (verma279)
 * @version 11/6/2025
 */
public interface DatabaseInterface {
    boolean createAccount(String firstName, String lastName, int age, String userName, String password, String email, String phoneNumber);
    boolean loginIntoAccount(String username, String password);
    boolean deleteAccount(String accountID, String userName, String password);
    Account getAccount(String accountID, String password);
    int createReservation(Account account, String showID, List<String> seatIDs, String date, String time, double totalPrice);
    boolean cancelReservation(int reservationID);
    ArrayList<Reservations> getReservationsByAccount(String accountID);
    Reservations getReservationByID(int reservationID);
    Seat getSeat(String show, String seatID);
    boolean createConcert(String name, String date, String time);
    ArrayList<String> getAllConcerts();
    String getTime(String concertID);
    boolean updateSeatAvailability(String show, String seatID, boolean available);
    Account getAccountByUsername(String username, String password);
}
