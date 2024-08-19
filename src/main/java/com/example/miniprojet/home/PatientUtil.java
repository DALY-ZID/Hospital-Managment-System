package com.example.miniprojet.home;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;

public class PatientUtil {
    private static String dernierTitreErreur = "";
    private static String dernierMessageErreur = "";
    public static String getDernierTitreErreur() {
        return dernierTitreErreur;
    }

    public static void setDernierTitreErreur(String dernierTitreErreur) {
        PatientUtil.dernierTitreErreur = dernierTitreErreur;
    }

    public static String getDernierMessageErreur() {
        return dernierMessageErreur;
    }

    public static void setDernierMessageErreur(String dernierMessageErreur) {
        PatientUtil.dernierMessageErreur = dernierMessageErreur;
    }
    public static ObservableList<Patient> getPatient() {
        ObservableList<Patient> liste = FXCollections.observableArrayList();
        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getConnection();
        if(connectDB != null) {
            String connectQuery = "select * from patien order by cin";
            try{

                Statement statement = connectDB.createStatement();
                ResultSet resultSet = statement.executeQuery(connectQuery);
                while(resultSet.next()){
                    int cin = resultSet.getInt("cin");
                    String nom = resultSet.getString("nom");
                    String prenom = resultSet.getString("prenom");
                    String sexe = resultSet.getString("sexe");
                    String tel = resultSet.getString("tel");


                    Patient patient = new Patient(cin, nom,prenom, sexe,tel);
                    liste.add(patient);

                }



            }catch (Exception e){
                e.printStackTrace();

            }

        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Probléme de connexion");
            alert.setHeaderText("Verifier Votre Connexion!");
            alert.showAndWait();

        }


        return liste;
    }

    //Methode Modifier

    public static boolean modifierPatient(int cin, String nom, String prenom, String tel,String sexe ) {
        int rowsAffected=0;
        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getConnection();
        String connectQuery = "UPDATE  patien SET `nom`=? , `prenom`=?, `tel`=?,`sexe`=?  where `cin`=? ; ";
        try{
            PreparedStatement preparedStatement = connectDB.prepareStatement(connectQuery);
            //affectation des attribut
            preparedStatement.setString(1, nom);
            preparedStatement.setString(2, prenom);
            preparedStatement.setString(3, tel);
            preparedStatement.setString(4, sexe);
            preparedStatement.setInt(5, cin);
            //Execution
            rowsAffected = preparedStatement.executeUpdate();



        }catch (Exception e){
            e.printStackTrace();

        }

        return rowsAffected>=1;
    }

    //Methode Ajout
    public static boolean ajouterPatient(int cin,String nom, String prenom, String sexe, String tel) {
        int rowsAffected=0;
        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getConnection();
        String connectQuery = "INSERT INTO patien ( `cin`, `nom`, `prenom`,`sexe`,`tel`) VALUES (?, ?, ?,?,?); ";
        try{
            PreparedStatement preparedStatement = connectDB.prepareStatement(connectQuery);
            //affectation des attribut
            preparedStatement.setInt(1, cin);
            preparedStatement.setString(2, nom);
            preparedStatement.setString(3, prenom);
            preparedStatement.setString(4, sexe);
            preparedStatement.setString(5, tel);
            //Execution
            rowsAffected = preparedStatement.executeUpdate();



        }catch (Exception e){
            e.printStackTrace();

        }

        return  rowsAffected>=1;
    }

    // Methode Supprimer
    public static boolean supprimerPatientmed(int cin) {
        int rowsAffected=0;
        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getConnection();
        String connectQuery = "DELETE from patientmed where cinPat = ?";

        try{
            PreparedStatement preparedStatement = connectDB.prepareStatement(connectQuery);
            preparedStatement.setInt(1, cin);
            //Execution
            rowsAffected = preparedStatement.executeUpdate();



        }catch (Exception e){
            e.printStackTrace();

        }

        return rowsAffected>=1;
    }


    public static boolean supprimerPatient(int cin) {
        supprimerPatientmed(cin);
        int rowsAffected=0;
        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getConnection();
        String connectQuery = "DELETE from patien where cin = ?";

        try{
            PreparedStatement preparedStatement = connectDB.prepareStatement(connectQuery);
            preparedStatement.setInt(1, cin);
            //Execution
            rowsAffected = preparedStatement.executeUpdate();



        }catch (Exception e){
            e.printStackTrace();

        }

        return rowsAffected>=1;
    }

    //Methode recherche

    public static ObservableList<Patient> rechercherPatient(String nomPartiel) {
        ObservableList<Patient> liste = FXCollections.observableArrayList();
        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getConnection();
        if(connectDB != null) {
            String connectQuery = "SELECT * FROM patien WHERE nom LIKE ?";
            try{
                PreparedStatement preparedStatement = connectDB.prepareStatement(connectQuery);
                preparedStatement.setString(1, "%" + nomPartiel + "%");
                ResultSet resultSet = preparedStatement.executeQuery();
                while(resultSet.next()){
                    int cin = resultSet.getInt("cin");
                    String nom = resultSet.getString("nom");
                    String prenom = resultSet.getString("prenom");
                    String sexe = resultSet.getString("sexe");
                    String tel = resultSet.getString("tel");

                    Patient patient = new Patient(cin, nom, prenom, sexe, tel);
                    liste.add(patient);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Problème de connexion");
            alert.setHeaderText("Vérifier votre connexion!");
            alert.showAndWait();
        }
        return liste;
    }

    public static ObservableList<Patient> rechercherPatientByCIN(int cinP) {
        ObservableList<Patient> liste = FXCollections.observableArrayList();
        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getConnection();
        if (connectDB != null) {
            String connectQuery = "SELECT * FROM patien WHERE cin LIKE ?";
            try {
                PreparedStatement preparedStatement = connectDB.prepareStatement(connectQuery);
                preparedStatement.setString(1, "%" + cinP + "%");
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    int cin = resultSet.getInt("cin");
                    String nom = resultSet.getString("nom");
                    String prenom = resultSet.getString("prenom");
                    String sexe = resultSet.getString("sexe");
                    String tel = resultSet.getString("tel");

                    Patient patient = new Patient(cin, nom, prenom, sexe, tel);
                    liste.add(patient);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Problème de connexion");
            alert.setHeaderText("Vérifier votre connexion!");
            alert.showAndWait();
        }
        return liste;
    }


}
