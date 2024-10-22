package it.ispw.globeapp.View;

import it.ispw.globeapp.Controller.Navigator;
import it.ispw.globeapp.Model.Bean.ItineraryBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class ReviewItineraryView {
    @FXML
    private Button homeButton;
    @FXML
    private Button loginButton;
    @FXML
    private Button tripButton;
    @FXML
    private Button nextAttrButton;
    @FXML
    private Button prevAttrButton;
    @FXML
    private Button backButton;
    @FXML
    private Button dayButton;
    @FXML
    private Label nameLabel;
    @FXML
    private Label daysLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private HBox buttonBox;

    public void setItineraryData(ItineraryBean itineraryBean){
        nameLabel.setText(itineraryBean.getName());
        descriptionLabel.setText(itineraryBean.getDescription());
        daysLabel.setText(String.valueOf(itineraryBean.getDays())+" days");
    }

    public void dayHandler(ActionEvent actionEvent){

    }

    public void prevAttrHandler(ActionEvent actionEvent){

    }

    public void nextAttrHandler(ActionEvent actionEvent){

    }

    public void backHandler(ActionEvent event) throws IOException {
        Navigator navigator = new Navigator();
        navigator.goToGuestTrip(event);
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
