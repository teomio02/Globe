package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.CreateItineraryController;
import it.uniroma2.ispw.globe.controller.applicationcontroller.ResponseRequestController;
import it.uniroma2.ispw.globe.exception.ItemNotFoundException;
import it.uniroma2.ispw.globe.exception.LoadViewException;
import it.uniroma2.ispw.globe.exception.PlaceApiException;
import it.uniroma2.ispw.globe.model.bean.*;
import it.uniroma2.ispw.globe.other.session.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class DisplayItineraryGUIController extends AbstractGUIController {

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
    private Node prev;

    private static final String LIGHT = "label-light";

    public void initialize(String sessionId) {
        NavigationData data = SessionManager.getInstance().getSession(sessionId).getNavigationData();
        this.sessionId = data.getSessionID();
        this.itineraryId = data.getItineraryID();
        this.requestId = data.getRequestID();
        String proposalId = data.getProposalID();
        this.prev = data.getPrev();

        accommodationVBox.setVisible(false);
        flightVBox.setVisible(false);
        ItineraryBean itinerary = null;
        List<StepBean> steps = null;
        try {
            itinerary = new CreateItineraryController().getItinerary(itineraryId,sessionId);
            steps = new CreateItineraryController().getSteps(itineraryId,sessionId);
        } catch (ItemNotFoundException e) {
            new ErrorPopUpGUIController().createPopUp(e.getMessage());
            return;
        }

        nameLabel.setText(itinerary.getName());
        descriptionLabel.setText(itinerary.getDescription());
        dayLabel.setText(String.valueOf(itinerary.getDuration()));

        int day = 1;
        for (StepBean step : steps) {
            try {
                drawDay(step, day);
            } catch (LoadViewException e) {
                new ErrorPopUpGUIController().createPopUp("page loading failed");
                goBack();
            }
            day ++;
        }


        if (!itinerary.getAccommodations().isEmpty()) {
            accommodationVBox.setVisible(true);
            for (int i = 0; i < itinerary.getAccommodations().size(); i++) {
                Pair<String,String> accommodation = itinerary.getAccommodations().get(i);
                Label accommmodationLabel = new Label((i+1)+": "+accommodation.getKey()+", "+accommodation.getValue());
                accommmodationLabel.getStyleClass().add(LIGHT);
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
            try {
                nextAgencyButton.setVisible(new ResponseRequestController().getProposal(null, sessionId) == null);
            } catch (ItemNotFoundException e) {
                new ErrorPopUpGUIController().createPopUp(e.getMessage());
                return;
            }
        }
        if (itineraryId != null) {
            nextUserButton.setVisible(false);
        }
    }

    public void drawDay(StepBean step, int day) throws LoadViewException {
        Tab tab = new Tab(String.valueOf(day));
        URL url;
        VBox dayVBox = null;
        try {
            url = new File("src/main/java/it/uniroma2/ispw/globe/view/DayTab.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            dayVBox = loader.load();
        } catch (IOException e) {
            throw new LoadViewException("days tab loading failed");
        }

        try {
            ScrollPane scrollPane = (ScrollPane) dayVBox.lookup("#scrollPane");
            VBox attractionVBox = (VBox) scrollPane.getContent();
            Label cityLabel = (Label) dayVBox.lookup("#cityLabel");
            CityBean city = new CreateItineraryController().getCity(step.getNum(),step.getCity().get(0),null);
            cityLabel.setText(city.getName()+", "+city.getCountry());
            for (String attractionID : step.getAttractions()) {
                AttractionBean attraction = new CreateItineraryController().getAttraction(step.getNum(),attractionID,null);
                Label attractionLabel = new Label(attraction.getName());
                Label addressLabel = new Label(attraction.getCity()+", "+attraction.getAddress());
                attractionLabel.setMaxWidth(Double.MAX_VALUE);
                addressLabel.setMaxWidth(Double.MAX_VALUE);
                attractionLabel.getStyleClass().add(LIGHT);
                addressLabel.getStyleClass().add(LIGHT);
                HBox attractionHBox = new HBox(attractionLabel,addressLabel);
                HBox.setHgrow(attractionLabel, Priority.ALWAYS);
                HBox.setHgrow(addressLabel, Priority.ALWAYS);
                attractionVBox.getChildren().add(attractionHBox);
            }
            tab.setContent(dayVBox);
            daysTabPane.getTabs().add(tab);
        } catch (ItemNotFoundException | PlaceApiException e) {
            new ErrorPopUpGUIController().createPopUp(e.getMessage());
        }
    }

    public void showItineraries(ActionEvent event) {
        if (itineraryId == null) {
            new CreateItineraryController().saveItinerary(sessionId);
        }

        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        ViewManager viewManager = new ViewManager();
        viewManager.goToManageItineraryGUI(sessionId,root);
    }

    public void createProposal(ActionEvent event) {
        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        ViewManager viewManager = new ViewManager();
        viewManager.goToCreateProposalGUI(sessionId, requestId, root);
    }

    public void goBack() {
        BorderPane root = (BorderPane) dayLabel.getScene().getRoot();
        if (prev != null) {
            root.setCenter(prev);
        }
    }

}
