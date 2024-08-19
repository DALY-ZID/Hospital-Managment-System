package com.example.miniprojet.home;

import com.example.miniprojet.home.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginPageController {

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;
    @FXML
    public void initialize() {
        loginField.setFocusTraversable(false);
        passwordField.setFocusTraversable(false);

    }
    @FXML
    private void handleLoginButtonAction() throws IOException {
        String login = loginField.getText();
        String password = passwordField.getText();
        DataBaseConnection connection = new DataBaseConnection();
        Connection connectDB = connection.getConnection();

        if (connectDB != null) {
            try {
                String query = "SELECT login, password, nom, prenom FROM personnel ";
                PreparedStatement statement = connectDB.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery();

                boolean found = false;
                while (resultSet.next()) {
                    String dbLogin = resultSet.getString("login");
                    String dbPassword = resultSet.getString("password");
                    String dbFullName = resultSet.getString("nom") + " " + resultSet.getString("prenom");




                    if (login.equals(dbLogin) && password.equals(dbPassword)) {
                        found = true;
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Success");
                        alert.setHeaderText(null);
                        alert.setContentText("Welcome " + dbFullName);
                        alert.showAndWait();

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
                        Parent root = loader.load();
                        HomeController controller = loader.getController();

                        controller.setProfilName(dbFullName);


                        Stage stage = new Stage();
                        Scene scene = new Scene(root);
                        stage.setScene(scene);
                        stage.show();
                        stage.setResizable(false);

                        Stage loginStage = (Stage) loginButton.getScene().getWindow();
                        loginStage.close();

                    }
                }

                if (!found) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Invalid credentials. Please try again.");
                    alert.showAndWait();
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    connectDB.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void handleRegisterButtonAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("register-view.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        Stage loginStage = (Stage) loginButton.getScene().getWindow();
        loginStage.close();
    }

}
