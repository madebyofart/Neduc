package com.neduc.neduc;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.neduc.neduc.database.DatabaseConnection;

import java.sql.*;

public class FilieresController {

    @FXML private TextField nomFiliereField;
    @FXML private TextField codeFiliereField;
    @FXML private TableView<FiliereData> tableFiliere;
    @FXML private TableColumn<FiliereData, String> colNom;
    @FXML private TableColumn<FiliereData, String> colCode;

    private final ObservableList<FiliereData> listeFilieres = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNom.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
        colCode.setCellValueFactory(cellData -> cellData.getValue().codeProperty());
        chargerFilieresDepuisBase();
    }

    @FXML
    public void ajouterFiliere() {
        String nomFiliere = nomFiliereField.getText().trim();
        String codeFiliere = codeFiliereField.getText().trim();

        if (nomFiliere.isEmpty() || codeFiliere.isEmpty()) {
            afficherAlerte("Erreur", "Les champs Nom et Code sont obligatoires.");
            return;
        }

        String requete = "INSERT INTO filieres (nom, code) VALUES (?, ?)";

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement preparation = connexion.prepareStatement(requete)) {
            preparation.setString(1, nomFiliere);
            preparation.setString(2, codeFiliere);
            preparation.executeUpdate();
            chargerFilieresDepuisBase();
            viderChamps();
        } catch (SQLException exception) {
            afficherAlerte("Erreur", "Échec de l'ajout : " + exception.getMessage());
        }
    }

    @FXML
    public void modifierFiliere() {
        FiliereData filiereSelectionnee = tableFiliere.getSelectionModel().getSelectedItem();
        if (filiereSelectionnee == null) {
            afficherAlerte("Sélectionner une filière", "Veuillez sélectionner une filière à modifier.");
            return;
        }

        String nomFiliere = nomFiliereField.getText().trim();
        String codeFiliere = codeFiliereField.getText().trim();

        if (nomFiliere.isEmpty() || codeFiliere.isEmpty()) {
            afficherAlerte("Erreur", "Les champs Nom et Code sont obligatoires.");
            return;
        }

        String requete = "UPDATE filieres SET nom = ?, code = ? WHERE id = ?";

        try (Connection connexion = DatabaseConnection.getConnection();
             PreparedStatement preparation = connexion.prepareStatement(requete)) {
            preparation.setString(1, nomFiliere);
            preparation.setString(2, codeFiliere);
            preparation.setInt(3, filiereSelectionnee.getIdentifiant());
            preparation.executeUpdate();
            chargerFilieresDepuisBase();
            viderChamps();
        } catch (SQLException exception) {
            afficherAlerte("Erreur", "Échec de la modification : " + exception.getMessage());
        }
    }

    private void chargerFilieresDepuisBase() {
        listeFilieres.clear();
        String requete = "SELECT id, nom, code FROM filieres";

        try (Connection connexion = DatabaseConnection.getConnection();
             Statement instruction = connexion.createStatement();
             ResultSet resultat = instruction.executeQuery(requete)) {

            while (resultat.next()) {
                int identifiant = resultat.getInt("id");
                String nom = resultat.getString("nom");
                String code = resultat.getString("code");
                listeFilieres.add(new FiliereData(identifiant, nom, code));
            }

            tableFiliere.setItems(listeFilieres);
        } catch (SQLException exception) {
            afficherAlerte("Erreur", "Chargement échoué : " + exception.getMessage());
        }
    }

    private void viderChamps() {
        nomFiliereField.clear();
        codeFiliereField.clear();
    }

    private void afficherAlerte(String titre, String message) {
        Alert alerte = new Alert(Alert.AlertType.ERROR);
        alerte.setTitle(titre);
        alerte.setHeaderText(null);
        alerte.setContentText(message);
        alerte.showAndWait();
    }

    public static class FiliereData {
        private final int identifiant;
        private final SimpleStringProperty nom;
        private final SimpleStringProperty code;

        public FiliereData(int identifiant, String nom, String code) {
            this.identifiant = identifiant;
            this.nom = new SimpleStringProperty(nom);
            this.code = new SimpleStringProperty(code);
        }

        public int getIdentifiant() { return identifiant; }
        public String getNom() { return nom.get(); }
        public String getCode() { return code.get(); }

        public SimpleStringProperty nomProperty() { return nom; }
        public SimpleStringProperty codeProperty() { return code; }
    }
}
