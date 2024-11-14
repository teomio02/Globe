package it.uniroma2.ispw.globe.controller.applicationcontroller;


import it.uniroma2.ispw.globe.controller.guicontroller.CreateTripViewController;
import it.uniroma2.ispw.globe.controller.guicontroller.DisplayItineraryViewController;
import it.uniroma2.ispw.globe.model.bean.ItineraryBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Navigator {
    private String page;

    public void goToManageItinerary(ActionEvent event) throws IOException {
        page = "src/main/java/it/uniroma2/ispw/globe/view/ManageItineraryView.fxml";
        FXMLLoader loader = goToPage(page, event);
    }

    public void goToCreateTrip(ActionEvent event) throws IOException {
        page = "src/main/java/it/uniroma2/ispw/globe/view/CreateTripView.fxml";
        FXMLLoader loader = goToPage(page, event);
        CreateTripViewController controller = loader.getController();
        controller.initialize();
    }

    public void goToDispalyItinerary (ActionEvent event, ItineraryBean itineraryBean) throws IOException {
        page = "src/main/java/it/uniroma2/ispw/globe/view/DisplayItineraryView.fxml";
        FXMLLoader loader = goToPage(page, event);
        DisplayItineraryViewController controller = loader.getController();
        controller.setItineraryData(itineraryBean);
    }

/*    public void  goToAddCity(ActionEvent event, String tripName) throws IOException {
        page = "src/main/java/it/uniroma2/ispw/globe/view/AddCityView.fxml";
        FXMLLoader loader = goToPage(page, event);
        AddCityViewController controller = loader.getController();
    }
 */


    public FXMLLoader goToPage(String Page, ActionEvent event) throws IOException {
        URL url = new File(Page).toURI().toURL();
        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);

        return loader;
    }

}
