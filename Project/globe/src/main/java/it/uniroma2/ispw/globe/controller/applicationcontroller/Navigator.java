package it.uniroma2.ispw.globe.controller.applicationcontroller;


import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Navigator {
    private String page;

    public void goToTrip(ActionEvent event) throws IOException {
        page = "src/main/java/it/uniroma2/ispw/globe/view/UserTripView.fxml";
        FXMLLoader loader = goToPage(page, event);
    }

    public void goToAddTrip(ActionEvent event) throws IOException {
        page = "src/main/java/it/uniroma2/ispw/globe/view/CreateTripView.fxml";
        FXMLLoader loader = goToPage(page, event);
    }

    public FXMLLoader goToPage(String Page, ActionEvent event) throws IOException {
        URL url = new File(Page).toURI().toURL();
        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);

        return loader;
    }

}
