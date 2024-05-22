package page;

import app.Menu;
import app.*;
import component.form.LoginForm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static app.Auth.getUser;
import static app.Debit.MINIMUM_TOTAL_PRICE;
import static app.Order.calculateDeliveryCost;
import static app.Order.generateOrderID;
import static component.BillPrinter.printBill;
import static component.form.LoginForm.createLoginForm;
import static page.MainApp.stage;

public class CustomerMenu extends MemberMenu {
    public ComboBox<String> boxRestaurant;
    public ComboBox<String> boxPaymentOption;
    public ComboBox<String> boxOrder;
    public DatePicker inputDate;
    public Label labelName;
    public Label labelBalance;
    public TableView<Menu> tableMenu;
    public TableColumn<Menu, String> menuNameColumn;
    public TableColumn<Menu, Double> menuPriceColumn;
    private final ObservableList<Menu> menus = FXCollections.observableArrayList();

    @Override
    public Scene createBaseMenu() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginForm.class.getResource("/view/pageCustomer.fxml"));
            Parent root = fxmlLoader.load();
            return new Scene(root, 280, 420);
        } catch (Exception e) {
            showAlert("DepeFood", "Internal Server Error", e.getMessage(), Alert.AlertType.ERROR);
            return createLoginForm();
        }
    }

    public Scene createTambahPesananView() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginForm.class.getResource("/view/addOrder.fxml"));
            Parent root = fxmlLoader.load();
            CustomerMenu controller = fxmlLoader.getController();
            controller.boxRestaurant.getItems().addAll(Data.getRestaurantList().stream().map(Restaurant::getRestaurantName).toList());
            return new Scene(root, 280, 420);
        } catch (Exception e) {
            showAlert("DepeFood", "Internal Server Error", e.getMessage(), Alert.AlertType.ERROR);
            return createBaseMenu();
        }
    }

    public Scene createCetakBillView() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginForm.class.getResource("/view/printBill.fxml"));
            Parent root = fxmlLoader.load();
            CustomerMenu controller = fxmlLoader.getController();
            controller.boxOrder.getItems().addAll(getUser().getOrderHistory().stream().map(Order::getOrderId).toList());
            return new Scene(root, 280, 420);
        } catch (Exception e) {
            showAlert("DepeFood", "Internal Server Error", e.getMessage(), Alert.AlertType.ERROR);
            return createBaseMenu();
        }
    }

    // ??
    public Scene createLihatMenuView() {
        return createBaseMenu();
    }

    public Scene createBayarBillView() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginForm.class.getResource("/view/payBill.fxml"));
            Parent root = fxmlLoader.load();
            CustomerMenu controller = fxmlLoader.getController();
            controller.boxOrder.getItems().addAll(getUser().getOrderHistory().stream().map(Order::getOrderId).toList());
            controller.boxPaymentOption.getItems().addAll("Debit", "Credit Card");
            return new Scene(root, 280, 420);
        } catch (Exception e) {
            showAlert("DepeFood", "Internal Server Error", e.getMessage(), Alert.AlertType.ERROR);
            return createBaseMenu();
        }
    }

    public Scene createCekSaldoView() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginForm.class.getResource("/view/getBalance.fxml"));
            Parent root = fxmlLoader.load();
            CustomerMenu controller = fxmlLoader.getController();
            controller.labelName.setText(STR."Name : \{getUser().getName()}");
            controller.labelBalance.setText(STR."Balance : Rp.\{getUser().getBalance()}");
            return new Scene(root, 280, 420);
        } catch (Exception e) {
            showAlert("DepeFood", "Internal Server Error", e.getMessage(), Alert.AlertType.ERROR);
            return createBaseMenu();
        }
    }

    public void handleTambahPesanan(String namaRestoran, LocalDate tanggalPemesanan, List<String> menuItems) {
        try {
            Restaurant selectedRestaurant = Data.getRestaurantList().stream().filter(r -> r.getRestaurantName().equals(namaRestoran)).findFirst().orElseThrow(() -> new IllegalArgumentException("Selected restaurant not found in the list."));
            List<Menu> menusList = menuItems.stream().map(itemName -> selectedRestaurant.getMenu().stream().filter(menu -> menu.getMenuName().equals(itemName)).findFirst().orElseThrow(() -> new IllegalArgumentException(STR."Menu item not found: \{itemName}"))).toList();
            Menu[] menusArray = menusList.toArray(new Menu[0]);
            String orderId = generateOrderID(namaRestoran, tanggalPemesanan.toString(), getUser().getPhoneNumber());
            Order order = new Order(orderId, tanggalPemesanan.toString(), calculateDeliveryCost(getUser().getLocation()), selectedRestaurant, menusArray);
            getUser().addOrder(order);
            showAlert("DepeFood", "Success", STR."Create new order with id \{orderId} success", Alert.AlertType.INFORMATION);
        } catch (IllegalArgumentException e) {
            showAlert("DepeFood", "Unprocessable Entity", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("DepeFood", "Internal Server Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void handleTambahPesanan() {
        try {
            String restaurantName = boxRestaurant.getValue();
            LocalDate date = inputDate.getValue();
            List<String> selectedMenuItems = tableMenu.getSelectionModel().getSelectedItems().stream().map(Menu::getMenuName).collect(Collectors.toList());
            StringBuilder errorMessage = new StringBuilder();
            if (restaurantName == null || restaurantName.isEmpty()) {
                errorMessage.append("Restaurant must be selected.\n");
            }
            if (date == null) {
                errorMessage.append("Date must be provided.\n");
            }
            if (selectedMenuItems.isEmpty()) {
                errorMessage.append("At least one menu item must be selected.\n");
            }
            if (!errorMessage.isEmpty()) {
                throw new IllegalArgumentException(errorMessage.toString());
            }
            handleTambahPesanan(restaurantName, date, selectedMenuItems);
        } catch (IllegalArgumentException e) {
            showAlert("DepeFood", "Unprocessable Entity", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("DepeFood", "Internal Server Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void handleBayarBill(String orderID, int pilihanPembayaran) {
        try {
            Order selectedOrder = Auth.getUser().getOrderHistory().stream().filter(r -> r.getOrderId().equals(orderID)).findFirst().orElseThrow(() -> new IllegalArgumentException("Selected order not found in the list."));
            if (selectedOrder.isFinished()) {
                throw new IllegalArgumentException("This Order already paid off");
            }
            Payment payment = getUser().getPayment();
            long totalAmount = (long) selectedOrder.getTotal();
            switch (pilihanPembayaran) {
                case 1:
                    handleCreditCardPayment(selectedOrder, payment, totalAmount);
                    break;
                case 2:
                    handleDebitPayment(selectedOrder, payment, totalAmount);
                    break;
                default:
                    throw new Exception("Invalid payment option selected.");
            }

        } catch (IllegalArgumentException e) {
            showAlert("DepeFood", "Unprocessable Entity", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("DepeFood", "Internal Server Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void handleCreditCardPayment(Order order, Payment payment, long totalAmount) throws Exception {
        if (!(payment instanceof CreditCard)) {
            throw new Exception("Credit Card payment method not set.");
        }
        long fee = CreditCard.countTransactionFee(totalAmount);
        payment.processPayment(totalAmount + fee);
        order.setFinished(true);
        getUser().setBalance(getUser().getBalance() - (totalAmount + fee));
        showAlert("DepeFood", "Success", STR."Success paid bill with total amount Rp. \{totalAmount} and fee Rp.\{fee}", Alert.AlertType.INFORMATION);
    }

    private void handleDebitPayment(Order order, Payment payment, long totalAmount) throws Exception {
        if (!(payment instanceof Debit)) {
            throw new Exception("Debit payment method not set.");
        }
        if (totalAmount < MINIMUM_TOTAL_PRICE) {
            throw new Exception("Amount are less than Rp.50000, please using another Payment");
        }
        if (getUser().getBalance() >= totalAmount) {
            payment.processPayment(totalAmount);
            order.setFinished(true);
            getUser().setBalance(getUser().getBalance() - totalAmount);
            showAlert("DepeFood", "Success", STR."Success paid bill with total amount Rp. \{totalAmount}", Alert.AlertType.INFORMATION);
        } else {
            throw new Exception("Insufficient funds for payment.");
        }
    }

    public void handleBayarBill() {
        try {
            String paymentOption = boxPaymentOption.getValue();
            String orderId = boxOrder.getValue();
            StringBuilder errorMessage = new StringBuilder();
            if (paymentOption == null || paymentOption.isEmpty()) {
                errorMessage.append("Payment option must be selected.\n");
            }
            if (orderId == null || orderId.isEmpty()) {
                errorMessage.append("Order id be selected.\n");
            }
            if (!errorMessage.isEmpty()) {
                throw new IllegalArgumentException(errorMessage.toString());
            }
            int method = (Objects.equals(paymentOption, "Debit")) ? 2 : 1;
            handleBayarBill(orderId, method);
        } catch (IllegalArgumentException e) {
            showAlert("DepeFood", "Unprocessable Entity", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("DepeFood", "Internal Server Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void handlePrintBill() {
        try {
            String orderId = boxOrder.getValue();
            if (orderId == null || orderId.isEmpty()) {
                throw new IllegalArgumentException("Order must id be selected.");
            }
            printBill(orderId);
        } catch (IllegalArgumentException e) {
            showAlert("DepeFood", "Unprocessable Entity", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("DepeFood", "Internal Server Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void setTambahPesananView() {
        try {
            stage.setScene(createTambahPesananView());
        } catch (Exception e) {
            showAlert("DepeFood", "Internal Server Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void setCetakBillView() {
        try {
            stage.setScene(createCetakBillView());
        } catch (Exception e) {
            showAlert("DepeFood", "Internal Server Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void setBayarBillView() {
        try {
            stage.setScene(createBayarBillView());
        } catch (Exception e) {
            showAlert("DepeFood", "Internal Server Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void setCekSaldoView() {
        try {
            stage.setScene(createCekSaldoView());
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
            tableMenu.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        }
    }
}
