package it.uniroma2.ispw.globe.view;

import it.uniroma2.ispw.globe.model.bean.AttractionBean;
import it.uniroma2.ispw.globe.model.bean.CityBean;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class DayTab {
    int count=0;
    int day=1;

    public void setTab(TabPane daysTabPane) {
        count++;
        Tab tab = new Tab(String.valueOf(count));
        daysTabPane.getTabs().add(tab);

        VBox vbox = new VBox(5);

        TextField city_tf = new TextField();
        city_tf.setPromptText("City");

        TextField accommodation_tf = new TextField();
        accommodation_tf.setPromptText("Accommodation");

        HBox cityHBox = new HBox(5, city_tf, accommodation_tf);
        cityHBox.setPrefWidth(vbox.getWidth());

        TextField attraction_tf = new TextField();
        attraction_tf.setPromptText("Attraction");

        //probabilmente l'handler del bottone va messo nel viewcontroller
        Button newAttraction_button = new Button("+");
        newAttraction_button.setOnAction(e -> {
            if (!attraction_tf.getText().isEmpty()){
                Label label = new Label(attraction_tf.getText());
                label.getStyleClass().add("body");
                vbox.getChildren().add(label);
                attraction_tf.setText("");
            }
        });

        HBox attractionHBox = new HBox(5, attraction_tf, newAttraction_button);
        attractionHBox.setPrefWidth(vbox.getWidth());

        vbox.getChildren().addAll(cityHBox,attractionHBox);

        tab.setContent(vbox);
    }

    public void removeTab(TabPane daysTabPane) {
        count--;
        daysTabPane.getTabs().remove(count);
    }

    public void setViewTab (TabPane daysTabPane, int attr_num) {
        Tab tab = new Tab(String.valueOf(day));
        daysTabPane.getTabs().add(tab);

        VBox vbox = new VBox(5);

        Label city = new Label("City");
        city.getStyleClass().add("subtitle");
        Label city_l = new Label();
        city_l.getStyleClass().add("body");

        Label accommodation = new Label("Accommodation");
        accommodation.getStyleClass().add("subtitle");
        Label accommodation_l = new Label();
        accommodation_l.getStyleClass().add("body");

        HBox cityHBox = new HBox(5, city, city_l,accommodation ,accommodation_l);
        cityHBox.setPrefWidth(vbox.getWidth());

        Label attraction_l = new Label("Attraction");
        attraction_l.getStyleClass().add("subtitle");

        VBox attractionVBox = new VBox(2, attraction_l);
        for (int i = 0; i < attr_num; i++) {
            Label attractionLabel = new Label();
            attractionLabel.getStyleClass().add("body");
            attractionVBox.getChildren().add(attractionLabel);
        }

        vbox.getChildren().addAll(cityHBox,attractionVBox);

        tab.setContent(vbox);
        day++;
    }
}
