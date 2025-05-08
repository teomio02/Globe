package it.uniroma2.ispw.globe.controller.clicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.CreateItineraryController;
import it.uniroma2.ispw.globe.controller.applicationcontroller.ResponseRequestController;
import it.uniroma2.ispw.globe.exception.DuplicateItemException;
import it.uniroma2.ispw.globe.exception.FailedOperationException;
import it.uniroma2.ispw.globe.exception.IncorrectDataException;
import it.uniroma2.ispw.globe.model.bean.*;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static it.uniroma2.ispw.globe.other.ItineraryType.*;

public class CreateItineraryCLIController {
    private String sessionId;
    private String requestId;

    private static final String CHOICE_ERROR = "ERROR: Invalid option\n";
    private static final String ERROR = "ERROR: ";

    CreateItineraryCLIController(String sessionId, String requestId) {
        this.sessionId = sessionId;
        this.requestId = requestId;
    }

    public void start() {
        System.out.println("# CREATE ITINERARY #");

        if (requestId != null) {
            try {
                //create proposal use case
                AgencyRequestBean requestBean = null;
                requestBean = new ResponseRequestController().getAgencyRequest(requestId, sessionId);

                if (requestBean != null) {
                    System.out.println("> User " + requestBean.getUser() + " request:");
                    System.out.println("    > Description: " + requestBean.getDescription());
                    System.out.println("    > Types: " + requestBean.getTypes());
                    System.out.println("    > Cities: " + requestBean.getCities());
                    System.out.println("    > Attractions: " + requestBean.getAttractions());
                }
            } catch (FailedOperationException | DuplicateItemException | IncorrectDataException e) {
                System.out.println(ERROR + e.getMessage());
                return;
            }
        }

        getItineraryData();
    }

    public void getItineraryData() {
        String name, str_duration, description;
        int duration;

        Scanner input = new Scanner(System.in);

        while (true){
            System.out.print("Please enter Itinerary name: ");
            name = input.nextLine();
            if (!name.isEmpty()) {
                break;
            }
            System.out.println(CHOICE_ERROR);
        }
        while (true) {
            System.out.print("Please enter itinerary duration: ");
            str_duration = input.nextLine();
            if (!str_duration.isEmpty() && str_duration.matches("[1-99]")) {
                duration = Integer.parseInt(str_duration);
                break;
            }
            System.out.println(CHOICE_ERROR);
        }
        while (true) {
            System.out.print("Please enter itinerary description: ");
            description = input.nextLine();
            if (!description.isEmpty()) {
                break;
            }
            System.out.println(CHOICE_ERROR);
        }

        List<String> types = getTypes();
        List<String> cities = getCities();
        List<String> attractions = getAttractions();

        System.out.println("Do you want to add an accommodation? (yes/no)");

        List<Pair<String, String>> accommodations = new ArrayList<>();
        while (true) {
            String response = input.nextLine();
            if (response.equalsIgnoreCase("yes") ) {
                accommodations = getAccommodations();
                break;
            } else if (response.equalsIgnoreCase("no") ) {
                break;
            }
            System.out.println(CHOICE_ERROR);
        }

        System.out.println("Do you want to add flight? (yes/no)");

        List<Double> flight = new ArrayList<>();

        while (true) {
            String response = input.nextLine();
            if (response.equalsIgnoreCase("yes") ) {
                flight = getFlight();
                break;
            } else if (response.equalsIgnoreCase("no") ) {
                break;
            }
            System.out.println(CHOICE_ERROR);
        }

        ItineraryBean itineraryBean = new ItineraryBean();
        try {
            itineraryBean.setId(null);
            itineraryBean.setName(name);
            itineraryBean.setDescription(description);
            itineraryBean.setTypes(types);
            itineraryBean.setCities(cities);
            itineraryBean.setAttractions(attractions);
            itineraryBean.setDuration(duration);

            if (!accommodations.isEmpty()) {
                itineraryBean.setAccommodations(accommodations);
            }
            if (!flight.isEmpty()) {
                itineraryBean.setInboundFlightDepartureTime(flight.get(0));
                itineraryBean.setInboundFlightArrivalTime(flight.get(1));
                itineraryBean.setOutboundFlightDepartureTime(flight.get(2));
                itineraryBean.setOutboundFlightArrivalTime(flight.get(3));
            }
            new CreateItineraryController().createItinerary(itineraryBean,sessionId);
        } catch (FailedOperationException | DuplicateItemException | IncorrectDataException e) {
            System.out.println(ERROR + e.getMessage());
        }
        DisplayItineraryCLIController controller;
        if (requestId != null) {
            controller = new DisplayItineraryCLIController(sessionId,null,requestId,null);
        } else {
            controller = new DisplayItineraryCLIController(sessionId,null,null,null);
        }
        controller.start();
    }

