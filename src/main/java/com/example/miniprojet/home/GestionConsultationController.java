package com.example.miniprojet.home;

import com.example.miniprojet.home.data.ConsultaionUtil;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ResourceBundle;

import static com.example.miniprojet.home.GestionPatientController.afficherErreur;

public class GestionConsultationController implements Initializable {
    @FXML
    private TableView tabConsultation;
    @FXML
    private DatePicker date;
    @FXML
    private Button btnAjouter,btnSupprimer,btnModifier;
    private Patient pat;
    public void setPat(Patient p) {
        this.pat = p;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ecouteurs();
        ajouterColonnes();

    }
    private void ecouteurs() {
            tabConsultation.getSelectionModel().selectedItemProperty().addListener(
                    (observable, oldValue, newValue) ->
                            showConsultationDetails((Consultation)newValue));
        btnAjouter.setOnAction(event -> {
            if (date.getValue() != null) {
                LocalDate selectedDate = date.getValue();
                LocalDate currentDate = LocalDate.now();

                if (selectedDate.isAfter(currentDate) || selectedDate.isEqual(currentDate)) {
                    ConsultaionUtil.ajoutConsultation(pat, Date.valueOf(selectedDate));
                    remplir(pat);
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Date invalide");
                    alert.setHeaderText("La date sélectionnée est invalide");
                    alert.setContentText("La date de consultation ne peut pas être antérieure à la date actuelle.");
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Aucune Date");
                alert.setHeaderText("Aucune Date choisie");
                alert.setContentText("Veuillez choisir une date.");
                alert.showAndWait();
            }
        });

        btnSupprimer.setOnAction(event->{
            Consultation selectedMed = (Consultation) tabConsultation.getSelectionModel().getSelectedItem();
            if (selectedMed != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

                alert.setTitle("Delete");
                alert.setHeaderText("Delete consultation pour ce patient");
                alert.setContentText("Do you confirm delete of consultation.");
                alert.showAndWait();
                if (alert.getResult() == ButtonType.OK) {
                    TablePosition position = (TablePosition) tabConsultation.getSelectionModel().getSelectedCells().get(0);
                    if (position.getRow() >= 0) {
                        Consultation consultation = (Consultation) tabConsultation.getItems().get(position.getRow());
                        int id = consultation.getId();
                        int cin = consultation.getcinPat();
                        if (ConsultaionUtil.supprimerConsultation(id,cin))
                            remplir(pat);
                        else
                            afficherErreur(com.example.miniprojet.home.PatientUtil.getDernierTitreErreur(), com.example.miniprojet.home.PatientUtil.getDernierMessageErreur());
                    }
                }


            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Aucune consultation sélectionnée");
                alert.setHeaderText("Aucune consultation n'a été sélectionnée");
                alert.setContentText("Veuillez sélectionner une consultation à suprimer.");
                alert.showAndWait();
            }
        });
        btnModifier.setOnAction(event -> {
            Consultation selectedCon = (Consultation) tabConsultation.getSelectionModel().getSelectedItem();

            if (selectedCon != null) {
                TablePosition position = (TablePosition) tabConsultation.getSelectionModel().getSelectedCells().get(0);

                if (position.getRow() >= 0) {
                    Consultation consultation = (Consultation) tabConsultation.getItems().get(position.getRow());
                    int id = consultation.getId();
                    int cin = consultation.getcinPat();
                    java.util.Date d = consultation.getDate();
                    LocalDate selectedDate = date.getValue();
                    LocalDate currentDate = LocalDate.now();

                    if (d.compareTo(Date.valueOf(LocalDate.now())) > 0) {
                        // La date de consultation est postérieure à la date actuelle
                        if (selectedDate != null && (selectedDate.isAfter(currentDate) || selectedDate.isEqual(currentDate))) {
                            // La date de modification est valide
                            if (ConsultaionUtil.modifierConsultation(id, cin, Date.valueOf(selectedDate))) {
                                remplir(pat);
                            } else {
                                afficherErreur(com.example.miniprojet.home.PatientUtil.getDernierTitreErreur(), com.example.miniprojet.home.PatientUtil.getDernierMessageErreur());
                            }
                        } else {
                            // La date de modification est antérieure ou égale à la date actuelle
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Modification invalide");
                            alert.setHeaderText("La date de consultation sélectionnée est invalide");
                            alert.setContentText("La date de consultation sélectionnée ne peut pas être antérieure ou égale à la date actuelle.");
                            alert.showAndWait();
                        }
                    } else {
                        // La date de consultation est antérieure ou égale à la date actuelle
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Date de consultation dépassée");
                        alert.setHeaderText("La date de consultation sélectionnée a été dépassée");
                        alert.setContentText("La date de consultation sélectionnée a été dépassée. Vous ne pouvez pas la modifier.");
                        alert.showAndWait();
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Aucune consultation sélectionnée");
                alert.setHeaderText("Aucune consultation n'a été sélectionnée");
                alert.setContentText("Veuillez sélectionner une consultation à modifier.");
                alert.showAndWait();
            }
        });
    }
    public void ajouterColonnes() {
        tabConsultation.getColumns().clear();
        TableColumn<Consultation, Integer> idCol = new TableColumn<>("ID");
//        TableColumn<Consultation, Integer> cinCol = new TableColumn<>("Cin Patient");
        TableColumn<Consultation, Date> dateCol = new TableColumn<>("Date");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
//        cinCol.setCellValueFactory(new PropertyValueFactory<>("cinPat"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        tabConsultation.getColumns().addAll(idCol, dateCol);
    }

    public   void remplir(Patient patient) {
        ObservableList<Consultation> listMed = ConsultaionUtil.getConsultation(patient.getCin());
        tabConsultation.setItems(listMed);

    }
    private void showConsultationDetails(Consultation p) {
        if (p != null) {
             date.setValue(((Date)(p.getDate())).toLocalDate());


        }
    }


}
