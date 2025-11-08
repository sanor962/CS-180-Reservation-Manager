import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Reservations - Implements a reservation with user, show, seat, and pricing details.
 *
 * @author Arav Nair and Saanvi Verma
 * @version Nov 6, 2025
 */

public class Reservations implements ReservationsInterface, Serializable {
    // Reservation fields
    private String reservationID;
    private String userID;
    private String showID;
    private List<String> seatIDs;
    private int numSeats;
    private String date;
    private String time;
    private double totalPrice;


    // Main Constructor
    public Reservations(String reservationID, String userID, String showID, List<String> seatIDs, String date, String time, double totalPrice) {
        //Assigns fields with null-checking
        this.reservationID = reservationID != null ? reservationID : "";
        this.userID = userID  != null ? userID : "";
        this.showID = showID  != null ? showID : "";
        this.seatIDs = seatIDs != null ? new ArrayList<>(seatIDs) : new ArrayList<>();
        this.numSeats = this.seatIDs.size(); //sync numSeats with size of seatIDs arraylist
        this.date = date != null ? date : "";
        this.time = time  != null ? time : "";
        this.totalPrice = totalPrice;
    }

    //Constructor that takes in from the line from the file
    public Reservations(String line) {

        //empty/null line handling
        if (line == null || line.isBlank()) {
            this.reservationID = "";
            this.userID = "";
            this.showID = "";
            this.seatIDs = new ArrayList<>();
            this.numSeats = 0;
            this.date = "";
            this.time = "";
            this.totalPrice = 0.0;
            return;
        }

        String[] parts= line.split(",", -1); // -1 keeps empty trailing fields

        //Assign fields if parts exist
        this.reservationID = parts.length > 0 && parts[0] != null ? parts[0] : "";
        this.userID = parts.length > 1 && parts[1] != null ? parts[1] : "";
        this.showID = parts.length > 2 && parts[2] != null ? parts[2] : "";

        //Handles seat IDs by splitting by | if not empty and making an empty list otherwise
        if (parts.length > 3 && parts[3] != null && !parts[3].isEmpty()) {
            this.seatIDs = new ArrayList<>(Arrays.asList(parts[3].split("\\|")));
        } else {
            this.seatIDs = new ArrayList<>();
        }
        this.numSeats = this.seatIDs.size();

        //Assign date and time if they exist
        this.date = parts.length > 4 && parts[4] != null ? parts[4] : "";
        this.time = parts.length > 5 && parts[5] != null ? parts[5] : "";

        //totalPrice defaults to 0 if missing or invalid
        if (parts.length > 6) {
            try {
                this.totalPrice = Double.parseDouble(parts[6]);
            } catch (NumberFormatException e) {
                this.totalPrice = 0.0;
            }
        } else {
            this.totalPrice = 0.0;
            }
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
        this.numSeats = seatIDs.size(); //updates numSeats to equal size of seatIDs arraylist
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
        String seatsJoined = String.join("|", seatIDs);
        return reservationID + "," +
                userID + "," +
                showID + "," +
                seatsJoined + "," +
                date + "," +
                time + "," +
                totalPrice;
    }
}
