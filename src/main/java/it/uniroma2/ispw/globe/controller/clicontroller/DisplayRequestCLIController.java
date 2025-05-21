package it.uniroma2.ispw.globe.controller.clicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.CreateItineraryController;
import it.uniroma2.ispw.globe.controller.applicationcontroller.RequestItineraryController;
import it.uniroma2.ispw.globe.controller.applicationcontroller.ResponseRequestController;
import it.uniroma2.ispw.globe.controller.guicontroller.ErrorPopUpGUIController;
import it.uniroma2.ispw.globe.exception.DuplicateItemException;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
import it.uniroma2.ispw.globe.exception.IncorrectDataException;
import it.uniroma2.ispw.globe.model.bean.*;
import javafx.scene.control.Label;
import javafx.util.Pair;

import java.util.List;
import java.util.Scanner;

public class DisplayRequestCLIController {
    private String sessionId;
    private String requestId;

    private static final String CHOICE_ERROR = "ERROR: Invalid option\n";
    private static final String ERROR = "ERROR: ";

    DisplayRequestCLIController(String sessionId, String requestId) {
        this.sessionId = sessionId;
        this.requestId = requestId;
    }

    public void start() {
        System.out.println("# DISPLAY REQUEST #");


        if (requestId == null) {
            RequestBean request;
            List<Object> optionals;
            List<AgencyBean> agencies;
            NatureBean nature;
            OnTheRoadBean onTheRoad;
            try {
                request = new RequestItineraryController().getRequest(requestId, sessionId);
                optionals = new RequestItineraryController().getRequestOptional(requestId, sessionId);
                agencies = new RequestItineraryController().getAgencies(sessionId);
            } catch (FailedOperationException | DuplicateItemException | IncorrectDataException e) {
                System.out.println(ERROR + e.getMessage());
                return;
            }


            System.out.println("> ID: " + request.getID());
            System.out.println("    > Agencies: ");
            for (AgencyBean agency : agencies) {
                System.out.println("        - " + agency.getName() + ", " + agency.getRating());
            }
            System.out.println("    > Days: " + request.getDayNum());
            System.out.println("    > Requests: " + request.getOtherRequests());
            System.out.println("    > Types: " + request.getTypes());

            for (Object optional: optionals) {
                if (optional instanceof OnTheRoadBean) {
                    onTheRoad = (OnTheRoadBean) optional;
                    System.out.println("    > Travel mode: " + onTheRoad.getMode());
                    System.out.println("    > Day driving hours: " + onTheRoad.getDayDrivingHours());

                } else if (optional instanceof NatureBean) {
                    nature = (NatureBean) optional;
                    System.out.println("    > Trekking difficulty: " + nature.getDifficulty());
                    System.out.println("    > Trekking distance:   " + nature.getTrekkingDistance());
                }
            }


            List<String> citiesID = request.getCities();
            List<String> attractionsID = request.getAttractions();

            System.out.println("    > Cities: ");
            for (String cityID: citiesID) {
                try {
                    CityBean city =  new RequestItineraryController().getCity(cityID);
                    System.out.println("        - " + city.getName() + ", " + city.getCountry());
                } catch (FailedOperationException e) {
                    System.out.println(ERROR + e.getMessage());
                    return;
                }
            }

            System.out.println("    > Attractions: ");
            for (String attractionID: attractionsID) {
                try {
                    AttractionBean attraction = new RequestItineraryController().getAttraction(attractionID);
                    System.out.println("        - " + attraction.getName() + ", " + attraction.getAddress());
                } catch (FailedOperationException e) {
                    System.out.println(ERROR + e.getMessage());
                    return;
                }
            }

            int choice = showUserMenu();
            switch (choice) {
                case 1 -> saveRequest();
                case 2 -> {
                    // go back
                }
            }
        }
    }

    public int showUserMenu() {
        System.out.println("What do you want do?\n");

        System.out.println("1 -> Save Request");
        System.out.println("2 -> Go Back");

        Scanner input = new Scanner(System.in);
        String strChoice;
        int choice;
        while (true) {
            System.out.print("Please enter your choice: ");
            strChoice = input.nextLine();
            if (!strChoice.isEmpty() && strChoice.matches("[1-2]")) {
                choice = Integer.parseInt(strChoice);
                break;
            }
            System.out.println(CHOICE_ERROR);
        }
        return choice;
    }

    public int showAgencyMenu() {
        System.out.println("What do you want do?\n");

        System.out.println("1 -> Create Proposal");
        System.out.println("2 -> Go Back");

        Scanner input = new Scanner(System.in);
        String strChoice;
        int choice;
        while (true) {
            System.out.print("Please enter your choice: ");
            strChoice = input.nextLine();
            if (!strChoice.isEmpty() && strChoice.matches("[1-2]")) {
                choice = Integer.parseInt(strChoice);
                break;
            }
            System.out.println(CHOICE_ERROR);
        }
        return choice;
    }

    public void saveRequest() {
        if (requestId == null) {
            try {
                new RequestItineraryController().saveRequest(sessionId);
            } catch (FailedOperationException | DuplicateItemException e) {
                System.out.println(ERROR + e.getMessage());
            }
        }
    }
}
