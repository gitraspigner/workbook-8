package com.pluralsight.Exercise7NorthwindDAO;
import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
/**
 * A command line application that performs basic searches on the Northwind (products) database
 * for its shippers specifically.
 * This program is capable of displaying all shippers (shipper id, company name, and phone
 * number), adding a new shipper (given a company name and phone number), deleting an existing
 * shipper (given its shipper id number), and changing an existing shipper's phone number (given
 * its shipper id and the new phone number it will be changed to).
 * Uses class ShipperDAO.java to handle querying and returning information.
 * @author Ravi Spigner
 */
public class NorthwindApplication {
    private static Scanner scanner;
    public static void main(String[] args) throws SQLException {
        //check login info exists
        if (args.length != 2) {
            System.out.println(
                    "Application needs two arguments to run: " +
                            "<username> <password>");
            System.exit(1);
        }
        //A database URL has the following format:
        //jdbc:mysql://[host][:port]/[databaseName]
        //1. Open datasource (DataManager handles opening/closing the connection)
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/northwind");
        dataSource.setUsername(args[0]);
        dataSource.setPassword(args[1]);
        ShipperDAO shipperDAO = new ShipperDAO(dataSource);
        menu(shipperDAO);
    }
    public static void menu(ShipperDAO shipperDAO) throws SQLException {
        displayWelcome();
        String input;
        scanner = new Scanner(System.in);
        while (true) {
            System.out.print("""
                    ------------Main Menu------------
                    What do you want to do?
                    1) Display all shippers
                    2) Add new shipper
                    3) Delete an existing shipper
                    4) Change a shipper's phone number
                    0) Exit
                    Select an option:\s""");
            input = scanner.nextLine().trim();
            if (!isNumber(input)) {
                errorMessageNumber(input, true);
                continue;
            }
            if (input.equals("1")) {
                List<NorthwindShipper> shippers = shipperDAO.getAllShippers();
                shippers.forEach(System.out::println);
            } else if (input.equals("2")) {
                String companyName;
                String phone = "";
                while (true) {
                    System.out.print("Please enter the company name for which you\n" +
                            "would like to add a new shipper for: ");
                    companyName = scanner.nextLine().trim();
                    if (!isNumber(input)) {
                        errorMessageNumber(input, true);
                        break;
                    }
                    System.out.print("Please enter the phone number for which you\n" +
                            "would like to add a new shipper for: ");
                    phone = scanner.nextLine().trim();
                    //TODO: add error checking for a phone number formatted: (###) ###-####
                    break;
                }
                NorthwindShipper newShipper = new NorthwindShipper(companyName, phone);
                newShipper = shipperDAO.add(newShipper);
                if (newShipper != null) {
                    System.out.println("------New Shipper Added!------");
                    System.out.println(newShipper);
                } else {
                    errorMessage("Invalid Input,", "No Shipper Added.");
                }
            } else if (input.equals("3")) {
                System.out.print("Please enter the shipper id if the shipper you would like " +
                        "to delete: ");
                input = scanner.nextLine().trim();
                if (!isNumber(input)) {
                    errorMessageNumber(input, true);
                    continue;
                }
                NorthwindShipper deletedShipper = shipperDAO.delete(Integer.parseInt(input));
                if (deletedShipper != null) {
                    System.out.println("------Shipper Deleted!------");
                    System.out.println(deletedShipper);
                } else {
                    errorMessage("Invalid Input,", "No Shipper Deleted.");
                }
            } else if (input.equals("4")) {
                System.out.print("Please enter the shipper id of the shipper you would like\n" +
                        "to change the phone number for: ");
                input = scanner.nextLine().trim();
                if (!isNumber(input)) {
                    errorMessageNumber(input, true);
                    continue;
                }
                int shipperID = Integer.parseInt(input);
                NorthwindShipper shipper = shipperDAO.getShipper(shipperID);
                if (shipper == null) {
                    errorMessage("Invalid Input,", "No Shipper found with that ID.");
                    continue;
                }
                System.out.print("Please enter the new phone number: ");
                String newPhoneNumber = scanner.nextLine().trim();
                NorthwindShipper updatedShipper = shipperDAO.updatePhoneNumber(shipper, newPhoneNumber);
                if (updatedShipper != null) {
                    System.out.println("------Shipper Phone Number Updated!------");
                    System.out.println(updatedShipper);
                } else {
                    errorMessage("Invalid Input,", "No Shipper Phone Number Updated.");
                }
            } else if (input.equals("0")) {
                displayGoodbye();
                break;
            } else {
                errorMessage(input, "Is An Invalid Menu Option. Only 1,2,3, 4, or 0 is " +
                        "acceptable for your menu input");
            }
        }
    }
    public static boolean isNumber(String input) {
        try {
            Double.parseDouble(input); //will return true for doubles/decimals and ints
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static void errorMessageNumber(String input, boolean wantedNumber) {
        if (wantedNumber) {
            errorMessage(input, " should be a number, not a word");
        } else {
            errorMessage(input, " should be a word, not a number");
        }
    }
    public static void errorMessage(String input, String errorMessage) {
        System.out.println("----------------------------------------");
        System.out.println("ERROR: " + input + " " + errorMessage);
        System.out.println("----------------------------------------");
    }
    public static void displayWelcome() {
        System.out.println("--------------------------------------");
        System.out.println("Welcome To The Northwind Products Database");
        System.out.println("--------------------------------------");
    }
    public static void displayGoodbye() {
        System.out.println("-----------------------------");
        System.out.println("Thank you! See you next time!");
        System.out.println("-----------------------------");
    }
}
