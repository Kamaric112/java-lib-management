package com.controller;

import com.app.App;
import com.model.Loan;
import com.service.LoanService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

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
  private TextField userIdField;
  @FXML
  private TextField bookIdField;
  @FXML
  private DatePicker loanDateField;
  @FXML
  private DatePicker dueDateField;

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
    } catch (SQLException e) {
      e.printStackTrace(); // Handle error properly later
    }
  }

  @FXML
  protected void handleLoanBook(ActionEvent event) {
    try {
      int userId = Integer.parseInt(userIdField.getText());
      int bookId = Integer.parseInt(bookIdField.getText());
      LocalDate loanDate = loanDateField.getValue();
      LocalDate dueDate = dueDateField.getValue();

      if (loanDate != null && dueDate != null) {
        int loanId = LoanService.createLoan(userId, bookId, loanDate, dueDate);
        if (loanId != -1) {
          System.out.println("Loan created successfully with ID: " + loanId);
          // Refresh the table
          refreshLoanedBooksTable();
        } else {
          System.err.println("Failed to create loan.");
        }
      } else {
        System.err.println("Please select both loan date and due date.");
      }
    } catch (NumberFormatException e) {
      System.err.println("Invalid User ID or Book ID format.");
    } catch (SQLException e) {
      System.err.println("Database error: " + e.getMessage());
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
  private void switchToMenu() throws IOException {
    App.setRoot("menu");
  }
}