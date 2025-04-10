package it.uniroma2.ispw.globe.controller.guicontroller;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class NavigationGUIController {
    private BorderPane root;

    private static final String CREATE_ITINERARY = "src/main/java/it/uniroma2/ispw/globe/view/CreateItineraryView.fxml";
    private static final String CREATE_PROPOSAL = "src/main/java/it/uniroma2/ispw/globe/view/CreateProposalView.fxml";
    private static final String DISPALY_ITINERARY = "src/main/java/it/uniroma2/ispw/globe/view/DisplayItineraryView.fxml";
    private static final String DISPALY_PROPOSAL = "src/main/java/it/uniroma2/ispw/globe/view/DisplayProposalView.fxml";
    private static final String DISPALY_REQUEST = "src/main/java/it/uniroma2/ispw/globe/view/DisplayRequestView.fxml";
    private static final String MANAGE_ITINERARY = "src/main/java/it/uniroma2/ispw/globe/view/ManageItineraryView.fxml";
    private static final String MANAGE_REQUEST= "src/main/java/it/uniroma2/ispw/globe/view/ManageRequestView.fxml";
    private static final String REQUEST_ITINERARY = "src/main/java/it/uniroma2/ispw/globe/view/RequestItineraryView.fxml";


    public NavigationGUIController() {

    }

    public NavigationGUIController(BorderPane root) {
        this.root = root;
    }

    public void loadView(String fxmlPath, Object controller) {
        try {
            URL url = new File(fxmlPath).toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            loader.setController(controller);
            root.setCenter(loader.load());
        } catch (IOException e) {
            new ErrorPopUpGUIController().createPopUp("page loading failed");
        }
    }

    public void loadView1(String fxmlPath, Object controller, BorderPane root1) {
        try {
            URL url = new File(fxmlPath).toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            loader.setController(controller);
            root1.setCenter(loader.load());
        } catch (IOException e) {
            new ErrorPopUpGUIController().createPopUp("page loading failed");
        }
    }

    public void goToCreateItineraryGUI(String sessionID, String requestID) {
        CreateItineraryGUIController controller = new CreateItineraryGUIController(sessionID,requestID, root.getCenter());
        loadView(CREATE_ITINERARY, controller);
    }

    public void goToCreateItineraryGUI1(String sessionID, String requestID, BorderPane root1) {
        CreateItineraryGUIController controller = new CreateItineraryGUIController(sessionID,requestID, root1.getCenter());
        loadView1(CREATE_ITINERARY, controller,root1);
    }

    public void goToCreateProposalGUI(String sessionID, String requestID) {
        CreateProposalGUIController controller = new CreateProposalGUIController(sessionID, requestID, root.getCenter());
        loadView(CREATE_PROPOSAL, controller);
    }

    public void goToDisplayItineraryGUI(String sessionID, String itineraryID, String requestID, String proposalID) {
        DisplayItineraryGUIController controller = new DisplayItineraryGUIController(sessionID,itineraryID,requestID,proposalID,root.getCenter());
        loadView(DISPALY_ITINERARY, controller);
    }

    public void goToDisplayItineraryGUI1(String sessionID, String itineraryID, String requestID, String proposalID, BorderPane root1) {
        DisplayItineraryGUIController controller = new DisplayItineraryGUIController(sessionID,itineraryID,requestID,proposalID,root1.getCenter());
        loadView1(DISPALY_ITINERARY, controller, root1);
    }

    public void goToDisplayProposalGUI(String sessionID, String requestID, String proposalID) {
        DisplayProposalGUIController controller = new DisplayProposalGUIController(sessionID, requestID, proposalID, root.getCenter());
        loadView(DISPALY_PROPOSAL, controller);
    }

    public void goToDisplayProposalGUI1(String sessionID, String requestID, String proposalID, BorderPane root1) {
        DisplayProposalGUIController controller = new DisplayProposalGUIController(sessionID, requestID, proposalID, root1.getCenter());
        loadView1(DISPALY_PROPOSAL, controller, root1);
    }

    public void goToDisplayRequestGUI(String sessionID, String requestID) {
        DisplayRequestGUIController controller = new DisplayRequestGUIController(sessionID, requestID, root.getCenter());
        loadView(DISPALY_REQUEST, controller);
    }

    public void goToManageItineraryGUI(String sessionID) {
        ManageItineraryGUIController controller = new ManageItineraryGUIController(sessionID);
        loadView(MANAGE_ITINERARY, controller);
    }

    public void goToManageRequestGUI(String sessionID) {
        ManageRequestGUIController controller = new ManageRequestGUIController(sessionID);
        loadView(MANAGE_REQUEST,controller);
    }

    public void goToRequestItineraryGUI(String sessionID) {
        CreateRequestGUIController controller = new CreateRequestGUIController(sessionID);
        loadView(REQUEST_ITINERARY,controller);
    }
}
