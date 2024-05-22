module page.depefoodfxml {
    requires javafx.controls;
    requires javafx.fxml;

    opens page to javafx.fxml;
    opens component to javafx.fxml;
    opens component.form to javafx.fxml;
    opens app to javafx.fxml;
    exports page;
    exports component;
    exports component.form;
    exports app;
}
