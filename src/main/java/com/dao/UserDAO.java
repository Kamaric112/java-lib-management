package com.dao;

import com.model.User;
import java.sql.SQLException;
import java.util.List;

public interface UserDAO {
  User getUserById(int id) throws SQLException;

  User getUserByUsername(String username) throws SQLException;

  List<User> getAllUsers() throws SQLException;

  int addUser(User user) throws SQLException;

  boolean updateUser(User user) throws SQLException;

  boolean deleteUser(int id) throws SQLException;
}