package com.neduc.neduc;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;
import javafx.application.Platform;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private Label userCountLabel;

    @FXML
    private Label studentCountLabel;

    @FXML
    private Label subjectCountLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Simuler des données dynamiques
        userCountLabel.setText("128");
        studentCountLabel.setText("542");
        subjectCountLabel.setText("42");

        // Centrer la fenêtre et définir resizable = true
        Platform.runLater(() -> {
            Stage stage = (Stage) userCountLabel.getScene().getWindow();
            stage.setResizable(true); // autoriser le redimensionnement

            double width = stage.getWidth();
            double height = stage.getHeight();

            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((screenBounds.getWidth() - width) / 2);
            stage.setY((screenBounds.getHeight() - height) / 2);
        });
    }
}
