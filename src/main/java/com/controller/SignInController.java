package com.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.app.App;
import com.model.User;
import com.service.AuthenticationService;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.event.ActionEvent;

public class SignInController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private void initialize() {
        // Set default error message to empty
        if (errorLabel != null) {
            errorLabel.setText("");
        }
    }

    @FXML
    private void signIn(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Username and password are required");
            return;
        }

        try {
            AuthenticationService authService = new AuthenticationService();
            User user = authService.authenticateUser(username, password);

            if (user != null) {
                try {
                    App.setRoot("book-list");
                } catch (IOException e) {
                    showError("Error loading application: " + e.getMessage());
                }
            } else {
                showError("Invalid username or password");
            }
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        }
    }

    private void showError(String message) {
        if (errorLabel != null) {
            errorLabel.setText(message);
            errorLabel.setVisible(true);
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Authentication Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }
    }

    @FXML
    private void switchToRegister() throws IOException {
        App.setRoot("register");
    }

    @FXML
    private void switchToMenu() throws IOException {
        App.setRoot("menu");
    }
}