package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.RequestItineraryController;
import it.uniroma2.ispw.globe.exception.ItemNotFoundException;
import it.uniroma2.ispw.globe.exception.PlaceApiException;
import it.uniroma2.ispw.globe.model.bean.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

import static it.uniroma2.ispw.globe.other.ItineraryType.NATURE;
import static it.uniroma2.ispw.globe.other.ItineraryType.ON_THE_ROAD;

public class CreateRequestGUIController extends AbstractGUIController {
    @FXML
    private TextField citiesField;
    @FXML
    private TextField attractionsField;
    @FXML
    private TextField otherRequestsField;
    @FXML
    private TextField trekkingDistanceField;
    @FXML
    private TextField dayDrivingHoursField;
    @FXML
    private Label dayLabel;
    @FXML
    private VBox cityResultVBox;
    @FXML
    private VBox cityVBox;
    @FXML
    private VBox attractionResultVBox;
    @FXML
    private VBox attractionVBox;
    @FXML
    private VBox otherRequestsVBox;
    @FXML
    private VBox agencyResultVBox;
    @FXML
    private VBox agencyVBox;
    @FXML
    private VBox natureVBox;
    @FXML
    private VBox onTheRoadVBox;
    @FXML
    private Button flightButton;
    @FXML
    private Button accommodationButton;
    @FXML
    private Button onTheRoadButton;
    @FXML
    private Button natureButton;
    @FXML
    private Button normalDifficulty;
    @FXML
    private Button mediumDifficulty;
    @FXML
    private Button hardDifficulty;
    @FXML
    private Button morningMode;
    @FXML
    private Button lateAfternoonMode;
    @FXML
    private Button nightMode;


    private String sessionId;

    public void initialize(String sessionId) {

        this.sessionId = sessionId;

        onTheRoadButton.setUserData(false);
        flightButton.setUserData(false);
        accommodationButton.setUserData(false);
        natureButton.setUserData(false);
        natureVBox.setVisible(false);
        onTheRoadVBox.setVisible(false);

    }

