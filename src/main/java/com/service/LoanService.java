package com.service;

import com.dao.LoanDAO;
import com.dao.impl.LoanDAOImpl;
import com.model.Loan;
import java.sql.*;
import java.time.LocalDate;
import java.util.List;

public class LoanService {

    private static LoanDAO loanDAO = new LoanDAOImpl();

    public static int createLoan(int userId, int bookId, LocalDate loanDate, LocalDate dueDate) throws SQLException {
        if (!BookService.isBookAvailable(bookId)) {
            return -2;
        }
        return loanDAO.createLoan(userId, bookId, loanDate, dueDate);
    }

    public static boolean returnBook(int loanId) throws SQLException {
        Loan loan = getLoanById(loanId);
        if (loan != null) {
            BookService.updateBookAvailability(loan.getBookId(), true);
        }
        return loanDAO.returnBook(loanId);
    }

    public static Loan getLoanById(int id) throws SQLException {
        return loanDAO.getLoanById(id);
    }

    public static List<Loan> getActiveLoans() throws SQLException {
        return loanDAO.getActiveLoans();
    }

    public static List<Loan> getUserLoans(int userId) throws SQLException {
        return loanDAO.getUserLoans(userId);
    }

    public static List<Loan> getAllLoans() throws SQLException {
        return loanDAO.getAllLoans();
    }

    public static boolean deleteLoan(int loanId) throws SQLException {
        Loan loan = getLoanById(loanId);
        if (loan != null) {
            BookService.updateBookAvailability(loan.getBookId(), true);
        }
        return loanDAO.deleteLoan(loanId);
    }
}
