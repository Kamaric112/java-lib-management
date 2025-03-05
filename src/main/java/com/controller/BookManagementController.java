package com.controller;

import java.io.IOException;
import java.sql.SQLException;

import com.app.App;
import com.model.Book;
import com.service.BookService;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

public class BookManagementController {

  @FXML
  private TextField titleField;

  @FXML
  private TextField authorField;

  @FXML
  private TextField isbnField;

  @FXML
  private TextField yearField;

  @FXML
  private void addBook() {
    // Validate input
    if (titleField.getText().trim().isEmpty() ||
        authorField.getText().trim().isEmpty()) {
      showAlert("Validation Error", "Missing Information",
          "Title and Author are required fields.", AlertType.ERROR);
      return;
    }

    int year;
    try {
      year = Integer.parseInt(yearField.getText().trim());
      if (year < 1000 || year > 3000) {
        showAlert("Validation Error", "Invalid Year",
            "Year must be between 1000 and 3000.", AlertType.ERROR);
        return;
      }
    } catch (NumberFormatException e) {
      showAlert("Validation Error", "Invalid Year",
          "Year must be a number.", AlertType.ERROR);
      return;
    }

    // Create and save book
    Book book = new Book(
        titleField.getText().trim(),
        authorField.getText().trim(),
        isbnField.getText().trim(),
        year);

    try {
      int bookId = BookService.addBook(book);
      if (bookId > 0) {
        showAlert("Success", "Book Added",
            "The book was successfully added to the library.", AlertType.INFORMATION);
        clearFields();
      } else {
        showAlert("Error", "Failed to Add Book",
            "The book could not be added. Please try again.", AlertType.ERROR);
      }
    } catch (SQLException e) {
      showAlert("Database Error", "Failed to Add Book",
          "Error: " + e.getMessage(), AlertType.ERROR);
    }
  }

  @FXML
  public void clearFields() {
    titleField.clear();
    authorField.clear();
    isbnField.clear();
    yearField.clear();
  }

  private void showAlert(String title, String header, String content, AlertType type) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.setContentText(content);
    alert.showAndWait();
  }

  @FXML
  private void switchToMenu() throws IOException {
    App.setRoot("menu");
  }
}