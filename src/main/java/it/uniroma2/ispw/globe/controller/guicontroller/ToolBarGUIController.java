package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.LogInController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static it.uniroma2.ispw.globe.other.UserType.GUEST;
import static it.uniroma2.ispw.globe.other.UserType.USER;

public class ToolBarGUIController {
    private String sessionId;
    private String userType;
    private BorderPane root;

    @FXML
    private Button logOutButton;

    public ToolBarGUIController(String sessionId, String userType, BorderPane root) {
        this.sessionId = sessionId;
        this.userType = userType;
        this.root = root;
    }

    public void initialize() {
        if (userType.equals(GUEST)) {
            logOutButton.setText("Log In");
        }
    }

    public void requestItinerary()  {
        URL url;
        AnchorPane newContentPane;

        try {
            url = new File("src/main/java/it/uniroma2/ispw/globe/view/RequestItineraryView.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            CreateRequestGUIController controller = new CreateRequestGUIController(sessionId);
            loader.setController(controller);
            newContentPane = loader.load();
            root.setCenter(newContentPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void manageItinerary(){
        URL url;
        AnchorPane newContentPane;
        FXMLLoader loader = null;

        try {
            if (userType.equals(USER) || userType.equals(GUEST)) {
                url = new File("src/main/java/it/uniroma2/ispw/globe/view/ManageItineraryView.fxml").toURI().toURL();
                loader = new FXMLLoader(url);
                ManageItineraryGUIController controller = new ManageItineraryGUIController(sessionId);
                loader.setController(controller);
            } else {
                url = new File("src/main/java/it/uniroma2/ispw/globe/view/ManageRequestView.fxml").toURI().toURL();
                loader = new FXMLLoader(url);
                ManageRequestGUIController controller = new ManageRequestGUIController(sessionId);
                loader.setController(controller);
            }

            newContentPane = loader.load();
            root.setCenter(newContentPane);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void manageProfile(){}
    public void logOut(ActionEvent event) {
        Button yesButton = new Button("Yes");
        Button noButton = new Button("no");
        if (userType.equals(GUEST)) {
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            yesButton.setOnAction(e -> {
                popupStage.close();
                yesButton.setUserData(true);
            });
            noButton.setOnAction(e -> {
                popupStage.close();
                yesButton.setUserData(false);
            });

            Label label = new Label("if you want to login, you will lose everything\nare you sure you want to login?");
            VBox popupContent = new VBox(label, new HBox(yesButton, noButton));
            Scene popupScene = new Scene(popupContent, 200, 100);

            popupStage.setScene(popupScene);
            popupStage.showAndWait();
        }
        if (!userType.equals(GUEST) || (boolean) yesButton.getUserData()) {
            URL url;
            Parent newRoot;

            new LogInController().logOut(sessionId);

            try {
                url = new File("src/main/java/it/uniroma2/ispw/globe/view/LoginView.fxml").toURI().toURL();
                FXMLLoader loader = new FXMLLoader(url);
                LogInGUIController controller = new LogInGUIController();
                loader.setController(controller);
                newRoot = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Scene scene = new Scene(newRoot);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }
}
