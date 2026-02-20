package com.cyberz.ayamu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("mainWindowView.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("my first JavaFX app");
        scene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}