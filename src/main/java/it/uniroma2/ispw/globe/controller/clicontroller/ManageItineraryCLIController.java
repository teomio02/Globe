package it.uniroma2.ispw.globe.controller.clicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.ManageItineraryController;
import it.uniroma2.ispw.globe.controller.guicontroller.CreateItineraryGUIController;
import it.uniroma2.ispw.globe.model.bean.ItineraryBean;
import it.uniroma2.ispw.globe.model.bean.ProposalBean;

import java.util.List;
import java.util.Scanner;

public class ManageItineraryCLIController {
    private String sessionId;

    public ManageItineraryCLIController(String sessionId) {
        this.sessionId = sessionId;
    }


    public void start() {
        while(true) {
            int choice;
            choice = showMenu();

            switch(choice) {
                case 1 -> showItineraries();
                case 2 -> showProposals();
                case 3 -> showRequests();
                case 4 -> createItinerary();
                case 5 -> System.exit(0);
                default -> throw new RuntimeException("Invalid choice");
            }
        }
    }

    public int showMenu() {
        System.out.println("*********************************");
        System.out.println("*    Manage Itinerary    *");
        System.out.println("*********************************\n");
        System.out.println("-> What do you want do?\n");
        System.out.println("1> Show Itineraries");
        System.out.println("2> Show Proposals");
        System.out.println("3> Show Requests");
        System.out.println("4> Create new Itinerary");
        System.out.println("5> Quit");

        Scanner input = new Scanner(System.in);
        String str_choice;
        int choice = 0;
        while (true) {
            System.out.print("Please enter your choice: ");
            str_choice = input.nextLine();
            if (!str_choice.isEmpty() && str_choice.matches("[1-5]")) {
                System.out.println("*********************************");
                choice = Integer.parseInt(str_choice);
                break;
            }
            System.out.println("Invalid option");
        }

        return choice;
    }

    public void showItineraries() {
        List<ItineraryBean> itineraries = new ManageItineraryController().getUserItineraries(sessionId);
        System.out.println("Itineraries:");
        for(ItineraryBean itinerary : itineraries) {
            System.out.println("> ID: "+itinerary.getId());
            System.out.println("    > Name: "+itinerary.getName());
            System.out.println("    > Days: "+itinerary.getDuration());
            System.out.println("    > Description: "+itinerary.getDescription());
        }
    }

    public void showProposals() {
        List<ProposalBean> proposals = new ManageItineraryController().getUserProposals(sessionId);
        System.out.println("Proposals:");
        for(ProposalBean proposal : proposals) {
            System.out.println("> ID: "+proposal.getID());
            System.out.println("    > Agency: "+proposal.getAgency());
            System.out.println("    > Price: "+proposal.getPrice());
            System.out.println("    > Description: "+proposal.getDescription());
        }
    }

    public void showRequests() {

    }

    public void createItinerary() {
        CreateItineraryCLIController controller = new CreateItineraryCLIController(sessionId,null);
        controller.start();
    }
}
