package it.uniroma2.ispw.globe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class GlobeApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        URL url = new File("src/main/java/it/uniroma2/ispw/globe/view/LoginView.fxml").toURI().toURL();
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        Parent rootParent = (Parent)fxmlLoader.load();
        Scene scene = new Scene(rootParent);
        stage.setTitle("GLOBE");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}