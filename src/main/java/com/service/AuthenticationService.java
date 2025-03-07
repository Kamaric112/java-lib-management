package com.service;

import com.model.User;
import java.sql.*;

public class AuthenticationService {

    /**
     * Authenticates a user with provided username and password
     * 
     * @param username The user's username'
     * @param password The user's password
     * @return User object if authentication succeeds, null otherwise
     * @throws SQLException if database error occurs
     */
    public User authenticateUser(String username, String password) throws SQLException {
        Connection connection = DatabaseManagerService.getConnection();
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        User.Role.valueOf(rs.getString("role")));
            }
            return null;
        }
    }

    /**
     * Registers a new user in the system
     * 
     * @param user The user to register
     * @return The ID of the newly registered user
     * @throws SQLException if database error occurs
     */
    public int registerUser(User user) throws SQLException {
        Connection connection = DatabaseManagerService.getConnection();
        String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole().toString());

            statement.executeUpdate();
            ResultSet rs = connection.createStatement().executeQuery("SELECT last_insert_rowid()");
            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    /**
     * Gets a user by their ID
     * 
     * @param id The user ID
     * @return User object if found, null otherwise
     * @throws SQLException if database error occurs
     */
    public User getUserById(int id) throws SQLException {
        Connection connection = DatabaseManagerService.getConnection();
        String sql = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getString("username"),
                        rs.getString("password"),
                        User.Role.valueOf(rs.getString("role")));
            }
            return null;
        }
    }

    /**
     * Gets a user's ID by their username
     * 
     * @param username The user's username
     * @return The user ID if found, -1 otherwise
     * @throws SQLException if database error occurs
     */
    public int getUserIdByUsername(String username) throws SQLException {
        Connection connection = DatabaseManagerService.getConnection();
        String sql = "SELECT id FROM users WHERE username = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);

            ResultSet rs = statement.executeQuery();
            return rs.next() ? rs.getInt("id") : -1;
        }
    }
}
