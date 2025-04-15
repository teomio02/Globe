package it.uniroma2.ispw.globe.controller.guicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.ResponseRequestController;
import it.uniroma2.ispw.globe.exception.DuplicateItemException;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
import it.uniroma2.ispw.globe.model.bean.*;
import it.uniroma2.ispw.globe.other.session.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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

    public void initialize(String sessionId) {
        NavigationData data = SessionManager.getInstance().getSession(sessionId).getNavigationData();
        this.sessionId = data.getSessionID();
        this.requestId = data.getRequestID();
        this.prev = data.getPrev();

        AgencyRequestBean request = null;
        ProposalBean proposal = null;
        try {
            request = new ResponseRequestController().getAgencyRequest(requestId, sessionId);
            proposal = new ResponseRequestController().getProposal(null,sessionId);
        } catch (FailedOperationException | DuplicateItemException e) {
            new ErrorPopUpGUIController().createPopUp(e.getMessage());
            return;
        }

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

        try {
            new ResponseRequestController().createProposal(proposalBean,userLabel.getText(),requestId,sessionId);
        } catch (FailedOperationException | DuplicateItemException e) {
            new ErrorPopUpGUIController().createPopUp(e.getMessage());
            return;
        }

        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        ViewManager viewManager = new ViewManager();
        viewManager.goToDisplayProposalGUI(sessionId,requestId,null, root);
    }

    public void goBack(ActionEvent event) {
        BorderPane root = (BorderPane) ((Node) event.getSource()).getScene().getRoot();
        if (prev != null) {
            root.setCenter(prev);
        }
    }
}
