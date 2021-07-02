package com.techelevator.view;


import com.techelevator.tenmo.model.TransferDTO;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class ConsoleService {

    private PrintWriter out;
    private Scanner in;

    public ConsoleService(InputStream input, OutputStream output) {
        this.out = new PrintWriter(output, true);
        this.in = new Scanner(input);
    }

    public Object getChoiceFromOptions(Object[] options) {
        Object choice = null;
        while (choice == null) {
            displayMenuOptions(options);
            choice = getChoiceFromUserInput(options);
        }
        out.println();
        return choice;
    }

    private Object getChoiceFromUserInput(Object[] options) {
        Object choice = null;
        String userInput = in.nextLine();
        try {
            int selectedOption = Integer.valueOf(userInput);
            if (selectedOption > 0 && selectedOption <= options.length) {
                choice = options[selectedOption - 1];
            }
        } catch (NumberFormatException e) {
            // eat the exception, an error message will be displayed below since choice will be null
        }
        if (choice == null) {
            out.println(System.lineSeparator() + "*** " + userInput + " is not a valid option ***" + System.lineSeparator());
        }
        return choice;
    }

    private void displayMenuOptions(Object[] options) {
        out.println();
        for (int i = 0; i < options.length; i++) {
            int optionNum = i + 1;
            out.println(optionNum + ") " + options[i]);
        }
        out.print(System.lineSeparator() + "Please choose an option >>> ");
        out.flush();
    }

    public String getUserInput(String prompt) {
        out.print(prompt + ": ");
        out.flush();
        return in.nextLine();
    }

    public Integer getUserInputInteger(String prompt) {
        Integer result = null;
        do {
            out.print(prompt + ": ");
            out.flush();
            String userInput = in.nextLine();
            try {
                result = Integer.parseInt(userInput);
            } catch (NumberFormatException e) {
                out.println(System.lineSeparator() + "*** " + userInput + " is not valid ***" + System.lineSeparator());
            }
        } while (result == null);
        return result;
    }

    public void printUserBalance(BigDecimal balance) {
        System.out.println("Your current balance is: " + balance);
    }

    public void printListOfUsers(String[] list) {
        System.out.println("Here are the available users: ");
        for (String transfer : list) {
            System.out.println(transfer);
            System.out.println("----------------------");
        }
    }

    public void printListOfTransfers(TransferDTO[] list) {
        System.out.println("Here is your transfer history: ");
        for (TransferDTO transfer : list) {
            if (transfer.getTransferStatusId() != 1) {
                System.out.println("Transfer Type: " + transfer.getTransferTypeId());
                if (transfer.getTransferStatusId()==2) {
					System.out.println("Transfer Status: Approved");
				} else if (transfer.getTransferStatusId()==3) {
					System.out.println("Transfer Status: Rejected");
				}
                System.out.println("Transfer From: " + transfer.getAccountFrom());
                System.out.println("Transfer To: " + transfer.getAccountTo());
                System.out.println("Transfer Amount: " + transfer.getAmount());
                System.out.println("----------------------");
            }
        }
    }

    public void printListOfPendingRequests(TransferDTO[] list) {
        System.out.println("Here are your pending requests: ");
        for (TransferDTO transfer : list) {
            if (transfer.getTransferStatusId() == 1 && transfer.getTransferTypeId() == 1) {
                System.out.println("Transfer Type: Request");
                System.out.println("Transfer Status: Pending");
                System.out.println("Transfer From: " + transfer.getAccountFrom());
                System.out.println("Transfer To: " + transfer.getAccountTo());
                System.out.println("Transfer Amount: " + transfer.getAmount());
                System.out.println("----------------------");
            }
        }
    }
}
