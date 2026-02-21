package com.cyberz.ayamu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    // Declare public static variables
    public static Stage primaryStage;
    public static Scene mainScene;


    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("components/mainWindowView.fxml"));
        mainScene = new Scene(loader.load());
        primaryStage.setTitle("Ayamu");
        mainScene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
        primaryStage.setScene(mainScene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}