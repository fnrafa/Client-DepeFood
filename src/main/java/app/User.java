package app;

import java.util.ArrayList;

public class User {
    private final String name;
    private final String phoneNumber;
    private String email;
    private final String location;
    private final String role;
    private final ArrayList<Order> orderHistory;
    private final Payment payment;

    private long balance;

    public User(String name, String phoneNumber, String email, String location, String role, long balance, Payment payment) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.location = location;
        this.role = role;
        orderHistory = new ArrayList<>();
        this.balance = balance;
        this.payment = payment;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getRole() {
        return role;
    }

    public long getBalance() {
        return balance;
    }

    public String getLocation() {
        return location;
    }

    public void addOrder(Order orderHistory) {
        this.orderHistory.add(orderHistory);
    }

    public ArrayList<Order> getOrderHistory() {
        return orderHistory;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setBalance(long balance) {
        this.balance = balance;
    }
}
