/**
 * PaymentManagerInterface - Interface defining payment manager operations.
 * Includes methods for processing payments, refunding, and checking amounts.
 *
 * @author Arav Nair (nair234)
 * @version Nov 16 2025
 */


public interface PaymentManagerInterface {
    boolean processPayment(int reservationID, double amount);
    boolean refundPayment(int reservationID);
    double getPaymentAmount(int reservationID);
}
