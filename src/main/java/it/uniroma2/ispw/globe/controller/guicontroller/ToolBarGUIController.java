package it.uniroma2.ispw.globe.controller.guicontroller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static it.uniroma2.ispw.globe.other.UserType.GUEST;
import static it.uniroma2.ispw.globe.other.UserType.USER;

public class ToolBarGUIController {
    private String sessionId;
    private String userType;
    private BorderPane root;

    @FXML
    private Button logOutButton;

    private static final String ERROR = "page loading failed";

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
        ViewManager viewManager = new ViewManager();
        viewManager.goToRequestItineraryGUI(sessionId,root);
    }


    public void manageItinerary(){
        ViewManager viewManager = new ViewManager();
        if (userType.equals(USER) || userType.equals(GUEST)) {
            viewManager.goToManageItineraryGUI(sessionId,root);
        } else {
           viewManager.goToManageRequestGUI(sessionId,root);
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
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            ViewManager viewManager = new ViewManager();
            viewManager.goToLogInGUI(stage);
        }
    }
}
