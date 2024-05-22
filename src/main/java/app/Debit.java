package app;

public class Debit implements Payment {
    public static final double MINIMUM_TOTAL_PRICE = 50000;

    @Override
    public long processPayment(long amount) {
        if (amount < MINIMUM_TOTAL_PRICE) {
            return -1;
        }
        return amount;
    }
}
