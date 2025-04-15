package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.LogInController;
import it.uniroma2.ispw.globe.exception.DuplicateItemException;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
import it.uniroma2.ispw.globe.model.bean.CredentialsBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
            try {
                new LogInController().signIn(credentials);
                goBack(event);
            } catch (FailedOperationException | DuplicateItemException exception) {
                new ErrorPopUpGUIController().createPopUp(exception.getMessage());
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
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        ViewManager viewManager = new ViewManager();
        viewManager.goToLogInGUI(stage);
    }
}