    public List<String> getTypes() {
        String str_choice, response;
        int choice;
        List<String> types = new ArrayList<>();

        Scanner input = new Scanner(System.in);

        while (true){
            System.out.println("Please enter the number of the type for your itinerary");
            System.out.println("1 -> " + ON_THE_ROAD);
            System.out.println("2 -> " + NATURE);
            System.out.println("3 -> " + CULTURE);
            System.out.println("4 -> " + RELAX);
            System.out.println("5 -> " + CITY);

            while (true) {
                System.out.print("Please enter your choice: ");
                str_choice = input.nextLine();
                if (!str_choice.isEmpty() && str_choice.matches("[1-5]")) {
                    choice = Integer.parseInt(str_choice);
                    String type;
                    switch (choice) {
                        case 1 -> type = ON_THE_ROAD;
                        case 2 -> type = NATURE;
                        case 3 -> type = CULTURE;
                        case 4 -> type = RELAX;
                        case 5 -> type = CITY;
                        default -> type = null;
                    }
                    if (!types.contains(type)) {
                        types.add(type);
                        break;
                    }
                    System.out.println(ERROR + "type already exist");
                }
                System.out.println(CHOICE_ERROR);
            }

            while (true){
                System.out.println("Do you want to add another type? (yes/no)");
                response = input.nextLine();
                if (!response.isEmpty() && (response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("no"))) {
                    break;
                }
                System.out.println(CHOICE_ERROR);
            }

            if (response.equalsIgnoreCase("no")) {
                return types;
            }
        }
    }

    public List<String> getCities() {
        String city;
        List<String> cities = new ArrayList<>();

        Scanner input = new Scanner(System.in);

        while (true){
            System.out.print("Please enter City (enter stop to termiante): ");
            city = input.nextLine();
            if (!city.isEmpty()) {
                if (city.equalsIgnoreCase("stop")) {
                    break;
                }
                String cityID = getCity(city);
                if (cityID != null) {
                    cities.add(cityID);
                }
            }else {
                System.out.println(CHOICE_ERROR);
            }
        }

        return cities;
    }

    public String getCity(String city) {
        List<CityBean> cities_result = new ArrayList<>();
        String matching;
        try {
            cities_result = new CreateItineraryController().getCities(city);
        } catch (FailedOperationException e) {
            System.out.println(ERROR + e.getMessage());
        }
        if (!cities_result.isEmpty()) {
            int i = 0;
            for (CityBean cityResult : cities_result) {
                i++;
                System.out.println(i + " -> " + cityResult.getName()+" - "+ cityResult.getCountry());
                if (i == 3) {
                    break;
                }
            }
            Scanner input = new Scanner(System.in);
            String str_choice;
            int choice;
            while (true) {
                System.out.println("Please enter the number of the city for your itinerary: ");
                str_choice = input.nextLine();
                try {
                    choice = Integer.parseInt(str_choice);
                    if (choice >= 1 && choice <= i) {
                        break;
                    } else {
                        System.out.println(CHOICE_ERROR);
                    }
                } catch (NumberFormatException e) {
                    System.out.println(CHOICE_ERROR);
                }
            }
            return cities_result.get(choice-1).getId();
        } else {
            System.out.println("> no place");
        }
        return null;
    }

    public List<String> getAttractions() {
        String attraction;
        List<String> attractions = new ArrayList<>();

        Scanner input = new Scanner(System.in);

        while (true){
            System.out.print("Please enter Attraction (enter stop to termiante): ");
            attraction = input.nextLine();
            if (!attraction.isEmpty()) {
                if (attraction.equalsIgnoreCase("stop")) {
                    break;
                }
                String attrID = getAttraction(attraction);
                if (attrID != null) {
                    attractions.add(attrID);
                }
            } else {
                System.out.println(CHOICE_ERROR);
            }
        }

        return attractions;
    }

