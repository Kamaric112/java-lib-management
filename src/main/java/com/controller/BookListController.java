package com.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.app.App;
import com.model.Book;
import com.service.BookService;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ListCell;

public class BookListController {

    @FXML
    private ListView<Book> bookListView;

    @FXML
    private Label totalBooksLabel;

    @FXML
    private Label availableBooksLabel;

    @FXML
    private void initialize() {
        loadBooks();
    }

    @FXML
    private void refreshBookList() {
        loadBooks();
    }

    private void loadBooks() {
        try {
            List<Book> books = BookService.getAllBooks();

            if (books.isEmpty()) {
                addSampleBooks();
                books = BookService.getAllBooks();
            }

            bookListView.getItems().clear();
            bookListView.getItems().addAll(books);

            int totalBooks = books.size();
            int availableBooks = (int) books.stream().filter(Book::isAvailable).count();

            totalBooksLabel.setText(String.valueOf(totalBooks));
            availableBooksLabel.setText(String.valueOf(availableBooks));

            bookListView.setCellFactory(param -> new ListCell<Book>() {
                @Override
                protected void updateItem(Book book, boolean empty) {
                    super.updateItem(book, empty);
                    if (empty || book == null) {
                        setText(null);
                    } else {
                        setText(book.getTitle() + " by " + book.getAuthor() +
                                " (" + book.getPublicationYear() + ")" +
                                (book.isAvailable() ? " - Available" : " - Checked Out"));
                    }
                }
            });
        } catch (SQLException e) {
            showError("Error loading books: " + e.getMessage());
        }
    }

    private void addSampleBooks() {
        try {
            BookService.addBook(new Book("The Great Gatsby", "F. Scott Fitzgerald", "9780743273565", 1925));
            BookService.addBook(new Book("To Kill a Mockingbird", "Harper Lee", "9780061120084", 1960));
            BookService.addBook(new Book("1984", "George Orwell", "9780451524935", 1949));
            BookService.addBook(new Book("The Catcher in the Rye", "J.D. Salinger", "9780316769488", 1951));
            BookService.addBook(new Book("Pride and Prejudice", "Jane Austen", "9780141439518", 1813));
        } catch (SQLException e) {
            showError("Error adding sample books: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void switchToBookManagement() throws IOException {
        App.setRoot("book-management");
    }

    @FXML
    private void switchToMenu() throws IOException {
        App.setRoot("menu");
    }
}