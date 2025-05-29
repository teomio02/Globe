package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.bean.*;
import it.uniroma2.ispw.globe.controller.applicationcontroller.RequestItineraryController;
import it.uniroma2.ispw.globe.controller.applicationcontroller.ResponseRequestController;
import it.uniroma2.ispw.globe.exception.DuplicateItemException;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
import it.uniroma2.ispw.globe.exception.IncorrectDataException;
import it.uniroma2.ispw.globe.model.bean.*;
import it.uniroma2.ispw.globe.engineering.session.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.util.List;

public class DisplayRequestGUIController extends AbstractGUIController {
    @FXML
    private Label daysLabel;
    @FXML
    private Label userLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private HBox typesHBox;
    @FXML
    private Button saveRequestButton;
    @FXML
    private Button createItineraryButton;
    @FXML
    private Label drivingHoursLabel;
    @FXML
    private Label modeLabel;
    @FXML
    private Label difficultLabel;
    @FXML
    private Label distanceLabel;
    @FXML
    private TabPane typeTabPane;
    @FXML
    private Tab onTheRoadTab;
    @FXML
    private Tab natureTab;
    @FXML
    private Tab relaxTab;
    @FXML
    private Tab cultureTab;
    @FXML
    private Tab cityTab;


    private String sessionId;
    private String requestId;
    private Node prev;

    public void initialize(String sessionId) {
        NavigationData data = SessionManager.getInstance().getSession(sessionId).getNavigationData();
        this.sessionId = data.getSessionID();
        this.requestId = data.getRequestID();
        this.prev = data.getPrev();

        RequestBean request;
        List<Object> optionals;
        NatureBean nature;
        OnTheRoadBean onTheRoad;


        if (requestId != null) {
            createItineraryButton.setVisible(true);
            saveRequestButton.setVisible(false);
            //create proposal use case

            try {
                request = new ResponseRequestController().getAgencyRequest(requestId, sessionId);
                optionals = new ResponseRequestController().getRequestOptional(requestId, sessionId);
            } catch (FailedOperationException | DuplicateItemException | IncorrectDataException e) {
                new ErrorPopUpGUIController().createPopUp(e.getMessage());
                goBack();
                return;
            }
            userLabel.setText(request.getUser());
            descriptionLabel.setText(request.getOtherRequests());
            daysLabel.setText(String.valueOf(request.getDayNum()));
            for (String type: request.getTypes()) {
                typesHBox.getChildren().add(new Label(type));
            }

        } else {
            if (requestId == null) {
                saveRequestButton.setVisible(true);
            }
            createItineraryButton.setVisible(false);

            List<AgencyBean> agencies;
            try {
                request = new RequestItineraryController().getRequest(requestId, sessionId);
                optionals = new RequestItineraryController().getRequestOptional(requestId, sessionId);
                agencies = new RequestItineraryController().getAgencies(sessionId);
            } catch (FailedOperationException | DuplicateItemException | IncorrectDataException e) {
                new ErrorPopUpGUIController().createPopUp(e.getMessage());
                goBack();
                return;
            }

            userLabel.setText("");
            for (AgencyBean agency : agencies) {
                userLabel.setText(userLabel.getText() + ", " + agency.getName());
            }
            descriptionLabel.setText(request.getOtherRequests());
            daysLabel.setText(String.valueOf(request.getDayNum()));
            for (String type: request.getTypes()) {
                typesHBox.getChildren().add(new Label(type));
            }
        }
        if (optionals != null && !optionals.isEmpty()) {
            for (Object optional: optionals) {
                if (optional instanceof OnTheRoadBean) {
                    onTheRoad = (OnTheRoadBean) optional;
                    onTheRoadTab.setDisable(false);
                    typeTabPane.getSelectionModel().select(onTheRoadTab);
                    drivingHoursLabel.setText(String.valueOf((onTheRoad).getDayDrivingHours()));
                    modeLabel.setText(onTheRoad.getMode());
                } else if (optional instanceof NatureBean) {
                    nature = (NatureBean) optional;
                    natureTab.setDisable(false);
                    typeTabPane.getSelectionModel().select(natureTab);
                    difficultLabel.setText(nature.getDifficulty());
                    distanceLabel.setText(String.valueOf(nature.getTrekkingDistance()));
                }
            }
        }
    }

    public void createItinerary(ActionEvent event) {
        try {
            new ResponseRequestController().setPendingRequest(sessionId,requestId);
        } catch (FailedOperationException | DuplicateItemException e) {
            new ErrorPopUpGUIController().createPopUp(e.getMessage());
            return;
        }

        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        ViewManager viewManager = new ViewManager();
        viewManager.goToCreateItineraryGUI(sessionId,requestId,root);
    }
    public void saveRequest(ActionEvent event) {
        try {
            new RequestItineraryController().saveRequest(sessionId);
        } catch (FailedOperationException | DuplicateItemException e) {
            new ErrorPopUpGUIController().createPopUp(e.getMessage());
            goBack();
            return;
        }

        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        ViewManager viewManager = new ViewManager();
        viewManager.goToManageItineraryGUI(sessionId,root);
    }

    public void goBack() {
        BorderPane root = (BorderPane) userLabel.getScene().getRoot();
        if (prev != null) {
            root.setCenter(prev);
        }
    }
}
