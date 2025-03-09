package com.service;

import com.dao.UserDAO;
import com.dao.impl.UserDAOImpl;
import com.model.User;
import java.sql.SQLException;
import java.util.List;

public class UserService {

  private static UserDAO userDAO = new UserDAOImpl();

  public static List<User> getAllUsers() throws SQLException {
    return userDAO.getAllUsers();
  }

  public static User authenticate(String username, String password) throws SQLException {
    User user = userDAO.getUserByUsername(username);
    if (user != null && user.getPassword().equals(password)) {
      return user;
    }
    return null;
  }

  public static User registerUser(String username, String password, User.Role role) throws SQLException {
    if (userDAO.getUserByUsername(username) != null) {
      return null;
    }
    User newUser = new User(username, password, role);
    int userId = userDAO.addUser(newUser);
    if (userId != -1) {
      return userDAO.getUserById(userId);
    } else {
      return null;
    }
  }

  public static User getUserByUsername(String username) throws SQLException {
    return userDAO.getUserByUsername(username);
  }

  public static User getUserById(int id) throws SQLException {
    return userDAO.getUserById(id);
  }
}