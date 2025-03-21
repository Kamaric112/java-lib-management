package com.controller;

import com.model.Book;
import com.model.Loan;
import com.model.User;
import com.service.LoanService;
import com.service.UserService;
import com.service.BookService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.io.IOException;
import com.app.App;

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
  private ComboBox<User> userIdComboBox;
  @FXML
  private ComboBox<Book> bookIdComboBox;
  @FXML
  private DatePicker loanDateField;
  @FXML
  private DatePicker dueDateField;
  @FXML
  private Button deleteLoanButton;
  private Loan selectedLoan;

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

      // Populate User ComboBox with User objects
      List<User> users = UserService.getAllUsers();
      userIdComboBox.setItems(FXCollections.observableArrayList(users));
      userIdComboBox.setButtonCell(new ListCell<User>() {
        @Override
        protected void updateItem(User user, boolean empty) {
          super.updateItem(user, empty);
          setText(empty ? "" : user.getUsername());
        }
      });
      userIdComboBox.setCellFactory(cell -> new ListCell<User>() {
        @Override
        protected void updateItem(User user, boolean empty) {
          super.updateItem(user, empty);
          setText(empty ? "" : user.getUsername());
        }
      });

      List<Book> books = BookService.getAllBooks();
      bookIdComboBox.setItems(FXCollections.observableArrayList(books));
      bookIdComboBox.setButtonCell(new ListCell<Book>() {
        @Override
        protected void updateItem(Book book, boolean empty) {
          super.updateItem(book, empty);
          setText(empty ? "" : book.getTitle());
        }
      });
      bookIdComboBox.setCellFactory(cell -> new ListCell<Book>() {
        @Override
        protected void updateItem(Book book, boolean empty) {
          super.updateItem(book, empty);
          setText(empty ? "" : book.getTitle());
        }
      });

      deleteLoanButton.setDisable(true);

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @FXML
  protected void handleLoanBook(ActionEvent event) {
    try {
      User selectedUser = userIdComboBox.getValue();
      Book selectedBook = bookIdComboBox.getValue();
      LocalDate loanDate = loanDateField.getValue();
      LocalDate dueDate = dueDateField.getValue();

      if (selectedUser != null && selectedBook != null && loanDate != null && dueDate != null) {
        int loanId = LoanService.createLoan(selectedUser.getId(), selectedBook.getId(), loanDate, dueDate);
        if (loanId != -1) {
          // Update book availability to false (unavailable)
          if (BookService.updateBookAvailability(selectedBook.getId(), false)) {
            System.out.println("Loan created successfully with ID: " + loanId);
            refreshLoanedBooksTable();
          } else {
            System.err.println("Failed to update book availability.");
          }
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