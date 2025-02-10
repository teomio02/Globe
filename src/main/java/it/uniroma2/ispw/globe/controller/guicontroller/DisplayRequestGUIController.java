package it.uniroma2.ispw.globe.controller.guicontroller;

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



public class DisplayRequestGUIController {
    private String sessionId;
    private String requestID;

    @FXML
    private Button backButton;
    @FXML
    private Label cityLabel;
    @FXML
    private Label attractionsLabel;
    @FXML
    private Label itineraryTypeLabel;
    @FXML
    private Label agencyLabel;
    @FXML
    private VBox otherRequestVBox;
    @FXML
    private VBox daysVBox;
    @FXML
    private VBox flightVBox;
    @FXML
    private VBox accommodationVBox;
    @FXML
    private Button sendButton;

    public void initialize() {

    }

    public DisplayRequestGUIController(String sessionId, String requestID) {
        this.sessionId = sessionId;
        this.requestID = requestID;
    }

    public void createProposal(ActionEvent event) {
        URL url;
        Parent root;

        try {
            url = new File("src/main/java/it/uniroma2/ispw/globe/view/CreateProposalView.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            CreateProposalGUIController controller = new CreateProposalGUIController(sessionId);
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
