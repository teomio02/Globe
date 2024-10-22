package it.ispw.globeapp.View;

import it.ispw.globeapp.Controller.Navigator;
import it.ispw.globeapp.Model.Bean.ItineraryBean;
import it.ispw.globeapp.Model.ItineraryEntity;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;


public class GuestTripView {
    @FXML
    private Button homeButton;
    @FXML
    private Button loginButton;
    @FXML
    private Button tripButton;
    @FXML
    private Button minusButton;
    @FXML
    private Button plusButton;
    @FXML
    private Button imageButton;
    @FXML
    private Button nextButton;
    @FXML
    private Label dayLabel;
    @FXML
    private Label nameErrorLabel;
    @FXML
    private Label dayErrorLabel;
    @FXML
    private Label descriptionErrorLabel;
    @FXML
    private TextField tripNameField;
    @FXML
    private TextField tripDescriptionField;


    public void minusHandler(ActionEvent event) throws IOException {
        int dayNum = Integer.valueOf(dayLabel.getText());
        if(dayNum>0){
            dayLabel.setText(String.valueOf(dayNum-1));
        }
    }

    public void plusHandler(ActionEvent event) throws IOException {
        int dayNum = Integer.valueOf(dayLabel.getText());
        dayLabel.setText(String.valueOf(dayNum+1));
    }

    public void imageHandler(ActionEvent event) throws IOException {}

    public void nextHandler(ActionEvent event) throws IOException {
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
            System.out.println(tripName+" "+description+" "+dayNum);
            Navigator navigator = new Navigator();
            navigator.goToReviewItinerary(itineraryBean,event);


        }

    }

    public void GuestHomeHandler(ActionEvent event) throws IOException {
        Navigator navigator = new Navigator();
        navigator.goToGuestHome(event);
    }

    public void loginHandler(ActionEvent event) throws IOException {
        Navigator navigator = new Navigator();
        navigator.goToLogin(event);
    }
    public void GuestTripHandler(ActionEvent event) throws IOException {
        Navigator navigator = new Navigator();
        navigator.goToGuestTrip(event);
    }
}
