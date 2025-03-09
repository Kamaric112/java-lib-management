package com.dao.impl;

import com.dao.UserDAO;
import com.model.User;
import com.service.DatabaseManagerService;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

  @Override
  public User getUserById(int id) throws SQLException {
    Connection connection = DatabaseManagerService.getConnection();
    String sql = "SELECT * FROM users WHERE id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, id);
      ResultSet rs = statement.executeQuery();
      if (rs.next()) {
        return createUserFromResultSet(rs);
      }
      return null;
    }
  }

  @Override
  public User getUserByUsername(String username) throws SQLException {
    Connection connection = DatabaseManagerService.getConnection();
    String sql = "SELECT * FROM users WHERE username = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, username);
      ResultSet rs = statement.executeQuery();
      if (rs.next()) {
        return createUserFromResultSet(rs);
      }
      return null;
    }
  }

  @Override
  public List<User> getAllUsers() throws SQLException {
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

  @Override
  public int addUser(User user) throws SQLException {
    Connection connection = DatabaseManagerService.getConnection();
    String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
    try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
      statement.setString(1, user.getUsername());
      statement.setString(2, user.getPassword());
      statement.setString(3, user.getRole().toString()); // Store role as String
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
  public boolean updateUser(User user) throws SQLException {
    Connection connection = DatabaseManagerService.getConnection();
    String sql = "UPDATE users SET username = ?, password = ?, role = ? WHERE id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setString(1, user.getUsername());
      statement.setString(2, user.getPassword());
      statement.setString(3, user.getRole().toString()); // Store role as String
      statement.setInt(4, user.getId());
      int updatedRows = statement.executeUpdate();
      return updatedRows > 0;
    }
  }

  @Override
  public boolean deleteUser(int id) throws SQLException {
    Connection connection = DatabaseManagerService.getConnection();
    String sql = "DELETE FROM users WHERE id = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
      statement.setInt(1, id);
      int deletedRows = statement.executeUpdate();
      return deletedRows > 0;
    }
  }

  private User createUserFromResultSet(ResultSet rs) throws SQLException {
    User user = new User(
        rs.getString("username"),
        rs.getString("password"),
        User.Role.valueOf(rs.getString("role")) // Convert role String to enum
    );
    user.setId(rs.getInt("id"));
    return user;
  }
}