package com.example.miniprojet.home;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class HomeController implements Initializable {
    @FXML
    Label profilLabel,timeLabel;
    @FXML
    Button logoutButton,patientsButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            String formattedTime = dateFormat.format(new Date());
            timeLabel.setText(formattedTime);
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    ecouteurs();

    }

    private void ecouteurs() {


    }

    public void setProfilName(String profilName) {
        profilLabel.setText(profilName);
    }

    @FXML
    private void handleLogoutButtonAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("login-view.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        Stage loginStage = (Stage) logoutButton.getScene().getWindow();
        loginStage.close();
    }

    @FXML
    private void handlePatientButtonAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gestion-patient-view.fxml"));
        Parent root = loader.load();
        GestionPatientController controller = loader.getController();
        controller.setProfilName(profilLabel.getText());
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        Stage loginStage = (Stage) patientsButton.getScene().getWindow();
        loginStage.close();
    }

    @FXML
    private void handleMedicamentButtonAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gestion-mediacament-view.fxml"));
        Parent root = loader.load();
        GestionMedicamenController controller1 = loader.getController();
        controller1.setProfilName(profilLabel.getText());

        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        Stage loginStage = (Stage) patientsButton.getScene().getWindow();
        loginStage.close();
    }

}
