package com.example.miniprojet.home.data;

import com.example.miniprojet.home.DataBaseConnection;
import com.example.miniprojet.home.Consultation;
import com.example.miniprojet.home.Medicament;
import com.example.miniprojet.home.Patient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;

public class ConsultaionUtil {

    public static ObservableList<Consultation> getConsultation(int cin ) {
        ObservableList<Consultation> liste = FXCollections.observableArrayList();
        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getConnection();
        if(connectDB != null) {
            String connectQuery = "select * from consultation where `cinPat` =?";
            try{

                PreparedStatement checkStatement = connectDB.prepareStatement(connectQuery);
                checkStatement.setInt(1, cin);
                ResultSet resultSet = checkStatement.executeQuery();
                while(resultSet.next()){
                    int id = resultSet.getInt("id");
                    int cinPat = resultSet.getInt("cinPat");
                    Date date = resultSet.getDate("date");



                    Consultation patient = new Consultation(id, cinPat,date);
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
    public static void ajoutConsultation(Patient patient,Date d) {
        DataBaseConnection connection = new DataBaseConnection();
        Connection connectDB = connection.getConnection();

        if (connectDB != null) {
            try {

                    String insertQuery = "INSERT INTO Consultation (cinPat, date) VALUES (?, ?)";
                    PreparedStatement insertStatement = connectDB.prepareStatement(insertQuery);
                    insertStatement.setInt(1, patient.getCin());
                    insertStatement.setDate(2,d);


                    insertStatement.executeUpdate();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Succès");
                    alert.setHeaderText(null);
                    alert.setContentText("Consultaion ajouté avec succès.");
                    alert.showAndWait();

            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    connectDB.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }


    public static boolean getConsultationByDate(Date d,int cin) {
        Consultation c = new Consultation(0, 0, null);
        DataBaseConnection connection = new DataBaseConnection();
        Connection connectDB = connection.getConnection();
        boolean res = false;

        if (connectDB != null) {
            try {
                String query = "SELECT * FROM Consultation where `date`=? and `cinPat`=?";
                PreparedStatement preparedStatement = connectDB.prepareStatement(query);
                preparedStatement.setDate(1,d );
                preparedStatement.setInt(2,cin );
                ResultSet resultSet = preparedStatement.executeQuery();


                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    int cinPat = resultSet.getInt("cinPat");
                    Date date = resultSet.getDate("date");

                    c = new Consultation(id, cinPat, date);
                    res = true;

                }
            } catch (SQLException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur!");
                alert.setHeaderText("Erreur lors de la recupération des consultation depuis la base de données : " + ex.getMessage());
                alert.showAndWait();            }
            finally {
                try {
                    connectDB.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur!");
            alert.setHeaderText("Échec de la connexion à la base de données!");
            alert.showAndWait();

        }

        return res;

    }
    public static boolean supprimerConsultation(int id,int cin) {
        int rowsAffected=0;
        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getConnection();
        String connectQuery = "DELETE from Consultation where `id` = ? and `cinPat` = ?";
        try{
            PreparedStatement preparedStatement = connectDB.prepareStatement(connectQuery);
            preparedStatement.setInt(1, id);
            preparedStatement.setInt(2, cin);
            //Execution
            rowsAffected = preparedStatement.executeUpdate();



        }catch (Exception e){
            e.printStackTrace();

        }

        return rowsAffected>=1;
    }

    public static boolean modifierConsultation(int id, int cin, Date date ) {
        int rowsAffected=0;
        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getConnection();
        String connectQuery = "UPDATE  Consultation SET `date`=?  where `id`=? and `cinPat`=? ";
        try{
            PreparedStatement preparedStatement = connectDB.prepareStatement(connectQuery);
            //affectation des attribut
            preparedStatement.setDate(1, date);
            preparedStatement.setInt(2, id);
            preparedStatement.setInt(3, cin);

            //Execution
            rowsAffected = preparedStatement.executeUpdate();



        }catch (Exception e){
            e.printStackTrace();

        }

        return rowsAffected>=1;
    }

}
