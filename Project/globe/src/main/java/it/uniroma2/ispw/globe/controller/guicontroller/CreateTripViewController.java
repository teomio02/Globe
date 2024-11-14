package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.ManageItineraryController;
import it.uniroma2.ispw.globe.controller.applicationcontroller.Navigator;
import it.uniroma2.ispw.globe.model.bean.AccommodationBean;
import it.uniroma2.ispw.globe.model.bean.AttractionBean;
import it.uniroma2.ispw.globe.model.bean.CityBean;
import it.uniroma2.ispw.globe.model.bean.ItineraryBean;
import it.uniroma2.ispw.globe.other.Session;
import it.uniroma2.ispw.globe.view.DayTab;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CreateTripViewController {
    @FXML
    private Label nameErrorLabel;
    @FXML
    private Button minusButton;
    @FXML
    private Button tripButton;
    @FXML
    private TextField tripNameField;
    @FXML
    private Button homeButton;
    @FXML
    private Label dayLabel;
    @FXML
    private Button nextButton;
    @FXML
    private Button plusButton;
    @FXML
    private Label dayErrorLabel;
    @FXML
    private Button userProfileButton;
    @FXML
    private TextField tripDescriptionField;
    @FXML
    private Button backButton;
    @FXML
    private Label descriptionErrorLabel;
    @FXML
    private Button imageButton;
    @FXML
    private TabPane daysTabPane;

    private DayTab dayTab;

    public void initialize() {
        dayTab = new DayTab();
    }

    //HANDLER

    public void userTripHandler (ActionEvent event) {}

    public void userProfileHandler (ActionEvent event) {}

    public void userHomeHandler (ActionEvent event) {}

    public void backHandler (ActionEvent event) {}

    public void imageHandler (ActionEvent event) {}

    public void minusHandler (ActionEvent event) {
        int dayNum = Integer.valueOf(dayLabel.getText());
        if(dayNum>0){
            dayLabel.setText(String.valueOf(dayNum-1));
            dayTab.removeTab(daysTabPane);
        }
    }

    public void plusHandler (ActionEvent event) {
        int dayNum = Integer.valueOf(dayLabel.getText());
        if(dayNum<99){
            dayLabel.setText(String.valueOf(dayNum+1));
            dayTab.setTab(daysTabPane);
        }
    }

    public void nextHandler (ActionEvent event) throws SQLException, IOException {
        String tripName, description;
        int dayNum;
        int count=0;
        List<CityBean> cities = new ArrayList<>();

        tripName = this.tripNameField.getText();
        if(tripName.isEmpty()){
            this.nameErrorLabel.setVisible(true);
            count ++;
        }else{
            this.nameErrorLabel.setVisible(false);
        }

        description = this.tripDescriptionField.getText();
        if(description.isEmpty()){
            this.descriptionErrorLabel.setVisible(true);
            count ++;
        }else{
            this.descriptionErrorLabel.setVisible(false);
        }

        dayNum = Integer.valueOf(this.dayLabel.getText());
        if(dayNum==0){
            this.dayErrorLabel.setVisible(true);
            count ++;
        }else{
            this.dayErrorLabel.setVisible(false);
        }
        for(Tab tab : daysTabPane.getTabs()){
            int attraction_count=2;
            List<AttractionBean> attractions = new ArrayList<>();

            VBox vbox = (VBox) tab.getContent();
            HBox cityHbox = (HBox) vbox.getChildren().get(0);
            HBox attractionHbox = (HBox) vbox.getChildren().get(1);

            TextField city_tf = (TextField) cityHbox.getChildren().get(0);
            TextField accommodation_tf = (TextField) cityHbox.getChildren().get(1);
            if (city_tf.getText().isEmpty() || accommodation_tf.getText().isEmpty()) {
                count++;
            }else{
                while(vbox.getChildren().size()>attraction_count){
                    AttractionBean attraction;
                    Label attraction_label = (Label) vbox.getChildren().get(attraction_count);
                    attraction = new AttractionBean(attraction_label.getText());
                    if (attraction_label.getText().isEmpty()) {
                        count++;
                    }
                    attractions.add(attraction);
                    attraction_count++;
                }
                AccommodationBean accommodation = new AccommodationBean(accommodation_tf.getText());
                CityBean city = new CityBean(city_tf.getText(),accommodation,attractions);
                cities.add(city);
            }


            
        }

        if (count == 0 ){
            List<ItineraryBean> itineraries = new ArrayList<>();
            ItineraryBean itineraryBean = new ItineraryBean(tripName, description, dayNum, cities);
            itineraries = Session.getInstance().getUser().getItineraries();
            itineraries.add(itineraryBean);
            Session.getInstance().getUser().setItineraries(itineraries);

            ManageItineraryController controller = new ManageItineraryController();
            controller.addItinerary(itineraryBean);
        }

        Navigator navigator = new Navigator();
        navigator.goToManageItinerary(event);
    }
}
