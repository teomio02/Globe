package it.uniroma2.ispw.globe.controller.guicontroller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ErrorPopUpGUIController {

    @FXML
    private Label descriptionLabel;
    @FXML
    private Label errorLabel;
    @FXML
    private Button okButton;


    public void createPopUp(Exception exception) {
        try {
            URL url = new File("src/main/java/it/uniroma2/ispw/globe/view/ErrorPopUp.fxml").toURI().toURL();
            FXMLLoader loader = new FXMLLoader(url);
            loader.setController(this);
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.setFill(Color.TRANSPARENT);

            Stage popupStage = new Stage();
            popupStage.setScene(scene);

            popupStage.initStyle(StageStyle.TRANSPARENT);
            popupStage.initModality(Modality.APPLICATION_MODAL);

            descriptionLabel.setText(exception.getMessage());
            errorLabel.setText("ERROR");
            okButton.setOnAction(e -> popupStage.close());

            popupStage.setScene(scene);
            popupStage.showAndWait();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
