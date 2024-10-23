package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.ManageItineraryController;
import it.uniroma2.ispw.globe.controller.applicationcontroller.Navigator;
import it.uniroma2.ispw.globe.model.Bean.ItineraryBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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
        }
    }

    public void plusHandler (ActionEvent event) {
        int dayNum = Integer.valueOf(dayLabel.getText());
        if(dayNum<99){
            dayLabel.setText(String.valueOf(dayNum+1));
        }
    }

    public void nextHandler (ActionEvent event) {
        String tripName, description;
        int dayNum;
        int count=0;

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

        if (count == 0 ){
            ItineraryBean itineraryBean = new ItineraryBean(tripName, description, dayNum);
            ManageItineraryController controller = new ManageItineraryController();
            controller.addItinerary(itineraryBean);
        }
    }
}
