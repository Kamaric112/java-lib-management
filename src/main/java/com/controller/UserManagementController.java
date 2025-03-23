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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
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
      e.printStackTrace();
    }
  }

  @FXML
  private void handleEditUser() {
    User selectedUser = userTableView.getSelectionModel().getSelectedItem();
    if (selectedUser == null) {
      showAlert(AlertType.WARNING, "No Selection", "Please select a user to edit.");
      return;
    }

    Dialog<User> dialog = createUserEditDialog(selectedUser);
    Optional<User> result = dialog.showAndWait();

    result.ifPresent(editedUser -> {
      try {
        if (UserService.updateUser(editedUser)) {
          refreshUserList();
          showAlert(AlertType.INFORMATION, "Success", "User updated successfully.");
        }
      } catch (IllegalArgumentException e) {
        showAlert(AlertType.ERROR, "Error", e.getMessage());
      } catch (SQLException e) {
        showAlert(AlertType.ERROR, "Database Error", "Error updating user: " + e.getMessage());
      }
    });
  }

  @FXML
  private void handleDeleteUser() {
    User selectedUser = userTableView.getSelectionModel().getSelectedItem();
    if (selectedUser == null) {
      showAlert(AlertType.WARNING, "No Selection", "Please select a user to delete.");
      return;
    }

    Alert confirmDialog = new Alert(AlertType.CONFIRMATION);
    confirmDialog.setTitle("Confirm Delete");
    confirmDialog.setHeaderText("Delete User");
    confirmDialog.setContentText("Are you sure you want to delete user: " + selectedUser.getUsername() + "?");

    Optional<ButtonType> result = confirmDialog.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
      try {
        if (UserService.deleteUser(selectedUser.getId())) {
          refreshUserList();
          showAlert(AlertType.INFORMATION, "Success", "User deleted successfully.");
        }
      } catch (IllegalArgumentException e) {
        showAlert(AlertType.ERROR, "Error", e.getMessage());
      } catch (SQLException e) {
        showAlert(AlertType.ERROR, "Database Error", "Error deleting user: " + e.getMessage());
      }
    }
  }

  private Dialog<User> createUserEditDialog(User user) {
    Dialog<User> dialog = new Dialog<>();
    dialog.setTitle("Edit User");
    dialog.setHeaderText("Edit user information");

    ButtonType saveButtonType = new ButtonType("Save", ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(20, 150, 10, 10));

    TextField username = new TextField(user.getUsername());
    PasswordField password = new PasswordField();
    ComboBox<User.Role> role = new ComboBox<>(FXCollections.observableArrayList(User.Role.values()));
    role.setValue(user.getRole());

    grid.add(new javafx.scene.control.Label("Username:"), 0, 0);
    grid.add(username, 1, 0);
    grid.add(new javafx.scene.control.Label("Password:"), 0, 1);
    grid.add(password, 1, 1);
    grid.add(new javafx.scene.control.Label("Role:"), 0, 2);
    grid.add(role, 1, 2);

    dialog.getDialogPane().setContent(grid);

    dialog.setResultConverter(dialogButton -> {
      if (dialogButton == saveButtonType) {
        User editedUser = new User(username.getText(),
            password.getText().isEmpty() ? user.getPassword() : password.getText(),
            role.getValue());
        editedUser.setId(user.getId());
        return editedUser;
      }
      return null;
    });

    return dialog;
  }

  private void showAlert(AlertType type, String title, String content) {
    Alert alert = new Alert(type);
    alert.setTitle(title);
    alert.setContentText(content);
    alert.showAndWait();
  }

  private void refreshUserList() throws SQLException {
    userList.clear();
    userList.addAll(UserService.getAllUsers());
  }

  @FXML
  private void switchToMainMenu() throws IOException {
    App.setRoot("menu");
  }
}