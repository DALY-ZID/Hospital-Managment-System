package com.example.miniprojet.home;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class GestionHospitalApplication extends Application {
    private Stage primaryStage;
    public Stage getPrimaryStage() {
        return primaryStage;
    }
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GestionHospitalApplication.class.getResource("login-view.fxml"));
        Pane root = fxmlLoader.load();

        StackPane stackPane = new StackPane(root);
        stackPane.setStyle("-fx-background-color: #f8f2e6;");

        Scene scene = new Scene(stackPane, 500,400);

        root.prefWidthProperty().bind(scene.widthProperty());
        root.prefHeightProperty().bind(scene.heightProperty());

        stage.setTitle("Hospital Management System");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }


    public static void main(String[] args) {
        launch();
    }
}