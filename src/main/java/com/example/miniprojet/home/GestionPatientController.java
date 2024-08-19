package com.example.miniprojet.home;

import com.example.miniprojet.home.Exception.ChampVideException;
import com.example.miniprojet.home.Exception.CinException;
import com.example.miniprojet.home.Exception.TelephoneException;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import java.awt.Color;
import java.awt.Desktop;

import org.apache.pdfbox.pdmodel.common.PDRectangle;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class GestionPatientController implements Initializable {
    private GestionHospitalApplication mainApp;
    public void setMainApp(GestionHospitalApplication mainApp) {
        this.mainApp = mainApp;
    }
    @FXML
    private TableView<Patient> tabPatient;
    ObservableList<Integer> listId;
    boolean refrechCombo = true;
    @FXML
    private Label lbl,timeLabel;

    @FXML
    Button btnSupprime,btnModife,btnImprime,btnRechercher,btnAdd,logoutButton;
    @FXML
    TextField tfnom,tfprenom,tfcin,tftel;
    @FXML
    RadioButton BRm,BRf;
    @FXML
    HBox buttonBox,rechercheBox,titreBox;
    @FXML
    ImageView yessin;
    @FXML
    Label profilLabel;
    public void setProfilName(String profilName) {
        profilLabel.setText(profilName);
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ajouterColonnes();
        remplir();
        ecouteurs();
        lbl.setText("-- Consulter Patients --");
        lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: skyblue;-fx-font-size: 18px;");
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            String formattedTime = dateFormat.format(new Date());
            timeLabel.setText(formattedTime);
        }), new KeyFrame(Duration.seconds(1)));

        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();





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
    private void handleHomeButtonAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("home-view.fxml"));
        Parent root = loader.load();
        HomeController controller = loader.getController();
        controller.setProfilName(profilLabel.getText());
        Stage stage = new Stage();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        Stage loginStage = (Stage) logoutButton.getScene().getWindow();
        loginStage.close();
    }

    private void openSecondView(Patient patient) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("medicament-consultation-view.fxml"));
            Parent root = loader.load();
            MedicamentConsultationController controller = loader.getController();
            controller.setPatient(patient);
            // Passer le CIN à la deuxième vue
            Stage stage = new Stage();
            stage.setTitle(" "+patient.getNom()+" "+patient.getPrenom());
            stage.setScene(new Scene(root,480,350));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void etatField(boolean test)
    {
        tfcin.setEditable(test);
        tfnom.setEditable(test);
        tfprenom.setEditable(test);
        tfcin.setEditable(test);
        tftel.setEditable(test);

    }

    private void viderField()
    {
        tfcin.setText("");
        tfnom.setText("");
        tfprenom.setText("");
        tftel.setText("");
        BRm.setSelected(true);

    }

    //show patient selectionnee

    private void showPersonDetails(Patient p) {
        if (p != null) {
            // Fill the labels with info from the person object.
            tfcin.setText(Integer.toString(p.getCin()));
            tfnom.setText(p.getNom());
            tfprenom.setText(p.getPrenom());
            tftel.setText(p.getTel());
            if(p.getSexe().equals("Male"))
                BRm.setSelected(true);
            else
                BRf.setSelected(true);


            // TODO: We need a way to convert the birthday into a String!
            // birthdayLabel.setText(...);
        } else {
            // Person is null, remove all the text.
            viderField();

            //birthdayLabel.setText(" ");
        }
    }
    private void restoreOriginalButtons() {
        buttonBox.getChildren().clear();
        buttonBox.getChildren().addAll(btnImprime, btnSupprime, btnModife);
    }

    // Button add

    private void onAddClicked() {
       lbl.setText("-- Ajouter Patient --");
        lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: skyblue;-fx-font-size: 18px;");

        viderField();
        Button btnOK = new Button("✅");
        Button btnCancel = new Button("❌");
        tfcin.requestFocus();
        etatField(true);
        buttonBox.getChildren().clear();
        btnOK.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 14px; -fx-pref-width: 50px; -fx-pref-height: 26px;");
        btnCancel.setStyle("-fx-background-color: #cc0618; -fx-text-fill: white; -fx-font-size: 14px; -fx-pref-width: 50px; -fx-pref-height: 26px;");
        buttonBox.setSpacing(20);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(btnOK, btnCancel);
        btnOK.setOnAction(e -> {

            try {
                ajouter();
            } catch (CinException | ChampVideException | TelephoneException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Fields");
                alert.setHeaderText("Please correct invalid fields");
                alert.setContentText( ex.getMessage());
                alert.showAndWait();
            }            etatField(false);
            lbl.setText("-- Consulter Patients --");
            lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: skyblue;-fx-font-size: 18px;");



        });
        btnCancel.setOnAction(event->{
            restoreOriginalButtons();
            viderField();
            etatField(false);
            lbl.setText("-- Consulter Patients --");
            lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: skyblue;-fx-font-size: 18px;");

        });






        // Ajoutez des gestionnaires d'événements aux nouveaux boutons si nécessaire


    }

    @FXML
    private void onModifierClicked() {
        lbl.setText("-- Modifier Patient --");
        lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: skyblue;-fx-font-size: 18px;");
        Button btnOK = new Button("✅");
        Button btnCancel = new Button("❌");
        etatField(true);
        tfcin.setEditable(false);

        // Ajoutez des gestionnaires d'événements aux nouveaux boutons si nécessaire

        buttonBox.getChildren().clear();
        btnOK.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 14px; -fx-pref-width: 50px; -fx-pref-height: 26px;");
        btnCancel.setStyle("-fx-background-color: #cc0618; -fx-text-fill: white; -fx-font-size: 14px; -fx-pref-width: 50px; -fx-pref-height: 26px;");
        buttonBox.getChildren().addAll(btnOK, btnCancel);
        buttonBox.setSpacing(20);
        buttonBox.setAlignment(Pos.CENTER);
        btnOK.setOnAction(e -> {
            modifier();
            restoreOriginalButtons();
            etatField(false);
            lbl.setText("-- Consultater Patients --");
            lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: skyblue;-fx-font-size: 18px;");

        });
        btnCancel.setOnAction(event->{
           lbl.setText("-- Consultater Patients --");
           lbl.setStyle("-fx-font-weight: bold; -fx-text-fill: skyblue;-fx-font-size: 18px;");
            restoreOriginalButtons();
            etatField(false);
        });
    }

    //Button rechercher

    private void onRechercherClicked() {
        if(rechercheBox.getChildren().size()==1){
            TextField textField = new TextField();
            RadioButton R1 = new RadioButton("By CIN");
            RadioButton R2 = new RadioButton("By Name");
            R1.setSelected(true);
            rechercheBox.getChildren().add(textField);
            rechercheBox.getChildren().add(R1);
            rechercheBox.getChildren().add(R2);
            ToggleGroup toggleGroup = new ToggleGroup();
            R1.setToggleGroup(toggleGroup);
            R2.setToggleGroup(toggleGroup);

            textField.setOnKeyTyped(event->{
                if(textField.getText().equals(""))
                    remplir();
                else{
                    if(R1.isSelected()){
                        try {
                            int value = Integer.parseInt(textField.getText());
                            remplir3(value);
                        } catch (NumberFormatException e) {
                            // Gérer le cas où le texte n'est pas un nombre valide
                            System.out.println("Le texte n'est pas un nombre valide.");
                        }

                    }
                    if(R2.isSelected()){
                        remplir2(textField.getText());
                    }
                }


            });

        }



    }

    private void ecouteurs() {
        tfcin.setEditable(false);
        tfnom.setEditable(false);
        tfprenom.setEditable(false);
        tftel.setEditable(false);
        tabPatient.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) ->
                        showPersonDetails(newValue));



        btnSupprime.setOnAction(event -> {
            Patient selectedPatient = tabPatient.getSelectionModel().getSelectedItem();
            if (selectedPatient != null) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

                alert.setTitle("Delete");
                alert.setHeaderText("Delete person");
                alert.setContentText("Do you confirm delete of person.");
                alert.showAndWait();
                if (alert.getResult() == ButtonType.OK) {
                    supprimer();
                }


            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Aucune patient sélectionnée");
                alert.setHeaderText("Aucune patient n'a été sélectionnée");
                alert.setContentText("Veuillez sélectionner une patient à modifier.");
                alert.showAndWait();
            }

        });
        btnModife.setOnAction(event -> {
            Patient selectedPatient = tabPatient.getSelectionModel().getSelectedItem();
            if (selectedPatient != null) {
                onModifierClicked();

            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Aucune patient sélectionnée");
                alert.setHeaderText("Aucune patient n'a été sélectionnée");
                alert.setContentText("Veuillez sélectionner une patient à supprimer.");
                alert.showAndWait();
            }


        });
        btnImprime.setOnAction(event -> {
            Patient selectedPatient = tabPatient.getSelectionModel().getSelectedItem();
            if (selectedPatient != null) {
                onImprimeClicked();

            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Aucune patient sélectionnée");
                alert.setHeaderText("Aucune patient n'a été sélectionnée");
                alert.setContentText("Veuillez sélectionner une patient à imprimer.");
                alert.showAndWait();
            }

        });
        btnRechercher.setOnAction(event -> {
            onRechercherClicked();

        });

        btnAdd.setOnAction(event->{
            onAddClicked();
        });
        tabPatient.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    TablePosition position = tabPatient.getSelectionModel().getSelectedCells().get(0);
                    if (position.getRow() >= 0) {
                        Patient p = tabPatient.getItems().get(position.getRow());
                        openSecondView(p);
                    }
                }
            }
        });


    }

    public static void afficherErreur(String titre, String message) {
        Alert dialog = new Alert(Alert.AlertType.ERROR);
        dialog.setTitle(titre);
        dialog.setHeaderText(message);
        dialog.showAndWait();
    }

    private void ajouterColonnes() {
        tabPatient.getColumns().clear();
        TableColumn<Patient, Integer> cinCol = new TableColumn<>("CIN");
        TableColumn<Patient, String> nomCol = new TableColumn<>("Nom");
        TableColumn<Patient, String> prenomCol = new TableColumn<>("Prenom");
        TableColumn<Patient, String> sexeCol = new TableColumn<>("Sexe");
        TableColumn<Patient, String> telCol = new TableColumn<>("Telephone");
        cinCol.setCellValueFactory(new PropertyValueFactory<>("cin"));
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        sexeCol.setCellValueFactory(new PropertyValueFactory<>("sexe"));
        telCol.setCellValueFactory(new PropertyValueFactory<>("tel"));

        tabPatient.getColumns().addAll(cinCol, nomCol, prenomCol, sexeCol,telCol);
    }
    private void remplir() {
        ObservableList<Patient> listPatients = PatientUtil.getPatient();
        tabPatient.setItems(listPatients);
        listId = listPatients.stream().map(Patient::getCin).collect(Collectors.toCollection(FXCollections::observableArrayList));


    }
    private void remplir2(String nom) {
        ObservableList<Patient> listPatients = PatientUtil.rechercherPatient(nom);
        tabPatient.setItems(listPatients);
        listId = listPatients.stream().map(Patient::getCin).collect(Collectors.toCollection(FXCollections::observableArrayList));


    }

    private void remplir3(int cin) {
        ObservableList<Patient> listPatients = PatientUtil.rechercherPatientByCIN(cin);
        tabPatient.setItems(listPatients);
        listId = listPatients.stream().map(Patient::getCin).collect(Collectors.toCollection(FXCollections::observableArrayList));


    }

    // Methode Ajout
    private void ajouter() throws CinException, ChampVideException, TelephoneException {
        if(isInputValid()){

            String sexe;
            if(BRm.isSelected())
                sexe = "Male";
            else
                sexe = "Female";
            if (PatientUtil.ajouterPatient(Integer.parseInt(tfcin.getText()) ,
                    tfnom.getText(),tfprenom.getText(),sexe,tftel.getText()))
            {
                remplir();
                viderField();
                restoreOriginalButtons();

            } else
                afficherErreur(PatientUtil.getDernierTitreErreur(), PatientUtil.getDernierMessageErreur());
        }


    }

    // methode Supprimer

    private void supprimer() {
        TablePosition position = tabPatient.getSelectionModel().getSelectedCells().get(0);
        if (position.getRow() >= 0) {
            Patient patientSelectionne = tabPatient.getItems().get(position.getRow());
            int cin = patientSelectionne.getCin();
            if (PatientUtil.supprimerPatient(cin))
                remplir();
            else
                afficherErreur(PatientUtil.getDernierTitreErreur(), PatientUtil.getDernierMessageErreur());
        }
    }

    //Methode Modifier

    private void modifier() {
        int cin = Integer.parseInt(tfcin.getText());
        String sexe;
        if(BRm.isSelected())
            sexe = "Male";
        else
            sexe = "Female";
        if (PatientUtil.modifierPatient(cin, tfnom.getText(),
                tfprenom.getText(),tftel.getText(), sexe))
        {
            remplir();

        } else
            afficherErreur(PatientUtil.getDernierTitreErreur(), PatientUtil.getDernierMessageErreur());
    }
    private boolean isInputValid() throws CinException, TelephoneException, ChampVideException {
        StringBuilder exceptionMessage = new StringBuilder();

        if (tfcin.getText() == null || tfcin.getText().isEmpty()) {
            exceptionMessage.append("The CIN field cannot be empty.\n");
        } else {
            // Check if CIN is composed of 8 digits
            String cinStr = tfcin.getText();
            if (cinStr.length() != 8) {
                exceptionMessage.append("The CIN number must be composed of 8 digits.\n");
            }
            // Check if CIN is an integer
            try {
                Integer.parseInt(cinStr);
            } catch (NumberFormatException e) {
                exceptionMessage.append("No valid CIN (must be an integer)!\n");
            }
        }

        if (tfnom.getText() == null || tfnom.getText().isEmpty()) {
            exceptionMessage.append("The Name field cannot be empty.\n");
        }

        if (tfprenom.getText() == null || tfprenom.getText().isEmpty()) {
            exceptionMessage.append("The Last Name field cannot be empty.\n");
        }

        if (tftel.getText() == null || tftel.getText().isEmpty() || tftel.getText().length() != 8) {
            exceptionMessage.append("The telephone number must be an 8-digit integer.\n");
        } else {
            // Check if telephone number is an integer
            try {
                Integer.parseInt(tftel.getText());
            } catch (NumberFormatException e) {
                exceptionMessage.append("No valid telephone (must be an integer)!\n");
            }
        }

        if (exceptionMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(exceptionMessage.toString());
            alert.showAndWait();
            return false;
        }
    }

    //Methode d'imprission

    private void onImprimeClicked() {
        String cin = tfcin.getText();
        String nom = tfnom.getText();
        String prenom = tfprenom.getText();
        String tel = tftel.getText();
        String sexe = BRm.isSelected() ? "Male" : "Female";

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Set background color
                contentStream.setNonStrokingColor(Color.CYAN);
                contentStream.fillRect(0, 0, (int) PDRectangle.A4.getWidth(), (int) PDRectangle.A4.getHeight());

                // Add user icon
                //Image userIcon = ImageIO.read(new File("@../resources/com/images/patient.png"));
                //contentStream.drawImage(userIcon, 40, 720, 40, 40);

                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);
                contentStream.setNonStrokingColor(Color.BLACK);
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 750);
                contentStream.showText("Données du patient :");
                contentStream.setFont(PDType1Font.HELVETICA, 14);
                contentStream.newLineAtOffset(0, -30);
                contentStream.showText("CIN: " + cin);
                contentStream.newLineAtOffset(0, -25);
                contentStream.showText("Nom: " + nom);
                contentStream.newLineAtOffset(0, -25);
                contentStream.showText("Prénom: " + prenom);
                contentStream.newLineAtOffset(0, -25);
                contentStream.showText("Téléphone: " + tel);
                contentStream.newLineAtOffset(0, -25);
                contentStream.showText("Sexe: " + sexe);
                contentStream.endText();
            }

            File file = new File("D:\\" + nom + ".pdf");
            document.save(file);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Téléchargement terminé");
            alert.setHeaderText(null);
            alert.setContentText("Le fichier PDF a été téléchargé avec succès.");

            ButtonType openButton = new ButtonType("Ouvrir");
            alert.getButtonTypes().add(openButton);

            alert.setOnCloseRequest(event -> {
                if (alert.getResult() == openButton) {
                    try {
                        Desktop.getDesktop().open(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}