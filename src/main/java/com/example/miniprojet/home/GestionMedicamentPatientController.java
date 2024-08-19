package com.example.miniprojet.home;

import com.example.miniprojet.home.*;
import com.example.miniprojet.home.data.MedicamentUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static com.example.miniprojet.home.GestionPatientController.afficherErreur;
public class GestionMedicamentPatientController implements Initializable {
    private Patient pat;
    private Stage dialogStage;
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    @FXML
    ComboBox<String> listMed;

    @FXML
    Label lbl;
    @FXML
    TableView tabPatientMed;
    @FXML
    Button btnRemoveMP,btnAddMP;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ajouterColonnes();
        ecouteurs();
        chargerMedicaments();

    }

    private void ecouteurs() {
        btnAddMP.setOnAction(e -> {
            String selectedMedName = listMed.getValue();
            if (selectedMedName != null && !selectedMedName.isEmpty()) {
                Medicament selectedMed = MedicamentPatientUtil.getMedicamentByLibelle(selectedMedName);
                if (selectedMed != null) {
                    // Vérifier si le médicament est déjà associé au patient
                    if (!MedicamentPatientUtil.medicamentExistsForPatient(selectedMed.getRef(), pat.getCin())) {
                        // Ajouter le médicament au patient
                        MedicamentPatientUtil.ajouterPM(selectedMed.getRef(), pat.getCin());
                        remplir(pat);
                    } else {
                        // Afficher un message si le médicament est déjà associé au patient
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Médicament déjà associé");
                        alert.setHeaderText("Le médicament sélectionné est déjà associé au patient");
                        alert.setContentText("Le médicament sélectionné est déjà dans la liste des médicaments du patient.");
                        alert.showAndWait();
                    }
                } else {
                    // Gérer le cas où le médicament correspondant au nom sélectionné n'est pas trouvé
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Médicament non trouvé");
                    alert.setContentText("Le médicament correspondant au nom sélectionné n'a pas été trouvé.");
                    alert.showAndWait();
                }
            } else {
                // Gérer le cas où aucun élément n'est sélectionné dans la liste
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Aucune medicament sélectionnée");
                alert.setHeaderText("Aucune medicament n'a été sélectionnée");
                alert.setContentText("Veuillez sélectionner une medicament à ajouter.");
                alert.showAndWait();
            }
        });


        btnRemoveMP.setOnAction(e -> {
            Medicament selectedMed = (Medicament) tabPatientMed.getSelectionModel().getSelectedItem();
            if (selectedMed != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

                alert.setTitle("Delete");
                alert.setHeaderText("Delete medicament pour ce patient");
                alert.setContentText("Do you confirm delete of medicament.");
                alert.showAndWait();
                if (alert.getResult() == ButtonType.OK) {
                    supprimer(pat);
                }


            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Aucune medicament sélectionnée");
                alert.setHeaderText("Aucune medicament n'a été sélectionnée");
                alert.setContentText("Veuillez sélectionner une medicament à suprimer.");
                alert.showAndWait();
            }


        });

    }

    private void chargerMedicaments() {
        ObservableList<String> allMedicamentNames = FXCollections.observableArrayList(
                MedicamentPatientUtil.getMedicaments().stream()
                        .map(Medicament::getLibelle) // Récupère uniquement le nom de chaque médicament
                        .collect(Collectors.toList())
        );
        listMed.setItems(allMedicamentNames);
    }


    private void ajouterColonnes() {
        tabPatientMed.getColumns().clear();
        TableColumn<Patient, Integer> refCol = new TableColumn<>("Ref");
        TableColumn<Patient, String> libelleCol = new TableColumn<>("Libelle");
        TableColumn<Patient, String> prixCol = new TableColumn<>("Prix");
        refCol.setCellValueFactory(new PropertyValueFactory<>("ref"));
        libelleCol.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        prixCol.setCellValueFactory(new PropertyValueFactory<>("prix"));
        tabPatientMed.getColumns().addAll(refCol, libelleCol, prixCol);
    }

    public   void remplir(Patient patient) {
        ObservableList<Medicament> listMed = MedicamentPatientUtil.getMedicamentsByRef(patient);
        tabPatientMed.setItems(listMed);
        lbl.setText(patient.getPrenom()+" "+patient.getNom());
        lbl.setStyle("-fx-text-fill: #6509d8;");
        pat = patient;
    }

    private void supprimer(Patient patient) {
        TablePosition position = (TablePosition) tabPatientMed.getSelectionModel().getSelectedCells().get(0);
        if (position.getRow() >= 0) {
            Medicament medicament = (Medicament) tabPatientMed.getItems().get(position.getRow());
            int ref = medicament.getRef();
            int cin = patient.getCin();
            if (MedicamentPatientUtil.supprimerMed(ref,cin))
                remplir(patient);
            else
                afficherErreur(com.example.miniprojet.home.PatientUtil.getDernierTitreErreur(), com.example.miniprojet.home.PatientUtil.getDernierMessageErreur());
        }
    }


}
