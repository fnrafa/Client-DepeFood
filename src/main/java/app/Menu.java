package app;

public class Menu {
    private final String menuName;
    private final double menuPrice;

    public Menu(String menuName, double price) {
        this.menuName = menuName;
        this.menuPrice = price;
    }

    public String getMenuName() {
        return menuName;
    }

    public double getMenuPrice() {
        return menuPrice;
    }
}
