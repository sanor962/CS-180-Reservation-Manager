import java.io.Serializable;
import java.util.List;

/**
 * Reservations - Implements a reservation with user, show, seat, and pricing details.
 *
 * @author Arav Nair
 * @version Nov 6, 2025
 */

public class Reservations implements ReservationsInterface, Serializable {
    private String reservationID;
    private String userID;
    private String showID;
    private List<String> seatIDs;
    private int numSeats;
    private String date;
    private String time;
    private double totalPrice;

    // Constructor
    public Reservations(String reservationID, String userID, String showID, List<String> seatIDs, String date, String time, double totalPrice) {
        this.reservationID = reservationID;
        this.userID = userID;
        this.showID = showID;
        this.seatIDs = seatIDs;
        this.date = date;
        this.time = time;
        this.totalPrice = totalPrice;
    }

    //Getter Methods
    public String getReservationID() {
        return reservationID;
    }
    public String getUserID() {
        return userID;
    }

    public String getShowID() {
        return showID;
    }

    public List<String> getSeatIDs() {
        return seatIDs;
    }

    public int getNumSeats() {
        return numSeats;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    //Setter Methods
    public void setUserID(String userID) {
        this.userID = userID;
    }
    public void setShowID(String showID) {
        this.showID = showID;
    }

    public void setSeatIDs(List<String> seatIDs) {
        this.seatIDs = seatIDs;
    }

    public void setNumSeats(int numSeats) {
        this.numSeats = numSeats;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    //toString
    @Override
    public String toString() {
        return "Reservation{" +
                "reservationID=\'" + reservationID + "\'" +
                ", userID=\'" + userID + "\'" +
                ", showID=\'" + showID + "\'" +
                ", seatIDs=" + seatIDs +
                ", numSeats=" + numSeats +
                ", date=\'" + date + "\'" +
                ", time=\'" + time + "\'" +
                ", totalPrice=" + totalPrice +
                "}";
    }
}
