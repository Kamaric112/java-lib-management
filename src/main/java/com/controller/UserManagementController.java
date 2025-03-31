package com.controller;

import com.model.User;
import com.service.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import com.app.App;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class UserManagementController implements Initializable {

  @FXML
  private TableView<User> userTableView;
  @FXML
  private TableColumn<User, Integer> idColumn;
  @FXML
  private TableColumn<User, String> usernameColumn;
  @FXML
  private TableColumn<User, User.Role> roleColumn;
  @FXML
  private Button viewHistoryButton;

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
      showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load user list: " + e.getMessage());
    }
  }

  @FXML
  private void handleEditUser() {
    User selectedUser = userTableView.getSelectionModel().getSelectedItem();
    if (selectedUser == null) {
      showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a user to edit.");
      return;
    }

    Dialog<User> dialog = createUserEditDialog(selectedUser);
    Optional<User> result = dialog.showAndWait();

    result.ifPresent(editedUser -> {
      try {
        if (UserService.updateUser(editedUser)) {
          refreshUserList();
          showAlert(Alert.AlertType.INFORMATION, "Success", "User updated successfully.");
        }
      } catch (IllegalArgumentException e) {
        showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
      } catch (SQLException e) {
        showAlert(Alert.AlertType.ERROR, "Database Error", "Error updating user: " + e.getMessage());
      }
    });
  }

  @FXML
  private void handleDeleteUser() {
    User selectedUser = userTableView.getSelectionModel().getSelectedItem();
    if (selectedUser == null) {
      showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a user to delete.");
      return;
    }

    Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
    confirmDialog.setTitle("Confirm Delete");
    confirmDialog.setHeaderText("Delete User");
    confirmDialog.setContentText("Are you sure you want to delete user: " + selectedUser.getUsername() + "?");

    Optional<ButtonType> result = confirmDialog.showAndWait();
    if (result.isPresent() && result.get() == ButtonType.OK) {
      try {
        if (UserService.deleteUser(selectedUser.getId())) {
          refreshUserList();
          showAlert(Alert.AlertType.INFORMATION, "Success", "User deleted successfully.");
        }
      } catch (IllegalArgumentException e) {
        showAlert(Alert.AlertType.ERROR, "Error", e.getMessage());
      } catch (SQLException e) {
        showAlert(Alert.AlertType.ERROR, "Database Error", "Error deleting user: " + e.getMessage());
      }
    }
  }

  @FXML
  private void handleViewHistoryAction() {
    User selectedUser = userTableView.getSelectionModel().getSelectedItem();
    if (selectedUser == null) {
      showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a user to view their loan history.");
      return;
    }

    try {
      FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/loan-history.fxml"));
      if (loader.getLocation() == null) {
        throw new IOException("Cannot find FXML file: /view/loan-history.fxml");
      }
      Parent root = loader.load();

      LoanHistoryController controller = loader.getController();
      controller.initializeData(selectedUser.getId());

      Stage stage = new Stage();
      stage.setTitle("Loan History for " + selectedUser.getUsername());
      stage.setScene(new Scene(root));
      stage.initModality(Modality.APPLICATION_MODAL);
      stage.showAndWait();

    } catch (IOException e) {
      e.printStackTrace();
      showAlert(Alert.AlertType.ERROR, "Error Loading View", "Could not load the loan history view: " + e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
      showAlert(Alert.AlertType.ERROR, "Error", "An unexpected error occurred: " + e.getMessage());
    }
  }

  private Dialog<User> createUserEditDialog(User user) {
    Dialog<User> dialog = new Dialog<>();
    dialog.setTitle("Edit User");
    dialog.setHeaderText("Edit user information (leave password blank to keep unchanged)");

    ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(20, 150, 10, 10));

    TextField username = new TextField(user.getUsername());
    PasswordField password = new PasswordField();
    password.setPromptText("Leave blank to keep current");
    ComboBox<User.Role> role = new ComboBox<>(FXCollections.observableArrayList(User.Role.values()));
    role.setValue(user.getRole());

    grid.add(new javafx.scene.control.Label("Username:"), 0, 0);
    grid.add(username, 1, 0);
    grid.add(new javafx.scene.control.Label("New Password:"), 0, 1);
    grid.add(password, 1, 1);
    grid.add(new javafx.scene.control.Label("Role:"), 0, 2);
    grid.add(role, 1, 2);

    dialog.getDialogPane().setContent(grid);

    dialog.setResultConverter(dialogButton -> {
      if (dialogButton == saveButtonType) {
        String newPassword = password.getText();
        User editedUser = new User(
            username.getText(),
            (newPassword == null || newPassword.isEmpty()) ? user.getPassword() : newPassword,
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
    alert.setHeaderText(null);
    alert.setContentText(content);
    alert.showAndWait();
  }

  private void refreshUserList() {
    try {
      userList.clear();
      userList.addAll(UserService.getAllUsers());
      userTableView.setItems(userList);
    } catch (SQLException e) {
      e.printStackTrace();
      showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to refresh user list: " + e.getMessage());
    }
  }

  @FXML
  private void switchToMainMenu() throws IOException {
    App.setRoot("menu");
  }
}