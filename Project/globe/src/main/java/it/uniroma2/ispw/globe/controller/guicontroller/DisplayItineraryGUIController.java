package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.ManageItineraryController;
import it.uniroma2.ispw.globe.model.bean.AttractionBean;
import it.uniroma2.ispw.globe.model.bean.CityBean;
import it.uniroma2.ispw.globe.model.bean.ItineraryBean;
import it.uniroma2.ispw.globe.model.bean.StepBean;
import it.uniroma2.ispw.globe.view.DayTab;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;

public class DisplayItineraryGUIController {

    @FXML
    private Label dayLabel;
    @FXML
    private TabPane daysTabPane;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label nameLabel;

    public void init(String itineraryName) {
        if (itineraryName != null) {
            ItineraryBean itinerary = new ManageItineraryController().getItinerary(itineraryName);
            List<StepBean> steps = new ManageItineraryController().getSteps(itineraryName);
            nameLabel.setText(itinerary.getName());
            descriptionLabel.setText(itinerary.getDescription());
            dayLabel.setText(String.valueOf(itinerary.getDuration()));
            DayTab dayTab = new DayTab();
            int day = 0;
            for (StepBean step : steps) {
                dayTab.setViewTab(daysTabPane, step.getAttractions().size());
                Tab tab = daysTabPane.getTabs().get(day);

                VBox vbox = (VBox) tab.getContent();
                HBox cityBox = (HBox) vbox.getChildren().get(0);
                VBox attractionBox = (VBox) vbox.getChildren().get(1);

                Label city_l = (Label) cityBox.getChildren().get(1);

                CityBean city = new ManageItineraryController().getCity(step.getCity().getFirst());
                city_l.setText(city.getName()+", "+city.getCountry());

                Label accommodation_l = (Label) cityBox.getChildren().get(3);

                int i = 0;
                for (String attractionID : step.getAttractions()) {
                    AttractionBean attraction = new ManageItineraryController().getAttraction(attractionID);
                    Label label = (Label) attractionBox.getChildren().get(i);
                    label.setText(attraction.getName()+" - "+attraction.getCity()+", "+attraction.getAddress());
                    i++;
                }
                day++;
            }
        }
    }
}
