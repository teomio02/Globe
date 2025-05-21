package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.AcceptItineraryController;
import it.uniroma2.ispw.globe.controller.applicationcontroller.ManageItineraryController;
import it.uniroma2.ispw.globe.controller.applicationcontroller.ResponseRequestController;
import it.uniroma2.ispw.globe.exception.DuplicateItemException;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
import it.uniroma2.ispw.globe.exception.IncorrectDataException;
import it.uniroma2.ispw.globe.model.bean.*;
import it.uniroma2.ispw.globe.other.session.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import static it.uniroma2.ispw.globe.exception.ErrorMessage.ERROR_IO;
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
    @FXML
    private Button closeButton;
    @FXML
    private Slider ratingSlider;


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
        } catch (FailedOperationException | DuplicateItemException | IncorrectDataException e) {
            new ErrorPopUpGUIController().createPopUp(e.getMessage());
            goBack();
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
            } catch (FailedOperationException | DuplicateItemException | IncorrectDataException e) {
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
            try {
                URL url = new File("src/main/java/it/uniroma2/ispw/globe/view/PaymentPopUp.fxml").toURI().toURL();
                FXMLLoader loader = new FXMLLoader(url);
                loader.setController(this);
                Parent root = loader.load();
                Scene scene = new Scene(root);
                scene.setFill(Color.TRANSPARENT);

                Stage popupStage = new Stage();
                popupStage.setScene(scene);

                popupStage.initStyle(StageStyle.TRANSPARENT);
                popupStage.initModality(Modality.APPLICATION_MODAL);

                closeButton.setOnAction(e -> {
                    popupStage.close();
                    //rating.set(ratingSlider.getValue());
                });

                popupStage.setScene(scene);
                popupStage.showAndWait();
                
                responseHBox.getChildren().clear();

            } catch (IOException e) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, ERROR_IO, e);
            }
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

    public void goBack() {
        BorderPane root = (BorderPane) agencyLabel.getScene().getRoot();
        if (prev != null) {
            root.setCenter(prev);
        }
    }
}
