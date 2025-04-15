package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.AcceptItineraryController;
import it.uniroma2.ispw.globe.controller.applicationcontroller.ManageItineraryController;
import it.uniroma2.ispw.globe.controller.applicationcontroller.ResponseRequestController;
import it.uniroma2.ispw.globe.exception.DuplicateItemException;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
import it.uniroma2.ispw.globe.model.bean.*;
import it.uniroma2.ispw.globe.other.session.SessionManager;
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

public class DisplayProposalGUIController extends AbstractGUIController {
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

    public void initialize(String sessionId) {
        NavigationData data = SessionManager.getInstance().getSession(sessionId).getNavigationData();
        this.sessionId = data.getSessionID();
        this.requestID = data.getRequestID();
        this.proposalID = data.getProposalID();
        this.prev = data.getPrev();

        String type = new ManageItineraryController().getAccountType(sessionId);

        ProposalBean proposal;
        try {
            proposal = new ManageItineraryController().getProposal(proposalID, sessionId);
        } catch (FailedOperationException | DuplicateItemException e) {
            new ErrorPopUpGUIController().createPopUp(e.getMessage());
            return;
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
            } catch (FailedOperationException | DuplicateItemException e) {
                new ErrorPopUpGUIController().createPopUp(e.getMessage());
                return;
            }
        } else {
            itineraryId = null;
        }

        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        ViewManager viewManager = new ViewManager();
        viewManager.goToDisplayItineraryGUI(sessionId,itineraryId,requestID,proposalID, root);
    }

    public void acceptProposal() {
        String paymentResult = null;
        try {
            paymentResult = new AcceptItineraryController().sendResponse(proposalID,ACCEPTED);
        } catch (FailedOperationException | DuplicateItemException e) {
            new ErrorPopUpGUIController().createPopUp(e.getMessage());
            return;
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
        } catch (FailedOperationException | DuplicateItemException e) {
            new ErrorPopUpGUIController().createPopUp(e.getMessage());
            return;
        }
        responseHBox.getChildren().clear();
    }

    public void saveProposal(ActionEvent event) {
        try {
            new ResponseRequestController().saveProposal(sessionId);
        } catch (FailedOperationException | DuplicateItemException e) {
            new ErrorPopUpGUIController().createPopUp(e.getMessage());
            return;
        }

        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        ViewManager viewManager = new ViewManager();
        viewManager.goToManageRequestGUI(sessionId, root);
    }

    public void goBack(ActionEvent event) {
        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        if (prev != null) {
            root.setCenter(prev);
        }
    }
}
