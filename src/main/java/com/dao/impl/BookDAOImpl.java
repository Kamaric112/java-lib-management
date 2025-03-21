package com.dao.impl;

import com.dao.BookDAO;
import com.model.Book;
import com.service.DatabaseManagerService;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAOImpl implements BookDAO {

  @Override
  public Book getBookById(int id) throws SQLException {
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

  @Override
  public List<Book> getAllBooks() throws SQLException {
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

  @Override
  public int addBook(Book book) throws SQLException {
    Connection connection = DatabaseManagerService.getConnection();
    String sql = "INSERT INTO books (title, author, isbn, genre, available, publication_year) VALUES (?, ?, ?, ?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      statement.setString(1, book.getTitle());
      statement.setString(2, book.getAuthor());
      statement.setString(3, book.getIsbn());
      statement.setString(4, book.getGenre());
      statement.setBoolean(5, book.isAvailable());
      statement.setInt(6, book.getPublicationYear());
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
  public boolean updateBook(Book book) throws SQLException {
    Connection connection = DatabaseManagerService.getConnection();
    String sql = "UPDATE books SET title = ?, author = ?, isbn = ?, genre = ?, available = ?, publication_year = ? WHERE id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, book.getTitle());
      statement.setString(2, book.getAuthor());
      statement.setString(3, book.getIsbn());
      statement.setString(4, book.getGenre());
      statement.setBoolean(5, book.isAvailable());
      statement.setInt(6, book.getPublicationYear());
      statement.setInt(7, book.getId());
      int updatedRows = statement.executeUpdate();
      return updatedRows > 0;
    }
  }

  @Override
  public boolean deleteBook(int id) throws SQLException {
    Connection connection = DatabaseManagerService.getConnection();
    String sql = "DELETE FROM books WHERE id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, id);
      int deletedRows = statement.executeUpdate();
      return deletedRows > 0;
    }
  }

  @Override
  public boolean isBookAvailable(int bookId) throws SQLException {
    Connection connection = DatabaseManagerService.getConnection();
    String sql = "SELECT available FROM books WHERE id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, bookId);
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        return resultSet.getBoolean("available");
      }
      return false;
    }
  }

  @Override
  public boolean updateBookAvailability(int bookId, boolean available) throws SQLException {
    Connection connection = DatabaseManagerService.getConnection();
    String sql = "UPDATE books SET available = ? WHERE id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setBoolean(1, available);
      statement.setInt(2, bookId);
      int updatedRows = statement.executeUpdate();
      return updatedRows > 0;
    }
  }

  @Override
  public List<Book> searchBooksByTitle(String title) throws SQLException {
    Connection connection = DatabaseManagerService.getConnection();
    List<Book> books = new ArrayList<>();
    String sql = "SELECT * FROM books WHERE title LIKE ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, "%" + title + "%");
      ResultSet rs = statement.executeQuery();
      while (rs.next()) {
        books.add(createBookFromResultSet(rs));
      }
    }
    return books;
  }

  @Override
  public List<Book> searchBooksByAuthor(String author) throws SQLException {
    Connection connection = DatabaseManagerService.getConnection();
    List<Book> books = new ArrayList<>();
    String sql = "SELECT * FROM books WHERE author LIKE ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, "%" + author + "%");
      ResultSet rs = statement.executeQuery();
      while (rs.next()) {
        books.add(createBookFromResultSet(rs));
      }
    }
    return books;
  }

  @Override
  public List<Book> searchBooksByGenre(String genre) throws SQLException {
    Connection connection = DatabaseManagerService.getConnection();
    List<Book> books = new ArrayList<>();
    String sql = "SELECT * FROM books WHERE genre LIKE ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, "%" + genre + "%");
      ResultSet rs = statement.executeQuery();
      while (rs.next()) {
        books.add(createBookFromResultSet(rs));
      }
    }
    return books;
  }

  private Book createBookFromResultSet(ResultSet rs) throws SQLException {
    Book book = new Book(
        rs.getString("title"),
        rs.getString("author"),
        rs.getString("isbn"),
        rs.getInt("publication_year"),
        rs.getString("genre"));
    book.setId(rs.getInt("id"));
    book.setAvailable(rs.getBoolean("available"));
    return book;
  }
}