package com.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;

import com.service.DatabaseManagerService;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        // Try to connect to the database
        boolean connected = DatabaseManagerService.connect();
        
        // Show connection status message
        if (connected) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Database Status");
            alert.setHeaderText("Database Connection Successful");
            alert.setContentText("Successfully connected to the SQLite database.");
            alert.show();
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Database Status");
            alert.setHeaderText("Database Connection Failed");
            alert.setContentText("Failed to connect to the SQLite database. The application may not function correctly.");
            alert.showAndWait();
        }
        
        scene = new Scene(loadFXML("menu"), 640, 480);
        stage.setTitle("Library Management System");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        // Close database connection when application exits
        DatabaseManagerService.disconnect();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/view/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}