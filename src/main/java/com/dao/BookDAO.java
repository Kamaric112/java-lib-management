package com.dao;

import com.model.Book;
import java.sql.SQLException;
import java.util.List;

public interface BookDAO {
  Book getBookById(int id) throws SQLException;

  List<Book> getAllBooks() throws SQLException;

  int addBook(Book book) throws SQLException;

  boolean updateBook(Book book) throws SQLException;

  boolean deleteBook(int id) throws SQLException;

  boolean isBookAvailable(int bookId) throws SQLException;

  boolean updateBookAvailability(int bookId, boolean available) throws SQLException;

  List<Book> searchBooksByTitle(String title) throws SQLException;

  List<Book> searchBooksByAuthor(String author) throws SQLException;

  List<Book> searchBooksByGenre(String genre) throws SQLException;
}