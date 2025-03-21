package com.service;

import com.dao.UserDAO;
import com.dao.impl.UserDAOImpl;
import com.model.User;
import java.sql.SQLException;
import java.util.List;

public class UserService {
  private static UserDAO userDAO = new UserDAOImpl();
  private static User currentUser;

  public static void setCurrentUser(User user) {
    currentUser = user;
  }

  public static User getCurrentUser() {
    return currentUser;
  }

  public static boolean isCurrentUser(int userId) {
    return currentUser != null && currentUser.getId() == userId;
  }

  public static List<User> getAllUsers() throws SQLException {
    return userDAO.getAllUsers();
  }

  public static User getUserByUsername(String username) throws SQLException {
    return userDAO.getUserByUsername(username);
  }

  public static User getUserById(int id) throws SQLException {
    return userDAO.getUserById(id);
  }

  public static boolean updateUser(User user) throws SQLException {
    if (user == null) {
      throw new IllegalArgumentException("User cannot be null");
    }

    User existingUser = userDAO.getUserById(user.getId());
    if (existingUser == null) {
      throw new IllegalArgumentException("User not found");
    }

    if (!existingUser.getUsername().equals(user.getUsername())
        && userDAO.getUserByUsername(user.getUsername()) != null) {
      throw new IllegalArgumentException("Username already exists");
    }

    return userDAO.updateUser(user);
  }

  public static boolean deleteUser(int userId) throws SQLException {
    if (currentUser == null) {
      throw new IllegalStateException("No user is currently logged in");
    }

    if (currentUser.getId() == userId) {
      throw new IllegalArgumentException("Cannot delete your own account");
    }

    User userToDelete = userDAO.getUserById(userId);
    if (userToDelete == null) {
      throw new IllegalArgumentException("User not found");
    }

    return userDAO.deleteUser(userId);
  }
}