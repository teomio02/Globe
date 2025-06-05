package it.uniroma2.ispw.globe;

import it.uniroma2.ispw.globe.controller.clicontroller.LogInCLIController;
import it.uniroma2.ispw.globe.engineering.Persistence;

public class GlobeApplicationCLI {

    public static void main(String[] args) {
        Persistence.getInstance().setDefaultType(Persistence.IN_DATABASE);
        Persistence.getInstance().setType(Persistence.IN_DATABASE);

        System.out.println("*********************************");
        System.out.println("*      GLOBE  APPLICATION       *");
        System.out.println("*********************************");
        System.out.println();

        new LogInCLIController().start();
    }
}