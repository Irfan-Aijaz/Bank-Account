package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.AuthenticationServiceException;
import com.techelevator.view.ConsoleService;

import java.math.BigDecimal;
import java.util.Scanner;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private static final String MENU_OPTION_EXIT = "Exit";
    private static final String LOGIN_MENU_OPTION_REGISTER = "Register";
    private static final String LOGIN_MENU_OPTION_LOGIN = "Login";
    private static final String[] LOGIN_MENU_OPTIONS = {LOGIN_MENU_OPTION_REGISTER, LOGIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT};
    private static final String MAIN_MENU_OPTION_VIEW_BALANCE = "View your current balance";
    private static final String MAIN_MENU_OPTION_SEND_BUCKS = "Send TE bucks";
    private static final String MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS = "View your past transfers";
    private static final String MAIN_MENU_OPTION_REQUEST_BUCKS = "Request TE bucks";
    private static final String MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS = "View your pending requests";
    private static final String MAIN_MENU_OPTION_LOGIN = "Login as different user";
    private static final String[] MAIN_MENU_OPTIONS = {MAIN_MENU_OPTION_VIEW_BALANCE, MAIN_MENU_OPTION_SEND_BUCKS, MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS, MAIN_MENU_OPTION_REQUEST_BUCKS, MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS, MAIN_MENU_OPTION_LOGIN, MENU_OPTION_EXIT};

    private AuthenticatedUser currentUser;
    private ConsoleService console;
    private AuthenticationService authenticationService;
    private AccountService accountService;
    private Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        App app = new App(new ConsoleService(System.in, System.out), new AuthenticationService(API_BASE_URL), new AccountService(API_BASE_URL));
        app.run();
    }

    public App(ConsoleService console, AuthenticationService authenticationService, AccountService accountService) {
       //where we declare account svc, console svc, auth svc
        this.console = console;
        this.authenticationService = authenticationService;
        this.accountService = accountService;
    }

    public void run() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");

        registerAndLogin();
        mainMenu();
    }

    private void mainMenu() {
        while (true) {
            String choice = (String) console.getChoiceFromOptions(MAIN_MENU_OPTIONS);
            if (MAIN_MENU_OPTION_VIEW_BALANCE.equals(choice)) {
                viewCurrentBalance();
            } else if (MAIN_MENU_OPTION_SEND_BUCKS.equals(choice)) {
                sendBucks();
            } else if (MAIN_MENU_OPTION_VIEW_PAST_TRANSFERS.equals(choice)) {
                viewTransferHistory();
            } else if (MAIN_MENU_OPTION_REQUEST_BUCKS.equals(choice)) {
                requestBucks();
            } else if (MAIN_MENU_OPTION_VIEW_PENDING_REQUESTS.equals(choice)) {
                viewPendingRequests();
            } else if (MAIN_MENU_OPTION_LOGIN.equals(choice)) {
                login();
            } else {
                // the only other option on the main menu is to exit
                exitProgram();
            }
        }
    }


    private void viewCurrentBalance() {
        // TODO Auto-generated method stub
        // use token login info to view balance
        console.printUserBalance(accountService.getBalance(currentUser.getToken()));

    }

    private void sendBucks() {
        // TODO Auto-generated method stub
        // use token login to view Users to transfer to and amount
        console.printListOfUsers(accountService.getUsers(currentUser.getToken()));
        System.out.println("Select the user ID you would like to transfer to: ");
        int selectedId = Integer.parseInt(scanner.nextLine());
        System.out.println("How much would you like to transfer? ");
        BigDecimal amountToTransfer = new BigDecimal(scanner.nextLine());
        console.createSendTransfer(accountService.createTransfer(currentUser.getToken(), 2, 2, currentUser.getUser().getId(), selectedId, amountToTransfer));

    }

    private void viewTransferHistory() {
        // TODO Auto-generated method stub
        // use token login to view previous transfers
        console.printListOfTransfers(accountService.getTransfers(currentUser.getToken()));

    }

    private void requestBucks() {
        // TODO Auto-generated method stub
        // use token login to view who you can request from and amount
        console.printListOfUsers(accountService.getUsers(currentUser.getToken()));
        System.out.println("Select the user ID you would like to request from: ");
        int selectedId = Integer.parseInt(scanner.nextLine());
        System.out.println("How much would you like to request? ");
        BigDecimal amountToTransfer = new BigDecimal(scanner.nextLine());
        console.createSendTransfer(accountService.createTransfer(currentUser.getToken(), 1, 1, selectedId, currentUser.getUser().getId(), amountToTransfer));

    }

    private void viewPendingRequests() {
        // TODO Auto-generated method stub
        // use token login to see and accept / reject pending requests
        int transferRequestOption = console.printListOfPendingRequests(accountService.getTransfers(currentUser.getToken()), currentUser.getUser().getUsername());
        if (transferRequestOption > 0) {
            System.out.println("If you would like to approve the request, type 2. \n" +
                    "To reject the request, type 3. To cancel, type 0. ");
            int transferStatus = Integer.parseInt(scanner.nextLine());
            if (transferStatus == 2) {
                console.updateTransfer(accountService.updatePendingTransfer(currentUser.getToken(), transferRequestOption, transferStatus));
                System.out.println("Request accepted. Transfer complete. ");
            } else if (transferStatus == 3) {
                console.updateTransfer(accountService.updatePendingTransfer(currentUser.getToken(), transferRequestOption, transferStatus));
                System.out.println("Request rejected. ");
            } else {
                System.out.println("Canceled.");
            }
        } else if (transferRequestOption == 0) {
            System.out.println("Canceled.");
        } else {
            System.out.println("No pending requests.");
        }
    }

    private void exitProgram() {
        System.exit(0);
    }


    private void registerAndLogin() {
        while (!isAuthenticated()) {
            String choice = (String) console.getChoiceFromOptions(LOGIN_MENU_OPTIONS);
            if (LOGIN_MENU_OPTION_LOGIN.equals(choice)) {
                login();
            } else if (LOGIN_MENU_OPTION_REGISTER.equals(choice)) {
                register();
            } else {
                // the only other option on the login menu is to exit
                exitProgram();
            }
        }
    }

    private boolean isAuthenticated() {
        return currentUser != null;
    }

    private void register() {
        System.out.println("Please register a new user account");
        boolean isRegistered = false;
        while (!isRegistered) //will keep looping until user is registered
        {
            UserCredentials credentials = collectUserCredentials();
            try {
                authenticationService.register(credentials);
                isRegistered = true;
                System.out.println("Registration successful. You can now login.");
            } catch (AuthenticationServiceException e) {
                System.out.println("REGISTRATION ERROR: " + e.getMessage());
                System.out.println("Please attempt to register again.");
            }
        }
    }

    private void login() {
        System.out.println("Please log in");
        currentUser = null;
        while (currentUser == null) //will keep looping until user is logged in
        {
            UserCredentials credentials = collectUserCredentials();
            try {
                currentUser = authenticationService.login(credentials);
            } catch (AuthenticationServiceException e) {
                System.out.println("LOGIN ERROR: " + e.getMessage());
                System.out.println("Please attempt to login again.");
            }
        }
    }

    private UserCredentials collectUserCredentials() {
        String username = console.getUserInput("Username");
        String password = console.getUserInput("Password");
        return new UserCredentials(username, password);
    }
}
