package app;

public class CreditCard implements Payment {
    static final long TRANSACTION_FEE_PERCENTAGE = 2;

    public static long countTransactionFee(long amount) {
        return (amount * TRANSACTION_FEE_PERCENTAGE / 100);
    }

    @Override
    public long processPayment(long amount) {
        return amount + countTransactionFee(amount);
    }
}
