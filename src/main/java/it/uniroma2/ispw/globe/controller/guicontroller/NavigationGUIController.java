package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.model.bean.NavigationData;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.uniroma2.ispw.globe.exception.ErrorMessage.ERROR_SQL;
import static it.uniroma2.ispw.globe.exception.ErrorMessage.ERROR_VIEW;

public class NavigationGUIController {
    private BorderPane root;

    public static final String CREATE_ITINERARY = "src/main/java/it/uniroma2/ispw/globe/view/CreateItineraryView.fxml";
    public static final String CREATE_PROPOSAL = "src/main/java/it/uniroma2/ispw/globe/view/CreateProposalView.fxml";
    public static final String DISPALY_ITINERARY = "src/main/java/it/uniroma2/ispw/globe/view/DisplayItineraryView.fxml";
    public static final String DISPALY_PROPOSAL = "src/main/java/it/uniroma2/ispw/globe/view/DisplayProposalView.fxml";
    public static final String DISPALY_REQUEST = "src/main/java/it/uniroma2/ispw/globe/view/DisplayRequestView.fxml";
    public static final String MANAGE_ITINERARY = "src/main/java/it/uniroma2/ispw/globe/view/ManageItineraryView.fxml";
    public static final String MANAGE_REQUEST= "src/main/java/it/uniroma2/ispw/globe/view/ManageRequestView.fxml";
    public static final String REQUEST_ITINERARY = "src/main/java/it/uniroma2/ispw/globe/view/RequestItineraryView.fxml";


    public NavigationGUIController(BorderPane root) {
        this.root = root;
    }

    private void loadView(String fxmlPath, NavigationData data) {
        try {
            URL url = new File(fxmlPath).toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            root.setCenter(loader.load());
            ViewManager.getInstance().notifyViewChange(fxmlPath, data);
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_VIEW, e);
            new ErrorPopUpGUIController().createPopUp("page loading failed");
        }
    }

    public void goToCreateItineraryGUI(String sessionID, String requestID, Node prev) {
        NavigationData data = new NavigationData(sessionID, null, null, requestID, prev);
        loadView(CREATE_ITINERARY, data);
    }

    public void goToCreateProposalGUI(String sessionID, String requestID, Node prev) {
        NavigationData data = new NavigationData(sessionID, null, null, requestID, prev);
        loadView(CREATE_PROPOSAL, data);
    }

    public void goToDisplayItineraryGUI(String sessionID, String itineraryID, String requestID, String proposalID, Node prev) {
        NavigationData data = new NavigationData(sessionID, itineraryID, proposalID, requestID, prev);
        loadView(DISPALY_ITINERARY, data);
    }

    public void goToDisplayProposalGUI(String sessionID, String requestID, String proposalID, Node prev) {
        NavigationData data = new NavigationData(sessionID, null, proposalID, requestID, prev);
        loadView(DISPALY_PROPOSAL, data);
    }

    public void goToDisplayRequestGUI(String sessionID, String requestID, Node prev) {
        NavigationData data = new NavigationData(sessionID, null, null, requestID, prev);
        loadView(DISPALY_REQUEST, data);
    }

    public void goToManageItineraryGUI(String sessionID) {
        NavigationData data = new NavigationData(sessionID, null, null, null, null);
        loadView(MANAGE_ITINERARY, data);
    }

    public void goToManageRequestGUI(String sessionID) {
        NavigationData data = new NavigationData(sessionID, null, null, null, null);
        loadView(MANAGE_REQUEST, data);
    }

    public void goToRequestItineraryGUI(String sessionID) {
        NavigationData data = new NavigationData(sessionID, null, null, null, null);
        loadView(REQUEST_ITINERARY, data);
    }
}
