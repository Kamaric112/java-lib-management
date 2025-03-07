package com.model;

public class Book {
    private String title;
    private String author;
    private String isbn;
    private int publicationYear;
    private boolean available;
    private String genre;
    private int id;

    public Book() {
        this.available = true;
    }

    /**
     * Parameterized constructor.
     *
     * @param title           The book's title
     * @param author          The book's author
     * @param isbn            The book's ISBN
     * @param publicationYear The book's publication year
     * @param genre           The book's genre
     */
    public Book(String title, String author, String isbn, int publicationYear, String genre) {
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.genre = genre;
        this.available = true;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean checkOut() {
        if (available) {
            available = false;
            return true;
        }
        return false;
    }

    public void returnBook() {
        available = true;
    }

    @Override
    public String toString() {
        return "Book{" +
                ", id='" + id + '\'' +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", isbn='" + isbn + '\'' +
                ", publicationYear=" + publicationYear +
                ", available=" + available +
                ", genre='" + genre + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
