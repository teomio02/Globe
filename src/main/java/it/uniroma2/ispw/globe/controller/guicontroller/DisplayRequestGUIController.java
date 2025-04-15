package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.RequestItineraryController;
import it.uniroma2.ispw.globe.controller.applicationcontroller.ResponseRequestController;
import it.uniroma2.ispw.globe.exception.ItemNotFoundException;
import it.uniroma2.ispw.globe.model.bean.AgencyRequestBean;
import it.uniroma2.ispw.globe.model.bean.*;
import it.uniroma2.ispw.globe.other.session.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class DisplayRequestGUIController extends AbstractGUIController {
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
    private Node prev;

    public void initialize(String sessionId) {
        NavigationData data = SessionManager.getInstance().getSession(sessionId).getNavigationData();
        this.sessionId = data.getSessionID();
        this.requestId = data.getRequestID();
        this.prev = data.getPrev();

        // popola con use case simo (create request use case)

        if (requestId != null) {

            //create proposal use case
            AgencyRequestBean request = null;
            try {
                request = new ResponseRequestController().getAgencyRequest(requestId, sessionId);
            } catch (ItemNotFoundException e) {
                new ErrorPopUpGUIController().createPopUp(e.getMessage());
                return;
            }
            userLabel.setText(request.getUser());
            descriptionLabel.setText(request.getDescription());
            daysLabel.setText(String.valueOf(request.getDays()));
            for (String type: request.getTypes()) {
                typesHBox.getChildren().add(new Label(type));
            }
        } else {
            RequestBean request = new RequestItineraryController().getRequest(requestId, sessionId);
            AgencyBean agency = null;
            try {
                agency = new RequestItineraryController().getAgency(null,sessionId);
            } catch (ItemNotFoundException e) {
                new ErrorPopUpGUIController().createPopUp(e.getMessage());
                return;
            }
            userLabel.setText(agency.getName());
            descriptionLabel.setText(request.getOtherRequests());
            daysLabel.setText(String.valueOf(request.getDayNum()));
            for (String type: request.getItineraryType()) {
                typesHBox.getChildren().add(new Label(type));
            }


        }
    }


    public void createItinerary(ActionEvent event) {
        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        ViewManager viewManager = new ViewManager();
        viewManager.goToCreateItineraryGUI(sessionId,requestId,root);
    }

    public void goBack(ActionEvent event) {
        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        if (prev != null) {
            root.setCenter(prev);
        }
    }
}
