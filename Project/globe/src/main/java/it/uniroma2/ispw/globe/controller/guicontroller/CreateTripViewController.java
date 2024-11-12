package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.ManageItineraryController;
import it.uniroma2.ispw.globe.controller.applicationcontroller.Navigator;
import it.uniroma2.ispw.globe.model.bean.ItineraryBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

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
    private HBox daysHBox;

    private List<Button> dayButtons;

    //HANDLER
    public void initialize() {
        dayButtons = new ArrayList<>();
    }

    public void userTripHandler (ActionEvent event) {}

    public void userProfileHandler (ActionEvent event) {}

    public void userHomeHandler (ActionEvent event) {}

    public void backHandler (ActionEvent event) {}

    public void imageHandler (ActionEvent event) {}

    public void minusHandler (ActionEvent event) {
        int dayNum = Integer.valueOf(dayLabel.getText());
        if(dayNum>0){
            dayLabel.setText(String.valueOf(dayNum-1));
            daysHBox.getChildren().remove(dayButtons.get(dayNum));
            this.dayButtons.remove(dayNum);
        }
    }

    public void plusHandler (ActionEvent event) {
        int dayNum = Integer.valueOf(dayLabel.getText());
        if(dayNum<99){
            dayLabel.setText(String.valueOf(dayNum+1));
            Button button = new Button(dayLabel.getText());
            this.dayButtons.add(button);
            daysHBox.getChildren().add(dayButtons.get(dayNum));
        }
    }

    public void nextHandler (ActionEvent event) throws SQLException, IOException {
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

       // Navigator navigator = new Navigator();
       // navigator.goToAddCity(event,tripName);
    }
}
