package com.DataBase;

import java.io.IOException;
import java.util.Scanner;

public class DBInterface {
    private final Scanner scanner;
    private final String path;
    private DBControlSystem dbControlSystem;

    public DBInterface(Scanner scanner, String path) {
        this.scanner = scanner;
        this.path = path;
    }

    public void startUp () {
        try {
            dbControlSystem = new DBControlSystem(path);
        } catch (IOException e) {
            System.err.println("Oops, something is wrong: ");
            System.out.println(e.getMessage());
        }
        String command;
        System.out.println("Welcome!");
        while(true) {
            showInitialMenu();
            command = scanner.next();
            switch (command) {
                case "1":
                    showAddMenu();
                    break;
                case "2":
                    showDeleteMenu();
                    break;
                case "3":
                    showModifyMenu();
                    break;
                case "4":
                    showSearchMenu();
                    break;
                case "q":
                    break;
                default:
                    System.out.println("Looks like you've entered a wrong command.");
                    System.out.println("Try again");
            }
            if(command.equals("q")) {
                System.out.println("Bye!");
                break;
            }
        }
    }

    private void showInitialMenu() {
        System.out.println("Please enter one of the following commands:");
        System.out.println("1 for adding");
        System.out.println("2 for deleting");
        System.out.println("3 for modifying");
        System.out.println("4 for searching");
        System.out.println("q to quit");
    }

    private void showAddMenu() {
            System.out.println("Input what you want to add: ");
            try {
                dbControlSystem.addNewRecord(scanner.next());
            } catch (IOException e) {
                System.err.println("Oops, something is wrong: ");
                System.out.println(e.getMessage());
            }
    }
    private void showDeleteMenu() {
        System.out.println("Input the key of the element that you want to delete (q to exit): ");
        try {
            String input= scanner.next();
            if(input.equals("q")) return;
            dbControlSystem.deleteRecord(input);
            System.out.println("Delete successful");
        } catch (IOException e) {
            System.err.println("Oops, something is wrong: ");
            System.out.println(e.getMessage());
        } catch (StackOverflowError er) {
            System.err.println("There is no record containing the entered key");
            showDeleteMenu();
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Invalid key or no record with such key");
            showDeleteMenu();
        }
    }
    private void showModifyMenu() {
        System.out.println("Input the key of the element that you want to edit (q to exit): ");
        try {
            String input= scanner.next();
            if(input.equals("q")) return;
            System.out.println(dbControlSystem.findRecord(input));
            System.out.println("Enter the new value:");
            dbControlSystem.editRecord(input, scanner.next());
        } catch (IOException e) {
            System.err.println("Oops, something is wrong: ");
            System.out.println(e.getMessage());
        } catch (StackOverflowError er) {
            System.err.println("There is no record containing the entered key");
            showDeleteMenu();
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Invalid key or no record with such key");
            showDeleteMenu();
        }
    }
    private void showSearchMenu() {
        System.out.println("Input the key of the element that you want to delete (q to exit): ");
        try {
            String input= scanner.next();
            if(input.equals("q")) return;
            System.out.println(dbControlSystem.findRecord(input));
        } catch (IOException e) {
            System.err.println("Oops, something is wrong: ");
            System.out.println(e.getMessage());
        } catch (StackOverflowError er) {
            System.err.println("There is no record containing the entered key");
            showDeleteMenu();
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Invalid key or no record with such key");
            showDeleteMenu();
        }
    }
}
