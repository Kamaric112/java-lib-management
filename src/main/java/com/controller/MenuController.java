package com.controller;

import java.io.IOException;
import com.service.AuthenticationService;
import com.model.User; // Import User model
import com.app.App;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.application.Platform;

public class MenuController {

    @FXML
    private Button signOutButton;

    @FXML
    private Button bookListButton;

    @FXML
    private Button signInButton;

    @FXML
    private Button registerButton;

    @FXML
    private Button bookManagementButton;

    @FXML
    private Button loanManagementButton;

    @FXML
    private Button userManagementButton;

    @FXML
    private Button teamMembersButton;

    @FXML
    private Button exitButton;
    @FXML
    private Label currentUserLabel;

    @FXML
    public void initialize() {
        updateMenuVisibility();
    }

    private void updateMenuVisibility() {
        User currentUser = AuthenticationService.getCurrentUser();
        if (currentUser != null) {
            currentUserLabel.setText(String.format("User ID: %d, Username: %s, Role: %s",
                    currentUser.getId(), currentUser.getUsername(), currentUser.getRole()));
            signInButton.setVisible(false);
            registerButton.setVisible(false);
            signOutButton.setVisible(true);

            boolean isAdmin = currentUser.getRole() == User.Role.ADMIN;
            bookManagementButton.setVisible(isAdmin);
            loanManagementButton.setVisible(isAdmin);
            userManagementButton.setVisible(isAdmin);

            bookListButton.setVisible(true);
            teamMembersButton.setVisible(true);
        } else {
            currentUserLabel.setText("Not logged in");
            signInButton.setVisible(true);
            registerButton.setVisible(true);
            signOutButton.setVisible(false);

            bookManagementButton.setVisible(false);
            loanManagementButton.setVisible(false);
            userManagementButton.setVisible(false);

            bookListButton.setVisible(false);
            teamMembersButton.setVisible(false);
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
    private void switchToLoanManagement() throws IOException {
        App.setRoot("loan-management");
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
    private void switchToTeamMembers() throws IOException {
        App.setRoot("member-list");
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

    @FXML
    private void switchToUserManagement() throws IOException {
        App.setRoot("user-management");
    }
}