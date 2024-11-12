package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class ManageItineraryViewController {
    @FXML
    private Button addButton;
    @FXML
    private Button tripButton;
    @FXML
    private Button profileButton;
    @FXML
    private Button homeButton;

    //HANDLER
    public void addHandler (ActionEvent event) throws IOException {
        Navigator navigator = new Navigator();
        navigator.goToCreateTrip(event);
    }

    public void userTripHandler (ActionEvent event) {}

    public void userProfileHandler (ActionEvent event) {}

    public void userHomeHandler (ActionEvent event) {}
}
