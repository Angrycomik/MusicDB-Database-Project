module bd {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive java.sql;

    opens bd to javafx.fxml;
    exports bd;
}
