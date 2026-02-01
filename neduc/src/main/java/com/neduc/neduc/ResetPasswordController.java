package com.neduc.neduc;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class ResetPasswordController implements Initializable {

    @FXML
    private ImageView myGif;
    
    @FXML
    private javafx.scene.control.TextField idField;

    @FXML
    private javafx.scene.control.TextField emailField;

    @FXML
    private Label errorLabel;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image gif = new Image(getClass().getResource("/com/neduc/neduc/assets/admitted.gif").toExternalForm());
        myGif.setImage(gif);
    }
    
    public void handleReset(ActionEvent event) {
        String userId = idField.getText();
        String email = emailField.getText();

        errorLabel.setText("");

        if (userId.isEmpty() || email.isEmpty()) {
            errorLabel.setText("L'ID et l'email ne peuvent pas être vides.");
            return;
        }
    }

    public void handleBack(ActionEvent event) throws Exception {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/com/neduc/neduc/login.fxml")));
        stage.setScene(scene);
        stage.setResizable(false);
    }
}
