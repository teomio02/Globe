package it.uniroma2.ispw.globe.view;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class DayTab {
    int count=0;
    int day=1;

    public void setTab(TabPane daysTabPane) {
        count++;
        Tab tab = new Tab(String.valueOf(count));
        daysTabPane.getTabs().add(tab);

        VBox vbox = new VBox(5);

        TextField cityTF = new TextField();
        cityTF.setPromptText("City");

        TextField accommodationTF = new TextField();
        accommodationTF.setPromptText("Accommodation");

        HBox cityHBox = new HBox(5, cityTF, accommodationTF);
        cityHBox.setPrefWidth(vbox.getWidth());

        TextField attractionTF = new TextField();
        attractionTF.setPromptText("Attraction");

        //probabilmente l'handler del bottone va messo nel viewcontroller
        Button newAttractionButton = new Button("+");
        newAttractionButton.setOnAction(e -> {
            if (!attractionTF.getText().isEmpty()){
                Label label = new Label(attractionTF.getText());
                label.getStyleClass().add("body");
                vbox.getChildren().add(label);
                attractionTF.setText("");
            }
        });

        HBox attractionHBox = new HBox(5, attractionTF, newAttractionButton);
        attractionHBox.setPrefWidth(vbox.getWidth());

        vbox.getChildren().addAll(cityHBox,attractionHBox);

        tab.setContent(vbox);
    }

    public void removeTab(TabPane daysTabPane) {
        count--;
        daysTabPane.getTabs().remove(count);
    }

    public void setViewTab (TabPane daysTabPane, int attrNum) {
        Tab tab = new Tab(String.valueOf(day));
        daysTabPane.getTabs().add(tab);

        VBox vbox = new VBox(5);

        Label city = new Label("City");
        city.getStyleClass().add("subtitle");
        Label cityLabel = new Label();
        cityLabel.getStyleClass().add("body");

        Label accommodation = new Label("Accommodation");
        accommodation.getStyleClass().add("subtitle");
        Label accommodationLabel = new Label();
        accommodationLabel.getStyleClass().add("body");

        HBox cityHBox = new HBox(5, city, cityLabel,accommodation , accommodationLabel);
        cityHBox.setPrefWidth(vbox.getWidth());

        Label attractionLabel = new Label("Attraction");
        attractionLabel.getStyleClass().add("subtitle");

        VBox attractionVBox = new VBox(2, attractionLabel);
        for (int i = 0; i < attrNum; i++) {
            Label attractionL = new Label();
            attractionL.getStyleClass().add("body");
            attractionVBox.getChildren().add(attractionL);
        }

        vbox.getChildren().addAll(cityHBox,attractionVBox);

        tab.setContent(vbox);
        day++;
    }
}
