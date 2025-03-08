package com.service;

import com.model.Book;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookService {

    public static List<Book> getAllBooks() throws SQLException {
        Connection connection = DatabaseManagerService.getConnection();
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql)) {
            while (rs.next()) {
                books.add(createBookFromResultSet(rs));
            }
        }
        return books;
    }

    public static Book getBookById(int id) throws SQLException {
        Connection connection = DatabaseManagerService.getConnection();
        String sql = "SELECT * FROM books WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return createBookFromResultSet(rs);
            }
            return null;
        }
    }

    public static int addBook(Book book) throws SQLException {
        Connection connection = DatabaseManagerService.getConnection();
        String sql = "INSERT INTO books (title, author, isbn, publication_year, genre) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getIsbn());
            statement.setInt(4, book.getPublicationYear());
            statement.setString(5, book.getGenre());
            statement.executeUpdate();

            ResultSet generatedKeys = connection.createStatement().executeQuery("SELECT last_insert_rowid()");
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                // Fallback for SQLite which may not properly return generated keys
                Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT last_insert_rowid()");
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
            return -1; // Indicate failure if no ID is returned

        }
    }

    public static boolean updateBook(Book book) throws SQLException {
        Connection connection = DatabaseManagerService.getConnection();
        String sql = "UPDATE books SET title = ?, author = ?, isbn = ?, publication_year = ?, genre = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, book.getTitle());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getIsbn());
            statement.setInt(4, book.getPublicationYear());
            statement.setString(5, book.getGenre());
            statement.setInt(6, book.getId());
            return statement.executeUpdate() > 0;
        }
    }

    public static boolean deleteBook(int id) throws SQLException {
        Connection connection = DatabaseManagerService.getConnection();
        String sql = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    public static void updateBookAvailability(int bookId, boolean available) throws SQLException {
        Connection connection = DatabaseManagerService.getConnection();
        String sql = "UPDATE books SET available = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, available ? 1 : 0);
            statement.setInt(2, bookId);
            statement.executeUpdate();
        }
    }

    // New method to check book availability
    public static boolean isBookAvailable(int bookId) throws SQLException {
        Connection connection = DatabaseManagerService.getConnection();
        String sql = "SELECT available FROM books WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, bookId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt("available") == 1; // 1 for available, 0 for not available
            }
            return false; // Book not found, consider it not available
        }
    }

    private static Book createBookFromResultSet(ResultSet rs) throws SQLException {
        Book book = new Book(); // Use no-argument constructor
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setId(rs.getInt("id"));
        book.setIsbn(rs.getString("isbn"));
        book.setPublicationYear(rs.getInt("publication_year"));
        book.setGenre(rs.getString("genre"));
        book.setAvailable(rs.getInt("available") == 1);
        return book;
    }

    /**
     * Filters books by genre.
     *
     * @param genre The genre to filter by.
     * @return A list of books matching the genre.
     * @throws SQLException if a database error occurs.
     */
    public static List<Book> filterBooksByGenre(String genre) throws SQLException {
        Connection connection = DatabaseManagerService.getConnection();
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE genre = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, genre);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Book book = new Book(
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getInt("publication_year"),
                        rs.getString("genre"));
                book.setId(rs.getInt("id"));
                book.setAvailable(rs.getInt("available") == 1);
                books.add(book);
            }
        }
        return books;
    }
}
