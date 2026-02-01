package com.neduc.neduc;

import com.neduc.neduc.database.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.fxml.FXML;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import org.mindrot.jbcrypt.BCrypt;  // Assure-toi que bcrypt est bien ajouté à ton projet

public class LoginController implements Initializable {

    @FXML
    private ImageView myGif;

    @FXML
    private javafx.scene.control.TextField idField;  // Champ pour l'ID ou l'email de l'utilisateur

    @FXML
    private javafx.scene.control.PasswordField passwordField;  // Champ pour le mot de passe

    @FXML
    private Label errorLabel;  // Label pour afficher les erreurs

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image gif = new Image(getClass().getResource("/com/neduc/neduc/assets/admitted.gif").toExternalForm());
        myGif.setImage(gif);
    }

    // Méthode appelée lors de la tentative de connexion
    public void handleLogin(ActionEvent event) {
        String userId = idField.getText();
        String password = passwordField.getText();

        // Réinitialiser le label d'erreur
        errorLabel.setText("");

        if (userId.isEmpty() || password.isEmpty()) {
            errorLabel.setText("L'ID et le mot de passe ne peuvent pas être vides.");
            return;
        }

        // Connexion à la base de données pour vérifier l'identifiant et le mot de passe
        try (Connection conn = DatabaseConnection.getConnection()) {
            // SQL pour récupérer l'utilisateur avec l'email ou ID fourni
            String sql = "SELECT * FROM users WHERE id = ? OR email = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, userId);  // Recherche par ID
            pst.setString(2, userId);  // Recherche par email
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                // Récupérer le mot de passe stocké dans la base de données
                String storedHashedPassword = rs.getString("password");

                // Vérifier le mot de passe avec bcrypt
                if (BCrypt.checkpw(password, storedHashedPassword)) {
                    // Connexion réussie, rediriger l'utilisateur
                    System.out.println("Connexion réussie!");

                    // Récupérer les informations de l'utilisateur
                    String nom = rs.getString("nom");
                    String prenom = rs.getString("prenom");
                    String role = rs.getString("role");

                    // Stocker ces informations dans UserSession
                    UserSession.setNom(nom);
                    UserSession.setPrenom(prenom);
                    UserSession.setRole(role);

                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/com/neduc/neduc/primary.fxml")));
                    stage.setScene(scene);
                    stage.setMaximized(true);
                    stage.setResizable(true);
                } else {
                    // Mot de passe incorrect
                    errorLabel.setText("Mot de passe incorrect.");
                }
            } else {
                // Aucun utilisateur trouvé avec l'ID ou l'email donné
                errorLabel.setText("Aucun utilisateur trouvé avec cet ID ou cet email.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Erreur lors de la connexion : " + e.getMessage());
        }
    }


    // Méthode pour la réinitialisation du mot de passe
    public void handleForgotPassword(ActionEvent event) throws Exception {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/com/neduc/neduc/reset_password.fxml")));
        stage.setScene(scene);
        stage.setResizable(false);
    }
}
