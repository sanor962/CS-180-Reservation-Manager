/**
 * SeatInterface - Interface for Seating class
 *
 * @author Kunj Arora
 * @version November 7, 2025
 */

public interface SeatInterface {
    String getSeatID();
    boolean isAvailable();
    double getPrice();
    String getRow();
    int getNumber();
    void setAvailable(boolean available);
    void setPrice(double price);
}
