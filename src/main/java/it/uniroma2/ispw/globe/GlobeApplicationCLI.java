package it.uniroma2.ispw.globe;

import it.uniroma2.ispw.globe.controller.clicontroller.LogInCLIController;
import it.uniroma2.ispw.globe.controller.guicontroller.LogInGUIController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class GlobeApplicationCLI {

    public static void main(String[] args) {
        LogInCLIController controller = new LogInCLIController();
        System.out.println("*********************************");
        System.out.println("*    GLOBE  APPLICATION         *");
        System.out.println("*********************************\n");
        controller.start();
    }
}