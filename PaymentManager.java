import java.util.ArrayList;
import java.util.List;

/**
 * PaymentManager - Manages processing and refunding payments for reservations.
 * Maintains a list of payments and allows querying payment amounts.
 *
 * @author Arav Nair (nair234)
 * @version Nov 16 2025
 */


public class PaymentManager implements PaymentManagerInterface {

    private List<Payment> payments;

    public PaymentManager() {
        payments = new ArrayList<>();
    }

    //Processing the payment
    @Override
    public boolean processPayment(int reservationID, double amount) {
        if (amount <= 0 || getPaymentAmount(reservationID) > 0) {
            return false; // already paid or invalid amount
        }
        payments.add(new Payment(reservationID, amount));
        return true;
    }

    //Refunding the payment
    @Override
    public boolean refundPayment(int reservationID) {
        for (int i = 0; i < payments.size(); i++) {
            if (payments.get(i).getReservationID() == reservationID) {
                payments.remove(i);
                return true;
            }
        }
        return false; // no payment found
    }

    //Getting the full payment amount
    @Override
    public double getPaymentAmount(int reservationID) {
        for (Payment p : payments) {
            if (p.getReservationID() == reservationID) {
                return p.getAmount();
            }
        }
        return 0.0; // not paid
    }
}
