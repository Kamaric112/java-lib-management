package com.model;

public class User {
  private int id;
  private String username;
  private String password;
  private Role role;

  public enum Role {
    ADMIN,
    STUDENT
  }

  public User(int id, String username, String password) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.role = Role.STUDENT; // Default role
  }

  public User(int id, String username, String password, Role role) {
    this.id = id;
    this.username = username;
    this.password = password;
    this.role = role;
  }

  public User(String username, String password, Role role) { // Constructor without id for registration
    this.username = username;
    this.password = password;
    this.role = role;
  }

  public User(String username, String password) { // Constructor without id for registration
    this.username = username;
    this.password = password;
    this.role = Role.STUDENT;
  }

  public int getId() {
    return id;
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
