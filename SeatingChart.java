import java.util.ArrayList;
import java.util.List;

/**
 * SeatingChart - Manages all the seats in a layout
 *
 * @author Kunj Arora (arora271)
 * @version November 8, 2025
 */

public class SeatingChart implements SeatingChartInterface {
    private List<Seat> seats;

    public SeatingChart() {
        this.seats = new ArrayList<>();
    }

    public void addSeat(Seat seat) {
        seats.add(seat);
    }

    public Seat getSeat(String seatID) {
        for (int i = 0; i < seats.size(); i++) {
            if (seats.get(i).getSeatID().equals(seatID)) {
                return seats.get(i);
            }
        }

        return null;
    }

    public boolean reserveSeat(String seatID) {
        Seat seat = getSeat(seatID);
        if (seat.isAvailable()) {
            seat.setAvailable(false);
            return true;
        }

        return false;
    }

    public boolean cancelSeat(String seatID) {
        Seat seat = getSeat(seatID);
        if (!seat.isAvailable()) {
            seat.setAvailable(true);
            return true;
        }

        return false;
    }

    public List<Seat> getAllSeats() {
        return seats;
    }

    public List<Seat> getAvailableSeats() {
        List<Seat> availableSeats = new ArrayList<>();
        for (int i = 0; i < seats.size(); i++) {
            if (seats.get(i).isAvailable()) {
                availableSeats.add(seats.get(i));
            }
        }

        return availableSeats;
    }
}
