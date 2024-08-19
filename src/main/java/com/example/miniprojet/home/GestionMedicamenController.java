package com.example.miniprojet.home;

import com.example.miniprojet.home.data.MedicamentUtil;
import com.example.miniprojet.home.*;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.DoubleStringConverter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;


public class GestionMedicamenController  {
    @FXML
    private TableView<Medicament> tableView;
    @FXML
    private TableColumn<Medicament, Integer> referenceColumn;
    @FXML
    private TableColumn<Medicament, String> libelleColumn;
    @FXML
    private TableColumn<Medicament, Double> prixColumn;

    @FXML
    private TextField searchTextField;
    @FXML
    private Button ajouterButton;
    @FXML
    private Label timeLabel;
    @FXML
    private Label profilLabel;
    @FXML
    private Button logoutButton;

    @FXML
    public void initialize() {
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            String formattedTime = dateFormat.format(new Date());
            timeLabel.setText(formattedTime);
        }), new KeyFrame(Duration.seconds(1)));

        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

        searchTextField.setFocusTraversable(false);

        searchTextField.setOnMouseClicked(event -> {
            searchTextField.clear();
        });
        searchTextField.promptTextProperty().bind(
                Bindings.when(searchTextField.textProperty().isEmpty())
                        .then("Search ... 🔎")
                        .otherwise("")
        );


        referenceColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getRef()).asObject());
        libelleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLibelle()));
        prixColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrix()).asObject());

        tableView.setItems(MedicamentUtil.getMedicaments());

        libelleColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        libelleColumn.setOnEditCommit(event -> {
            Medicament medicament = event.getRowValue();
            medicament.setLibelle(event.getNewValue());
            MedicamentUtil.updateMedicament(medicament);
        });

        prixColumn.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        prixColumn.setOnEditCommit(event -> {
            Medicament medicament = event.getRowValue();
            medicament.setPrix(event.getNewValue());
            MedicamentUtil.updateMedicament(medicament);
        });

        tableView.setEditable(true);



        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                if (isInteger(newValue)) {
                    searchById(newValue);
                } else {
                    searchByLibelle(newValue);
                }
            } else {
                tableView.setItems(MedicamentUtil.getMedicaments());
            }
        });
    }
    public void setProfilName(String profilName) {
        profilLabel.setText(profilName);
    }

    private boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    @FXML
    private void handleHomeButtonAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
        Parent root = loader.load();

        HomeController controller = loader.getController();
        controller.setProfilName(profilLabel.getText());Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();

        Stage loginStage = (Stage) logoutButton.getScene().getWindow();
        loginStage.close();
    }
    private void searchByLibelle(String searchText) {
        tableView.getItems().clear();

        if (searchText.isEmpty()) {
            tableView.setItems(MedicamentUtil.getMedicaments());
            return;
        }

        List<Medicament> filteredMedicaments = new ArrayList<>();
        for (Medicament medicament : MedicamentUtil.getMedicaments()) {
            if (medicament.getLibelle().toLowerCase().contains(searchText.toLowerCase())) {
                filteredMedicaments.add(medicament);
            }
        }

        tableView.setItems(FXCollections.observableArrayList(filteredMedicaments));
    }


    private void searchById (String searchText){
        tableView.getItems().clear();

        if (searchText.isEmpty()) {
            tableView.setItems(MedicamentUtil.getMedicaments());
            return;
        }

        List<Medicament> filteredMedicaments = new ArrayList<>();
        for (Medicament medicament : MedicamentUtil.getMedicaments()) {
            if (medicament.getRef() == Integer.parseInt(searchText)) {
                filteredMedicaments.add(medicament);
            }
        }

        tableView.setItems(FXCollections.observableArrayList(filteredMedicaments));
    }




    @FXML
    private void onAjouterButtonClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ajout-medicament-view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Ajouter Médicament");
            stage.setScene(new Scene(root));

            AjoutMedicamentController ajoutMedicamentController = loader.getController();

            ajoutMedicamentController.setTableView(tableView);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void onDeleteButtonClick() {
        Medicament selectedMedicament = tableView.getSelectionModel().getSelectedItem();

        if (selectedMedicament != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation de suppression");
            alert.setHeaderText("Suppression de médicament");
            alert.setContentText("Êtes-vous sûr de vouloir supprimer ce médicament ?");

            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                MedicamentUtil.deleteMedicament(selectedMedicament);

                tableView.getItems().remove(selectedMedicament);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucune sélection");
            alert.setHeaderText("Aucun médicament sélectionné");
            alert.setContentText("Veuillez sélectionner un médicament à supprimer.");
            alert.showAndWait();
        }
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
}