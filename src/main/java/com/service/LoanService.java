package com.service;

import com.model.Loan;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class that handles business logic for Loan operations.
 * This provides a layer of abstraction between controllers and the database.
 */
public class LoanService {

    /**
     * Creates a new loan record in the database.
     *
     * @param userId   the ID of the user borrowing the book
     * @param bookId   the ID of the book being borrowed
     * @param loanDate the loan date
     * @param dueDate  the due date
     * @return the ID of the newly created loan, or -1 if operation failed
     * @throws SQLException if a database error occurs
     */
    public static int createLoan(int userId, int bookId, LocalDate loanDate, LocalDate dueDate) throws SQLException {
        Connection connection = DatabaseManagerService.getConnection();

        // Check book availability first
        if (!BookService.isBookAvailable(bookId)) {
            return -2; // Return -2 to indicate book not available
        }

        String sql = "INSERT INTO loans (user_id, book_id, loan_date, due_date, active) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, userId);
            statement.setInt(2, bookId);
            statement.setString(3, loanDate.toString());
            statement.setString(4, dueDate.toString());
            statement.setInt(5, 1); // Loan is active by default

            statement.executeUpdate();

            // Update book availability
            BookService.updateBookAvailability(bookId, false);

            ResultSet rs = connection.createStatement().executeQuery("SELECT last_insert_rowid()");
            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    /**
     * Marks a loan as returned in the database.
     *
     * @param loanId the ID of the loan to return
     * @return true if the loan was returned successfully
     * @throws SQLException if a database error occurs
     */
    public static boolean returnBook(int loanId) throws SQLException {
        Connection connection = DatabaseManagerService.getConnection();
        String sql = "UPDATE loans SET return_date = ?, active = 0 WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, LocalDate.now().toString());
            statement.setInt(2, loanId);

            int result = statement.executeUpdate();
            if (result > 0) {
                // Get the book ID from the loan
                Loan loan = getLoanById(loanId);
                if (loan != null) {
                    // Update book availability
                    BookService.updateBookAvailability(loan.getBookId(), true);
                }
                return true;
            }
            return false;
        }
    }

    /**
     * Retrieves a loan by its ID.
     *
     * @param id the ID of the loan to retrieve
     * @return the loan object if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public static Loan getLoanById(int id) throws SQLException {
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

    /**
     * Retrieves all active loans from the database.
     *
     * @return a list of all active loans
     * @throws SQLException if a database error occurs
     */
    public static List<Loan> getActiveLoans() throws SQLException {
        Connection connection = DatabaseManagerService.getConnection();
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans WHERE active = 1";
        try (Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                loans.add(createLoanFromResultSet(rs));
            }
        }
        return loans;
    }

    /**
     * Retrieves all loans for a specific user.
     *
     * @param userId the ID of the user
     * @return a list of all loans for the user
     * @throws SQLException if a database error occurs
     */
    public static List<Loan> getUserLoans(int userId) throws SQLException {
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

    /**
     * Helper method to create a Loan object from a ResultSet.
     *
     * @param rs the ResultSet containing loan data
     * @return a new Loan object
     * @throws SQLException if a database error occurs
     */
    private static Loan createLoanFromResultSet(ResultSet rs) throws SQLException {
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

    /**
     * Retrieves all loans from the database.
     *
     * @return a list of all loans
     * @throws SQLException if a database error occurs
     */
    public static List<Loan> getAllLoans() throws SQLException {
        Connection connection = DatabaseManagerService.getConnection();
        List<Loan> loans = new ArrayList<>();
        String sql = "SELECT * FROM loans"; // Select all loans
        try (Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                loans.add(createLoanFromResultSet(rs));
            }
        }
        return loans;
    }

    /**
     * Deletes a loan record from the database and updates book availability.
     *
     * @param loanId the ID of the loan to delete
     * @return true if the loan was successfully deleted
     * @throws SQLException if a database error occurs
     */
    public static boolean deleteLoan(int loanId) throws SQLException {
        Connection connection = DatabaseManagerService.getConnection();
        String sql = "DELETE FROM loans WHERE id = ?";

        Loan loan = getLoanById(loanId); // Get loan to update book availability later

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, loanId);
            int result = statement.executeUpdate();
            if (result > 0) {
                // Update book availability to true since loan is deleted
                if (loan != null) {
                    BookService.updateBookAvailability(loan.getBookId(), true);
                }
                return true;
            }
            return false;
        }
    }
}
