package com.controller;

import java.io.IOException;
import com.service.AuthenticationService;
import com.model.User; // Import User model
import com.app.App;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.application.Platform;

public class MenuController {

    @FXML
    private Button signOutButton;

    @FXML
    private Button bookListButton;

    @FXML
    private Button signInButton;

    @FXML
    private Button registerButton; // Need to get reference to registerButton

    @FXML
    private Button bookManagementButton;

    @FXML
    private Button exitButton;

    @FXML
    public void initialize() {
        updateMenuVisibility();
    }

    private void updateMenuVisibility() {
        User currentUser = AuthenticationService.getCurrentUser();
        if (currentUser != null) {
            signInButton.setVisible(false);
            registerButton.setVisible(false);
            signOutButton.setVisible(true);
        } else {
            signInButton.setVisible(true);
            registerButton.setVisible(true);
            signOutButton.setVisible(false);
        }
    }

    @FXML
    private void switchToBookList() throws IOException {
        App.setRoot("book-list");
    }

    @FXML
    private void switchToBookManagement() throws IOException {
        App.setRoot("book-management");
    }

    @FXML
    private void switchToSignIn() throws IOException {
        App.setRoot("sign-in");
    }

    @FXML
    private void switchToRegister() throws IOException {
        App.setRoot("register");
    }

    @FXML
    private void exitApplication() {
        Platform.exit();
    }

    @FXML
    private void signOut() throws IOException {
        AuthenticationService.signOut();
        App.setRoot("sign-in");
    }
}