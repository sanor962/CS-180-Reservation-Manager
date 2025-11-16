public class Payment implements PaymentInterface {
    private String reservationID;
    private double amount;

    public Payment(String reservationID, double amount) {
        this.reservationID = reservationID;
        this.amount = amount;
    }

    @Override
    public String getReservationID() {
        return reservationID;
    }

    @Override
    public double getAmount() {
        return amount;
    }
}
