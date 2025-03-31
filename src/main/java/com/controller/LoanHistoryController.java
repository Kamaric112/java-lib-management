package com.controller;

import com.model.Book;
import com.model.Loan;
import com.service.BookService;
import com.service.LoanService;
import com.service.UserService;
import com.model.User;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanHistoryController {

  @FXML
  private Label titleLabel;

  @FXML
  private TableView<LoanDisplayRecord> loanHistoryTable;

  @FXML
  private TableColumn<LoanDisplayRecord, String> bookTitleColumn;

  @FXML
  private TableColumn<LoanDisplayRecord, String> loanDateColumn;

  @FXML
  private TableColumn<LoanDisplayRecord, String> dueDateColumn;

  @FXML
  private TableColumn<LoanDisplayRecord, String> returnDateColumn;

  @FXML
  private TableColumn<LoanDisplayRecord, String> statusColumn;

  private int targetUserId;

  @FXML
  public void initialize() {
    bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
    loanDateColumn.setCellValueFactory(new PropertyValueFactory<>("loanDate"));
    dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
    returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
    statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
  }

  public void initializeData(int userId) {
    this.targetUserId = userId;
    loadLoanHistory();
    updateTitleLabel();
  }

  private void updateTitleLabel() {
    try {
      User user = UserService.getUserById(targetUserId);
      if (user != null) {
        titleLabel.setText("Loan History for User: " + user.getUsername() + " (ID: " + targetUserId + ")");
      } else {
        titleLabel.setText("Loan History for User ID: " + targetUserId);
      }
    } catch (SQLException e) {
      titleLabel.setText("Loan History for User ID: " + targetUserId + " (Error fetching username)");
      System.err.println("Error fetching user details: " + e.getMessage());
    }
  }

  private void loadLoanHistory() {
    ObservableList<LoanDisplayRecord> loanRecords = FXCollections.observableArrayList();
    try {
      List<Loan> loans = LoanService.getUserLoans(targetUserId);
      for (Loan loan : loans) {
        Book book = BookService.getBookById(loan.getBookId());
        String bookTitle = (book != null) ? book.getTitle() : "Book ID: " + loan.getBookId() + " (Not Found)";
        loanRecords.add(new LoanDisplayRecord(loan, bookTitle));
      }
      loanHistoryTable.setItems(loanRecords);
    } catch (SQLException e) {
      System.err.println("Error loading loan history: " + e.getMessage());
      showAlert("Database Error", "Failed to load loan history: " + e.getMessage());
    }
  }

  private void showAlert(String title, String content) {
    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
  }

  public static class LoanDisplayRecord {
    private final SimpleStringProperty bookTitle;
    private final SimpleStringProperty loanDate;
    private final SimpleStringProperty dueDate;
    private final SimpleStringProperty returnDate;
    private final SimpleStringProperty status;

    public LoanDisplayRecord(Loan loan, String bookTitle) {
      this.bookTitle = new SimpleStringProperty(bookTitle);
      this.loanDate = new SimpleStringProperty(loan.getLoanDate().toString());
      this.dueDate = new SimpleStringProperty(loan.getDueDate().toString());
      this.returnDate = new SimpleStringProperty(
          loan.getReturnDate() != null ? loan.getReturnDate().toString() : "N/A");

      if (!loan.isActive()) {
        this.status = new SimpleStringProperty("Returned");
      } else if (loan.isOverdue()) {
        this.status = new SimpleStringProperty("Overdue");
      } else {
        this.status = new SimpleStringProperty("Active");
      }
    }

    public String getBookTitle() {
      return bookTitle.get();
    }

    public String getLoanDate() {
      return loanDate.get();
    }

    public String getDueDate() {
      return dueDate.get();
    }

    public String getReturnDate() {
      return returnDate.get();
    }

    public String getStatus() {
      return status.get();
    }
  }
}