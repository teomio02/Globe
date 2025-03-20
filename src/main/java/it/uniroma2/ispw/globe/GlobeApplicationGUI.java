package it.uniroma2.ispw.globe;

import it.uniroma2.ispw.globe.controller.guicontroller.LogInGUIController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class GlobeApplicationGUI extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(new File("src/main/java/it/uniroma2/ispw/globe/view/LoginView.fxml").toURI().toURL());
        InputStream url = getClass().getResourceAsStream("file:src/main/resources/it/uniroma2/ispw/globe/logo.png");

        if (url != null) {
            Image icon = new Image(url);
            stage.getIcons().add(icon);
        }

        LogInGUIController controller = new LogInGUIController();
        fxmlLoader.setController(controller);

        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("GLOBE");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}