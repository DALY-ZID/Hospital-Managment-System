package com.example.miniprojet.home;

import com.example.miniprojet.home.DataBaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterController {
    @FXML
    TextField cinField, nameField, lastNameField,loginField,passwordField, functionField;
    @FXML
    Button registerButton, loginButton;

    @FXML
    public void initialize() {
        cinField.setFocusTraversable(false);
        nameField.setFocusTraversable(false);
        lastNameField.setFocusTraversable(false);
        loginField.setFocusTraversable(false);
        passwordField.setFocusTraversable(false);
        cinField.setFocusTraversable(false);
        functionField.setFocusTraversable(false);
    }
    @FXML
    private void handleLoginButtonAction(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        Stage loginStage = (Stage) registerButton.getScene().getWindow();
        loginStage.close();
    }
    @FXML
    private void handleRegisterButtonAction() {
        String cin = cinField.getText();
        String name = nameField.getText().substring(0,1).toUpperCase() + nameField.getText().substring(1);
        String lastName = lastNameField.getText().toUpperCase();
        String login = loginField.getText();
        String password = passwordField.getText();
        String function = functionField.getText();

        if (!cin.matches("\\d+")) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "CIN must contain only numbers.");
            return;
        }

        if (!isValidEmail(login)) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Invalid email format for login.");
            return;
        }

        String query = "INSERT INTO personnel (cin, nom, prenom, login, password, fonction) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            DataBaseConnection connection = new DataBaseConnection();
            Connection connectDB = connection.getConnection();
            PreparedStatement statement = connectDB.prepareStatement(query);
            statement.setString(1, cin);
            statement.setString(2, name);
            statement.setString(3, lastName);
            statement.setString(4, login);
            statement.setString(5, password);
            statement.setString(6, function);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {

                    showAlert(Alert.AlertType.INFORMATION, "Success", "Personnel inserted successfully.");
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
                    Parent root = loader.load();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root));
                    stage.setResizable(false);
                    stage.show();
                    Stage loginStage = (Stage) registerButton.getScene().getWindow();
                    loginStage.close();

            }
        } catch (SQLException | IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Error inserting data into personnel table: " + e.getMessage());
        }
    }


    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


}
