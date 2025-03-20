package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.CreateItineraryController;
import it.uniroma2.ispw.globe.controller.applicationcontroller.ResponseRequestController;
import it.uniroma2.ispw.globe.model.bean.AttractionBean;
import it.uniroma2.ispw.globe.model.bean.CityBean;
import it.uniroma2.ispw.globe.model.bean.ItineraryBean;
import it.uniroma2.ispw.globe.model.bean.StepBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

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
    @FXML
    private VBox accommodationVBox;
    @FXML
    private VBox flightVBox;

    private String sessionId;
    private String itineraryId;
    private String requestId;
    private String proposalId;
    private Node prev;

    public DisplayItineraryGUIController(String sessionId,String itineraryId, String requestId, String proposalId,Node prev) {
        this.sessionId = sessionId;
        this.itineraryId = itineraryId;
        this.requestId = requestId;
        this.proposalId = proposalId;
        this.prev = prev;
    }

    public void initialize() {
        accommodationVBox.setVisible(false);
        flightVBox.setVisible(false);

        ItineraryBean itinerary = new CreateItineraryController().getItinerary(itineraryId,sessionId);
        List<StepBean> steps = new CreateItineraryController().getSteps(itineraryId,sessionId);
        nameLabel.setText(itinerary.getName());
        descriptionLabel.setText(itinerary.getDescription());
        dayLabel.setText(String.valueOf(itinerary.getDuration()));

        int day = 1;
        for (StepBean step : steps) {
            Tab tab = new Tab(String.valueOf(day));
            URL url;
            VBox dayVBox;
            try {
                url = new File("src/main/java/it/uniroma2/ispw/globe/view/DayTab.fxml").toURI().toURL();
                FXMLLoader loader = new FXMLLoader(url);
                dayVBox = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            ScrollPane scrollPane = (ScrollPane) dayVBox.lookup("#scrollPane");
            VBox attractionVBox = (VBox) scrollPane.getContent();
            Label cityLabel = (Label) dayVBox.lookup("#cityLabel");
            CityBean city = new CreateItineraryController().getCity(step.getNum(),step.getCity().get(0),null);
            cityLabel.setText(city.getName()+", "+city.getCountry());
            for (String attractionID : step.getAttractions()) {
                HBox attractionHBox;
                try {
                    url = new File("src/main/java/it/uniroma2/ispw/globe/view/AttractionHBox.fxml").toURI().toURL();
                    FXMLLoader loader = new FXMLLoader(url);
                    attractionHBox = loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                AttractionBean attraction = new CreateItineraryController().getAttraction(step.getNum(),attractionID,null);
                Label attractionLabel = (Label) attractionHBox.lookup("#attractionLabel");
                Label addressLabel = (Label) attractionHBox.lookup("#addressLabel");
                attractionLabel.setText(attraction.getName());
                addressLabel.setText(attraction.getCity()+", "+attraction.getAddress());
                attractionVBox.getChildren().add(attractionHBox);
            }
            tab.setContent(dayVBox);
            daysTabPane.getTabs().add(tab);
            day++;
        }


        if (!itinerary.getAccommodations().isEmpty()) {
            accommodationVBox.setVisible(true);
            for (int i = 0; i < itinerary.getAccommodations().size(); i++) {
                Pair<String,String> accommodation = itinerary.getAccommodations().get(i);
                Label accommmodationLabel = new Label((i+1)+": "+accommodation.getKey()+", "+accommodation.getValue());
                accommmodationLabel.getStyleClass().add("label-light");
                accommodationVBox.getChildren().add(accommmodationLabel);
            }
        }
        if (itinerary.getInboundFlightDepartureTime() != -1) {
            flightVBox.setVisible(true);
            flightVBox.getChildren().add(new Label("Inbound: "+itinerary.getInboundFlightDepartureTime()+" - "+itinerary.getInboundFlightArrivalTime()));
            flightVBox.getChildren().add(new Label("Outbound"+itinerary.getOutboundFlightDepartureTime()+" - "+itinerary.getOutboundFlightArrivalTime()));
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
            new CreateItineraryController().saveItinerary(sessionId);
        }

        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        NavigationGUIController nav = new NavigationGUIController(root);
        nav.loadView("src/main/java/it/uniroma2/ispw/globe/view/ManageItineraryView.fxml", new ManageItineraryGUIController(sessionId));
    }

    public void createProposal(ActionEvent event) {
        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        NavigationGUIController nav = new NavigationGUIController(root);
        nav.loadView("src/main/java/it/uniroma2/ispw/globe/view/CreateProposalView.fxml", new CreateProposalGUIController(sessionId, requestId,root.getCenter()));
    }

    public void goBack(ActionEvent event) {
        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        if (prev != null) {
            root.setCenter(prev);
        }
    }

}
