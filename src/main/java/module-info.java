module projekt.bd {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires com.fasterxml.jackson.databind;
    requires java.sql;
    requires java.desktop;

    opens projekt.bd to javafx.fxml;
    exports projekt.bd;
}