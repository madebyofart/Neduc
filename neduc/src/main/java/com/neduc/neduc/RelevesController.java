package com.neduc.neduc;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Import de la classe modèle
import com.neduc.neduc.EtudiantResultat;

// Connexion à la base de données
import com.neduc.neduc.database.DatabaseConnection;

public class RelevesController {

    @FXML private TableView<EtudiantResultat> relevesTable;
    @FXML private TableColumn<EtudiantResultat, Integer> colRang;
    @FXML private TableColumn<EtudiantResultat, String> colMatricule;
    @FXML private TableColumn<EtudiantResultat, String> colNom;
    @FXML private TableColumn<EtudiantResultat, String> colFiliere;
    @FXML private TableColumn<EtudiantResultat, String> colAnnee;
    @FXML private TableColumn<EtudiantResultat, Double> colMoyenneGenerale;

    private ObservableList<EtudiantResultat> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colRang.setCellValueFactory(cell -> cell.getValue().rangProperty().asObject());
        colMatricule.setCellValueFactory(cell -> cell.getValue().matriculeProperty());
        colNom.setCellValueFactory(cell -> cell.getValue().nomProperty());
        colFiliere.setCellValueFactory(cell -> cell.getValue().filiereProperty());
        colAnnee.setCellValueFactory(cell -> cell.getValue().anneeProperty());
        colMoyenneGenerale.setCellValueFactory(cell -> cell.getValue().moyenneGeneraleProperty().asObject());

        chargerReleves();
    }

    private void chargerReleves() {
    String query = 
        "SELECT e.id, e.nom, e.matricule, f.nom AS filiere, a.nom AS annee, m.moyenne_generale " +
        "FROM etudiants e " +
        "JOIN filieres f ON e.filiere_id = f.id " +
        "JOIN annees a ON e.annee_id = a.id " +
        "JOIN moyennes m ON m.etudiant_id = e.id " +
        "ORDER BY m.moyenne_generale DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            int rang = 1;
            while (rs.next()) {
                data.add(new EtudiantResultat(
                        rang++,
                        rs.getString("matricule"),
                        rs.getString("nom"),
                        rs.getString("filiere"),
                        rs.getString("annee"),
                        rs.getDouble("moyenne_generale")
                ));
            }

            relevesTable.setItems(data);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
