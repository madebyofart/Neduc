package com.neduc.neduc;

import com.neduc.neduc.UserSession;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class MenusController {



    @FXML private MenuButton profileMenu;
    @FXML private MenuItem modifierMenuItem;
    @FXML private MenuItem deconnexionMenuItem;
    
    @FXML
    private StackPane mainContent;
    
    @FXML
    public void initialize() {
        // Affiche les infos utilisateur
        profileMenu.setText(UserSession.getNom() + " " + UserSession.getPrenom() + " (" + UserSession.getRole() + ")");

        // Actions menu
        modifierMenuItem.setOnAction(this::handleModifierProfil);
        deconnexionMenuItem.setOnAction(this::handleDeconnexion);
        
        // Charge le dashboard par défaut
        loadDashboard();
        
    }

    private void handleModifierProfil(ActionEvent event) {
        // Modifier le nom
        TextInputDialog nomDialog = new TextInputDialog(UserSession.getNom());
        nomDialog.setTitle("Modifier le profil");
        nomDialog.setHeaderText("Modifier le nom");
        nomDialog.setContentText("Nouveau nom :");
        nomDialog.showAndWait().ifPresent(UserSession::setNom);

        // Modifier le prénom
        TextInputDialog prenomDialog = new TextInputDialog(UserSession.getPrenom());
        prenomDialog.setTitle("Modifier le profil");
        prenomDialog.setHeaderText("Modifier le prénom");
        prenomDialog.setContentText("Nouveau prénom :");
        prenomDialog.showAndWait().ifPresent(UserSession::setPrenom);

        // Met à jour l'affichage du menu
        profileMenu.setText(UserSession.getNom() + " " + UserSession.getPrenom() + " (" + UserSession.getRole() + ")");
    }

    private void handleDeconnexion(ActionEvent event) {
        UserSession.clear();
        Stage currentStage = (Stage) profileMenu.getScene().getWindow();
        currentStage.close();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/neduc/neduc/login.fxml"));
            Parent root = loader.load();

            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(root));
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void loadContent(String fxml) {
        try {
            Node content = FXMLLoader.load(getClass().getResource(fxml));
            mainContent.getChildren().setAll(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void loadDashboard() {
        loadContent("/com/neduc/neduc/dashboard.fxml");
    }

    @FXML
    private void loadEtudiants() {
        loadContent("/com/neduc/neduc/etudiant.fxml");
    }

    @FXML
    private void loadEvaluations() {
        loadContent("/com/neduc/neduc/evaluations.fxml");
    }

    @FXML
    private void loadResultats() {
        loadContent("/com/neduc/neduc/resultats.fxml");
    }

    @FXML
    private void loadParametres() {
        loadContent("/com/neduc/neduc/parametres.fxml");
    }


}
