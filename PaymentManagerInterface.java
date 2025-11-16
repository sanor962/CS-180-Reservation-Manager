public interface PaymentManagerInterface {
    boolean processPayment(String reservationID, double amount);
    boolean refundPayment(String reservationID);
    double getPaymentAmount(String reservationID);
}
