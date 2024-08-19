package com.example.miniprojet.home;

import com.example.miniprojet.home.Exception.ChampVideException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.example.miniprojet.home.Medicament;

public class AjoutMedicamentController {

    @FXML
    private TextField refField;

    @FXML
    private TextField libelleField;

    @FXML
    private TextField prixField;

    @FXML
    private Button ajouterButton;

    private TableView<Medicament> tableView;

    public void setTableView(TableView<Medicament> tableView) {
        this.tableView = tableView;
    }

    @FXML
    private void onAjouterButtonClick() {
        try {
            if (refField.getText().isEmpty() || libelleField.getText().isEmpty() || prixField.getText().isEmpty()) {
                throw new ChampVideException("Veuillez remplir tous les champs obligatoires.");
            }

            int ref = Integer.parseInt(refField.getText());
            String libelle = libelleField.getText();
            double prix = Double.parseDouble(prixField.getText());

            Medicament nouveauMedicament = new Medicament(ref, libelle, prix);
            MedicamentPatientUtil.ajout(nouveauMedicament);
            tableView.setItems(com.example.miniprojet.home.MedicamentPatientUtil.getMedicaments());

            Stage stage = (Stage) ajouterButton.getScene().getWindow();
            stage.close();

        } catch (ChampVideException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez entrer des valeurs valides pour la référence et le prix.");
            alert.showAndWait();
        }
    }
}
