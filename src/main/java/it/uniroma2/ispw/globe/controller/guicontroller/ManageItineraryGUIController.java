package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.ManageItineraryController;
import it.uniroma2.ispw.globe.model.bean.ItineraryBean;
import it.uniroma2.ispw.globe.model.bean.ProposalBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static it.uniroma2.ispw.globe.other.ProposalState.ACCEPTED;
import static it.uniroma2.ispw.globe.other.ProposalState.REJECTED;

public class ManageItineraryGUIController {
    @FXML
    private VBox itinerariesVBox;
    @FXML
    private VBox proposalsVBox;

    private String sessionId;

    public ManageItineraryGUIController(String sessionId) {
        this.sessionId = sessionId;
    }

    public void initialize() {
        List<ItineraryBean> itineraries = new ManageItineraryController().getUserItineraries(sessionId);
        List<ProposalBean> proposals = new ManageItineraryController().getUserProposals(sessionId);
        for (ItineraryBean itinerary : itineraries) {
            try {
                URL url = new File("src/main/java/it/uniroma2/ispw/globe/view/ItineraryElement.fxml").toURI().toURL();
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
        for (ProposalBean proposal : proposals) {
            try {
                URL url = new File("src/main/java/it/uniroma2/ispw/globe/view/proposalElement.fxml").toURI().toURL();
                FXMLLoader loader = new FXMLLoader(url);
                Button proposalBox = loader.load();
                proposalBox.setUserData(proposal.getID());
                proposalBox.setOnAction(actionEvent -> viewProposal(actionEvent));
                Label nameLabel = (Label) proposalBox.getGraphic().lookup("#nameLabel");
                nameLabel.setText(proposal.getAgency());
                Label descriptionLabel = (Label) proposalBox.getGraphic().lookup("#descriptionLabel");
                descriptionLabel.setText(proposal.getDescription());
                Label priceLabel = (Label) proposalBox.getGraphic().lookup("#priceLabel");
                priceLabel.setText(String.valueOf(proposal.getPrice()));

                if (proposal.getAccepted().equals(ACCEPTED)) {
                    ImageView acceptedImage = (ImageView) proposalBox.getGraphic().lookup("#acceptedImage");
                    acceptedImage.setVisible(true);
                } else if (proposal.getAccepted().equals(REJECTED)){
                    ImageView acceptedImage = (ImageView) proposalBox.getGraphic().lookup("#rejectedImage");
                    acceptedImage.setVisible(true);
                }

                proposalsVBox.getChildren().add(proposalBox);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void deleteItinerary() {}

    public void modifyItinerary() {}

    public void viewItinerary(ActionEvent event) {
        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();

        String itineraryId = (String) ((Button)event.getSource()).getUserData();

        try {
            URL url = new File("src/main/java/it/uniroma2/ispw/globe/view/DisplayItineraryView.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            DisplayItineraryGUIController controller = new DisplayItineraryGUIController(sessionId,itineraryId,null,null,root.getCenter());
            loader.setController(controller);
            root.setCenter(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void viewProposal(ActionEvent event) {
        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();

        String proposalId = (String) ((Button)event.getSource()).getUserData();

        try {
            URL url = new File("src/main/java/it/uniroma2/ispw/globe/view/DisplayProposalView.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            DisplayProposalGUIController controller = new DisplayProposalGUIController(sessionId, null, proposalId,root.getCenter());
            loader.setController(controller);
            root.setCenter(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void displayItineraryList() {}

    public void displayProposalList() {}

    public void createItinerary(ActionEvent event) {
        URL url;
        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();

        try {
            url = new File("src/main/java/it/uniroma2/ispw/globe/view/CreateItineraryView.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            CreateItineraryGUIController controller = new CreateItineraryGUIController(sessionId,null, root.getCenter());
            loader.setController(controller);
            root.setCenter(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
