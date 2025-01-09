package it.uniroma2.ispw.globe.view;

import it.uniroma2.ispw.globe.model.bean.ItineraryBean;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.scene.control.*;

public class ItineraryBox extends HBox {

    public HBox createHBox(ItineraryBean itinerary) {
        Label name_l = new Label(itinerary.getName());
        name_l.getStyleClass().add("title");
        Label description_l = new Label(itinerary.getDescription());
        description_l.getStyleClass().add("thin");
        Label dayNum_l = new Label(String.valueOf(itinerary.getDuration()));
        dayNum_l.getStyleClass().add("subtitle");

        HBox itineraryHBox = new HBox(10, name_l, description_l, dayNum_l);
        itineraryHBox.setAlignment(Pos.CENTER_LEFT);
        itineraryHBox.setStyle("-fx-background-color: #4a6a3d;");

        return itineraryHBox;
    }
}
