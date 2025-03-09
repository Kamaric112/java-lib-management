package com.dao.impl;

import com.dao.LoanDAO;
import com.model.Loan;
import com.service.DatabaseManagerService;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LoanDAOImpl implements LoanDAO {

  @Override
  public Loan getLoanById(int id) throws SQLException {
    Connection connection = DatabaseManagerService.getConnection();
    String sql = "SELECT * FROM loans WHERE id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, id);
      ResultSet rs = statement.executeQuery();
      if (rs.next()) {
        return createLoanFromResultSet(rs);
      }
      return null;
    }
  }

  @Override
  public List<Loan> getAllLoans() throws SQLException {
    Connection connection = DatabaseManagerService.getConnection();
    List<Loan> loans = new ArrayList<>();
    String sql = "SELECT * FROM loans";
    try (Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql)) {
      while (rs.next()) {
        loans.add(createLoanFromResultSet(rs));
      }
    }
    return loans;
  }

  @Override
  public List<Loan> getActiveLoans() throws SQLException {
    Connection connection = DatabaseManagerService.getConnection();
    List<Loan> loans = new ArrayList<>();
    String sql = "SELECT * FROM loans WHERE active = 1";
    try (PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet rs = statement.executeQuery()) {
      while (rs.next()) {
        loans.add(createLoanFromResultSet(rs));
      }
    }
    return loans;
  }

  @Override
  public List<Loan> getUserLoans(int userId) throws SQLException {
    Connection connection = DatabaseManagerService.getConnection();
    List<Loan> loans = new ArrayList<>();
    String sql = "SELECT * FROM loans WHERE user_id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, userId);
      ResultSet rs = statement.executeQuery();
      while (rs.next()) {
        loans.add(createLoanFromResultSet(rs));
      }
    }
    return loans;
  }

  @Override
  public int createLoan(int userId, int bookId, LocalDate loanDate, LocalDate dueDate) throws SQLException {
    Connection connection = DatabaseManagerService.getConnection();
    String sql = "INSERT INTO loans (user_id, book_id, loan_date, due_date, active) VALUES (?, ?, ?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      statement.setInt(1, userId);
      statement.setInt(2, bookId);
      statement.setString(3, loanDate.toString());
      statement.setString(4, dueDate.toString());
      statement.setInt(5, 1); // Loan is active by default
      statement.executeUpdate();
      ResultSet rs = connection.createStatement().executeQuery("SELECT last_insert_rowid()");
      if (rs.next()) {
        return rs.getInt(1);
      } else {
        return -1; // No generated key was returned, or an error occurred
      }
    }
  }

  @Override
  public boolean returnBook(int loanId) throws SQLException {
    Connection connection = DatabaseManagerService.getConnection();
    String sql = "UPDATE loans SET return_date = ?, active = 0 WHERE id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, LocalDate.now().toString());
      statement.setInt(2, loanId);
      int result = statement.executeUpdate();
      return result > 0;
    }
  }

  @Override
  public boolean deleteLoan(int loanId) throws SQLException {
    Connection connection = DatabaseManagerService.getConnection();
    String sql = "DELETE FROM loans WHERE id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, loanId);
      int result = statement.executeUpdate();
      return result > 0;
    }
  }

  private Loan createLoanFromResultSet(ResultSet rs) throws SQLException {
    Loan loan = new Loan(
        rs.getInt("user_id"),
        rs.getInt("book_id"));
    loan.setId(rs.getInt("id"));
    loan.setLoanDate(LocalDate.parse(rs.getString("loan_date")));
    loan.setDueDate(LocalDate.parse(rs.getString("due_date")));
    loan.setActive(rs.getInt("active") == 1);

    String returnDate = rs.getString("return_date");
    if (returnDate != null) {
      loan.setReturnDate(LocalDate.parse(returnDate));
    }
    return loan;
  }
}