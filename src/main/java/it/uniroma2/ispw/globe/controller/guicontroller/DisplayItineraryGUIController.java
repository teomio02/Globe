package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.ManageItineraryController;
import it.uniroma2.ispw.globe.controller.applicationcontroller.ResponseRequestController;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
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
    @FXML
    private Button nextUserButton;
    @FXML
    private Button nextAgencyButton;

    private String sessionId;
    private String itineraryId;
    private String requestId;
    private String proposalId;

    public DisplayItineraryGUIController(String sessionId,String itineraryId, String requestId, String proposalId) {
        this.sessionId = sessionId;
        this.itineraryId = itineraryId;
        this.requestId = requestId;
        this.proposalId = proposalId;

        System.out.println("\nDisplayItineraryGUIController\n"+"itineraryID: "+itineraryId+"\nrequestID: "+requestId+"\nproposalID: "+proposalId);
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
                //pending itinerary
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
        nextAgencyButton.setVisible(false);
        if (proposalId != null) {
            nextUserButton.setVisible(false);
            nextAgencyButton.setVisible(false);
        }
        if (requestId != null) {
            nextUserButton.setVisible(false);
            nextAgencyButton.setVisible(true);
            if (new ResponseRequestController().getProposal(null,sessionId) != null) {
                nextAgencyButton.setVisible(false);
            } else {
                nextAgencyButton.setVisible(true);
            }
        }
        if (itineraryId != null) {
            nextUserButton.setVisible(false);
        }
    }

    public void showItineraries(ActionEvent event) {

        if (itineraryId == null) {
            new ManageItineraryController().saveItinerary(sessionId);
        }

        URL url;
        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();

        try {
            url = new File("src/main/java/it/uniroma2/ispw/globe/view/ManageItineraryView.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            ManageItineraryGUIController controller = new ManageItineraryGUIController(sessionId);
            loader.setController(controller);
            root.setCenter(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createProposal(ActionEvent event) {
        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();

        try {
            URL url = new File("src/main/java/it/uniroma2/ispw/globe/view/CreateProposalView.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            CreateProposalGUIController controller = new CreateProposalGUIController(sessionId,requestId);
            loader.setController(controller);
            root.setCenter(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void goBack(ActionEvent event) {
        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        FXMLLoader loader;

        try {
            URL url;
            if (new ResponseRequestController().getProposal(null,sessionId) != null || (itineraryId != null && proposalId != null)) {
                url = new File("src/main/java/it/uniroma2/ispw/globe/view/DisplayProposalView.fxml").toURI().toURL();
                DisplayProposalGUIController controller = new DisplayProposalGUIController(sessionId,requestId,proposalId);
                loader = new FXMLLoader(url);
                loader.setController(controller);
            } else if (itineraryId != null) {
                url = new File("src/main/java/it/uniroma2/ispw/globe/view/ManageItineraryView.fxml").toURI().toURL();
                ManageItineraryGUIController controller = new ManageItineraryGUIController(sessionId);
                loader = new FXMLLoader(url);
                loader.setController(controller);
            } else {
                url = new File("src/main/java/it/uniroma2/ispw/globe/view/CreateItineraryView.fxml").toURI().toURL();
                CreateItineraryGUIController controller = new CreateItineraryGUIController(sessionId,requestId);
                loader = new FXMLLoader(url);
                loader.setController(controller);
            }
            root.setCenter(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
