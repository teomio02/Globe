package it.ispw.globeapp.Controller;

import it.ispw.globeapp.Model.Bean.ItineraryBean;
import it.ispw.globeapp.View.GuestHomeView;
import it.ispw.globeapp.View.GuestTripView;
import it.ispw.globeapp.View.LoginView;
import it.ispw.globeapp.View.ReviewItineraryView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Navigator {
    private String page;

    public void goToLogin(ActionEvent event) throws IOException {
        page = "src/main/java/it/ispw/globeapp/View/LoginView.fxml";
        FXMLLoader loader = goToPage(page, event);
        LoginView controller = loader.getController();
    }

    public void goToGuestHome(ActionEvent event) throws IOException {
        page = "src/main/java/it/ispw/globeapp/View/GuestHomeView.fxml";
        FXMLLoader loader = goToPage(page, event);
        GuestHomeView controller = loader.getController();
    }

    public void goToGuestTrip(ActionEvent event) throws IOException {
        page = "src/main/java/it/ispw/globeapp/View/GuestTripView.fxml";
        FXMLLoader loader = goToPage(page, event);
        GuestTripView controller = loader.getController();
    }

    public void goToReviewItinerary(ItineraryBean itineraryBean, ActionEvent event) throws IOException {
        page = "src/main/java/it/ispw/globeapp/View/ReviewItineraryView.fxml";
        FXMLLoader loader = goToPage(page, event);
        ReviewItineraryView controller = loader.getController();
        controller.setItineraryData(itineraryBean);
    }

    public FXMLLoader goToPage(String Page, ActionEvent event) throws IOException {
        URL url = new File(Page).toURI().toURL();
        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

        return loader;
    }

}
