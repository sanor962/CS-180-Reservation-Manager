public interface PaymentManagerInterface {
    boolean processPayment(int reservationID, double amount);
    boolean refundPayment(int reservationID);
    double getPaymentAmount(int reservationID);
}
