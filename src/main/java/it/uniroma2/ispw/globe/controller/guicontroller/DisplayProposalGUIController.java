package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.AcceptItineraryController;
import it.uniroma2.ispw.globe.controller.applicationcontroller.ManageItineraryController;
import it.uniroma2.ispw.globe.model.bean.PaymentBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class DisplayProposalGUIController {
    @FXML
    private Label priceLabel;
    @FXML
    private Label agencyLabel;
    @FXML
    private Label descriptionLabel;
    @FXML
    private Label nameLabel;

    private String sessionId;
    private String proposalID;

    public DisplayProposalGUIController(String sessionId, String proposalID) {
        this.sessionId = sessionId;
        this.proposalID = proposalID;
    }

    public void initialize() {

    }

    public void showItinerary(ActionEvent event) {
        Parent root;

        String itineraryId = new ManageItineraryController().getProposalItinerary(proposalID).getId();

        try {
            URL url = new File("src/main/java/it/uniroma2/ispw/globe/view/DisplayItineraryView.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            DisplayItineraryGUIController controller = new DisplayItineraryGUIController(sessionId,null,itineraryId);
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
        //PaymentBean paymentBean = new AcceptItineraryController().sendResponse(proposalID,true);

    }

    public void rejectProposal(ActionEvent event) {
        new AcceptItineraryController().sendResponse(proposalID,false);
    }

}
