package it.uniroma2.ispw.globe.controller.clicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.CreateItineraryController;
import it.uniroma2.ispw.globe.controller.applicationcontroller.ResponseRequestController;
import it.uniroma2.ispw.globe.model.bean.AttractionBean;
import it.uniroma2.ispw.globe.model.bean.CityBean;
import it.uniroma2.ispw.globe.model.bean.ItineraryBean;
import it.uniroma2.ispw.globe.model.bean.StepBean;
import it.uniroma2.ispw.globe.view.DayTab;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CreateItineraryCLIController {
    private String sessionId;
    private String requestId;

    private static String YES = "yes";
    private static String NO = "no";

    public CreateItineraryCLIController(String sessionId,String requestId) {
        this.sessionId = sessionId;
        this.requestId = requestId;
    }

    public void start() {
        getItineraryInfo();
        showItinerary();
    }

    private void getItineraryInfo() {
        String name,str_day,description;

        int day;

        List<String> cities = new ArrayList<>();
        List<String> attractions = new ArrayList<>();
        List<String> types = new ArrayList<>();

        List<Pair<String, String>> accommodations = new ArrayList<>();

        Scanner input = new Scanner(System.in);
        System.out.println("<CreateItinerary> operation:\n");

        while (true){
            System.out.print("Please enter Itinerary Name: ");
            name = input.nextLine();
            if (!name.isEmpty()) {
                break;
            }
            System.out.println("Invalid option");
        }

        while (true){
            System.out.print("Please enter Itinerary Duration: ");

            str_day = input.nextLine();
            if (!str_day.isEmpty() && str_day.matches("[1-99]")) {
                day = Integer.parseInt(str_day);
                break;
            }
            System.out.println("Invalid option");
        }

        while (true){
            System.out.print("Please enter Itinerary Description: ");
            description = input.nextLine();
            if (!description.isEmpty()) {
                break;
            }
            System.out.println("Invalid option");
        }

        while (true){
            System.out.println("-> Do you want to add city?\n");
            System.out.println("1> Yes");
            System.out.println("2> No");

            String str_choice;
            int choice = 0;
            while (true) {
                System.out.print("Please enter your choice: ");
                str_choice = input.nextLine();
                if (!str_choice.isEmpty() && str_choice.matches("[1-2]")) {
                    choice = Integer.parseInt(str_choice);
                    break;
                }
                System.out.println("Invalid option");
            }

            if (choice == 1) {
                String cityID = getCity();
                if (cityID != null) {
                    cities.add(cityID);
                }
            } else if (choice == 2){
                break;
            } else {
                throw new RuntimeException("Invalid choice");
            }
        }

        while (true) {
            System.out.println("-> Do you want to add attraction?\n");
            System.out.println("1> Yes");
            System.out.println("2> No");

            String str_choice;
            int choice = 0;
            while (true) {
                System.out.print("Please enter your choice: ");
                str_choice = input.nextLine();
                if (!str_choice.isEmpty() && str_choice.matches("[1-2]")) {
                    choice = Integer.parseInt(str_choice);
                    break;
                }
                System.out.println("Invalid option");
            }

            if (choice == 1) {
                String attractionID = getAttraction();
                if (attractionID != null) {
                    attractions.add(attractionID);
                }
            } else if (choice == 2){
                break;
            } else {
                throw new RuntimeException("Invalid choice");
            }
        }


        //popola types

        ItineraryBean itineraryBean = new ItineraryBean(null,name,description,types,day,cities,attractions);


        while (true) {
            System.out.println("-> Do you want to add some accommodations?\n");
            System.out.println("1> Yes");
            System.out.println("2> No");

            String str_choice;
            int choice = 0;
            while (true) {
                System.out.print("Please enter your choice: ");
                str_choice = input.nextLine();
                if (!str_choice.isEmpty() && str_choice.matches("[1-2]")) {
                    choice = Integer.parseInt(str_choice);
                    break;
                }
                System.out.println("Invalid option");
            }

            if (choice == 1) {
                accommodations.add(getAccommodation());
            } else if (choice == 2){
                break;
            } else {
                throw new RuntimeException("Invalid choice");
            }
        }
        itineraryBean.setAccommodations(accommodations);

        System.out.println("-> Do you want to add in and out bound flights?\n");
        System.out.println("1> Yes");
        System.out.println("2> No");

        String str_choice;
        int choice = 0;
        while (true) {
            System.out.print("Please enter your choice: ");
            str_choice = input.nextLine();
            if (!str_choice.isEmpty() && str_choice.matches("[1-2]")) {
                choice = Integer.parseInt(str_choice);
                break;
            }
            System.out.println("Invalid option");
        }

        if (choice == 1) {
            double inDepartureTime,inArrivalTime,outDepartureTime,outArrivalTime;
            String str_inDepartureTime,str_inArrivalTime,str_outDepartureTime,str_outArrivalTime;

            while (true){
                System.out.print("Please enter inbound departure time: ");
                str_inDepartureTime = input.nextLine();
                if (!str_inDepartureTime.isEmpty() && str_inDepartureTime.matches("([01]\\d|2[0-3])[.][0-5]\\d")) {
                    inDepartureTime = Double.parseDouble(str_inDepartureTime);
                    break;
                } else {
                    System.out.println("Invalid option");
                }
            }

            while (true) {
                System.out.print("Please enter inbound arrival time: ");
                str_inArrivalTime = input.nextLine();
                if (!str_inArrivalTime.isEmpty() && str_inArrivalTime.matches("([01]\\d|2[0-3])[.][0-5]\\d")) {
                    inArrivalTime = Double.parseDouble(str_inArrivalTime);
                    break;
                } else {
                    System.out.println("Invalid option");
                }
            }

            while (true){
                System.out.print("Please enter outbound departure time: ");
                str_outDepartureTime = input.nextLine();
                if (!str_outDepartureTime.isEmpty() && str_outDepartureTime.matches("([01]\\d|2[0-3])[.][0-5]\\d")) {
                    outDepartureTime = Double.parseDouble(str_outDepartureTime);
                    break;
                } else {
                    System.out.println("Invalid option");
                }
            }

            while (true){
                System.out.print("Please enter outbound arrival time: ");
                str_outArrivalTime = input.nextLine();
                if (!str_outArrivalTime.isEmpty() && str_outArrivalTime.matches("([01]\\d|2[0-3])[.][0-5]\\d")) {
                   outArrivalTime = Double.parseDouble(str_outArrivalTime);
                   break;
                } else {
                    System.out.println("Invalid option");
                }
            }

            itineraryBean.setInboundFlightDepartureTime(inDepartureTime);
            itineraryBean.setInboundFlightArrivalTime(inArrivalTime);
            itineraryBean.setOutboundFlightDepartureTime(outDepartureTime);
            itineraryBean.setOutboundFlightArrivalTime(outArrivalTime);

        } else if (choice != 2){
            throw new RuntimeException("Invalid choice");
        }

        new CreateItineraryController().createItinerary(itineraryBean,sessionId);
    }

    public String getCity() {
        String city;
        List<CityBean> cities;

        Scanner input = new Scanner(System.in);
        while (true){
            System.out.print("Please enter City name: ");
            city = input.nextLine();
            if (!city.isEmpty()) {
                break;
            }
            System.out.println("Invalid option");
        }

        cities = new CreateItineraryController().getCities(city);

        if (!cities.isEmpty()) {
            System.out.println(">Cities found");
            int count = 1;
            for (CityBean cityResult : cities) {
                System.out.println("    " + count + ">" + cityResult.getName() + " - " + cityResult.getCountry());
                if (count == 5 || cities.size() == count) {
                    break;
                }
                count++;
            }

            while (true){
                System.out.println("-> Which one do you choose (insert number)?\n");

                String str_choice;
                int choice = 0;
                while (true) {
                    System.out.print("Please enter your choice: ");
                    str_choice = input.nextLine();
                    if (!str_choice.isEmpty() && str_choice.matches("[1-" + count + "]")) {
                        choice = Integer.parseInt(str_choice);
                        break;
                    }
                    System.out.println("Invalid option");
                }

                return cities.get(choice-1).getId();
            }
        } else {
            System.out.println(">Error: no place");
            return null;
        }
    }

    public String getAttraction() {
        String attraction;
        List<AttractionBean> attractions;

        Scanner input = new Scanner(System.in);
        while (true){
            System.out.print("Please enter Attraction name: ");
            attraction = input.nextLine();
            if (!attraction.isEmpty()) {
                break;
            }
            System.out.println("Invalid option");
        }

        attractions = new CreateItineraryController().getAttractions(attraction);

        if (!attractions.isEmpty()) {
            System.out.println(">Attractions found");
            int count = 1;
            for (AttractionBean attractionResult : attractions) {
                System.out.println("    " + count + ">" + attractionResult.getName() + " - " + attractionResult.getCity());
                if (count == 5) {
                    break;
                }
                count++;
            }

            while (true){
                System.out.println("-> Which one do you choose (insert number)?\n");

                String str_choice;
                int choice = 0;
                while (true) {
                    System.out.print("Please enter your choice: ");
                    str_choice = input.nextLine();
                    if (!str_choice.isEmpty() && str_choice.matches("[1-" + count + "]")) {
                        choice = Integer.parseInt(str_choice);
                        break;
                    }
                    System.out.println("Invalid option");
                }

                return attractions.get(choice-1).getId();
            }
        } else {
            System.out.println(">Error: no place");
            return null;
        }
    }

    public Pair<String,String> getAccommodation(){
        String accommodation,address;
        Scanner input = new Scanner(System.in);

        while (true){
            System.out.print("Please enter Accommodation Name: ");
            accommodation = input.nextLine();
            if (!accommodation.isEmpty()) {
                break;
            }
            System.out.println("Invalid option");
        }

        while (true){
            System.out.print("Please enter Accommodation Address: ");
            address = input.nextLine();
            if (!address.isEmpty()) {
                break;
            }
            System.out.println("Invalid option");
        }

        return new Pair<>(accommodation,address);
    }

    public void showItinerary() {
        ItineraryBean itinerary = new CreateItineraryController().getItinerary(null,sessionId);
        List<StepBean> steps = new CreateItineraryController().getSteps(null,sessionId);

        System.out.println("> itinerary name: " + itinerary.getName());
        System.out.println("    > description: " + itinerary.getDescription());
        System.out.println("    > days: "+itinerary.getDuration());

        int day = 1;
        for (StepBean step : steps) {
            CityBean city = new CreateItineraryController().getCity(step.getNum(),step.getCity().get(0),sessionId);

            System.out.println("        > day: " + day);
            System.out.println("            > City: " + city.getName() + ", " + city.getCountry());
            System.out.println("            > Attraction: ");

            for (String attractionID : step.getAttractions()) {
                AttractionBean attraction;
                attraction = new CreateItineraryController().getAttraction(step.getNum(),attractionID,sessionId);
                System.out.println("                - " + attraction.getName() +", " + attraction.getCity());
            }
            day++;
        }

        if (!itinerary.getAccommodations().isEmpty()) {
            System.out.println("    > Accommodations: ");

            for (int i = 0; i < itinerary.getAccommodations().size(); i++) {
                Pair<String,String> accommodation = itinerary.getAccommodations().get(i);
                System.out.println("            - " + accommodation.getKey() + ", " + accommodation.getValue());
            }
        }

        if (itinerary.getInboundFlightDepartureTime() != -1) {
            System.out.println("    > Inbound Flight: " + itinerary.getInboundFlightDepartureTime() + " - " + itinerary.getInboundFlightArrivalTime());
            System.out.println("    > Outbound Flight:" + itinerary.getOutboundFlightDepartureTime()+ " - " + itinerary.getOutboundFlightArrivalTime());
        }

        String response;

        Scanner input = new Scanner(System.in);
        while (true){
            System.out.println("\nDo you want to save itinerary?");

            response = input.nextLine();
            if (response.equalsIgnoreCase(YES) || response.equalsIgnoreCase(NO)) {
                break;
            }
            System.out.println("Invalid option");
        }

        if (response.equalsIgnoreCase(YES)) {
            new CreateItineraryController().saveItinerary(sessionId);
        }
        ManageItineraryCLIController controller = new ManageItineraryCLIController(sessionId);
        controller.start();
    }
}
