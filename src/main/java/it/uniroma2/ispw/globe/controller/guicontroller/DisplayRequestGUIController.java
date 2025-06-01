package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.bean.*;
import it.uniroma2.ispw.globe.controller.applicationcontroller.RequestItineraryController;
import it.uniroma2.ispw.globe.controller.applicationcontroller.ResponseRequestController;
import it.uniroma2.ispw.globe.exception.DuplicateItemException;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
import it.uniroma2.ispw.globe.exception.IncorrectDataException;
import it.uniroma2.ispw.globe.engineering.session.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

import java.util.List;

public class DisplayRequestGUIController extends AbstractGUIController {
    @FXML
    private Label daysLabel;
    @FXML
    private Label userLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label typesLabel;
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
    @FXML
    private Label cityLabel;
    @FXML
    private Label attractionLabel;




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
            StringBuilder types = new StringBuilder();
            for (String type: request.getTypes()) {
                types.append(type).append(", ");
            }
            types.setLength(types.length() - 2);
            typesLabel.setText(types.toString());

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
            StringBuilder agenciesString = new StringBuilder();
            for (AgencyBean agency : agencies) {
                agenciesString.append(agency.getName()).append(", ");
            }
            agenciesString.setLength(agenciesString.length() - 2);
            userLabel.setText(agenciesString.toString());

            descriptionLabel.setText(request.getOtherRequests());
            daysLabel.setText(String.valueOf(request.getDayNum()));
            typesLabel.setText("");
            for (String type: request.getTypes()) {
                typesLabel.setText(typesLabel.getText() + type + ", ");
            }

            displayCities(request);

            displayAttractions(request);
        }

        displayOptinal(optionals);
    }

    public void displayCities(RequestBean request) {
        cityLabel.setText("");
        for (String cityID : request.getCities()) {
            CityBean city;
            try {
                city = new RequestItineraryController().getCity(cityID);
            } catch (FailedOperationException e) {
                throw new RuntimeException(e);
            }
            cityLabel.setText(cityLabel.getText() +  city.getName() + ", ");
        }
    }

    public void displayAttractions(RequestBean request) {
        attractionLabel.setText("");
        for (String attractionID: request.getAttractions()) {
            AttractionBean attraction;
            try {
                attraction = new RequestItineraryController().getAttraction(attractionID);
            } catch (FailedOperationException e) {
                throw new RuntimeException(e);
            }
            attractionLabel.setText(attractionLabel.getText() +  attraction.getName() + ", ");
        }
    }

    public void displayOptinal(List<Object> optionals) {
        if (optionals != null && !optionals.isEmpty()) {
            for (Object optional: optionals) {
                if (optional instanceof OnTheRoadBean onTheRoad) {
                    onTheRoadTab.setDisable(false);
                    typeTabPane.getSelectionModel().select(onTheRoadTab);
                    drivingHoursLabel.setText(String.valueOf((onTheRoad).getDayDrivingHours()));
                    modeLabel.setText(onTheRoad.getMode());
                } else if (optional instanceof NatureBean nature) {
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
