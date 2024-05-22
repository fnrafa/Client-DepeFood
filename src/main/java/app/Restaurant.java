package app;

import java.util.ArrayList;

public class Restaurant {
    private final String restaurantName;
    private final ArrayList<Menu> menu;

    public Restaurant(String restaurantName) {
        this.restaurantName = restaurantName;
        this.menu = new ArrayList<>();
    }

    public ArrayList<Menu> getMenu() {
        return this.menu;
    }

    public String getRestaurantName() {
        return this.restaurantName;
    }

    public void addMenu(Menu menu) {
        this.menu.add(menu);
    }
}
