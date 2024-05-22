package app;

import java.util.ArrayList;

public class Data {
    private static ArrayList<User> userList;
    private static ArrayList<Restaurant> restaurantList;

    public static void initial() {
        initUser();
        initRestaurant();
    }

    public static void initUser() {
        userList = new ArrayList<>();
        userList.add(new app.User("Rafa", "123456789", "rafa@gmail.com", "P", "Customer", 99999999, new Debit()));
        userList.add(new app.User("Admin", "123456789", "admin@gmail.com", "-", "Admin", 0, null));
    }

    public static void initRestaurant() {
        restaurantList = new ArrayList<>();

        Restaurant restaurant1 = new Restaurant("Gourmet Burgers");
        restaurant1.getMenu().add(new Menu("Classic Burger", 15000));
        restaurant1.getMenu().add(new Menu("Cheese Burger", 17000));
        restaurant1.getMenu().add(new Menu("Vegan Burger", 20000));
        restaurantList.add(restaurant1);

        Restaurant restaurant2 = new Restaurant("Italian Cuisine");
        restaurant2.getMenu().add(new Menu("Margherita Pizza", 17500));
        restaurant2.getMenu().add(new Menu("Pasta", 18000));
        restaurant2.getMenu().add(new Menu("Lasagna", 20000));
        restaurantList.add(restaurant2);
    }

    public static ArrayList<User> getUserList() {
        return userList;
    }

    public static ArrayList<Restaurant> getRestaurantList() {
        return restaurantList;
    }
}
