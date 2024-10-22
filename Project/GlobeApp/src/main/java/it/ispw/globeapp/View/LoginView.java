package it.ispw.globeapp.View;

import it.ispw.globeapp.Controller.Navigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;


public class LoginView {
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

    private AnchorPane mainPane;

    private void initialize() {

    }

    public void loginHandler() {
        String username, pswrd;

        username = usernameField.getText();
        pswrd = passwordField.getText();

        if (username.isEmpty() || pswrd.isEmpty()) {
            errorLabel.setText("Campi vuoti");
        }else{
            errorLabel.setText(username + " " + pswrd);
        }
    }

    public void singinHandler() {}

    public void guestHomeHandler(ActionEvent event) throws IOException {
        Navigator navigator = new Navigator();
        navigator.goToGuestHome(event);
    }
}
