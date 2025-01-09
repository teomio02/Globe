package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.model.Bean.UserRequestBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.List;

public class CreateRequestGUIController {
    @FXML
    private TextField citiesField;
    @FXML
    private TextField attractionsField;
    @FXML
    private TextField otherRequestsField;
    @FXML
    private TextField agencyField;
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
    public void searchCityHandler (ActionEvent event) {

    }
    public void searchAttractionHandler (ActionEvent event) {

    }
    public void flightHandler (ActionEvent event) {

    }
    public void accommodationHandler (ActionEvent event) {

    }
    public void onTheRoadHandler (ActionEvent event) {

    }
    public void natureHandler (ActionEvent event) {

    }
    public void searchAgencyHandler (ActionEvent event) {}
    public void sendRequestHandler (ActionEvent event) {
        List<String> cities, attractions, agencies, itineraryType;
        String otherRequests;
        boolean flight, accommodation;
        int dayNum;
        int count=0;



        otherRequests = this.otherRequestsField.getText();
        if(attractions.isEmpty()){
           // this.descriptionErrorLabel.setVisible(true);
            count ++;
        }else{
           // this.descriptionErrorLabel.setVisible(false);
        }

        dayNum = Integer.valueOf(this.dayLabel.getText());
        if(dayNum==0){
          //  this.dayErrorLabel.setVisible(true);
            count ++;
        }else{
           // this.dayErrorLabel.setVisible(false);
            if (count == 0 ){
                UserRequestBean requestBean = new UserRequestBean(cities,attractions, otherRequests, dayNum, agencies, flight, accommodation, itineraryType);
           //     RequestItineraryController controller = new RequestItineraryController();
              //  controller.sendRequest(requestBean);
            }
        }

    }

    public void searchCity() {

    }





}
