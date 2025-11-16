public class Payment implements PaymentInterface {
    private int reservationID;
    private double amount;

    public Payment(int reservationID, double amount) {
        this.reservationID = reservationID;
        this.amount = amount;
    }

    @Override
    public int getReservationID() {
        return reservationID;
    }

    @Override
    public double getAmount() {
        return amount;
    }
}
