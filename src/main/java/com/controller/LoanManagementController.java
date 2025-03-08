package com.controller;

import com.model.Loan;
import com.service.LoanService;
import com.service.UserService; // Import UserService
import com.service.BookService; // Import BookService
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox; // Import ComboBox
import javafx.scene.control.Button; // Import Button
import javafx.scene.input.MouseEvent; // Import MouseEvent

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors; // Import Collectors
import java.io.IOException; // Import IOException
import com.app.App; // Import App

public class LoanManagementController {

  @FXML
  private TableView<Loan> loanedBooksTable;
  @FXML
  private TableColumn<Loan, Integer> loanIdColumn;
  @FXML
  private TableColumn<Loan, Integer> userIdColumn;
  @FXML
  private TableColumn<Loan, Integer> bookIdColumn;
  @FXML
  private TableColumn<Loan, String> loanDateColumn;
  @FXML
  private TableColumn<Loan, String> dueDateColumn;
  @FXML
  private TableColumn<Loan, String> returnDateColumn;
  @FXML
  private TableColumn<Loan, Boolean> activeColumn;
  @FXML
  private ComboBox<Integer> userIdComboBox; // Use ComboBox for User ID
  @FXML
  private ComboBox<Integer> bookIdComboBox; // Use ComboBox for Book ID
  @FXML
  private DatePicker loanDateField;
  @FXML
  private DatePicker dueDateField;
  @FXML
  private Button deleteLoanButton; // FXML injection for Delete Loan Button
  private Loan selectedLoan; // To store the selected loan

  private ObservableList<Loan> loanData = FXCollections.observableArrayList();

  public void initialize() {
    loanIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    userIdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
    bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
    loanDateColumn.setCellValueFactory(new PropertyValueFactory<>("loanDate"));
    dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
    returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
    activeColumn.setCellValueFactory(new PropertyValueFactory<>("active"));

    try {
      List<Loan> allLoans = LoanService.getAllLoans();
      loanData.addAll(allLoans);
      loanedBooksTable.setItems(loanData);

      // Populate User ID ComboBox
      List<Integer> userIds = UserService.getAllUsers().stream()
          .map(user -> user.getId())
          .collect(Collectors.toList());
      userIdComboBox.setItems(FXCollections.observableArrayList(userIds));

      // Populate Book ID ComboBox
      List<Integer> bookIds = BookService.getAllBooks().stream()
          .map(book -> book.getId())
          .collect(Collectors.toList());
      bookIdComboBox.setItems(FXCollections.observableArrayList(bookIds));

      deleteLoanButton.setDisable(true); // Initially disable delete button

    } catch (SQLException e) {
      e.printStackTrace(); // Handle error properly later
    }
  }

  @FXML
  protected void handleLoanBook(ActionEvent event) {
    try {
      Integer userId = userIdComboBox.getValue(); // Get User ID from ComboBox
      Integer bookId = bookIdComboBox.getValue(); // Get Book ID from ComboBox
      LocalDate loanDate = loanDateField.getValue();
      LocalDate dueDate = dueDateField.getValue();

      if (userId != null && bookId != null && loanDate != null && dueDate != null) {
        int loanId = LoanService.createLoan(userId, bookId, loanDate, dueDate);
        if (loanId != -1) {
          System.out.println("Loan created successfully with ID: " + loanId);
          // Refresh the table
          refreshLoanedBooksTable();
        } else {
          System.err.println("Failed to create loan.");
        }
      } else {
        System.err.println("Please select User ID, Book ID, loan date and due date.");
      }
    } catch (NumberFormatException e) {
      System.err.println("Invalid User ID or Book ID format.");
    } catch (SQLException e) {
      System.err.println("Database error: " + e.getMessage());
    }
  }

  @FXML
  protected void handleDeleteLoan(ActionEvent event) {
    if (selectedLoan != null) {
      try {
        if (LoanService.deleteLoan(selectedLoan.getId())) {
          System.out.println("Loan deleted successfully with ID: " + selectedLoan.getId());
          refreshLoanedBooksTable();
          deleteLoanButton.setDisable(true); // Disable button after deletion
        } else {
          System.err.println("Failed to delete loan with ID: " + selectedLoan.getId());
        }
      } catch (SQLException e) {
        System.err.println("Database error: " + e.getMessage());
      }
    } else {
      System.err.println("No loan selected to delete.");
    }
  }

  @FXML
  protected void handleLoanTableClick(MouseEvent event) {
    if (loanedBooksTable.getSelectionModel().getSelectedItem() != null) {
      selectedLoan = loanedBooksTable.getSelectionModel().getSelectedItem();
      deleteLoanButton.setDisable(false); // Enable button when loan is selected
    } else {
      deleteLoanButton.setDisable(true); // Disable button if no loan selected
      selectedLoan = null;
    }
  }

  private void refreshLoanedBooksTable() {
    try {
      loanData.clear();
      List<Loan> allLoans = LoanService.getAllLoans();
      loanData.addAll(allLoans);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @FXML
  protected void switchToMenu() throws IOException {
    App.setRoot("menu");
  }
}