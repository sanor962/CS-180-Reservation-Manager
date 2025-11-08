import java.util.List;

/**
 * SeatingChartInterface - Interface for SeatingChart class
 *
 * @author Kunj Arora
 * @version November 7, 2025
 */

public interface SeatingChartInterface {
    Seat getSeat(String seatID);
    boolean reserveSeat(String seatID);
    boolean cancelSeat(String seatID);
    List<Seat> getAllSeats();
    List<Seat> getAvailableSeats();

}
