package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.CreateItineraryController;
import it.uniroma2.ispw.globe.controller.applicationcontroller.ResponseRequestController;
import it.uniroma2.ispw.globe.exception.ItemNotFoundException;
import it.uniroma2.ispw.globe.exception.PlaceApiException;
import it.uniroma2.ispw.globe.model.bean.*;
import it.uniroma2.ispw.globe.other.session.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class CreateItineraryGUIController extends AbstractGUIController {
    @FXML
    private TextField cityField;
    @FXML
    private TextField itineraryField;
    @FXML
    private TextField dayField;
    @FXML
    private VBox cityVBox;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField attractionField;
    @FXML
    private VBox attractionVBox;
    @FXML
    private VBox cityResultVBox;
    @FXML
    private VBox attractionResultVBox;
    @FXML
    private VBox requestVBox;
    @FXML
    private Label userLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private HBox typesHBox;
    @FXML
    private VBox requestCityVBox;
    @FXML
    private VBox requestAttractionVBox;
    @FXML
    private VBox accommodationVBox;
    @FXML
    private VBox flightVBox;
    @FXML
    private Button accommodationButton;
    @FXML
    private Button flightButton;
    @FXML
    private TextField accommodationField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField inDepartureTimeLabel;
    @FXML
    private TextField inArrivalTimeLabel;
    @FXML
    private TextField outDepartureTimeLabel;
    @FXML
    private TextField outArrivalTimeLabel;
    @FXML
    private VBox accommodationResultVBox;



    private String sessionId;
    private String requestId;

    private Node prev;

    public void initialize(String sessionId) {

        NavigationData data = SessionManager.getInstance().getSession(sessionId).getNavigationData();
        this.sessionId = data.getSessionID();
        this.requestId = data.getRequestID();
        this.prev = data.getPrev();


        accommodationVBox.setVisible(false);
        flightVBox.setVisible(false);

        if (requestId != null) {
            try {
                //create proposal use case
                AgencyRequestBean requestBean = null;
                requestBean = new ResponseRequestController().getAgencyRequest(requestId,sessionId);

                if (requestBean != null) {
                    requestVBox.setVisible(true);
                    userLabel.setText(requestBean.getUser());
                    descriptionLabel.setText(requestBean.getDescription());
                    for (String type : requestBean.getTypes()) {
                        typesHBox.getChildren().add(new Label(type));
                    }
                    for (String city : requestBean.getCities()) {
                        CityBean cityBean = new CreateItineraryController().getCity(0,city,null);
                        requestCityVBox.getChildren().add(new Label(cityBean.getName()+" - "+cityBean.getCountry()));
                    }
                    for (String attraction : requestBean.getAttractions()) {
                        AttractionBean attractionBean = new CreateItineraryController().getAttraction(0,attraction,null);
                        requestAttractionVBox.getChildren().add(new Label(attractionBean.getName()+" - "+attractionBean.getCity()));
                    }
                }
            } catch (ItemNotFoundException | PlaceApiException e) {
                new ErrorPopUpGUIController().createPopUp(e.getMessage());
            }

        } else {
            requestVBox.setVisible(false);
        }
    }

    public void generateItinerary(ActionEvent event) {
        String city;
        String attraction;

        String itinerary = itineraryField.getText();
        int day =  Integer.parseInt(dayField.getText());
        String description = descriptionField.getText();

        List<String> cities = new ArrayList<>();
        List<String> attractions = new ArrayList<>();

        for (int i = 0; i < cityVBox.getChildren().size(); i++) {
            city = (String) cityVBox.getChildren().get(i).getUserData();
            cities.add(city);
        }

        for (int i = 0; i < attractionVBox.getChildren().size(); i++) {
            attraction = (String) attractionVBox.getChildren().get(i).getUserData();
            attractions.add(attraction);
        }

        List<String> types = new ArrayList<>();
        //popola types

        ItineraryBean itineraryBean = new ItineraryBean(null,itinerary,description,types,day,cities,attractions);

        if (accommodationVBox.isVisible()) {
            List<Pair<String, String>> accommodations = new ArrayList<>();
            for (int i = 0; i < accommodationResultVBox.getChildren().size(); i++) {
                accommodations.add((Pair<String, String>) accommodationResultVBox.getChildren().get(i).getUserData());
            }
            itineraryBean.setAccommodations(accommodations);
        }

        if (flightVBox.isVisible()) {
            if (!inArrivalTimeLabel.getText().isEmpty() && !inDepartureTimeLabel.getText().isEmpty()) {
                itineraryBean.setInboundFlightDepartureTime(Double.valueOf(inDepartureTimeLabel.getText()));
                itineraryBean.setInboundFlightArrivalTime(Double.valueOf(inArrivalTimeLabel.getText()));
            }
            if (!outArrivalTimeLabel.getText().isEmpty() && !outDepartureTimeLabel.getText().isEmpty()) {
                itineraryBean.setOutboundFlightArrivalTime(Double.valueOf(outArrivalTimeLabel.getText()));
                itineraryBean.setOutboundFlightDepartureTime(Double.valueOf(outDepartureTimeLabel.getText()));
            }
        }

        try {
            new CreateItineraryController().createItinerary(itineraryBean,sessionId);
        } catch (PlaceApiException e) {
            new ErrorPopUpGUIController().createPopUp(e.getMessage());
            return;
        }

        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        ViewManager viewManager = new ViewManager();
        if (requestId != null) {
            viewManager.goToDisplayItineraryGUI(sessionId,null,requestId,null, root);
        } else {
            viewManager.goToDisplayItineraryGUI(sessionId,null,null,null, root);
        }
    }

    public void searchCity() {
        List<CityBean> cities;
        cityResultVBox.getChildren().clear();

        String city = cityField.getText();

        try {
            cities = new CreateItineraryController().getCities(city);
        } catch (PlaceApiException e) {
            new ErrorPopUpGUIController().createPopUp(e.getMessage());
            return;
        }
        if (!cities.isEmpty()) {
            for (CityBean cityResult : cities) {
                Button cityButton = new Button(cityResult.getName()+" - "+ cityResult.getCountry());
                cityButton.setOnAction(event -> addCity(cityResult));
                cityResultVBox.getChildren().add(cityButton);
            }
        } else {
            Label errorLabel = new Label("Error: no place");
            cityResultVBox.getChildren().add(errorLabel);
        }

    }

    public void addCity(CityBean city) {
        int count=0;
        if(cityVBox.getChildren().isEmpty()){
            Label cityLabel = new Label(city.getName());
            cityLabel.setUserData(city.getId());
            cityVBox.getChildren().add(cityLabel);
        } else {
            for (int i = 0; i < cityVBox.getChildren().size(); i++) {
                Label otherCity = (Label) cityVBox.getChildren().get(i);
                if(city.getName().equals(otherCity.getText())){
                    count++;
                }
            }
            if (count==0){
                Label cityLabel = new Label(city.getName());
                cityLabel.setUserData(city.getId());
                cityVBox.getChildren().add(cityLabel);
            }
        }
        cityResultVBox.getChildren().clear();
    }

    public void searchAttraction() {
        List<AttractionBean> attractions;
        attractionResultVBox.getChildren().clear();

        String attraction = attractionField.getText();

        try {
            attractions = new CreateItineraryController().getAttractions(attraction);
        } catch (PlaceApiException e) {
            new ErrorPopUpGUIController().createPopUp(e.getMessage());
            return;
        }

        if (!attractions.isEmpty()) {
            for (AttractionBean attractionResult : attractions) {
                Button attractionButton = new Button(attractionResult.getName()+" - "+attractionResult.getCity());
                attractionButton.setOnAction(event -> addAttraction(attractionResult));
                attractionResultVBox.getChildren().add(attractionButton);
            }
        } else {
            Label errorLabel = new Label("Error: no place");
            cityResultVBox.getChildren().add(errorLabel);
        }
    }

    public void addAttraction(AttractionBean attraction) {
        int count=0;
        if(attractionVBox.getChildren().isEmpty()){
            Label attractionLabel = new Label(attraction.getName());
            attractionLabel.setUserData(attraction.getId());
            attractionVBox.getChildren().add(attractionLabel);
        } else {
            for (int i = 0; i < attractionVBox.getChildren().size(); i++) {
                Label otherAttraction = (Label) attractionVBox.getChildren().get(i);
                if(attraction.getName().equals(otherAttraction.getText())){
                    count++;

                }
            }
            if (count==0){
                Label attractionLabel = new Label(attraction.getName());
                attractionLabel.setUserData(attraction.getId());
                attractionVBox.getChildren().add(attractionLabel);
            }
        }
        attractionResultVBox.getChildren().clear();
    }

    public void addFlight() {
        flightButton.setVisible(false);
        flightVBox.setVisible(true);
    }

    public void addAccommodation() {
        if (accommodationVBox.isVisible()) {
            if (!accommodationField.getText().isEmpty() && !addressField.getText().isEmpty()) {
                Label accommodationLabel = new Label(accommodationField.getText()+", "+addressField.getText());
                Pair<String,String> accommodation = new Pair<>(accommodationField.getText(),addressField.getText());
                accommodationLabel.setUserData(accommodation);
                accommodationResultVBox.getChildren().add(accommodationLabel);
                accommodationField.setText("");
                addressField.setText("");
            }
        } else {
            accommodationButton.setVisible(false);
            accommodationVBox.setVisible(true);
        }
    }

    public void goBack(ActionEvent event) {
        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        if (prev != null) {
            root.setCenter(prev);
        }
    }
}
