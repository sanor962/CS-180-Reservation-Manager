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
    String createReservation(String accountID, String showID, List<String> seatIDs, String date, String time, double totalPrice);
    boolean cancelReservation(String reservationID);
    ArrayList<Reservations> getReservationsByAccount(String accountID);
    Reservations getReservationByID(String reservationID);
    public Seat getSeat(String seatID);
    boolean createConcert(String name, String date, String time);
    ArrayList<String> getAllConcerts();
    String getTime(String concertID);
}
