module com.example.whowantstobemillioner {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires javafx.base;
    opens com.example.whowantstobemillioner.model to javafx.base;

    opens com.example.whowantstobemillioner to javafx.fxml;
    exports com.example.whowantstobemillioner;
}