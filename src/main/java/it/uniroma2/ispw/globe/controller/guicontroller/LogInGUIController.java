package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.LogInController;
import it.uniroma2.ispw.globe.exception.InvalidCredentialsException;
import it.uniroma2.ispw.globe.exception.ItemAlreadyExistsException;
import it.uniroma2.ispw.globe.exception.ItemNotFoundException;
import it.uniroma2.ispw.globe.model.bean.CredentialsBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.uniroma2.ispw.globe.exception.ErrorMessage.ERROR_VIEW;
import static it.uniroma2.ispw.globe.other.UserType.*;


public class LogInGUIController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;
    @FXML
    private Button loginButton;
    @FXML
    private Button signInButton;
    @FXML
    private Button guestButton;
    @FXML
    private VBox signInVBox;
    @FXML
    private Label yesLabel;
    @FXML
    private Label noLabel;

    public void signIn(ActionEvent event) {
        URL url;
        Parent root;

        try {
            url = new File("src/main/java/it/uniroma2/ispw/globe/view/SigninView.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            SignInGUIController controller = new SignInGUIController();
            loader.setController(controller);
            root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            new ErrorPopUpGUIController().createPopUp(e.getMessage());
        }
    }

    public void logIn(ActionEvent event) throws ItemAlreadyExistsException {
        // da mettere limite massimo di caratteri per password e username (nella guest è a 12)

        errorLabel.setVisible(false);

        Button clickedButton = (Button) event.getSource();
        CredentialsBean credentials;

        if (clickedButton == loginButton) {
            if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                errorLabel.setVisible(true);
                return;
            } else {
                credentials = new CredentialsBean(usernameField.getText(), passwordField.getText());
            }
        } else {
            credentials = new CredentialsBean(UUID.randomUUID().toString().substring(0,12), "",GUEST);
        }

        String sessionId;
        try {
            sessionId = new LogInController().logIn(credentials);
        } catch (ItemNotFoundException | InvalidCredentialsException exception) {
            new ErrorPopUpGUIController().createPopUp(exception.getMessage());
            return;
        }

        if (sessionId != null) {
            URL url;
            String type = new LogInController().getUserType(sessionId);
            BorderPane root = new BorderPane();

            try {
                url = new File("src/main/java/it/uniroma2/ispw/globe/view/ToolBar.fxml").toURI().toURL();
                FXMLLoader toolBarLoader = new FXMLLoader(url);
                ToolBarGUIController controllerToolBar = new ToolBarGUIController(sessionId,type,root);
                toolBarLoader.setController(controllerToolBar);
                ViewManager viewManager = new ViewManager();
                if (type.equals(USER) || type.equals(GUEST)) {
                    viewManager.goToManageItineraryGUI(sessionId,root);
                } else {
                    viewManager.goToManageRequestGUI(sessionId,root);
                }
                root.setBottom(toolBarLoader.load());

                Scene scene = new Scene(root);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_VIEW, e);
                new ErrorPopUpGUIController().createPopUp(e.getMessage());
            }
        } else {
            errorLabel.setVisible(true);
        }
    }
}
