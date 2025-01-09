package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.ResponseRequestController;
import it.uniroma2.ispw.globe.model.bean.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class CreateProposalGUIController {
    @FXML
    private Label userLabel;
    @FXML
    private Label requestLabel;
    @FXML
    private TextField proposalField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField descriptionField;
    @FXML
    private VBox selectVBox;
    @FXML
    private VBox createVBox;
    @FXML
    private VBox requestsVBox;

    private String sessionId;

    public CreateProposalGUIController(String sessionId) {
        this.sessionId = sessionId;
    }

    public void initialize() {
        List<RequestBean> requests = new ResponseRequestController().getAgencyRequests(sessionId);
        for (RequestBean request : requests) {
            try {
                URL url = new File("src/main/java/it/uniroma2/ispw/globe/view/tabElement.fxml").toURI().toURL();
                FXMLLoader loader = new FXMLLoader(url);
                Button requestsBox = loader.load();
                requestsBox.setOnAction(actionEvent -> {
                    userLabel.setText(request.getUser());
                    requestLabel.setText(request.getID());
                    selectVBox.getChildren().clear();
                    createVBox.setVisible(true);
                });
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

    public void createItinerary(ActionEvent event) {
        URL url;
        Parent root;

        String proposalName = proposalField.getText();
        double price = Double.parseDouble(priceField.getText());
        String description = descriptionField.getText();
        String requestId = requestLabel.getText();

        if (!proposalName.isEmpty() && !description.isEmpty() && price != 0) {
            ProposalBean proposalBean = new ProposalBean(proposalName,price,userLabel.getText(),description);
            String proposalID = new ResponseRequestController().saveProposal(proposalBean,requestId,sessionId);
            try {
                url = new File("src/main/java/it/uniroma2/ispw/globe/view/CreateItineraryView.fxml").toURI().toURL();
                FXMLLoader loader = new FXMLLoader(url);
                CreateItineraryGUIController controller = new CreateItineraryGUIController(sessionId,proposalID);
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
}
