package it.uniroma2.ispw.globe.controller.guicontroller;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class NavigationGUIController {
    private BorderPane root;

    private static final String MANAGE_ITINERARY = "src/main/java/it/uniroma2/ispw/globe/view/ManageItineraryView.fxml";
    private static final String CREATE_ITINERARY = "src/main/java/it/uniroma2/ispw/globe/view/CreateItineraryView.fxml";
    private static final String DISPALY_ITINERARY = "src/main/java/it/uniroma2/ispw/globe/view/DisplayItineraryView.fxml";

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
            throw new RuntimeException(e);
        }
    }

    public void goToManageItineraryGUI(String sessionID) {
        ManageItineraryGUIController controller = new ManageItineraryGUIController(sessionID);
        loadView(MANAGE_ITINERARY, controller);
    }

    public void goToDisplayItineraryGUI(String sessionID, String itineraryID, String requestID, String proposalID) {
        DisplayItineraryGUIController controller = new DisplayItineraryGUIController(sessionID,itineraryID,requestID,proposalID,root.getCenter());
        loadView(DISPALY_ITINERARY, controller);
    }

    public void goToCreateItineraryGUI(String sessionID, String requestID) {
        CreateItineraryGUIController controller = new CreateItineraryGUIController(sessionID,requestID, root.getCenter());
        loadView(CREATE_ITINERARY, controller);
    }
}
