package com.model;

public class User {
  private String name;
  private String email;
  private String password;
  private Role role;  // Added role field
  
  public enum Role {
    ADMIN,
    STUDENT
  }

  public User(String name, String email, String password) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.role = Role.STUDENT;  // Default role
  }

  // Additional constructor with role parameter
  public User(String name, String email, String password, Role role) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.role = role;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
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
    return "User [name=" + name + ", email=" + email + ", role=" + role + "]";
  }
}
