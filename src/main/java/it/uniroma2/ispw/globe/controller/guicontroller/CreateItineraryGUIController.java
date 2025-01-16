package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.ManageItineraryController;
import it.uniroma2.ispw.globe.controller.applicationcontroller.ResponseRequestController;
import it.uniroma2.ispw.globe.model.bean.AttractionBean;
import it.uniroma2.ispw.globe.model.bean.CityBean;
import it.uniroma2.ispw.globe.model.bean.ItineraryBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.uniroma2.ispw.globe.other.UserType.AGENCY;
import static it.uniroma2.ispw.globe.other.UserType.USER;

public class CreateItineraryGUIController {
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

    private String sessionId;
    private boolean isProposal;

    public CreateItineraryGUIController(String sessionId,boolean isProposal) {
        this.sessionId = sessionId;
        this.isProposal = isProposal;
    }

    public void initialize() {
        ItineraryBean itineraryBean = new ManageItineraryController().getItinerary(null,sessionId);
        if (itineraryBean != null) {
            itineraryField.setText(itineraryBean.getName());
            dayField.setText(String.valueOf(itineraryBean.getDuration()));
            descriptionField.setText(itineraryBean.getDescription());
            for (String cityID : itineraryBean.getCities()) {
                CityBean city = new ManageItineraryController().getCity(0,cityID,null);
                Button cityButton = new Button(city.getName() + " - " + city.getCountry());
                cityButton.setOnAction(event -> {
                    int count = 0;
                    if (cityVBox.getChildren().isEmpty()) {
                        Label cityLabel = new Label(city.getName());
                        cityLabel.setUserData(city.getId());
                        cityVBox.getChildren().add(cityLabel);
                    } else {
                        for (int i = 0; i < cityVBox.getChildren().size(); i++) {
                            Label otherCity = (Label) cityVBox.getChildren().get(i);
                            if (city.getName().equals(otherCity.getText())) {
                                count++;
                            }
                        }
                        if (count == 0) {
                            Label cityLabel = new Label(city.getName());
                            cityLabel.setUserData(city.getId());
                            cityVBox.getChildren().add(cityLabel);
                        }
                    }
                });
                cityResultVBox.getChildren().add(cityButton);
            }
            for (String attractionID : itineraryBean.getAttractions()) {
                AttractionBean attraction = new ManageItineraryController().getAttraction(0,attractionID,null);
                Button attractionButton = new Button(attraction.getName()+" - "+attraction.getCity());
                attractionButton.setOnAction(event -> {
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
                            attractionLabel.setUserData(attractionLabel.getId());
                            attractionVBox.getChildren().add(attractionLabel);
                        }
                    }
                });
                attractionResultVBox.getChildren().add(attractionButton);
            }
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

        Map<String,String> attractionMap = new HashMap<>();

        ItineraryBean itineraryBean = new ItineraryBean(null,itinerary,description,"",day,cities,attractions,0,0,0,0,attractionMap);

        new ManageItineraryController().createItinerary(itineraryBean,sessionId);

        //inserisci id itinerario

        URL url;
        Parent root;

        try {
            if (isProposal) {
                url = new File("src/main/java/it/uniroma2/ispw/globe/view/DisplayProposalView.fxml").toURI().toURL();

                DisplayProposalGUIController controller = new DisplayProposalGUIController(sessionId,null);
                FXMLLoader loader = new FXMLLoader(url);
                loader.setController(controller);
                root = loader.load();
            } else {
                url = new File("src/main/java/it/uniroma2/ispw/globe/view/DisplayItineraryView.fxml").toURI().toURL();
                DisplayItineraryGUIController controller = new DisplayItineraryGUIController(sessionId,null,null);
                FXMLLoader loader = new FXMLLoader(url);
                loader.setController(controller);
                root = loader.load();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void searchCity() {
        List<CityBean> cities;
        cityResultVBox.getChildren().clear();

        String city = cityField.getText();

        cities = new ManageItineraryController().getCities(city);
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

    public void searchAttraction() {
        List<AttractionBean> attractions;
        attractionResultVBox.getChildren().clear();

        String attraction = attractionField.getText();

        attractions = new ManageItineraryController().getAttractions(attraction);

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

    public void addFlight() {}

    public void addAccommodation() {}

    public void goBack(ActionEvent event) {
        URL url;
        Parent root;
        FXMLLoader loader;

        try {
            if (isProposal) {
                url = new File("src/main/java/it/uniroma2/ispw/globe/view/CreateProposalView.fxml").toURI().toURL();
                loader = new FXMLLoader(url);
                CreateProposalGUIController controller = new CreateProposalGUIController(sessionId);loader.setController(controller);
                root = loader.load();
            } else {
                url = new File("src/main/java/it/uniroma2/ispw/globe/view/ManageItineraryView.fxml").toURI().toURL();
                loader = new FXMLLoader(url);
                ManageItineraryGUIController controller = new ManageItineraryGUIController(sessionId);
                loader.setController(controller);
                root = loader.load();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
