package it.ispw.globeapp;

import it.ispw.globeapp.Controller.Navigator;
import it.ispw.globeapp.View.LoginView;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class GlobeApplication extends Application {
    private LoginView controller;

    @Override
    public void start(Stage stage) throws IOException {
        URL url = new File("src/main/java/it/ispw/globeapp/View/LoginView.fxml").toURI().toURL();
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        Scene scene = new Scene(fxmlLoader.load(), 1200, 700);
        stage.setTitle("GLOBE");
        stage.setScene(scene);
        stage.show();
        controller=fxmlLoader.getController();
    }

    public static void main(String[] args) {
        launch();
    }
}