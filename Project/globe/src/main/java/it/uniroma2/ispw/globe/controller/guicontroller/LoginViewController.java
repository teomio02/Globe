package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;


public class LoginViewController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;
    @FXML
    private Button loginButton;
    @FXML
    private Button singinButton;
    @FXML
    private Button guestButton;

    private void initialize() {}


    // HANDLER

    public void loginHandler(ActionEvent event) throws IOException {
        Navigator navigator = new Navigator();
        // da cambiare in goToHome (lo stiamo facendo per implementare la schermata trip)
        navigator.goToManageItinerary(event);
    }

    public void singinHandler(ActionEvent event) throws IOException {

    }

    public void guestHandler(ActionEvent event) throws IOException {

    }
}
