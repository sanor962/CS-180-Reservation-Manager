import java.util.List;

/**
 * ReservationsInterface - Defines methods for accessing and modifying reservation data.
 *
 * @author Arav Nair
 * @version November 6, 2025
 */

public interface ReservationsInterface {
    int getReservationID();
    String getUserID();
    String getShowID();
    List<String> getSeatIDs();
    int getNumSeats();
    String getDate();
    String getTime();
    double getTotalPrice();
    void setUserID(String userID);
    void setShowID(String showID);
    void setSeatIDs(List<String> seatIDs);
    void setDate(String date);
    void setTime(String time);
    void setTotalPrice(double totalPrice);
}