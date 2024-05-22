package component;

import app.Menu;
import app.Order;
import component.form.LoginForm;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import page.CustomerMenu;

import static app.Auth.getUser;
import static page.MainApp.stage;
import static page.MemberMenu.getScene;
import static page.MemberMenu.showAlert;

public class BillPrinter {

    private final CustomerMenu customerMenu = new CustomerMenu();
    public TextField fieldOrderId;
    public TextField fieldDate;
    public TextField fieldRestaurant;
    public TextField fieldLocation;
    public TextField fieldStatus;
    public Label labelItems;

    public static Scene createBillPrinterForm(Order order) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginForm.class.getResource("/view/pageBill.fxml"));
            Parent root = fxmlLoader.load();
            BillPrinter controller = fxmlLoader.getController();
            controller.fieldOrderId.setText(controller.fieldOrderId.getText() + order.getOrderId());
            controller.fieldDate.setText(controller.fieldDate.getText() + order.getDate());
            controller.fieldRestaurant.setText(controller.fieldRestaurant.getText() + order.getRestaurant().getRestaurantName());
            controller.fieldLocation.setText(controller.fieldLocation.getText() + getUser().getLocation());
            controller.fieldStatus.setText(controller.fieldStatus.getText() + ((order.isFinished()) ? "Finished" : "Not Finished"));

            StringBuilder items = new StringBuilder();
            for (Menu menu : order.getMenus()) {
                items.append(STR."\{menu.getMenuName()} : Rp.\{menu.getMenuPrice()}\n");
            }
            items.append(STR."Biaya Ongkos Kirim : Rp.\{order.getShippingPrice()}\n");
            items.append(STR."Total Biaya : Rp.\{order.getTotal()}");
            controller.labelItems.setText(items.toString());

            return new Scene(root, 280, 420);
        } catch (Exception e) {
            showAlert("DepeFood", "Internal Server Error", e.getMessage(), Alert.AlertType.ERROR);
            return getScene();
        }
    }

    public static void printBill(String orderId) {
        Order selectedOrder = getUser().getOrderHistory().stream().filter(r -> r.getOrderId().equals(orderId)).findFirst().orElseThrow(() -> new IllegalArgumentException("Selected restaurant not found in the list."));
        stage.setScene(createBillPrinterForm(selectedOrder));
    }

    public void getBack() {
        try {
            stage.setScene(customerMenu.createCetakBillView());
        } catch (Exception e) {
            showAlert("DepeFood", "Internal Server Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
