module it.uniroma2.ispw.globe {
    requires javafx.controls;
    requires javafx.fxml;


    opens it.uniroma2.ispw.globe to javafx.fxml;
    exports it.uniroma2.ispw.globe;
    opens it.uniroma2.ispw.globe.controller.guicontroller to  javafx.fxml;
}