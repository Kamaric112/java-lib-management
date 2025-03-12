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

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        boolean connected = DatabaseManagerService.connect();

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
            alert.setContentText(
                    "Failed to connect to the SQLite database. The application may not function correctly.");
            alert.showAndWait();
        }

        scene = new Scene(loadFXML("menu"), 1024, 768);
        stage.setTitle("Library Management System");
        stage.setScene(scene);
        stage.setMinWidth(800);
        stage.setMinHeight(600);
        stage.show();
    }

    @Override
    public void stop() {
        DatabaseManagerService.disconnect();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        String resourcePath = "/com/view/" + fxml + ".fxml";
        System.out.println("Loading FXML from: " + resourcePath);
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(resourcePath));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}