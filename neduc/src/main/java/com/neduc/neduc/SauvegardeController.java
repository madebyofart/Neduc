package com.neduc.neduc;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.io.IOException;

public class SauvegardeController {

    @FXML private TextArea consoleTextArea;
    @FXML private Button btnSauvegarder;
    @FXML private Button btnRestaurer;

    @FXML
    public void initialize() {
        // Initialiser la console avec un message de bienvenue
        consoleTextArea.setText("Console de sauvegarde et restauration prête.\n");

        // Ajoutez un gestionnaire d'événements pour le bouton "Sauvegarder"
        btnSauvegarder.setOnAction(event -> handleSauvegarder());
        
        // Ajoutez un gestionnaire d'événements pour le bouton "Restaurer"
        btnRestaurer.setOnAction(event -> handleRestaurer());
    }

    @FXML
    public void handleSauvegarder() {
        consoleTextArea.appendText("\nDébut de la sauvegarde de la base de données\n");

        try {
            File sauvegardeFile = new File("sauvegarde_bdd.sql"); // Remplace par le chemin réel si besoin
            if (!sauvegardeFile.exists()) {
                consoleTextArea.appendText("Création du fichier de sauvegarde...\n");
                sauvegardeFile.createNewFile();
            } else {
                consoleTextArea.appendText("Le fichier de sauvegarde existe déjà, il sera écrasé.\n");
            }

            // Ici, tu peux appeler ta méthode réelle de sauvegarde, par exemple un export SQL
            consoleTextArea.appendText("Sauvegarde en cours...\n");

            // Simulation de délai
            Thread.sleep(1000);

            consoleTextArea.appendText("Sauvegarde terminée avec succès.\n");
            consoleTextArea.appendText("Fichier créé : " + sauvegardeFile.getAbsolutePath() + "\n");
        } catch (IOException e) {
            consoleTextArea.appendText("Erreur lors de la sauvegarde : " + e.getMessage() + "\n");
        } catch (InterruptedException e) {
            consoleTextArea.appendText("Processus interrompu : " + e.getMessage() + "\n");
            Thread.currentThread().interrupt();
        }
    }

    @FXML
    public void handleRestaurer() {
        consoleTextArea.appendText("\nDébut de la restauration de la base de données\n");

        try {
            File restaurationFile = new File("sauvegarde_bdd.sql"); // Remplace par le chemin réel si besoin
            if (restaurationFile.exists()) {
                consoleTextArea.appendText("Fichier de sauvegarde trouvé : " + restaurationFile.getAbsolutePath() + "\n");
                consoleTextArea.appendText("Restauration en cours...\n");

                // Simulation de délai
                Thread.sleep(1000);

                // Ici, tu peux appeler ta méthode réelle de restauration

                consoleTextArea.appendText("Restauration terminée avec succès.\n");
            } else {
                consoleTextArea.appendText("Erreur : Le fichier de sauvegarde n'existe pas.\n");
            }
        } catch (InterruptedException e) {
            consoleTextArea.appendText("Processus interrompu : " + e.getMessage() + "\n");
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            consoleTextArea.appendText("Erreur lors de la restauration : " + e.getMessage() + "\n");
        }
    }
}
