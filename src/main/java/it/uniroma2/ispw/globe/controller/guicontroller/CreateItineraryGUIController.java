package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.ManageItineraryController;
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



    public void saveItinerary(ActionEvent event) {
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

        ItineraryBean itineraryBean = new ItineraryBean(itinerary,description,"",day,cities,attractions,0,0,0,0,attractionMap);

        new ManageItineraryController().saveItinerary(itineraryBean,null);

        URL url;
        Parent root;

        try {
            url = new File("src/main/java/it/uniroma2/ispw/globe/view/DisplayItineraryView.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            root = loader.load();
            DisplayItineraryGUIController controller = loader.getController();
            controller.init(itinerary);
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
}
