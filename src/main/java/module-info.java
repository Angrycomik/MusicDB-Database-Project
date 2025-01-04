module bd {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive java.sql;
    requires java.desktop;

    opens bd to javafx.fxml;
    exports bd;
}
