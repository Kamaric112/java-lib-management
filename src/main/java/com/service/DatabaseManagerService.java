package com.service;

import java.sql.*;
import java.io.File;

public class DatabaseManagerService {

    private static final String DB_URL = "jdbc:sqlite:library.db";
    private static Connection connection;

    public static boolean connect() {
        try {
            File dbFile = new File("library.db");
            boolean newDatabase = !dbFile.exists();
            connection = DriverManager.getConnection(DB_URL);
            if (newDatabase) {
                initializeDatabase();
            }
            return true;
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            return false;
        }
    }

    private static void initializeDatabase() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(
                    "CREATE TABLE IF NOT EXISTS books (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "title TEXT NOT NULL, " +
                            "author TEXT NOT NULL, " +
                            "isbn TEXT UNIQUE, " +
                            "publication_year INTEGER, " +
                            "genre TEXT, " +
                            "available INTEGER DEFAULT 1" +
                            ")");

            statement.execute(
                    "CREATE TABLE IF NOT EXISTS users (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "username TEXT NOT NULL, " +
                            "password TEXT NOT NULL, " +
                            "role TEXT DEFAULT 'STUDENT'" +
                            ")");

            statement.execute(
                    "CREATE TABLE IF NOT EXISTS loans (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                            "user_id INTEGER NOT NULL, " +
                            "book_id INTEGER NOT NULL, " +
                            "loan_date TEXT NOT NULL, " +
                            "due_date TEXT NOT NULL, " +
                            "return_date TEXT, " +
                            "active INTEGER DEFAULT 1, " +
                            "FOREIGN KEY (user_id) REFERENCES users (id), " +
                            "FOREIGN KEY (book_id) REFERENCES books (id)" +
                            ")");

            statement.execute(
                    "INSERT OR IGNORE INTO users (username, password, role) " +
                            "VALUES ('admin', '123', 'ADMIN')");
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
}