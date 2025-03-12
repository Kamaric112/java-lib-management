package com.controller;

import java.io.IOException;
import com.app.App;
import javafx.fxml.FXML;

public class MemberListController {
  @FXML
  private void switchToMenu() throws IOException {
    App.setRoot("menu");
  }
}