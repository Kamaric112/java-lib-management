package com.controller;

import java.io.IOException;

import com.app.App;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.application.Platform;

public class MenuController {

    @FXML
    private Button bookListButton;

    @FXML
    private Button signInButton;

    @FXML
    private Button bookManagementButton;

    @FXML
    private Button exitButton;

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
}