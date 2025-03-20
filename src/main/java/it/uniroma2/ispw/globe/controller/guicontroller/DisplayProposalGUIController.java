package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.AcceptItineraryController;
import it.uniroma2.ispw.globe.controller.applicationcontroller.ManageItineraryController;
import it.uniroma2.ispw.globe.controller.applicationcontroller.ResponseRequestController;
import it.uniroma2.ispw.globe.model.bean.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static it.uniroma2.ispw.globe.other.ProposalState.*;
import static it.uniroma2.ispw.globe.other.UserType.AGENCY;

public class DisplayProposalGUIController {
    @FXML
    private Label priceLabel;
    @FXML
    private Label agencyLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private HBox responseHBox;
    @FXML
    private Button saveButton;


    private String sessionId;
    private String requestID;
    private String proposalID;
    private Node prev;

    public DisplayProposalGUIController(String sessionId, String requestID, String proposalID, Node prev) {
        this.sessionId = sessionId;
        this.requestID = requestID;
        this.proposalID = proposalID; //differenzia tra utente e agenzia
        this.prev = prev;
    }

    public void initialize() {
        String type = new ManageItineraryController().getAccountType(sessionId);

        ProposalBean proposal = new ManageItineraryController().getProposal(proposalID, sessionId);
        nameLabel.setText(proposal.getID());
        descriptionLabel.setText(proposal.getDescription());
        agencyLabel.setText(proposal.getAgency());
        priceLabel.setText(String.valueOf(proposal.getPrice()));
        System.out.println(proposal.getID()+" - "+proposal.getAccepted()+" - "+type);
        saveButton.setVisible(false);
        if (!proposal.getAccepted().equals(PENDING) || type.equals(AGENCY)){
            responseHBox.getChildren().clear();
        }
        if (requestID != null) {
            responseHBox.getChildren().clear();
            saveButton.setVisible(true);
        }
    }

    public void showItinerary(ActionEvent event) {
        String itineraryId;

        if (proposalID != null) {
            itineraryId = new AcceptItineraryController().getProposalItinerary(proposalID).getId();
        } else {
            itineraryId = null;
        }

        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        NavigationGUIController nav = new NavigationGUIController(root);
        nav.loadView("src/main/java/it/uniroma2/ispw/globe/view/DisplayItineraryView.fxml", new DisplayItineraryGUIController(sessionId,itineraryId,requestID,proposalID,root.getCenter()));
    }

    public void acceptProposal(ActionEvent event) {
        String paymentResult = new AcceptItineraryController().sendResponse(proposalID,ACCEPTED);

        if (paymentResult == null) {
            //error
            return;
        } else {
            responseHBox.getChildren().clear();

            //popup da sistemare grafica
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL); // Blocca l'interazione con la finestra principale
            popupStage.setTitle("Payment Result");

            Button closeButton = new Button("Chiudi");
            closeButton.setOnAction(e -> popupStage.close());
            Label label = new Label("Proposal accepted:\n" + paymentResult);
            VBox popupContent = new VBox(label, closeButton);
            Scene popupScene = new Scene(popupContent, 200, 100);

            popupStage.setScene(popupScene);
            popupStage.showAndWait();
        }
    }

    public void rejectProposal(ActionEvent event) {
        new AcceptItineraryController().sendResponse(proposalID,REJECTED);
        responseHBox.getChildren().clear();
    }

    public void saveProposal(ActionEvent event) {

        System.out.println("saving proposal");

        new ResponseRequestController().saveProposal(sessionId);

        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        NavigationGUIController nav = new NavigationGUIController(root);
        nav.loadView("src/main/java/it/uniroma2/ispw/globe/view/ManageRequestView.fxml", new ManageRequestGUIController(sessionId));
    }

    public void goBack(ActionEvent event) {
        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        if (prev != null) {
            root.setCenter(prev);
        }
    }
}
