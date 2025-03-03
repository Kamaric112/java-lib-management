package com.service;

import com.model.Book;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class that handles business logic for Book operations.
 * This provides a layer of abstraction between controllers and the database.
 */
public class BookService {
    
    /**
     * Adds a new book to the database.
     * 
     * @param book the book to add
     * @return the ID of the newly added book, or -1 if operation failed
     * @throws SQLException if a database error occurs
     */
    public static int addBook(Book book) throws SQLException {
        Connection connection = DatabaseManagerService.getConnection();
        String sql = "INSERT INTO books (title, author, isbn, publication_year, available) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getIsbn());
            statement.setInt(4, book.getPublicationYear());
            statement.setInt(5, book.isAvailable() ? 1 : 0);
            
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            return rs.next() ? rs.getInt(1) : -1;
        }
    }
    
    /**
     * Updates an existing book in the database.
     * 
     * @param id the ID of the book to update
     * @param book the book object with updated information
     * @return true if the book was updated successfully
     * @throws SQLException if a database error occurs
     */
    public static boolean updateBook(int id, Book book) throws SQLException {
        Connection connection = DatabaseManagerService.getConnection();
        String sql = "UPDATE books SET title = ?, author = ?, isbn = ?, publication_year = ?, available = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getIsbn());
            statement.setInt(4, book.getPublicationYear());
            statement.setInt(5, book.isAvailable() ? 1 : 0);
            statement.setInt(6, id);
            
            return statement.executeUpdate() > 0;
        }
    }
    
    /**
     * Retrieves a book by its ID.
     * 
     * @param id the ID of the book to retrieve
     * @return the book object if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public static Book getBookById(int id) throws SQLException {
        Connection connection = DatabaseManagerService.getConnection();
        String sql = "SELECT * FROM books WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            
            if (rs.next()) {
                Book book = new Book(
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("isbn"),
                    rs.getInt("publication_year")
                );
                book.setAvailable(rs.getInt("available") == 1);
                return book;
            }
            return null;
        }
    }
    
    /**
     * Retrieves all books from the database.
     * 
     * @return a list of all books
     * @throws SQLException if a database error occurs
     */
    public static List<Book> getAllBooks() throws SQLException {
        Connection connection = DatabaseManagerService.getConnection();
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {
            
            while (rs.next()) {
                Book book = new Book(
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("isbn"),
                    rs.getInt("publication_year")
                );
                book.setAvailable(rs.getInt("available") == 1);
                books.add(book);
            }
        }
        return books;
    }
    
    /**
     * Deletes a book from the database.
     * 
     * @param id the ID of the book to delete
     * @return true if the book was deleted successfully
     * @throws SQLException if a database error occurs
     */
    public static boolean deleteBook(int id) throws SQLException {
        Connection connection = DatabaseManagerService.getConnection();
        String sql = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }
    
    /**
     * Updates the availability status of a book.
     * 
     * @param bookId the ID of the book
     * @param available the new availability status
     * @return true if the operation was successful
     * @throws SQLException if a database error occurs
     */
    public static boolean updateBookAvailability(int bookId, boolean available) throws SQLException {
        Connection connection = DatabaseManagerService.getConnection();
        String sql = "UPDATE books SET available = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, available ? 1 : 0);
            statement.setInt(2, bookId);
            return statement.executeUpdate() > 0;
        }
    }
}
