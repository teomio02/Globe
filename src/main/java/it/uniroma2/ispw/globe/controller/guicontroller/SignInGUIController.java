package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.LogInController;
import it.uniroma2.ispw.globe.model.bean.CredentialsBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static it.uniroma2.ispw.globe.other.ItineraryType.*;
import static it.uniroma2.ispw.globe.other.UserType.*;


public class SignInGUIController {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField descriptionField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField paymentCredentialsField;
    @FXML
    private Label errorLabel;
    @FXML
    private VBox agencyForm;
    @FXML
    private CheckBox onTheRoadCheckBox;
    @FXML
    private CheckBox natureCheckBox;
    @FXML
    private CheckBox cultureCheckBox;
    @FXML
    private CheckBox relaxCheckBox;
    @FXML
    private CheckBox cityCheckBox;

    public void initialize() {
        agencyForm.setVisible(false);
    }

    public void getAgencyForm() {
        agencyForm.setVisible(!agencyForm.isVisible());
    }

    public void signIn(ActionEvent event) {
        errorLabel.setVisible(false);
        CredentialsBean credentials;
        if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            errorLabel.setVisible(true);
        } else {
            credentials = new CredentialsBean(usernameField.getText(), passwordField.getText());
            if (agencyForm.isVisible()) {
                credentials = getAgencyDetails(credentials);
            } else {
                credentials.setType(USER);
            }
            if (new LogInController().signIn(credentials)) {
                goBack(event);
            } else {
                // errore
            }
        }
    }

    public CredentialsBean getAgencyDetails(CredentialsBean credentials) {
        List<String> preferences = new ArrayList<>();

        if (onTheRoadCheckBox.isSelected()) {
            preferences.add(ON_THE_ROAD);
        }
        if (natureCheckBox.isSelected()) {
            preferences.add(NATURE);
        }
        if (cultureCheckBox.isSelected()) {
            preferences.add(CULTURE);
        }
        if (relaxCheckBox.isSelected()) {
            preferences.add(RELAX);
        }
        if (cityCheckBox.isSelected()) {
            preferences.add(CITY);
        }

        credentials.setType(AGENCY);
        credentials.setDescription(descriptionField.getText());
        credentials.setPreferences(preferences);
        credentials.setPaymentCredentials(paymentCredentialsField.getText());

        return credentials;
    }

    public void goBack(ActionEvent event) {
        URL url;
        Parent root;

        try {
            url = new File("src/main/java/it/uniroma2/ispw/globe/view/LoginView.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            LogInGUIController controller = new LogInGUIController();
            loader.setController(controller);
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
