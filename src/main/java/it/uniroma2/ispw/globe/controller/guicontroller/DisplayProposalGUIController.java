package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.AcceptItineraryController;
import it.uniroma2.ispw.globe.controller.applicationcontroller.ManageItineraryController;
import it.uniroma2.ispw.globe.controller.applicationcontroller.ResponseRequestController;
import it.uniroma2.ispw.globe.exception.ItemNotFoundException;
import it.uniroma2.ispw.globe.model.bean.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
        this.proposalID = proposalID;
        this.prev = prev;
    }

    public void initialize() {
        String type = new ManageItineraryController().getAccountType(sessionId);

        ProposalBean proposal = null;
        try {
            proposal = new ManageItineraryController().getProposal(proposalID, sessionId);
        } catch (ItemNotFoundException e) {
            // pop up
        }
        nameLabel.setText(proposal.getID());
        descriptionLabel.setText(proposal.getDescription());
        agencyLabel.setText(proposal.getAgency());
        priceLabel.setText(String.valueOf(proposal.getPrice()));
        saveButton.setVisible(false);
        if (!proposal.getAccepted().equals(PENDING) || type.equals(AGENCY)){
            responseHBox.getChildren().clear();
            agencyLabel.setText(proposal.getUser());
        }
        if (requestID != null) {
            responseHBox.getChildren().clear();
            saveButton.setVisible(true);
        }
    }

    public void showItinerary(ActionEvent event) {
        String itineraryId;

        if (proposalID != null) {
            try {
                itineraryId = new AcceptItineraryController().getProposalItinerary(proposalID).getId();
            } catch (ItemNotFoundException e) {
                new ErrorPopUpGUIController().createPopUp(e);
                return;
            }
        } else {
            itineraryId = null;
        }

        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        NavigationGUIController nav = new NavigationGUIController(root);
        nav.goToDisplayItineraryGUI(sessionId,itineraryId,requestID,proposalID);
    }

    public void acceptProposal() {
        String paymentResult = null;
        try {
            paymentResult = new AcceptItineraryController().sendResponse(proposalID,ACCEPTED);
        } catch (ItemNotFoundException e) {
            // pop up
        }

        if (paymentResult != null) {
            responseHBox.getChildren().clear();

            //popup da sistemare grafica
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
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

    public void rejectProposal() {
        try {
            new AcceptItineraryController().sendResponse(proposalID,REJECTED);
        } catch (ItemNotFoundException e) {
            // pop up
        }
        responseHBox.getChildren().clear();
    }

    public void saveProposal(ActionEvent event) {

        new ResponseRequestController().saveProposal(sessionId);

        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        NavigationGUIController nav = new NavigationGUIController(root);
        nav.goToManageRequestGUI(sessionId);
    }

    public void goBack(ActionEvent event) {
        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        if (prev != null) {
            root.setCenter(prev);
        }
    }
}
