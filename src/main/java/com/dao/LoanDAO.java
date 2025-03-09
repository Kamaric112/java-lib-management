package com.dao;

import com.model.Loan;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface LoanDAO {
  Loan getLoanById(int id) throws SQLException;

  List<Loan> getAllLoans() throws SQLException;

  List<Loan> getActiveLoans() throws SQLException;

  List<Loan> getUserLoans(int userId) throws SQLException;

  int createLoan(int userId, int bookId, LocalDate loanDate, LocalDate dueDate) throws SQLException;

  boolean returnBook(int loanId) throws SQLException;

  boolean deleteLoan(int loanId) throws SQLException;
}