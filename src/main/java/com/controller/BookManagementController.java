package com.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.app.App;
import com.model.Book;
import com.service.BookService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

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
  private TextField genreField;

  @FXML
  private TableView<Book> bookTable;
  @FXML
  private TableColumn<Book, Integer> idColumn;
  @FXML
  private TableColumn<Book, String> titleColumn;
  @FXML
  private TableColumn<Book, String> authorColumn;
  @FXML
  private TableColumn<Book, String> isbnColumn;
  @FXML
  private TableColumn<Book, Integer> yearColumn;
  @FXML
  private TableColumn<Book, String> genreColumn;
  @FXML
  private TableColumn<Book, Boolean> availableColumn;

  @FXML
  private Button addButton;
  @FXML
  private Button editButton;
  @FXML
  private Button cancelButton;

  private ObservableList<Book> bookData = FXCollections.observableArrayList();
  private Book selectedBook = null;
  private boolean editMode = false;

  @FXML
  public void initialize() {
    // Initialize table columns
    idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
    authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
    isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
    yearColumn.setCellValueFactory(new PropertyValueFactory<>("publicationYear"));
    genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
    availableColumn.setCellValueFactory(new PropertyValueFactory<>("available"));

    // Load books
    refreshBookTable();

    // Add selection listener
    bookTable.getSelectionModel().selectedItemProperty().addListener(
        (obs, oldSelection, newSelection) -> handleBookSelection(newSelection));

    // Initialize button states
    editButton.setDisable(true);
    cancelButton.setDisable(true);
  }

  private void handleBookSelection(Book book) {
    if (book != null) {
      selectedBook = book;
      populateFields(book);
      editMode = true;
      editButton.setDisable(false);
      cancelButton.setDisable(false);
      addButton.setDisable(true);
    }
  }

  private void populateFields(Book book) {
    titleField.setText(book.getTitle());
    authorField.setText(book.getAuthor());
    isbnField.setText(book.getIsbn());
    yearField.setText(String.valueOf(book.getPublicationYear()));
    genreField.setText(book.getGenre());
  }

  @FXML
  private void addBook() {
    if (editMode) {
      showAlert("Error", "Edit in Progress",
          "Please save or cancel the current edit first.", AlertType.ERROR);
      return;
    }

    if (!validateInput()) {
      return;
    }

    try {
      Book book = createBookFromFields();
      int bookId = BookService.addBook(book);
      if (bookId > 0) {
        showAlert("Success", "Book Added",
            "The book was successfully added to the library.", AlertType.INFORMATION);
        clearFields();
        refreshBookTable();
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
  private void saveBookChanges() {
    if (!editMode || selectedBook == null) {
      return;
    }

    if (!validateInput()) {
      return;
    }

    try {
      Book updatedBook = createBookFromFields();
      updatedBook.setId(selectedBook.getId());
      updatedBook.setAvailable(selectedBook.isAvailable());

      if (BookService.updateBook(updatedBook)) {
        showAlert("Success", "Book Updated",
            "The book was successfully updated.", AlertType.INFORMATION);
        exitEditMode();
        refreshBookTable();
      } else {
        showAlert("Error", "Failed to Update Book",
            "The book could not be updated. Please try again.", AlertType.ERROR);
      }
    } catch (SQLException e) {
      showAlert("Database Error", "Failed to Update Book",
          "Error: " + e.getMessage(), AlertType.ERROR);
    }
  }

  @FXML
  private void cancelEdit() {
    exitEditMode();
  }

  private void exitEditMode() {
    editMode = false;
    selectedBook = null;
    clearFields();
    editButton.setDisable(true);
    cancelButton.setDisable(true);
    addButton.setDisable(false);
    bookTable.getSelectionModel().clearSelection();
  }

  private boolean validateInput() {
    if (titleField.getText().trim().isEmpty() ||
        authorField.getText().trim().isEmpty()) {
      showAlert("Validation Error", "Missing Information",
          "Title and Author are required fields.", AlertType.ERROR);
      return false;
    }

    try {
      int year = Integer.parseInt(yearField.getText().trim());
      if (year < 1000 || year > 3000) {
        showAlert("Validation Error", "Invalid Year",
            "Year must be between 1000 and 3000.", AlertType.ERROR);
        return false;
      }
    } catch (NumberFormatException e) {
      showAlert("Validation Error", "Invalid Year",
          "Year must be a number.", AlertType.ERROR);
      return false;
    }

    return true;
  }

  private Book createBookFromFields() {
    return new Book(
        titleField.getText().trim(),
        authorField.getText().trim(),
        isbnField.getText().trim(),
        Integer.parseInt(yearField.getText().trim()),
        genreField.getText().trim());
  }

  @FXML
  public void clearFields() {
    titleField.clear();
    authorField.clear();
    isbnField.clear();
    yearField.clear();
    genreField.clear();
  }

  private void refreshBookTable() {
    try {
      List<Book> books = BookService.getAllBooks();
      bookData.clear();
      bookData.addAll(books);
      bookTable.setItems(bookData);
    } catch (SQLException e) {
      showAlert("Database Error", "Failed to Load Books",
          "Error: " + e.getMessage(), AlertType.ERROR);
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
  private void switchToMenu() throws IOException {
    App.setRoot("menu");
  }
}