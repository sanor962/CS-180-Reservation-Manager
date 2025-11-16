/**
 * PaymentInterface - Interface defining required methods for a Payment.
 * Ensures each payment can return its reservation ID and amount.
 *
 * @author Arav Nair (nair234)
 * @version Nov 16 2025
 */


public interface PaymentInterface {
    int getReservationID();
    double getAmount();
}
