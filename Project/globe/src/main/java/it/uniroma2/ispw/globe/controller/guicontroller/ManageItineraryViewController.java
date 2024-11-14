package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.Navigator;
import it.uniroma2.ispw.globe.model.bean.ItineraryBean;
import it.uniroma2.ispw.globe.other.Session;
import it.uniroma2.ispw.globe.view.ItineraryBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class ManageItineraryViewController {
    @FXML
    private Button addButton;
    @FXML
    private Button tripButton;
    @FXML
    private Button profileButton;
    @FXML
    private Button homeButton;
    @FXML
    private VBox itinerariesVBox;

    public void initialize() {
        List<ItineraryBean> itineraries;
        itineraries = Session.getInstance().getUser().getItineraries();
        for (ItineraryBean itineraryBean : itineraries) {
            ItineraryBox itineraryBox = new ItineraryBox();
            itineraryBox.setItinerary(itinerariesVBox, itineraryBean).setOnAction(event -> {
                try {
                    viewItinerary(event, itineraryBean);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public void viewItinerary(ActionEvent event, ItineraryBean itineraryBean) throws IOException {
        Navigator navigator = new Navigator();
        navigator.goToDispalyItinerary(event, itineraryBean);
    }

    //HANDLER
    public void addHandler (ActionEvent event) throws IOException {
        Navigator navigator = new Navigator();
        navigator.goToCreateTrip(event);
    }

    public void userTripHandler (ActionEvent event) {}

    public void userProfileHandler (ActionEvent event) {}

    public void userHomeHandler (ActionEvent event) {}
}
