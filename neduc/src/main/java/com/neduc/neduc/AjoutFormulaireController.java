package com.neduc.neduc;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.neduc.neduc.database.DatabaseConnection;

import javax.mail.MessagingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import java.util.regex.Pattern;

public class AjoutFormulaireController {

    @FXML private TextField txtNom;
    @FXML private TextField txtPrenom;
    @FXML private TextField txtEmail;
    @FXML private ComboBox<String> comboRole;

    @FXML
    public void initialize() {
        comboRole.getItems().addAll("enseignant", "secrétaire", "censeur", "directeur");
    }

    @FXML
    public void handleAjouter() {
        String nom = txtNom.getText().trim();
        String prenom = txtPrenom.getText().trim();
        String email = txtEmail.getText().trim();
        String role = comboRole.getValue();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || role == null) {
            showAlert("Erreur", "Tous les champs sont obligatoires.");
            return;
        }

        if (!isValidEmail(email)) {
            showAlert("Erreur", "L'adresse email n'est pas valide.");
            return;
        }

        String id = genererIdUnique();
        String password = genererMotDePasse();

        try (Connection conn = DatabaseConnection.getConnection()) {
            PreparedStatement stmt = conn.prepareStatement(
                "INSERT INTO users (id, nom, prenom, role, email, password) VALUES (?, ?, ?, ?, ?, ?)"
            );
            stmt.setString(1, id);
            stmt.setString(2, nom);
            stmt.setString(3, prenom);
            stmt.setString(4, role);
            stmt.setString(5, email);
            stmt.setString(6, password);
            stmt.executeUpdate();

            try {
                MailUtil.envoyerEmail(email, "Création de compte NEDUC",
                    "Bonjour " + prenom + ",\n\nVotre compte a été créé.\nIdentifiant : " + id + "\nMot de passe : " + password +
                    "\n\nVeuillez changer votre mot de passe après votre première connexion.");
            } catch (MessagingException e) {
                showAlert("Utilisateur créé", "Utilisateur ajouté, mais l'envoi de l'email a échoué : " + e.getMessage());
                return;
            }

            showAlert("Succès", "Utilisateur ajouté et email envoyé avec succès.");
            ((Stage) txtNom.getScene().getWindow()).close();

        } catch (SQLException e) {
            showAlert("Erreur base de données", "Échec lors de l'ajout : " + e.getMessage());
        }
    }

    private void showAlert(String titre, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isValidEmail(String email) {
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return Pattern.matches(regex, email);
    }

    private String genererIdUnique() {
        return "NED" + (10000 + new Random().nextInt(90000)); // Exemple : NED54678
    }

    private String genererMotDePasse() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder pwd = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < 8; i++) {
            pwd.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return pwd.toString();
    }
}
