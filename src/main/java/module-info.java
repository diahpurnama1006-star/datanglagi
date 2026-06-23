module org.datanglagi {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
    requires mysql.connector.j;
    opens org.datanglagi to javafx.fxml;
    opens org.datanglagi.controller to javafx.fxml;
    exports org.datanglagi;
}