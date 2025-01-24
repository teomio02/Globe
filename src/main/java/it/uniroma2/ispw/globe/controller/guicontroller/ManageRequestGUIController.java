package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.ResponseRequestController;
import it.uniroma2.ispw.globe.model.Account;
import it.uniroma2.ispw.globe.model.bean.AgencyRequestBean;
import it.uniroma2.ispw.globe.model.bean.ProposalBean;
import it.uniroma2.ispw.globe.model.bean.RequestBean;
import it.uniroma2.ispw.globe.model.dao.AccountDao;
import it.uniroma2.ispw.globe.model.dao.DaoFactory;
import it.uniroma2.ispw.globe.other.Persistence;
import it.uniroma2.ispw.globe.other.session.Session;
import it.uniroma2.ispw.globe.other.session.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static it.uniroma2.ispw.globe.other.ProposalState.*;

public class ManageRequestGUIController {

    @FXML
    private VBox proposalsVBox;
    @FXML
    private VBox requestsVBox;

    private String sessionId;

    public ManageRequestGUIController(String sessionId) {
        this.sessionId = sessionId;
    }

    public void initialize() {

        List<ProposalBean> proposals = new ResponseRequestController().getAgencyProposals(sessionId);
        List<AgencyRequestBean> requests = new ResponseRequestController().getAgencyRequests(sessionId);

        for (ProposalBean proposal : proposals) {
            try {
                URL url = new File("src/main/java/it/uniroma2/ispw/globe/view/proposalElement.fxml").toURI().toURL();
                FXMLLoader loader = new FXMLLoader(url);
                Button proposalBox = loader.load();
                proposalBox.setUserData(proposal.getID());
                proposalBox.setOnAction(actionEvent -> viewProposal(actionEvent));
                Label nameLabel = (Label) proposalBox.getGraphic().lookup("#nameLabel");
                nameLabel.setText(proposal.getUser());
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

        for (AgencyRequestBean request : requests) {
            if (request.getAccepted().equals(PENDING)) {
                try {
                    URL url = new File("src/main/java/it/uniroma2/ispw/globe/view/requestElement.fxml").toURI().toURL();
                    FXMLLoader loader = new FXMLLoader(url);
                    Button requestsBox = loader.load();
                    requestsBox.setUserData(request.getID());
                    requestsBox.setOnAction(actionEvent -> viewRequest(actionEvent));
                    Label nameLabel = (Label) requestsBox.getGraphic().lookup("#nameLabel");
                    nameLabel.setText(request.getUser());
                    Label descriptionLabel = (Label) requestsBox.getGraphic().lookup("#descriptionLabel");
                    descriptionLabel.setText(request.getDescription());
                    Label daysLabel = (Label) requestsBox.getGraphic().lookup("#daysLabel");
                    daysLabel.setText(String.valueOf(request.getDays()));

                    requestsVBox.getChildren().add(requestsBox);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void viewProposal(ActionEvent event) {
        String proposalID = (String) ((Button)event.getSource()).getUserData();

        URL url;
        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();

        try {
            url = new File("src/main/java/it/uniroma2/ispw/globe/view/DisplayProposalView.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            DisplayProposalGUIController controller = new DisplayProposalGUIController(sessionId, null, proposalID);
            loader.setController(controller);
            root.setCenter(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void viewRequest(ActionEvent event) {
        String requestID = (String) ((Button)event.getSource()).getUserData();
        new ResponseRequestController().setPendingRequest(sessionId,requestID);

        URL url;
        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();

        try {
            url = new File("src/main/java/it/uniroma2/ispw/globe/view/DisplayRequestView.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            DisplayRequestGUIController controller = new DisplayRequestGUIController(sessionId,requestID);
            loader.setController(controller);
            root.setCenter(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
