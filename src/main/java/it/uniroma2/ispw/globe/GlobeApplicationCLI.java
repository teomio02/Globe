package it.uniroma2.ispw.globe;

import it.uniroma2.ispw.globe.controller.clicontroller.LogInCLIController;

public class GlobeApplicationCLI {

    public static void main(String[] args) {
        LogInCLIController controller = new LogInCLIController();
        System.out.println("*********************************");
        System.out.println("*    GLOBE  APPLICATION         *");
        System.out.println("*********************************\n");
        controller.start();
    }
}