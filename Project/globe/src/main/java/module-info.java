module it.uniroma2.ispw.globe {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires java.net.http;


    opens it.uniroma2.ispw.globe to javafx.fxml;
    exports it.uniroma2.ispw.globe;
    opens it.uniroma2.ispw.globe.controller.guicontroller to  javafx.fxml;
}