package com.example.miniprojet.home;


import com.example.miniprojet.home.DataBaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.sql.*;

public class MedicamentPatientUtil {

    public static boolean ajouterPM(int ref,int cin) {
        int rowsAffected = 0;
        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getConnection();
        String connectQuery = "INSERT INTO patientmed ( `refMed`, `cinPat`) VALUES (?, ?); ";
        try {
            PreparedStatement preparedStatement = connectDB.prepareStatement(connectQuery);
            //affectation des attribut
            preparedStatement.setInt(1, ref);
            preparedStatement.setInt(2, cin);

            //Execution
            rowsAffected = preparedStatement.executeUpdate();


        } catch (Exception e) {
            e.printStackTrace();

        }
        return rowsAffected > 0;
    }
    public static ObservableList<Medicament> getMedicaments() {
        ObservableList<Medicament> liste = FXCollections.observableArrayList();
        DataBaseConnection connection = new DataBaseConnection();
        Connection connectDB = connection.getConnection();

        if (connectDB != null) {
            try {
                String query = "SELECT * FROM Medicament order by ref";
                PreparedStatement preparedStatement = connectDB.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    int ref = resultSet.getInt("ref");
                    String libelle = resultSet.getString("libelle");
                    Double prix = resultSet.getDouble("prix");

                    Medicament m = new Medicament(ref, libelle, prix);
                    liste.add(m);
                }
            } catch (SQLException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur!");
                alert.setHeaderText("Erreur lors de la recupération des médicaments depuis la base de données : " + ex.getMessage());
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

        return liste;

    }
    public static ObservableList<Medicament> getMedicamentsByRef(Patient patient) {
        ObservableList<Medicament> liste = FXCollections.observableArrayList();
        DataBaseConnection connection = new DataBaseConnection();
        Connection connectDB = connection.getConnection();

        if (connectDB != null) {
            try {
                String query = "SELECT * FROM Medicament,patientmed where patientmed.`cinPat`=? and medicament.`ref`=patientmed.`refMed` ";
                PreparedStatement preparedStatement = connectDB.prepareStatement(query);
                preparedStatement.setInt(1, patient.getCin());
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    int ref = resultSet.getInt("ref");
                    String libelle = resultSet.getString("libelle");
                    Double prix = resultSet.getDouble("prix");

                    Medicament m = new Medicament(ref, libelle, prix);
                    liste.add(m);
                }
            } catch (SQLException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur!");
                alert.setHeaderText("Erreur lors de la recupération des médicaments depuis la base de données : " + ex.getMessage());
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

        return liste;

    }

    public static Medicament getMedicamentByLibelle(String libell) {
       Medicament m = new Medicament(0, null, 0);
        DataBaseConnection connection = new DataBaseConnection();
        Connection connectDB = connection.getConnection();

        if (connectDB != null) {
            try {
                String query = "SELECT * FROM Medicament where `libelle` LIKE ?";
                PreparedStatement preparedStatement = connectDB.prepareStatement(query);
                preparedStatement.setString(1,libell );
                ResultSet resultSet = preparedStatement.executeQuery();


                while (resultSet.next()) {
                    int ref = resultSet.getInt("ref");
                    String libelle = resultSet.getString("libelle");
                    Double prix = resultSet.getDouble("prix");

                     m = new Medicament(ref, libelle, prix);

                }
            } catch (SQLException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur!");
                alert.setHeaderText("Erreur lors de la recupération des médicaments depuis la base de données : " + ex.getMessage());
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

        return m;

    }




    public static void ajout(Medicament medicament) {
        DataBaseConnection connection = new DataBaseConnection();
        Connection connectDB = connection.getConnection();

        if (connectDB != null) {
            try {
                String checkQuery = "SELECT ref FROM Medicament WHERE ref = ?";
                PreparedStatement checkStatement = connectDB.prepareStatement(checkQuery);
                checkStatement.setInt(1, medicament.getRef());
                ResultSet resultSet = checkStatement.executeQuery();

                if (resultSet.next()) {

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Avertissement");
                    alert.setHeaderText(null);
                    alert.setContentText("La référence du médicament existe déjà dans la base de données.");
                    alert.showAndWait();

                } else {
                    String insertQuery = "INSERT INTO Medicament (ref, libelle, prix) VALUES (?, ?, ?)";
                    PreparedStatement insertStatement = connectDB.prepareStatement(insertQuery);
                    insertStatement.setInt(1, medicament.getRef());
                    insertStatement.setString(2, medicament.getLibelle());
                    insertStatement.setDouble(3, medicament.getPrix());

                    insertStatement.executeUpdate();

                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Succès");
                    alert.setHeaderText(null);
                    alert.setContentText("Médicament ajouté avec succès.");
                    alert.showAndWait();
                }
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

    public static void updateMedicament(Medicament medicament) {
        DataBaseConnection connection = new DataBaseConnection();
        Connection connectDB = connection.getConnection();

        if (connectDB != null) {
            try {
                String query = "UPDATE Medicament SET libelle = ?, prix = ? WHERE ref = ?";
                PreparedStatement preparedStatement = connectDB.prepareStatement(query);
                preparedStatement.setString(1, medicament.getLibelle());
                preparedStatement.setDouble(2, medicament.getPrix());
                preparedStatement.setInt(3, medicament.getRef());

                preparedStatement.executeUpdate();
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

    public static void deleteMedicament(Medicament medicament) {
        DataBaseConnection connection = new DataBaseConnection();
        Connection connectDB = connection.getConnection();

        if (connectDB != null) {
            try {
                String query = "DELETE FROM Medicament WHERE ref = ?";
                PreparedStatement preparedStatement = connectDB.prepareStatement(query);
                preparedStatement.setInt(1, medicament.getRef());

                preparedStatement.executeUpdate();
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
    public static boolean supprimerMed(int ref,int cin) {
        int rowsAffected=0;
        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getConnection();
        String connectQuery = "DELETE from patientmed where `refMed` = ? and `cinPat` = ?";
        try{
            PreparedStatement preparedStatement = connectDB.prepareStatement(connectQuery);
            preparedStatement.setInt(1, ref);
            preparedStatement.setInt(2, cin);
            //Execution
            rowsAffected = preparedStatement.executeUpdate();



        }catch (Exception e){
            e.printStackTrace();

        }

        return rowsAffected>=1;
    }

    public static boolean medicamentExistsForPatient(int ref, int cin) {
        boolean exists = false;
        DataBaseConnection connectNow = new DataBaseConnection();
        Connection connectDB = connectNow.getConnection();
        String query = "SELECT COUNT(*) FROM patientmed WHERE refMed = ? AND cinPat = ?";

        try {
            PreparedStatement preparedStatement = connectDB.prepareStatement(query);
            preparedStatement.setInt(1, ref);
            preparedStatement.setInt(2, cin);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                exists = (count > 0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                connectDB.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return exists;
    }

}
