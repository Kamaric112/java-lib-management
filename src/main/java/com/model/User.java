package com.model;

public class User {
  private String username;
  private String password;
  private Role role;  // Added role field
  
  public enum Role {
    ADMIN,
    STUDENT
  }

  public User(String username, String password) {
    this.username = username;
    this.password = password;
    this.role = Role.STUDENT;  // Default role
  }

  // Additional constructor with role parameter
  public User(String username, String password, Role role) {
    this.username = username;
    this.password = password;
    this.role = role;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }
  
  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public boolean isAdmin() {
    return role == Role.ADMIN;
  }

  @Override
  public String toString() {
    return "User [username=" + username + ", role=" + role + "]";
  }
}
