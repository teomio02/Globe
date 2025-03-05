package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.ResponseRequestController;
import it.uniroma2.ispw.globe.model.bean.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class CreateProposalGUIController {
    @FXML
    private Label userLabel;
    @FXML
    private Label requestLabel;
    @FXML
    private Label daysLabel;
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
    @FXML
    private HBox typesHBox;

    private String sessionId;
    private String requestId;

    private Node prev;

    public CreateProposalGUIController(String sessionId,String requestId,Node prev) {
        this.requestId = requestId;
        this.sessionId = sessionId;
        this.prev = prev;
    }

    public void initialize() {
        AgencyRequestBean request = new ResponseRequestController().getAgencyRequest(requestId, sessionId);
        ProposalBean proposal = new ResponseRequestController().getProposal(null,sessionId);

        userLabel.setText(request.getUser());
        requestLabel.setText(request.getDescription());
        daysLabel.setText(String.valueOf(request.getDays()));
        for (String type : request.getTypes()){
            typesHBox.getChildren().add(new Label(type));
        }

        if (proposal != null){
            priceField.setText(String.valueOf(proposal.getPrice()));
            descriptionField.setText(proposal.getDescription());
        }
    }

    public void createProposal(ActionEvent event) {

        int count = 0;

        if (priceField.getText().isEmpty()) {
            count ++;
        }
        if (descriptionField.getText().isEmpty()) {
            count ++;
        }
        if (count>0) {
            // errore
            return;
        }

        ProposalBean proposalBean = new ProposalBean(Double.parseDouble(priceField.getText()),descriptionField.getText());

        new ResponseRequestController().createProposal(proposalBean,requestId,sessionId);

        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();

        try {
            URL url = new File("src/main/java/it/uniroma2/ispw/globe/view/DisplayProposalView.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            DisplayProposalGUIController controller = new DisplayProposalGUIController(sessionId,requestId,null, root.getCenter());
            loader.setController(controller);
            root.setCenter(loader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void goBack(ActionEvent event) {
        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        if (prev != null) {
            root.setCenter(prev);
        }
    }

//    public void goBack(ActionEvent event) {
//        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
//        NavigationGUIController nav = new NavigationGUIController(root);
//        nav.loadView("src/main/java/it/uniroma2/ispw/globe/view/DisplayItineraryView.fxml",
//                new DisplayItineraryGUIController(sessionId, null, requestId, null));
//    }


//    public void goBack(ActionEvent event) {
//        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
//
//        try {
//            URL url = new File("src/main/java/it/uniroma2/ispw/globe/view/DisplayItineraryView.fxml").toURI().toURL();
//            FXMLLoader loader = new FXMLLoader(url);
//            DisplayItineraryGUIController controller = new DisplayItineraryGUIController(sessionId,null,requestId,null);
//            loader.setController(controller);
//            root.setCenter(loader.load());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
