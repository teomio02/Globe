package it.uniroma2.ispw.globe.controller.guicontroller;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class NavigationGUIController {
    private BorderPane root;

    public NavigationGUIController(BorderPane root) {
        this.root = root;
    }

    public void loadView(String fxmlPath, Object controller) {
        try {
            URL url = new File(fxmlPath).toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            loader.setController(controller);
            root.setCenter(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
