package com.neduc.neduc;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.*;

public class ComptesController {

    @FXML private TableView<User> tableUsers;
    @FXML private TableColumn<User, String> colId;
    @FXML private TableColumn<User, String> colNom;
    @FXML private TableColumn<User, String> colPrenom;
    @FXML private TableColumn<User, String> colRole;
    @FXML private TableColumn<User, String> colEmail;
    @FXML private TableColumn<User, String> colActions;
    @FXML private Button btnAjouter, btnModifier, btnSupprimer;

    private final ObservableList<User> usersList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getId()));
        colNom.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNom()));
        colPrenom.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPrenom()));
        colRole.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getRole()));
        colEmail.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getEmail()));
        colActions.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(getActionsForRole(data.getValue())));

        tableUsers.setItems(usersList);
        tableUsers.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            btnModifier.setDisable(newVal == null);
            btnSupprimer.setDisable(newVal == null);
        });

        btnAjouter.setOnAction(e -> afficherFormulaireAjout());
        chargerUtilisateurs();
    }

    private void chargerUtilisateurs() {
        usersList.clear();
        try (Connection conn = com.neduc.neduc.database.DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM users")) {
            while (rs.next()) {
                usersList.add(new User(
                        rs.getString("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("role"),
                        rs.getString("email"),
                        rs.getBoolean("is_superuser")
                ));
            }
        } catch (SQLException e) {
            showAlert("Erreur", "Chargement échoué : " + e.getMessage());
        }
    }

    private void afficherFormulaireAjout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/neduc/neduc/AjoutFormulaire.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Nouveau compte utilisateur");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            chargerUtilisateurs();
        } catch (Exception e) {
            showAlert("Erreur", "Impossible d’ouvrir le formulaire : " + e.getMessage());
        }
    }

    private String getActionsForRole(User user) {
        if (user.isSuperuser()) return "Tout modifier, Supprimer, Superviser";

        String role = user.getRole();
        switch (role) {
            case "enseignant":
                return "Attribuer notes par matière";
            case "secrétaire":
                return "Créer UEs/ECUEs, Tirer bulletins";
            case "censeur":
                return "Superviser les notes";
            case "directeur":
                return "Gérer les ressources humaines";
            default:
                return "Aucune action définie";
        }
    }


    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
