import java.util.ArrayList;
import java.util.List;

public class PaymentManager implements PaymentManagerInterface {

    private List<Payment> payments;

    public PaymentManager() {
        payments = new ArrayList<>();
    }

    @Override
    public boolean processPayment(int reservationID, double amount) {
        if (amount <= 0 || getPaymentAmount(reservationID) > 0) {
            return false; // already paid or invalid amount
        }
        payments.add(new Payment(reservationID, amount));
        return true;
    }

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
