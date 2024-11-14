package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.Navigator;
import it.uniroma2.ispw.globe.model.bean.AttractionBean;
import it.uniroma2.ispw.globe.model.bean.CityBean;
import it.uniroma2.ispw.globe.model.bean.ItineraryBean;
import it.uniroma2.ispw.globe.view.DayTab;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class DisplayItineraryViewController {
    @FXML
    private Label nameLabel;
    @FXML
    private Button tripButton;
    @FXML
    private Button homeButton;
    @FXML
    private Label dayLabel;
    @FXML
    private Button userProfileButton;
    @FXML
    private Button backButton;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Button imageButton;
    @FXML
    private TabPane daysTabPane;

    private DayTab dayTab;

    public void initialize() {
        dayTab = new DayTab();
    }

    public void setItineraryData (ItineraryBean itineraryBean) {
        int day=0;

        nameLabel.setText(itineraryBean.getName());
        descriptionLabel.setText(itineraryBean.getDescription());
        dayLabel.setText(String.valueOf(itineraryBean.getNumberOfDays()));

        for (CityBean city : itineraryBean.getCities()) {
            dayTab.setViewTab(daysTabPane, city.getAttractions().size());
            Tab tab = daysTabPane.getTabs().get(day);

            VBox vbox = (VBox) tab.getContent();
            HBox cityBox = (HBox) vbox.getChildren().get(0);
            VBox attractionBox = (VBox) vbox.getChildren().get(1);

            Label city_l = (Label) cityBox.getChildren().get(1);
            city_l.setText(city.getName());

            Label accommodation_l = (Label) cityBox.getChildren().get(3);
            accommodation_l.setText(city.getAccommodation().getName());

            int i = 1;
            for (AttractionBean attraction : city.getAttractions()) {
                Label label = (Label) attractionBox.getChildren().get(i);
                label.setText(attraction.getName());
                i++;
            }
            day++;
        }
    }

    //HANDLER

    public void userTripHandler (ActionEvent event) {}

    public void userProfileHandler (ActionEvent event) {}

    public void userHomeHandler (ActionEvent event) {}

    public void backHandler (ActionEvent event) throws IOException {
        Navigator navigator = new Navigator();
        navigator.goToManageItinerary(event);
    }

    public void imageHandler (ActionEvent event) {}

}
