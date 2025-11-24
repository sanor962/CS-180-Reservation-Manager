import java.io.Serializable;

/**
 * Payment - Represents a payment for a reservation including reservation ID and amount.
 * Provides getters to access the reservation ID and payment amount.
 *
 * @author Arav Nair (nair234)
 * @version Nov 16 2025
 */


public class Payment implements PaymentInterface, Serializable {
    private int reservationID;
    private double amount;

    //Constructor
    public Payment(int reservationID, double amount) {
        this.reservationID = reservationID;
        this.amount = amount;
    }

    //Getter methods
    @Override
    public int getReservationID() {
        return reservationID;
    }

    @Override
    public double getAmount() {
        return amount;
    }
}
