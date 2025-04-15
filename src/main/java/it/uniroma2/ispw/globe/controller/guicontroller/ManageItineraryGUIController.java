package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.ManageItineraryController;
import it.uniroma2.ispw.globe.exception.DuplicateItemException;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
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

public class ManageItineraryGUIController extends AbstractGUIController {
    @FXML
    private VBox itinerariesVBox;
    @FXML
    private VBox proposalsVBox;

    private String sessionId;

    public void initialize(String sessionId) {

        this.sessionId = sessionId;

        List<ItineraryBean> itineraries = new ManageItineraryController().getUserItineraries(sessionId);
        List<ProposalBean> proposals = null;
        try {
            proposals = new ManageItineraryController().getUserProposals(sessionId);
        } catch (FailedOperationException | DuplicateItemException e) {
            new ErrorPopUpGUIController().createPopUp(e.getMessage());
            return;
        }
        for (ItineraryBean itinerary : itineraries) {
            try {
                URL url = new File("src/main/java/it/uniroma2/ispw/globe/view/ItineraryElement.fxml").toURI().toURL();
                FXMLLoader loader = new FXMLLoader(url);
                Button itineraryBox = loader.load();
                itineraryBox.setUserData(itinerary.getId());
                itineraryBox.setOnAction(this::viewItinerary);
                Label nameLabel = (Label) itineraryBox.getGraphic().lookup("#nameLabel");
                nameLabel.setText(itinerary.getName());
                Label descriptionLabel = (Label) itineraryBox.getGraphic().lookup("#descriptionLabel");
                descriptionLabel.setText(itinerary.getDescription());
                Label daysLabel = (Label) itineraryBox.getGraphic().lookup("#daysLabel");
                daysLabel.setText(String.valueOf(itinerary.getDuration()));

                itinerariesVBox.getChildren().add(itineraryBox);
            } catch (IOException e) {
                new ErrorPopUpGUIController().createPopUp("'Manage Itinerary' page loading failed");
                return;
            }
        }
        for (ProposalBean proposal : proposals) {
            try {
                URL url = new File("src/main/java/it/uniroma2/ispw/globe/view/proposalElement.fxml").toURI().toURL();
                FXMLLoader loader = new FXMLLoader(url);
                Button proposalBox = loader.load();
                proposalBox.setUserData(proposal.getID());
                proposalBox.setOnAction(this::viewProposal);
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
                new ErrorPopUpGUIController().createPopUp("'Manage Itinerary' page loading failed");
                return;
            }
        }
    }

    public void viewItinerary(ActionEvent event) {
        String itineraryId = (String) ((Button)event.getSource()).getUserData();
        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        ViewManager viewManager = new ViewManager();
        viewManager.goToDisplayItineraryGUI(sessionId,itineraryId,null,null, root);
    }

    public void viewProposal(ActionEvent event) {
        String proposalId = (String) ((Button)event.getSource()).getUserData();
        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        ViewManager viewManager = new ViewManager();
        viewManager.goToDisplayProposalGUI(sessionId, null, proposalId, root);
    }

    public void createItinerary(ActionEvent event) {
        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        ViewManager viewManager = new ViewManager();
        viewManager.goToCreateItineraryGUI(sessionId,null, root);
    }
}
