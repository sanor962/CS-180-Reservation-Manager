import java.util.ArrayList;
import java.util.List;

/**
 * ServerInterface - Interface for Server class
 *
 * @author Kunj Arora (arora271)
 * @version November 21, 2025
 */

public interface ServerInterface {
    boolean login(String username, String password);
    boolean createAccount(String firstName, String lastName, int age, String username,
                          String password, String email, String phoneNumber);
    boolean deleteAccount(String accountID, String username, String password);
    Account getAccount(String accountID, String password);
    int createReservation(String accountID, String showID, List<String> seatIDs,
                          String date, String time, double totalPrice);
    boolean cancelReservation(int reservationID);
    ArrayList<Reservations> getReservationsByAccount(String accountID);
    Reservations getReservationByID(int reservationID);
    Seat getSeat(String seatID);
    boolean reserveSeat(String seatID);
    boolean cancelSeat(String seatID);
    ArrayList<Seat> getAvailableSeats();
}