    //HANDLER
    public void userTripHandler (ActionEvent event) {}
    public void userProfileHandler (ActionEvent event) {}
    public void userHomeHandler (ActionEvent event) {}
    public void backHandler (ActionEvent event) {}
    public void minusHandler (ActionEvent event) {
        int dayNum = Integer.valueOf(dayLabel.getText());
        if(dayNum>0){
            dayLabel.setText(String.valueOf(dayNum-1));}
    }
    public void plusHandler (ActionEvent event) {
        int dayNum = Integer.valueOf(dayLabel.getText());
        if (dayNum < 99) {
            dayLabel.setText(String.valueOf(dayNum + 1));
        }

    }
    public void searchCityHandler () {
        List<CityBean> cities;
        cityResultVBox.getChildren().clear();
        String city = citiesField.getText();
        cities = new RequestItineraryController().getCities(city);
        if (!cities.isEmpty()) {
            for (CityBean cityResult : cities) {
                Button cityButton = new Button(cityResult.getName()+" - "+ cityResult.getCountry());
                cityButton.setOnAction(event -> {
                    int count=0;
                    if(cityVBox.getChildren().isEmpty()){
                        Label cityLabel = new Label(cityResult.getName());
                        cityLabel.setUserData(cityResult.getId());
                        cityVBox.getChildren().add(cityLabel);
                    } else {
                        for (int i = 0; i < cityVBox.getChildren().size(); i++) {
                            Label otherCity = (Label) cityVBox.getChildren().get(i);
                            if(cityResult.getName().equals(otherCity.getText())){
                                count++;
                            }
                        }
                        if (count==0){
                            Label cityLabel = new Label(cityResult.getName());
                            cityLabel.setUserData(cityResult.getId());
                            cityVBox.getChildren().add(cityLabel);
                        }
                    }
                });
                cityResultVBox.getChildren().add(cityButton);
            }
        } else {
            Label errorLabel = new Label("Error: no place");
            cityResultVBox.getChildren().add(errorLabel);
        }

    }
    public void searchAttractionHandler () {
        List<AttractionBean> attractions;
        attractionResultVBox.getChildren().clear();

        String attraction = attractionsField.getText();

        attractions = new RequestItineraryController().getAttractions(attraction);

        if (!attractions.isEmpty()) {
            for (AttractionBean attractionResult : attractions) {
                Button attractionButton = new Button(attractionResult.getName()+" - "+attractionResult.getCity());
                attractionButton.setOnAction(event -> {
                    int count=0;
                    if(attractionVBox.getChildren().isEmpty()){
                        Label attractionLabel = new Label(attractionResult.getName());
                        attractionLabel.setUserData(attractionResult.getId());
                        attractionVBox.getChildren().add(attractionLabel);
                    } else {
                        for (int i = 0; i < attractionVBox.getChildren().size(); i++) {
                            Label otherAttraction = (Label) attractionVBox.getChildren().get(i);
                            if(attractionResult.getName().equals(otherAttraction.getText())){
                                count++;

                            }
                        }
                        if (count==0){
                            Label attractionLabel = new Label(attractionResult.getName());
                            attractionLabel.setUserData(attractionResult.getId());
                            attractionVBox.getChildren().add(attractionLabel);
                        }
                    }
                });
                attractionResultVBox.getChildren().add(attractionButton);
            }
        } else {
            Label errorLabel = new Label("Error: no place");
            cityResultVBox.getChildren().add(errorLabel);
        }
    }
    public void flightHandler (ActionEvent event) {
        flightButton.setUserData(true);
    }
    public void accommodationHandler (ActionEvent event) {
        accommodationButton.setUserData(true);

    }
    public void onTheRoadHandler (ActionEvent event) {
        onTheRoadButton.setUserData(true);
        onTheRoadVBox.setVisible(true);
    }
    public void chooseMode (ActionEvent event) {
        morningMode.setUserData(false);
        lateAfternoonMode.setUserData(false);
        nightMode.setUserData(false);
        Button mode = (Button) event.getSource();
        mode.setUserData(true);

    }
    public void natureHandler (ActionEvent event) {
        natureButton.setUserData(true);
        natureVBox.setVisible(true);
    }
    public void chooseDifficulty (ActionEvent event) {
        normalDifficulty.setUserData(false);
        mediumDifficulty.setUserData(false);
        hardDifficulty.setUserData(false);
        Button difficulty = (Button) event.getSource();
        difficulty.setUserData(true);
    }
    public void searchAgencyHandler () {
        List<AgencyBean> agencies;
        List<String> types = new ArrayList<>();
        if ((boolean) onTheRoadButton.getUserData()) {
            types.add(ON_THE_ROAD);
        }
        if ((boolean) natureButton.getUserData()) {
            types.add(NATURE);
        }

        agencies = new RequestItineraryController().getAgenciesByType(types);

        if (!agencies.isEmpty()) {
            for (AgencyBean agencyResult : agencies) {
                Button agencyButton = new Button(agencyResult.getName()+"-"+agencyResult.getRating());
                agencyButton.setOnAction(event -> {
                    int count=0;
                    if(agencyVBox.getChildren().isEmpty()){
                        Label agencyLabel = new Label(agencyResult.getName());
                        agencyLabel.setUserData(agencyResult.getName());
                        agencyVBox.getChildren().add(agencyLabel);
                    } else {
                        for (int i = 0; i < agencyVBox.getChildren().size(); i++) {
                            Label otherAgency = (Label) agencyVBox.getChildren().get(i);
                            if(agencyResult.getName().equals(otherAgency.getText())){
                                count++;
                            }
                        }
                        if (count==0){
                            Label agencyLabel = new Label(agencyResult.getName());
                            agencyLabel.setUserData(agencyResult.getName());
                            agencyVBox.getChildren().add(agencyLabel);
                        }
                    }
                });
                agencyResultVBox.getChildren().add(agencyButton);
            }
        } else {
            Label errorLabel = new Label("Error: no agency");
            agencyResultVBox.getChildren().add(errorLabel);
        }


    }
    public void sendRequestHandler (ActionEvent event) {

        int count=0;
        String city;
        String attraction;
        String agency;
        List<String> cities = new ArrayList<>();
        List<String> attractions = new ArrayList<>();
        List<String> agencies = new ArrayList<>();
        List<String> itineraryType = new ArrayList<>();
        String otherRequests = otherRequestsField.getText();
        int dayNum =  Integer.parseInt(dayLabel.getText());
        boolean flight = (boolean) flightButton.getUserData();
        boolean accommodation = (boolean) accommodationButton.getUserData();
        OnTheRoadBean onTheRoadBean = null;
        NatureBean natureBean = null;
        String mode = null;
        String difficulty = null;

        if ((boolean) onTheRoadButton.getUserData()) {
            itineraryType.add("onTheRoad");
            if((boolean) morningMode.getUserData()) {
                mode = "morningMode";
            } else if ((boolean) lateAfternoonMode.getUserData()) {
                mode = "lateAfternoonMode";
            } else if ((boolean) natureButton.getUserData()) {
                mode = "nightMode";
            }
            onTheRoadBean = new OnTheRoadBean( mode, dayDrivingHoursField.getText());
        }

        if ((boolean) natureButton.getUserData()) {
            itineraryType.add("nature");
            if((boolean) normalDifficulty.getUserData()) {
                difficulty = "normalDifficulty";
            } else if ((boolean) mediumDifficulty.getUserData()) {
                difficulty = "mediumDifficulty";
            } else if ((boolean) hardDifficulty.getUserData()) {
                difficulty = "hardDifficulty";
            }
            natureBean = new NatureBean( difficulty, trekkingDistanceField.getText());
        }

        for (int i = 0; i < cityVBox.getChildren().size(); i++) {
            city = (String) cityVBox.getChildren().get(i).getUserData();
            cities.add(city);
        }

        for (int i = 0; i < attractionVBox.getChildren().size(); i++) {
            attraction = (String) attractionVBox.getChildren().get(i).getUserData();
            attractions.add(attraction);
        }

        for (int i = 0; i < agencyVBox.getChildren().size(); i++) {
            agency = (String) agencyVBox.getChildren().get(i).getUserData();
            agencies.add(agency);
        }

        if(attractions.isEmpty()){
           // this.descriptionErrorLabel.setVisible(true);
            count ++;
        }else{
           // this.descriptionErrorLabel.setVisible(false);
        }
        if(dayNum==0){
          //  this.dayErrorLabel.setVisible(true);
            count ++;
        }else{
           // this.dayErrorLabel.setVisible(false);
            if (count == 0 ){
                RequestBean requestBean = new RequestBean(cities, attractions, otherRequests, dayNum, agencies, flight, accommodation, itineraryType);
                RequestItineraryController controller = new RequestItineraryController();
                try {
                    controller.createRequest(requestBean, onTheRoadBean, natureBean, sessionId);
                } catch (ItemNotFoundException | PlaceApiException e) {
                    new ErrorPopUpGUIController().createPopUp(e.getMessage());
                    return;
                }
            }
        }

        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        ViewManager viewManager = new ViewManager();
        viewManager.goToDisplayRequestGUI(sessionId,null, root);
    }
}
