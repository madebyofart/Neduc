package com.neduc.neduc;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class WelcomeController implements Initializable {

    @FXML
    private ImageView myGif;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image gif = new Image(getClass().getResource("/com/neduc/neduc/assets/admitted.gif").toExternalForm());
        myGif.setImage(gif);
    }

    public void handleLogin(ActionEvent event) throws Exception {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        // Rendre l'interface Welcome non redimensionnable ici
        stage.setResizable(true);

        Parent loginView = FXMLLoader.load(getClass().getResource("/com/neduc/neduc/login.fxml"));

        Scene scene = ((Node) event.getSource()).getScene();

        StackPane root = new StackPane();
        root.getChildren().add(loginView);
        loginView.translateYProperty().set(scene.getHeight());

        Scene newScene = new Scene(root, scene.getWidth(), scene.getHeight());
        stage.setScene(newScene);

        TranslateTransition slideUp = new TranslateTransition(Duration.millis(500), loginView);
        slideUp.setToY(0);
        slideUp.play();
    }
}
