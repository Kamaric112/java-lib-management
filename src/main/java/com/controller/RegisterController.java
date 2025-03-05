package com.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.app.App;
import com.model.User;
import com.service.AuthenticationService;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

public class RegisterController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Button registerButton;

    @FXML
    private Label messageLabel;

    @FXML
    private void initialize() {
        // Set default message to empty
        if (messageLabel != null) {
            messageLabel.setText("");
        }
    }

    @FXML
    private void registerUser(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validate input
        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showMessage("All fields are required", true);
            return;
        }

        if (!password.equals(confirmPassword)) {
            showMessage("Passwords do not match", true);
            return;
        }

        try {
            User newUser = new User(
                    usernameField.getText().trim(),
                    passwordField.getText());

            AuthenticationService authService = new AuthenticationService();
            int userId = authService.registerUser(newUser);

            if (userId > 0) {
                showMessage("Registration successful!", false);
                clearFields();
                try {
                    switchToSignIn();
                } catch (IOException e) {
                    showMessage("Error navigating to sign in: " + e.getMessage(), true);
                }
            } else {
                showMessage("Registration failed. Username may already exist.", true);
            }
        } catch (SQLException e) {
            showMessage("Database error: " + e.getMessage(), true);
        }
    }

    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
    }

    private void showMessage(String message, boolean isError) {
        if (messageLabel != null) {
            messageLabel.setText(message);
            messageLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
        } else {
            // Fallback to alert if message label doesn't exist
            Alert alert = new Alert(isError ? AlertType.ERROR : AlertType.INFORMATION);
            alert.setTitle(isError ? "Error" : "Success");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }
    }

    private void showAlert(String title, String header, String content, AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void switchToSignIn() throws IOException {
        App.setRoot("sign-in");
    }

    @FXML
    private void switchToMenu() throws IOException {
        App.setRoot("menu");
    }
}