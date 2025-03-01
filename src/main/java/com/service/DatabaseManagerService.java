package com.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.File;

public class DatabaseManagerService {
    
    private static final String DB_URL = "jdbc:sqlite:library.db";
    private static Connection connection;
    
    /**
     * Establishes connection to the SQLite database
     * Creates the database file if it doesn't exist
     * 
     * @return true if connection is successful, false otherwise
     */
    public static boolean connect() {
        try {
            // Create database directory if it doesn't exist
            File dbFile = new File("library.db");
            boolean newDatabase = !dbFile.exists();
            
            // Establish connection
            connection = DriverManager.getConnection(DB_URL);
            
            // If this is a new database, create initial tables
            if (newDatabase) {
                initializeDatabase();
            }
            
            return true;
        } catch (SQLException e) {
            System.err.println("Database connection error: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Creates initial database tables
     */
    private static void initializeDatabase() throws SQLException {
        try (Statement statement = connection.createStatement()) {
            // Create books table
            statement.execute(
                "CREATE TABLE IF NOT EXISTS books (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, " +
                "author TEXT NOT NULL, " +
                "year INTEGER, " +
                "isbn TEXT UNIQUE" +
                ")");
            
            // Create users table
            statement.execute(
                "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT NOT NULL UNIQUE, " +
                "password TEXT NOT NULL, " +
                "is_admin INTEGER DEFAULT 0" +
                ")");
                
            // Create a default admin user
            statement.execute(
                "INSERT OR IGNORE INTO users (username, password, is_admin) " +
                "VALUES ('admin', 'admin123', 1)");
        }
    }
    
    /**
     * Register a new user
     * 
     * @param username The username
     * @param password The password (plain text for now, but should be hashed in production)
     * @return true if registration is successful, false otherwise
     */
    public static boolean registerUser(String username, String password) throws SQLException {
        String sql = "INSERT INTO users (username, password, is_admin) VALUES (?, ?, 0)";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password); // In a real app, hash this password!
            
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            // If username already exists, this will throw a constraint violation
            System.err.println("Error registering user: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Verify user credentials
     * 
     * @param username The username
     * @param password The password to verify
     * @return true if credentials are valid, false otherwise
     */
    public static boolean verifyUser(String username, String password) {
        String sql = "SELECT id FROM users WHERE username = ? AND password = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password); // In a real app, verify against hashed password
            
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // If any row is returned, credentials are valid
            }
        } catch (SQLException e) {
            System.err.println("Error verifying user: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get the database connection object
     */
    public static Connection getConnection() {
        return connection;
    }
    
    /**
     * Close the database connection
     */
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