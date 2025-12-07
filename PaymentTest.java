import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test cases for the Payment class.
 * Ensures that reservation ID and amount are stored and returned correctly.
 *
 * @author Arav Nair (nair234)
 * @version Nov 22 2025
 */
public class PaymentTest {

    //Testing constructors and getters
    @Test
    public void testPaymentConstructorAndGetters() {
        int reservationID = 42;
        double amount = 75.50;

        Payment payment = new Payment(reservationID, amount);

        assertEquals(reservationID, payment.getReservationID(),
                "Reservation ID should match the value passed to constructor.");

        assertEquals(amount, payment.getAmount(),
                "Amount should match the value passed to constructor.");
    }

    //Payment of 0 value
    @Test
    public void testZeroAmount() {
        Payment payment = new Payment(10, 0.0);

        assertEquals(0.0, payment.getAmount(),
                "Zero amount should be stored correctly.");
    }

    @Test
    public void testNegativeAmountAllowedInModel() {
        // Payment validation happens in PaymentManager, so all values are stored.
        Payment payment = new Payment(100, -5.0);

        assertEquals(-5.0, payment.getAmount(),
                "Payment class should store negative values because validation is in PaymentManager.");
    }

    //Making sure the ID was stored correctly
    @Test
    public void testReservationIDStoredCorrectly() {
        Payment payment = new Payment(999, 12.34);

        assertEquals(999, payment.getReservationID(),
                "Reservation ID must be stored and returned correctly.");
    }
}

