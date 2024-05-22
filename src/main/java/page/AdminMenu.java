package page;

import app.Data;
import app.Menu;
import app.Restaurant;
import component.form.LoginForm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;

import static component.form.LoginForm.createLoginForm;
import static page.MainApp.stage;

public class AdminMenu extends MemberMenu {

    public ComboBox<String> boxRestaurant;
    public TextField inputRestaurant;
    public TextField inputMenu;
    public TextField inputPrice;
    public TableView<Menu> tableMenu;
    public TableColumn<Menu, String> menuNameColumn;
    public TableColumn<Menu, Double> menuPriceColumn;
    private final ObservableList<Menu> menus = FXCollections.observableArrayList();

    @Override
    public Scene createBaseMenu() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginForm.class.getResource("/view/pageAdmin.fxml"));
            Parent root = fxmlLoader.load();
            return new Scene(root, 280, 420);
        } catch (Exception e) {
            showAlert("DepeFood", "Internal Server Error", e.getMessage(), Alert.AlertType.ERROR);
            return createLoginForm();
        }
    }

    public Scene createAddRestaurantView() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginForm.class.getResource("/view/addRestaurant.fxml"));
            Parent root = fxmlLoader.load();
            return new Scene(root, 280, 420);
        } catch (Exception e) {
            showAlert("DepeFood", "Internal Server Error", e.getMessage(), Alert.AlertType.ERROR);
            return createBaseMenu();
        }
    }

    public Scene createAddMenuView() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginForm.class.getResource("/view/addMenu.fxml"));
            Parent root = fxmlLoader.load();
            AdminMenu controller = fxmlLoader.getController();
            controller.boxRestaurant.getItems().addAll(Data.getRestaurantList().stream().map(Restaurant::getRestaurantName).toList());
            return new Scene(root, 280, 420);
        } catch (Exception e) {
            showAlert("DepeFood", "Internal Server Error", e.getMessage(), Alert.AlertType.ERROR);
            return createBaseMenu();
        }
    }

    public Scene createViewRestaurantView() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginForm.class.getResource("/view/getRestaurant.fxml"));
            Parent root = fxmlLoader.load();
            AdminMenu controller = fxmlLoader.getController();
            controller.boxRestaurant.getItems().addAll(Data.getRestaurantList().stream().map(Restaurant::getRestaurantName).toList());
            return new Scene(root, 280, 420);
        } catch (Exception e) {
            showAlert("DepeFood", "Internal Server Error", e.getMessage(), Alert.AlertType.ERROR);
            return createBaseMenu();
        }
    }

    public void handleTambahRestoran(String nama) {
        ArrayList<Restaurant> restaurantList = Data.getRestaurantList();
        if (restaurantList.stream().anyMatch(r -> r.getRestaurantName().equalsIgnoreCase(nama))) {
            showAlert("DepeFood", "Unprocessable Entity", "Name already exists", Alert.AlertType.ERROR);
        } else {
            Restaurant newRestaurant = new Restaurant(nama);
            restaurantList.add(newRestaurant);
            showAlert("DepeFood", "Created", STR."Restaurant \{nama} successfully registered.", Alert.AlertType.INFORMATION);
        }
    }

    public void handleTambahRestoran() {
        try {
            String restaurant = inputRestaurant.getText().trim();
            if (restaurant.isEmpty()) {
                throw new IllegalArgumentException("Restaurant Name must be provided.");
            }
            handleTambahRestoran(restaurant);
        } catch (IllegalArgumentException e) {
            showAlert("DepeFood", "Unprocessable Entity", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("DepeFood", "Internal Server Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void handleTambahMenuRestoran(Restaurant restaurant, String itemName, double price) {
        Menu newMenu = new Menu(itemName, price);
        restaurant.addMenu(newMenu);
        showAlert("DepeFood", "Success", "Menu item added successfully!", Alert.AlertType.INFORMATION);
    }

    public void handleTambahMenuRestoran() {
        try {
            String restaurantName = boxRestaurant.getValue();
            String menuName = inputMenu.getText().trim();
            StringBuilder errorMessage = new StringBuilder();
            if (restaurantName == null || restaurantName.isEmpty()) {
                errorMessage.append("Restaurant must be selected.\n");
            }
            if (menuName.isEmpty()) {
                errorMessage.append("Menu name must be provided.\n");
            }
            if (inputPrice.getText().isEmpty()) {
                errorMessage.append("Price must be provided.\n");
            }
            if (!errorMessage.isEmpty()) {
                throw new IllegalArgumentException(errorMessage.toString());
            }
            double price;
            try {
                price = Double.parseDouble(inputPrice.getText().trim());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid price format. Please enter a valid number.");
            }
            Restaurant selectedRestaurant = Data.getRestaurantList().stream().filter(r -> r.getRestaurantName().equals(restaurantName)).findFirst().orElseThrow(() -> new IllegalArgumentException("Selected restaurantName not found in the list."));
            handleTambahMenuRestoran(selectedRestaurant, menuName, price);
        } catch (IllegalArgumentException e) {
            showAlert("DepeFood", "Unprocessable Entity", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("DepeFood", "Internal Server Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void setAddRestaurantView() {
        try {
            stage.setScene(createAddRestaurantView());
        } catch (Exception e) {
            showAlert("DepeFood", "Internal Server Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void setAddMenuRestaurantView() {
        try {
            stage.setScene(createAddMenuView());
        } catch (Exception e) {
            showAlert("DepeFood", "Internal Server Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void setViewRestaurantView() {
        try {
            stage.setScene(createViewRestaurantView());
        } catch (Exception e) {
            showAlert("DepeFood", "Internal Server Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void updateTableMenu() {
        String restaurantName = boxRestaurant.getValue();
        Restaurant selectedRestaurant = Data.getRestaurantList().stream().filter(r -> r.getRestaurantName().equals(restaurantName)).findFirst().orElse(null);
        if (selectedRestaurant != null) {
            menus.setAll(selectedRestaurant.getMenu());
            menuNameColumn.setCellValueFactory(new PropertyValueFactory<>("menuName"));
            menuPriceColumn.setCellValueFactory(new PropertyValueFactory<>("menuPrice"));
            tableMenu.setItems(menus);
        }
    }
}
