package component.form;

import app.Auth;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import page.AdminMenu;
import page.CustomerMenu;

import static page.MainApp.stage;
import static page.MemberMenu.showAlert;

public class LoginForm {

    public TextField inputName;
    public TextField inputPhoneNumber;

    private final AdminMenu adminMenu = new AdminMenu();
    private final CustomerMenu customerMenu = new CustomerMenu();

    public static Scene createLoginForm() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LoginForm.class.getResource("/view/pageLogin.fxml"));
            Parent root = fxmlLoader.load();
            return new Scene(root, 280, 420);
        } catch (Exception e) {
            showAlert("DepeFood", "Internal Server Error", e.getMessage(), Alert.AlertType.ERROR);
            return null;
        }
    }

    public void handleLogin() {
        try {
            String userName = inputName.getText().trim();
            String phoneNumber = inputPhoneNumber.getText().trim();
            StringBuilder errorMessage = new StringBuilder();

            if (userName.isEmpty() && phoneNumber.isEmpty()) {
                errorMessage.append("Name and phone number must be provided.\n");
            } else {
                if (userName.isEmpty()) {
                    errorMessage.append("Name must be provided.\n");
                }
                if (phoneNumber.isEmpty()) {
                    errorMessage.append("Phone number must be provided.\n");
                }
            }
            if (!errorMessage.isEmpty()) {
                throw new IllegalArgumentException(errorMessage.toString());
            }
            Auth.setUser(userName, phoneNumber);
            if (Auth.getUser() == null) {
                showAlert("DepeFood", "Unauthorized", "Invalid login credentials.", Alert.AlertType.ERROR);
                return;
            }
            switch (Auth.getUser().getRole()) {
                case "Admin" -> stage.setScene(adminMenu.createBaseMenu());
                case "Customer" -> stage.setScene(customerMenu.createBaseMenu());
                default -> throw new Exception("Unsupported role.");
            }
        } catch (IllegalArgumentException e) {
            showAlert("DepeFood", "Unprocessable Entity", e.getMessage(), Alert.AlertType.ERROR);
        } catch (Exception e) {
            showAlert("DepeFood", "Internal Server Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
