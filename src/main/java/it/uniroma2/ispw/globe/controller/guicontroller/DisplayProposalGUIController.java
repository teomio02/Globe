package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.AcceptItineraryController;
import it.uniroma2.ispw.globe.controller.applicationcontroller.ManageItineraryController;
import it.uniroma2.ispw.globe.model.bean.*;
import it.uniroma2.ispw.globe.view.DayTab;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static it.uniroma2.ispw.globe.other.ProposalState.*;

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


    private String sessionId;
    private String proposalID;

    public DisplayProposalGUIController(String sessionId, String proposalID) {
        this.sessionId = sessionId;
        this.proposalID = proposalID;
    }

    public void initialize() {
        ProposalBean proposal = new ManageItineraryController().getProposal(proposalID, sessionId);
        nameLabel.setText(proposal.getName());
        descriptionLabel.setText(proposal.getDescription());
        agencyLabel.setText(proposal.getAgency());
        priceLabel.setText(String.valueOf(proposal.getPrice()));
        if (!proposal.getAccepted().equals(PENDING)){
            responseHBox.getChildren().clear();
        }
    }

    public void showItinerary(ActionEvent event) {
        Parent root;

        String itineraryId = new AcceptItineraryController().getProposalItinerary(proposalID).getId();

        try {
            URL url = new File("src/main/java/it/uniroma2/ispw/globe/view/DisplayItineraryView.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            DisplayItineraryGUIController controller = new DisplayItineraryGUIController(sessionId,proposalID,itineraryId);
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

    public void goBack (ActionEvent event) {
        URL url;
        Parent root;

        try {
            url = new File("src/main/java/it/uniroma2/ispw/globe/view/ManageItineraryView.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            ManageItineraryGUIController controller = new ManageItineraryGUIController(sessionId);
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
