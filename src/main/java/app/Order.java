package app;

public class Order {
    private final String orderId;
    private final String date;
    private final int shippingPrice;
    private final Restaurant restaurant;
    private boolean isFinished;
    private final Menu[] menus;

    public Order(String orderId, String date, int shippingPrice, Restaurant restaurant, Menu[] menus) {
        this.orderId = orderId;
        this.date = date;
        this.shippingPrice = shippingPrice;
        this.restaurant = restaurant;
        this.isFinished = false;
        this.menus = menus;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getDate() {
        return date;
    }

    public int getShippingPrice() {
        return shippingPrice;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public Menu[] getMenus() {
        return menus;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public static String generateOrderID(String namaRestoran, String tanggalOrder, String noTelepon) {

        String restaurantCode = getRestaurantCode(namaRestoran);
        String formattedDate = formatDate(tanggalOrder);
        String phoneNumberChecksum = getPhoneNumberChecksum(noTelepon);

        String id = restaurantCode + formattedDate + phoneNumberChecksum;
        String checksum = calculateChecksum(id);

        return id + checksum;
    }

    public static String calculateChecksum(String id) {
        int sumEven = 0;
        int sumOdd = 0;

        for (int i = 0; i < id.length(); i++) {
            char c = id.charAt(i);
            int numericValue = getNumericValue(c);
            if (i % 2 == 0) {
                sumEven += numericValue;
            } else {
                sumOdd += numericValue;
            }
        }
        int remainderEven = sumEven % 36;
        int remainderOdd = sumOdd % 36;
        return reverseAssignment(remainderEven) + reverseAssignment(remainderOdd);
    }

    public static int getNumericValue(char c) {
        if (Character.isDigit(c)) {
            return Character.getNumericValue(c);
        } else {
            return c - 'A' + 10;
        }
    }

    public static String reverseAssignment(int remainder) {
        String code39CharacterSet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        return String.valueOf(code39CharacterSet.charAt(remainder));
    }

    public static String getRestaurantCode(String restaurantName) {
        String[] words = restaurantName.split(" ");

        StringBuilder code = new StringBuilder();

        for (String word : words) {
            code.append(word);
        }

        return code.substring(0, Math.min(code.length(), 4));
    }

    public static String formatDate(String date) {
        String[] parts = date.split("-");
        String year = parts[0];
        String month = parts[1];
        String day = parts[2];

        return day + month + year;
    }

    public static String getPhoneNumberChecksum(String phoneNumber) {
        int sum = 0;
        for (char c : phoneNumber.toCharArray()) {
            if (Character.isDigit(c)) {
                sum += Character.getNumericValue(c);
            }
        }
        int checksum = sum % 100;
        return (checksum < 10) ? STR."0\{checksum}" : String.valueOf(checksum);
    }

    public static int calculateDeliveryCost(String location) {
        return switch (location) {
            case "P" -> 10000;
            case "U" -> 20000;
            case "T" -> 35000;
            case "S" -> 40000;
            case "B" -> 60000;
            default -> 0;
        };
    }

    public double getTotal() {
        double sum = 0;
        for (Menu menu : getMenus()) {
            sum += menu.getMenuPrice();
        }
        return sum + getShippingPrice();
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }
}
