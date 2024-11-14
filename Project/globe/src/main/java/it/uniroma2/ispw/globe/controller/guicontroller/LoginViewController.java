package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.LogInController;
import it.uniroma2.ispw.globe.controller.applicationcontroller.Navigator;
import it.uniroma2.ispw.globe.model.bean.UserBean;
import it.uniroma2.ispw.globe.other.Session;
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

    private void initialize() {
    }


    // HANDLER

    public void loginHandler(ActionEvent event) throws IOException {
        UserBean user;

        LogInController loginController = new LogInController();
        // da completare
        user=loginController.logIn(usernameField.getText(),passwordField.getText());

        if (user!=null){
            Session.getInstance().setUser(user);
        }

        Navigator navigator = new Navigator();
        // da cambiare in goToHome (lo stiamo facendo per implementare la schermata trip)
        navigator.goToManageItinerary(event);
    }

    public void singinHandler(ActionEvent event) throws IOException {

    }

    public void guestHandler(ActionEvent event) throws IOException {

    }
}
