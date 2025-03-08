package com.service;

import com.model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {

  /**
   * Retrieves all users from the database.
   *
   * @return a list of all users
   * @throws SQLException if a database error occurs
   */
  public static List<User> getAllUsers() throws SQLException {
    Connection connection = DatabaseManagerService.getConnection();
    List<User> users = new ArrayList<>();
    String sql = "SELECT * FROM users";
    try (Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(sql)) {

      while (rs.next()) {
        users.add(createUserFromResultSet(rs));
      }
    }
    return users;
  }

  /**
   * Helper method to create a User object from a ResultSet.
   *
   * @param rs the ResultSet containing user data
   * @return a new User object
   * @throws SQLException if a database error occurs
   * @throws SQLException if a database error occurs
   */
  private static User createUserFromResultSet(ResultSet rs) throws SQLException {
    int id = rs.getInt("id");
    String username = rs.getString("username");
    String roleStr = rs.getString("role");
    User.Role role = User.Role.STUDENT; // Default to STUDENT
    try {
      role = User.Role.valueOf(roleStr.toUpperCase()); // Parse role from string
    } catch (IllegalArgumentException e) {
      System.err.println("Unknown role: " + roleStr + ". Defaulting to STUDENT.");
    }

    // Use the new constructor
    return new User(id, username, role);
  }
}