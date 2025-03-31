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
import javafx.scene.control.ComboBox;
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
    private ComboBox<String> genreComboBox;

    @FXML
    private void initialize() {
        loadGenres();
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

    private void loadGenres() {
        genreComboBox.getItems().addAll("Fiction", "Science Fiction", "Romance");
        genreComboBox.setOnAction(e -> {
            String selectedGenre = genreComboBox.getValue();
            if (selectedGenre != null && !selectedGenre.isEmpty()) {
                loadBooksByGenre(selectedGenre);
            } else {
                loadBooks();
            }
        });
    }

    private void loadBooksByGenre(String genre) {
        try {
            List<Book> books = BookService.filterBooksByGenre(genre);

            if (books.isEmpty()) {
                bookListView.getItems().clear();
                totalBooksLabel.setText("0");
                availableBooksLabel.setText("0");
                showError("No books found for genre: " + genre);
                return;
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
            showError("Error loading books by genre: " + e.getMessage()); // Updated error message
        }
    }

    private void addSampleBooks() {
        try {
            BookService.addBook(new Book("The Great Gatsby", "F. Scott Fitzgerald", "9780743273565", 1925, "Fiction"));
            BookService.addBook(new Book("To Kill a Mockingbird", "Harper Lee", "9780061120084", 1960, "Fiction"));
            BookService.addBook(new Book("The Catcher in the Rye", "J.D. Salinger", "9780316769488", 1951, "Fiction"));
            BookService.addBook(new Book("One Hundred Years of Solitude", "Gabriel García Márquez", "9780060883287",
                    1967, "Fiction"));
            BookService.addBook(new Book("The Lord of the Rings", "J.R.R. Tolkien", "9780618640157", 1954, "Fiction"));
            BookService.addBook(new Book("The Alchemist", "Paulo Coelho", "9780062315007", 1988, "Fiction"));
            BookService.addBook(new Book("The Kite Runner", "Khaled Hosseini", "9781594631931", 2003, "Fiction"));

            BookService.addBook(new Book("1984", "George Orwell", "9780451524935", 1949, "Science Fiction"));
            BookService.addBook(new Book("Dune", "Frank Herbert", "9780441172719", 1965, "Science Fiction"));
            BookService.addBook(new Book("Foundation", "Isaac Asimov", "9780553293357", 1951, "Science Fiction"));
            BookService.addBook(new Book("Neuromancer", "William Gibson", "9780441569595", 1984, "Science Fiction"));
            BookService.addBook(new Book("The Hitchhiker's Guide to the Galaxy", "Douglas Adams", "9780345391803", 1979,
                    "Science Fiction"));
            BookService.addBook(new Book("Brave New World", "Aldous Huxley", "9780060850524", 1932, "Science Fiction"));
            BookService.addBook(new Book("Ready Player One", "Ernest Cline", "9780307887436", 2011, "Science Fiction"));

            BookService.addBook(new Book("Pride and Prejudice", "Jane Austen", "9780141439518", 1813, "Romance"));
            BookService.addBook(new Book("Jane Eyre", "Charlotte Brontë", "9780141441146", 1847, "Romance"));
            BookService.addBook(new Book("Outlander", "Diana Gabaldon", "9780440212560", 1991, "Romance"));
            BookService.addBook(new Book("The Notebook", "Nicholas Sparks", "9780553816716", 1996, "Romance"));
            BookService.addBook(new Book("Me Before You", "Jojo Moyes", "9780143124542", 2012, "Romance"));
            BookService.addBook(
                    new Book("The Time Traveler's Wife", "Audrey Niffenegger", "9780099464464", 2003, "Romance"));
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

    @FXML
    private void deleteBook() {
        Book selectedBook = bookListView.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            showError("Please select a book to delete.");
        } else {
            try {
                BookService.deleteBook(selectedBook.getId());
                loadBooks(); // Refresh with all books
                showAlert("Book deleted successfully.");
            } catch (SQLException e) {
                showError("Error deleting book: " + e.getMessage());
            }
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}