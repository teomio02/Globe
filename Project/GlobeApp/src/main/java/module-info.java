module it.ispw.globeapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens it.ispw.globeapp to javafx.fxml;
    exports it.ispw.globeapp;

    opens it.ispw.globeapp.View to javafx.fxml;
    exports it.ispw.globeapp.View to javafx.fxml;
}