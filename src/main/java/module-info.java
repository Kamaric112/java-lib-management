module com.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    
    opens com.app to javafx.fxml;
    opens com.controller to javafx.fxml;

    exports com.app;
    exports com.controller;
    exports com.model;
    exports com.service;
}
