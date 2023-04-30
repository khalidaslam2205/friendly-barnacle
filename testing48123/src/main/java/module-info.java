module com.example.testing48123 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.testing48123 to javafx.fxml;
    exports com.example.testing48123;
}