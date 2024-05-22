package page;

import component.form.LoginForm;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;
import static page.MainApp.stage;

public abstract class MemberMenu {
    abstract Scene createBaseMenu();

    public static void showAlert(String title, String header, String content, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        if (content != null && !content.isEmpty()) {
            alert.setContentText(content);
        } else {
            alert.setContentText(null);
        }
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new Image(Objects.requireNonNull(MemberMenu.class.getResourceAsStream("/assets/images/icon.png"))));
        alert.getDialogPane().getStylesheets().add(Objects.requireNonNull(MemberMenu.class.getResource("/assets/css/main.css")).toExternalForm());
        alert.showAndWait();
    }

    public static Scene getScene() {
        return stage.getScene();
    }

    // ??
    public static void refresh() {
        stage.setScene(getScene());
    }

    public void getBack() {
        try {
            stage.setScene(createBaseMenu());
        } catch (Exception e) {
            MemberMenu.showAlert("DepeFood", "Internal Server Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    public void handleLogout() {
        try {
            stage.setScene(LoginForm.createLoginForm());
        } catch (Exception e) {
            MemberMenu.showAlert("DepeFood", "Internal Server Error", e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
