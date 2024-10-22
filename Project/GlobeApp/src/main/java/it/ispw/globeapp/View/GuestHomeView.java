package it.ispw.globeapp.View;


import it.ispw.globeapp.Controller.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

public class GuestHomeView {
    @FXML
    private Button HomeButton;
    @FXML
    private Button LoginButton;
    @FXML
    private Button TripButton;

    public void GuestHomeHandler(ActionEvent event) throws IOException {
        Navigator navigator = new Navigator();
        navigator.goToGuestHome(event);
    }

    public void loginHandler(ActionEvent event) throws IOException {
        Navigator navigator = new Navigator();
        navigator.goToLogin(event);
    }
    public void GuestTripHandler(ActionEvent event) throws IOException {
        Navigator navigator = new Navigator();
        navigator.goToGuestTrip(event);
    }

}
