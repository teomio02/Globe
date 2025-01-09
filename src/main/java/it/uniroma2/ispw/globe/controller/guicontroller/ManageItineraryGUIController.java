package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.LogInController;
import it.uniroma2.ispw.globe.controller.applicationcontroller.ManageItineraryController;
import it.uniroma2.ispw.globe.model.bean.ItineraryBean;
import it.uniroma2.ispw.globe.view.ItineraryBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ManageItineraryGUIController {

    @FXML
    private VBox itinerariesVBox;

    private String sessionId;

    public ManageItineraryGUIController(String sessionId) {
        this.sessionId = sessionId;
    }

    public void initialize() {
        List<ItineraryBean> itineraries = new ManageItineraryController().getUserItineraries(sessionId);
        for (ItineraryBean itinerary : itineraries) {
            try {
                URL url = new File("src/main/java/it/uniroma2/ispw/globe/view/tabElement.fxml").toURI().toURL();
                FXMLLoader loader = new FXMLLoader(url);
                Button itineraryBox = loader.load();
                itineraryBox.setUserData(itinerary.getId());
                itineraryBox.setOnAction(actionEvent -> viewItinerary(actionEvent));
                Label nameLabel = (Label) itineraryBox.getGraphic().lookup("#nameLabel");
                nameLabel.setText(itinerary.getName());
                Label descriptionLabel = (Label) itineraryBox.getGraphic().lookup("#descriptionLabel");
                descriptionLabel.setText(itinerary.getDescription());
                Label daysLabel = (Label) itineraryBox.getGraphic().lookup("#daysLabel");
                daysLabel.setText(String.valueOf(itinerary.getDuration()));

                itinerariesVBox.getChildren().add(itineraryBox);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void deleteItinerary() {}

    public void modifyItinerary() {}

    public void viewItinerary(ActionEvent event) {
        URL url;
        Parent root;

        String itineraryId = (String) ((Button)event.getSource()).getUserData();

        try {
            url = new File("src/main/java/it/uniroma2/ispw/globe/view/DisplayItineraryView.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            DisplayItineraryGUIController controller = new DisplayItineraryGUIController(sessionId,itineraryId);
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

    public void displayItineraryList() {}

    public void displayProposalList() {}

    public void createItinerary(ActionEvent event) {
        URL url;
        Parent root;

        try {
            url = new File("src/main/java/it/uniroma2/ispw/globe/view/CreateItineraryView.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            CreateItineraryGUIController controller = new CreateItineraryGUIController(sessionId);
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

    public void logOut(ActionEvent event) {
        URL url;
        Parent root;

        new LogInController().logOut(sessionId);

        try {
            url = new File("src/main/java/it/uniroma2/ispw/globe/view/LoginView.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            LogInGUIController controller = new LogInGUIController();
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