    public String getAttraction(String attr) {
        List<AttractionBean> attractionsResult = new ArrayList<>();
        String matching;
        try {
            attractionsResult = new CreateItineraryController().getAttractions(attr);
        } catch (FailedOperationException e) {
            System.out.println(ERROR + e.getMessage());
        }
        if (!attractionsResult.isEmpty()) {
            int i = 0;
            for (AttractionBean attractionResult : attractionsResult) {
                i++;
                System.out.println(i + " -> " + attractionResult.getName()+" - "+ attractionResult.getCity());
                if (i == 3) {
                    break;
                }
            }
            Scanner input = new Scanner(System.in);
            String str_choice;
            int choice;
            while (true) {
                System.out.println("Please enter the number of the attraction for your itinerary: ");
                str_choice = input.nextLine();
                try {
                    choice = Integer.parseInt(str_choice);
                    if (choice >= 1 && choice <= i) {
                        break;
                    } else {
                        System.out.println(CHOICE_ERROR);
                    }
                } catch (NumberFormatException e) {
                    System.out.println(CHOICE_ERROR);
                }
            }
            return attractionsResult.get(choice-1).getId();
        } else {
            System.out.println("> no place");
        }
        return null;
    }

    public  List<Pair<String, String>> getAccommodations() {
        String accommodation, address, choice;
        List<Pair<String, String>> accommodations = new ArrayList<>();

        Scanner input = new Scanner(System.in);

        while (true) {
            while (true){
                System.out.print("Please enter accommodation name: ");
                accommodation = input.nextLine();
                if (!accommodation.isEmpty()) {
                    break;
                }
                System.out.println(CHOICE_ERROR);
            }
            while (true){
                System.out.print("Please enter accommodation address: ");
                address = input.nextLine();
                if (!address.isEmpty()) {
                    break;
                }
                System.out.println(CHOICE_ERROR);
            }
            accommodations.add(new Pair<>(accommodation, address));

            while (true){
                System.out.println("Do you want to add another accommodation? (yes/no)");
                choice = input.nextLine();
                if (!choice.isEmpty() && (choice.equalsIgnoreCase("yes") || choice.equalsIgnoreCase("no"))) {
                    break;
                }
                System.out.println(CHOICE_ERROR);
            }

            if (choice.equalsIgnoreCase("no")) {
                return accommodations;
            }
        }
    }

    public List<Double> getFlight() {
        String inDepartureTime_str, inArrivalTime_str, outDepartureTime_str, outArrivalTime_str;
        double inDepartureTime, inArrivalTime, outDepartureTime, outArrivalTime ;
        List<Double> times = new ArrayList<>();

        Scanner input = new Scanner(System.in);

        while (true){
            System.out.print("Please enter inbound departure time: ");
            inDepartureTime_str = input.nextLine();
            if (!inDepartureTime_str.isEmpty() && inDepartureTime_str.matches("^([0-9]|1[0-9]|2[0-3])(\\.[0-5][0-9])?$")) {
                inDepartureTime = Double.parseDouble(inDepartureTime_str);
                break;
            }
            System.out.println(CHOICE_ERROR);
        }
        while (true){
            System.out.print("Please enter inbound arrival time: ");
            inArrivalTime_str = input.nextLine();
            if (!inArrivalTime_str.isEmpty() && inArrivalTime_str.matches("^([0-9]|1[0-9]|2[0-3])(\\.[0-5][0-9])?$")) {
                inArrivalTime = Double.parseDouble(inArrivalTime_str);
                break;
            }
            System.out.println(CHOICE_ERROR);
        }
        while (true){
            System.out.print("Please enter outbound departure time: ");
            outDepartureTime_str = input.nextLine();
            if (!outDepartureTime_str.isEmpty() && outDepartureTime_str.matches("^([0-9]|1[0-9]|2[0-3])(\\.[0-5][0-9])?$")) {
                outDepartureTime = Double.parseDouble(outDepartureTime_str);
                break;
            }
            System.out.println(CHOICE_ERROR);
        }
        while (true){
            System.out.print("Please enter outbound arrival time: ");
            outArrivalTime_str = input.nextLine();
            if (!outArrivalTime_str.isEmpty() && outArrivalTime_str.matches("^([0-9]|1[0-9]|2[0-3])(\\.[0-5][0-9])?$")) {
                outArrivalTime = Double.parseDouble(outArrivalTime_str);
                break;
            }
            System.out.println(CHOICE_ERROR);
        }
        times.add(inDepartureTime);
        times.add(inArrivalTime);
        times.add(outDepartureTime);
        times.add(outArrivalTime);

        return times;
    }
}
