package com.example.miniprojet.home;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MedicamentConsultationController implements Initializable {
    private Patient patient;
    @FXML
    private Button Medicament,Consultation;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ecouteurs();

    }

    private void ecouteurs() {
        Medicament.setOnAction(event->{
            openMedicamentView(patient);
        });
        Consultation.setOnAction(event->{
            openConsultationView(patient);
        });

    }
    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    private void openConsultationView(Patient patient) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("gestion-consultation-view.fxml"));
            Parent root = loader.load();
            GestionConsultationController controller = loader.getController();
            controller.remplir(patient);
            controller.setPat(patient);
            // Passer le CIN à la deuxième vue
            Stage stage = new Stage();
            stage.setTitle("Medication Management : "+patient.getNom()+" "+patient.getPrenom());
            stage.setScene(new Scene(root,480,350));
            stage.setResizable(false);
            stage.show();
            Stage page = (Stage) Consultation.getScene().getWindow();
            page.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void openMedicamentView(Patient patient) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("gestion-medicament-patient-view.fxml"));
            Parent root = loader.load();
            GestionMedicamentPatientController controller = loader.getController();
            controller.remplir(patient);
            // Passer le CIN à la deuxième vue
            Stage stage = new Stage();
            stage.setTitle("Medication Management : "+patient.getNom()+" "+patient.getPrenom());
            stage.setScene(new Scene(root,480,350));
            stage.setResizable(false);

            stage.show();
            Stage page = (Stage) Medicament.getScene().getWindow();
            page.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
