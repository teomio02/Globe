package it.uniroma2.ispw.globe.controller.guicontroller;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.uniroma2.ispw.globe.exception.ErrorMessage.ERROR_SQL;
import static it.uniroma2.ispw.globe.exception.ErrorMessage.ERROR_VIEW;

public class GUIController {
    public static final String CREATE_ITINERARY = "src/main/java/it/uniroma2/ispw/globe/view/CreateItineraryView.fxml";
    public static final String CREATE_PROPOSAL = "src/main/java/it/uniroma2/ispw/globe/view/CreateProposalView.fxml";
    public static final String DISPALY_ITINERARY = "src/main/java/it/uniroma2/ispw/globe/view/DisplayItineraryView.fxml";
    public static final String DISPALY_PROPOSAL = "src/main/java/it/uniroma2/ispw/globe/view/DisplayProposalView.fxml";
    public static final String DISPALY_REQUEST = "src/main/java/it/uniroma2/ispw/globe/view/DisplayRequestView.fxml";
    public static final String MANAGE_ITINERARY = "src/main/java/it/uniroma2/ispw/globe/view/ManageItineraryView.fxml";
    public static final String MANAGE_REQUEST= "src/main/java/it/uniroma2/ispw/globe/view/ManageRequestView.fxml";
    public static final String REQUEST_ITINERARY = "src/main/java/it/uniroma2/ispw/globe/view/RequestItineraryView.fxml";


    public void loadView(String fxmlPath, Object controller, BorderPane root) {
        try {
            URL url = new File(fxmlPath).toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            loader.setController(controller);
            root.setCenter(loader.load());
        } catch (IOException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_VIEW, e);
            new ErrorPopUpGUIController().createPopUp("page loading failed");
        }
    }
}
