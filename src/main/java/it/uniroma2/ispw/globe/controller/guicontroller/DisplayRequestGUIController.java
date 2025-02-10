package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.ResponseRequestController;
import it.uniroma2.ispw.globe.model.bean.AgencyRequestBean;
import it.uniroma2.ispw.globe.model.bean.ProposalBean;
import it.uniroma2.ispw.globe.model.bean.RequestBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import static it.uniroma2.ispw.globe.other.ProposalState.PENDING;



public class DisplayRequestGUIController {
    @FXML
    private Label daysLabel;
    @FXML
    private Label userLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private HBox typesHBox;


    private String sessionId;
    private String requestId;

    public DisplayRequestGUIController(String sessionId, String requestId) {
        this.sessionId = sessionId;
        this.requestId = requestId;

        System.out.println("\nDisplayRequestGUIController\n"+"itineraryID: /"+"\nrequestID: "+requestId+"\nproposalID: /");
    }

    public void initialize() {
        // popola con use case simo (create request use case)

        if (requestId != null) {

            //create proposal use case
            AgencyRequestBean request = new ResponseRequestController().getAgencyRequest(requestId, sessionId);
            userLabel.setText(request.getUser());
            descriptionLabel.setText(request.getDescription());
            daysLabel.setText(String.valueOf(request.getDays()));
            for (String type: request.getTypes()) {
                typesHBox.getChildren().add(new Label(type));
            }
        }
    }


    public void createItinerary(ActionEvent event) {

        URL url;
        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();

        try {
            url = new File("src/main/java/it/uniroma2/ispw/globe/view/CreateItineraryView.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            CreateItineraryGUIController controller = new CreateItineraryGUIController(sessionId,requestId);
            loader.setController(controller);
            root.setCenter(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void goBack(ActionEvent event) {
        URL url;
        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();

        try {
            url = new File("src/main/java/it/uniroma2/ispw/globe/view/ManageRequestView.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            ManageRequestGUIController controller = new ManageRequestGUIController(sessionId);
            loader.setController(controller);
            root.setCenter(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
