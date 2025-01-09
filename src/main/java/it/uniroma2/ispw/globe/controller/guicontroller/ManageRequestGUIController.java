package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.LogInController;
import it.uniroma2.ispw.globe.controller.applicationcontroller.ResponseRequestController;
import it.uniroma2.ispw.globe.model.bean.ProposalBean;
import it.uniroma2.ispw.globe.model.bean.AgencyRequestBean;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

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
                URL url = new File("src/main/java/it/uniroma2/ispw/globe/view/tabElement.fxml").toURI().toURL();
                FXMLLoader loader = new FXMLLoader(url);
                Button proposalBox = loader.load();
                proposalBox.setUserData(proposal.getID());
                proposalBox.setOnAction(actionEvent -> viewProposal(actionEvent));
                Label nameLabel = (Label) proposalBox.getGraphic().lookup("#nameLabel");
                nameLabel.setText(proposal.getUser());
                Label descriptionLabel = (Label) proposalBox.getGraphic().lookup("#descriptionLabel");
                descriptionLabel.setText(proposal.getDescription());
                Label daysLabel = (Label) proposalBox.getGraphic().lookup("#daysLabel");
                daysLabel.setText(String.valueOf(proposal.getPrice()));

                proposalsVBox.getChildren().add(proposalBox);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        for (AgencyRequestBean request : requests) {
            try {
                URL url = new File("src/main/java/it/uniroma2/ispw/globe/view/tabElement.fxml").toURI().toURL();
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

    public void viewProposal(ActionEvent event) {

        URL url;
        Parent root;

        String proposalID = (String) ((Button)event.getSource()).getUserData();

        try {
            url = new File("src/main/java/it/uniroma2/ispw/globe/view/DisplayProposalView.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            DisplayProposalGUIController controller = new DisplayProposalGUIController(sessionId,proposalID);
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

    public void viewRequest(ActionEvent event) {
        URL url;
        Parent root;

        String requestID = (String) ((Button)event.getSource()).getUserData();

        try {
            url = new File("src/main/java/it/uniroma2/ispw/globe/view/DisplayRequestView.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            DisplayRequestGUIController controller = new DisplayRequestGUIController(sessionId,requestID);
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
