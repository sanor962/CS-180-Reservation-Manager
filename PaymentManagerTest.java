import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * PaymentManagerTest - JUnit tests for PaymentManager class.
 * Tests payment processing, refunding, and handling of invalid/duplicate payments.
 *
 * @author Arav Nair (nair234)
 * @version Nov 16 2025
 */


public class PaymentManagerTest {

    @Test
    public void testProcessAndRefund() {
        PaymentManager pm = new PaymentManager();

        // process a payment
        assertTrue(pm.processPayment(1, 50.0));
        assertEquals(50.0, pm.getPaymentAmount(1));

        // cannot pay again for the same reservation
        assertFalse(pm.processPayment(1, 30.0));

        // refund the payment
        assertTrue(pm.refundPayment(1));
        assertEquals(0.0, pm.getPaymentAmount(1));

        // refunding again should fail
        assertFalse(pm.refundPayment(1));
    }

    @Test
    public void testInvalidPayment() {
        PaymentManager pm = new PaymentManager();

        // negative amount
        assertFalse(pm.processPayment(2, -10.0));

        // zero amount
        assertFalse(pm.processPayment(3, 0.0));
    }

    @Test
    public void testMultiplePayments() {
        PaymentManager pm = new PaymentManager();

        assertTrue(pm.processPayment(1, 20.0));
        assertTrue(pm.processPayment(2, 35.0));

        assertEquals(20.0, pm.getPaymentAmount(1));
        assertEquals(35.0, pm.getPaymentAmount(2));

        // refund one reservation
        assertTrue(pm.refundPayment(1));
        assertEquals(0.0, pm.getPaymentAmount(1));
        assertEquals(35.0, pm.getPaymentAmount(2));
    }
}