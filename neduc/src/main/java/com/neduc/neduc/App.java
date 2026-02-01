package com.neduc.neduc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {

    public static StackPane rootPane;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent welcomeView = FXMLLoader.load(getClass().getResource("/com/neduc/neduc/welcome.fxml"));

        rootPane = new StackPane();
        rootPane.getChildren().add(welcomeView);
        
        Scene scene = new Scene(rootPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Néduc");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
