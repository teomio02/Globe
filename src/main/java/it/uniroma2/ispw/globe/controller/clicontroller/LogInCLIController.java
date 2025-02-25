package it.uniroma2.ispw.globe.controller.clicontroller;

import it.uniroma2.ispw.globe.controller.applicationcontroller.LogInController;
import it.uniroma2.ispw.globe.model.bean.CredentialsBean;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

import static it.uniroma2.ispw.globe.other.ItineraryType.*;
import static it.uniroma2.ispw.globe.other.UserType.*;

public class LogInCLIController {

    public void start() {
        while(true) {
            int choice;
            choice = showMenu();

            switch(choice) {
                case 1 -> logIn();
                case 2 -> signIn();
                case 3 -> logInAsGuest();
                case 4 -> System.exit(0);
                default -> throw new RuntimeException("Invalid choice");
            }
        }
    }

    public int showMenu() {
        System.out.println("Welcome, what do you want do?\n");
        System.out.println("1 -> Log In");
        System.out.println("2 -> Sign In");
        System.out.println("3 -> Log In as Guest ");
        System.out.println("4 -> Quit");

        Scanner input = new Scanner(System.in);
        String str_choice;
        int choice = 0;
        while (true) {
            System.out.print("Please enter your choice: ");
            str_choice = input.nextLine();
            if (!str_choice.isEmpty() && str_choice.matches("[1-4]")) {
                System.out.println("*********************************");
                choice = Integer.parseInt(str_choice);
                break;
            }
            System.out.println("Invalid option");
        }

        return choice;
    }

    public void signIn() {
        System.out.println("# SIGN IN #\n");
        System.out.println("Who are you? User or Agency?");

        Scanner input = new Scanner(System.in);
        String response;
        while (true) {
            response = input.nextLine();
            if (response.equalsIgnoreCase(USER) || response.equalsIgnoreCase(AGENCY)) {
                break;
            }
            System.out.println("Invalid option");
        }
        System.out.println();
        if (response.equalsIgnoreCase(USER)) {
            userSignIn();
        }
        else if (response.equalsIgnoreCase(AGENCY)) {
            agencySignIn();
        }
    }

    public void userSignIn() {
        String username,password;

        Scanner input = new Scanner(System.in);

        while (true){
            System.out.print("Please enter Username: ");
            username = input.nextLine();
            if (!username.isEmpty()) {
                break;
            }
            System.out.println("Invalid option");
        }

        while (true){
            System.out.print("Please enter Password: ");
            password = input.nextLine();
            if (!password.isEmpty()) {
                break;
            }
            System.out.println("Invalid option");
        }

        CredentialsBean credentials = new CredentialsBean(username, password,USER);

        if (new LogInController().signIn(credentials)) {
            showMenu();
        } else {
            // errore
        }
    }

    public void agencySignIn() {
        String username,password,description,paymentCredentials;
        List<String> preferences = new ArrayList<>();

        Scanner input = new Scanner(System.in);
        System.out.println("<SignIn - AGENCY> operation:\n");

        while (true){
            System.out.print("Please enter Agency Name: ");
            username = input.nextLine();
            if (!username.isEmpty()) {
                break;
            }
            System.out.println("Invalid option");
        }

        while (true){
            System.out.print("Please enter Password: ");
            password = input.nextLine();
            if (!password.isEmpty()) {
                break;
            }
            System.out.println("Invalid option");
        }

        CredentialsBean credentials = new CredentialsBean(username, password,AGENCY);


        System.out.println("-> Select tour preferences? (select stop to terminate)\n");
        System.out.println("1> "+ON_THE_ROAD);
        System.out.println("2> "+NATURE);
        System.out.println("3> "+CULTURE);
        System.out.println("4> "+RELAX);
        System.out.println("5> "+CITY);
        System.out.println("6> stop");

        String str_choice;
        int choice = 0;
        boolean stop = false;
        while (!stop) {
            while (true) {
                System.out.print("Please enter your choice: ");
                str_choice = input.nextLine();
                if (!str_choice.isEmpty() && str_choice.matches("[1-6]")) {
                    System.out.println("*********************************");
                    choice = Integer.parseInt(str_choice);
                    break;
                }
                System.out.println("Invalid option");
            }
            switch(choice) {
                case 1 -> preferences.add(ON_THE_ROAD);
                case 2 -> preferences.add(NATURE);
                case 3 -> preferences.add(CULTURE);
                case 4 -> preferences.add(RELAX);
                case 5 -> preferences.add(CITY);
                case 6 -> stop = true;
                default -> throw new RuntimeException("Invalid choice");
            }
        }
        credentials.setPreferences(preferences);

        while (true){
            System.out.print("Please enter a description for your agency: ");
            description = input.nextLine();
            if (!username.isEmpty()) {
                break;
            }
            System.out.println("Invalid option");
        }
        credentials.setDescription(description);

        while (true){
            System.out.print("Please enter payment credentials: ");
            paymentCredentials = input.nextLine();
            if (!username.isEmpty()) {
                break;
            }
            System.out.println("Invalid option");
        }
        credentials.setPaymentCredentials(paymentCredentials);

        if (new LogInController().signIn(credentials)) {
            showMenu();
        } else {
            // errore
        }
    }

    public void logIn() {

        String username,password;

        Scanner input = new Scanner(System.in);
        System.out.println("LOG IN\n");

        while (true){
            System.out.print("Please enter Username: ");
            username = input.nextLine();
            if (!username.isEmpty()) {
                break;
            }
            System.out.println("Invalid option");
        }

        while (true){
            System.out.print("Please enter Password: ");
            password = input.nextLine();
            if (!password.isEmpty()) {
                break;
            }
            System.out.println("Invalid option");
        }


        CredentialsBean credentials = new CredentialsBean(username, password);

        String sessionId = new LogInController().logIn(credentials);

        if (sessionId != null) {
            String type = new LogInController().getUserType(sessionId);
            System.out.println("*********************************");
            System.out.println("> Benvenuto "+ username);
            System.out.println("*********************************");

            if (type.equals(USER)) {
                ManageItineraryCLIController controller = new ManageItineraryCLIController(sessionId);
                controller.start();
            } else {
                ManageRequestCLIController controller = new ManageRequestCLIController(sessionId);
                controller.start();
            }
        } else {
            System.out.println("You are not logged in");
            showMenu();
        }
    }

    public void logInAsGuest() {

        CredentialsBean credentials = new CredentialsBean(UUID.randomUUID().toString().substring(0,12), "",GUEST);

        String sessionId = new LogInController().logIn(credentials);

        if (sessionId != null) {
            ManageItineraryCLIController controller = new ManageItineraryCLIController(sessionId);
            controller.start();
        } else {
            System.out.println("error");
            showMenu();
        }
    }
}
