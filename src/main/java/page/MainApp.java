package page;

import app.Data;
import component.form.LoginForm;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class MainApp extends Application {

    public static Stage stage;

    @Override
    public void start(Stage stage) {
        try {
            Data.initial();
            MainApp.stage = stage;
            stage.setScene(LoginForm.createLoginForm());
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/images/icon.png"))));
            stage.setTitle("DepeFood");
            stage.show();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
