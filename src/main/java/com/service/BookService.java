package com.service;

import com.dao.BookDAO;
import com.dao.impl.BookDAOImpl;
import com.model.Book;
import java.sql.SQLException;
import java.util.List;

public class BookService {

    private static BookDAO bookDAO = new BookDAOImpl();

    public static List<Book> getAllBooks() throws SQLException {
        return bookDAO.getAllBooks();
    }

    public static Book getBookById(int id) throws SQLException {
        return bookDAO.getBookById(id);
    }

    public static int addBook(Book book) throws SQLException {
        return bookDAO.addBook(book);
    }

    public static boolean updateBook(Book book) throws SQLException {
        return bookDAO.updateBook(book);
    }

    public static boolean deleteBook(int id) throws SQLException {
        return bookDAO.deleteBook(id);
    }

    public static boolean isBookAvailable(int bookId) throws SQLException {
        return bookDAO.isBookAvailable(bookId);
    }

    public static boolean updateBookAvailability(int bookId, boolean available) throws SQLException {
        return bookDAO.updateBookAvailability(bookId, available);
    }

    public static List<Book> searchBooksByTitle(String title) throws SQLException {
        return bookDAO.searchBooksByTitle(title);
    }

    public static List<Book> searchBooksByAuthor(String author) throws SQLException {
        return bookDAO.searchBooksByAuthor(author);
    }

    public static List<Book> searchBooksByGenre(String genre) throws SQLException {
        return bookDAO.searchBooksByGenre(genre);
    }

    public static List<Book> filterBooksByGenre(String genre) throws SQLException {
        return bookDAO.searchBooksByGenre(genre); // Assuming searchBooksByGenre can filter by genre
    }
}
