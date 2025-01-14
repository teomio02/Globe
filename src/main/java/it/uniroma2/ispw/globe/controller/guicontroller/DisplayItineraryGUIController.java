package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.ManageItineraryController;
import it.uniroma2.ispw.globe.model.Account;
import it.uniroma2.ispw.globe.model.Agency;
import it.uniroma2.ispw.globe.model.User;
import it.uniroma2.ispw.globe.model.bean.AttractionBean;
import it.uniroma2.ispw.globe.model.bean.CityBean;
import it.uniroma2.ispw.globe.model.bean.ItineraryBean;
import it.uniroma2.ispw.globe.model.bean.StepBean;
import it.uniroma2.ispw.globe.other.session.SessionManager;
import it.uniroma2.ispw.globe.view.DayTab;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
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

    private String sessionId;
    private String proposalId;
    private String itineraryId;

    public DisplayItineraryGUIController(String sessionId, String proposalId, String itineraryId) {
        this.sessionId = sessionId;
        this.proposalId = proposalId;
        this.itineraryId = itineraryId;
    }

    public void initialize() {
        ItineraryBean itinerary = new ManageItineraryController().getItinerary(itineraryId,sessionId);
        List<StepBean> steps = new ManageItineraryController().getSteps(itineraryId,sessionId);
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

            Label cityLabel = (Label) cityBox.getChildren().get(1);
            CityBean city;
            if (itineraryId == null) {
                city = new ManageItineraryController().getCity(step.getNum(),step.getCity().get(0),sessionId);
            } else {
                city = new ManageItineraryController().getCity(step.getNum(),step.getCity().get(0),null);
            }
            cityLabel.setText(city.getName()+", "+city.getCountry());

            Label accommodationLabel = (Label) cityBox.getChildren().get(3);

            int i = 0;
            for (String attractionID : step.getAttractions()) {
                AttractionBean attraction;
                if (itineraryId == null) {
                    attraction = new ManageItineraryController().getAttraction(step.getNum(),attractionID,sessionId);
                } else {
                    attraction = new ManageItineraryController().getAttraction(step.getNum(),attractionID,null);
                }
                Label label = (Label) attractionBox.getChildren().get(i);
                label.setText(attraction.getName()+" - "+attraction.getCity()+", "+attraction.getAddress());
                i++;
            }
            day++;
        }
    }

    public void showItineraries(ActionEvent event) {

        if (itineraryId == null) {
            new ManageItineraryController().saveItinerary(sessionId);
        }

        URL url;
        Parent root;

        try {
            url = new File("src/main/java/it/uniroma2/ispw/globe/view/ManageItineraryView.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            ManageItineraryGUIController controller = new ManageItineraryGUIController(sessionId);
            loader.setController(controller);
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}
