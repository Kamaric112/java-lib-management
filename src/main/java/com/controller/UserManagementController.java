package com.controller;

import com.model.User;
import com.service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import com.app.App;

public class UserManagementController implements Initializable {

  @FXML
  private TableView<User> userTableView;
  @FXML
  private TableColumn<User, Integer> idColumn;
  @FXML
  private TableColumn<User, String> usernameColumn;
  @FXML
  private TableColumn<User, User.Role> roleColumn;

  private ObservableList<User> userList = FXCollections.observableArrayList();

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
    roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

    try {
      List<User> users = UserService.getAllUsers();
      userList.addAll(users);
      userTableView.setItems(userList);
    } catch (SQLException e) {
      e.printStackTrace(); // Handle exception properly later
    }
  }

  @FXML
  private void switchToMainMenu() throws IOException {
    App.setRoot("menu");
  }
}