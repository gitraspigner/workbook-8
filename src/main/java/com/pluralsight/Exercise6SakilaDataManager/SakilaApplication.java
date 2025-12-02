package com.pluralsight.Exercise6SakilaDataManager;
import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
/**
 * A command line application that performs basic searches on the Sakila (Movies) database
 * in order to retrieve all actors matching a last name, or all movies (id, title,
 * release year, and length (in minutes)) matching an actor's id.
 * Uses class SakilaDataManager.java to handle querying and returning information.
 *
 * @author Ravi Spigner
 */
public class SakilaApplication {
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
        dataSource.setUrl("jdbc:mysql://localhost:3306/sakila");
        dataSource.setUsername(args[0]);
        dataSource.setPassword(args[1]);
        SakilaDataManager dataManager = new SakilaDataManager(dataSource);
        menu(dataManager);
    }
    public static void menu(SakilaDataManager dataManager) throws SQLException {
        displayWelcome();
        String input;
        scanner = new Scanner(System.in);
        while (true) {
            System.out.print("""
                    ------------Main Menu------------
                    What do you want to do?
                    1) Display all actors with matching last name
                    2) Display all movies starring actor matching actor id
                    0) Exit
                    Select an option:\s""");
            input = scanner.nextLine().trim();
            if (!isNumber(input)) {
                errorMessageNumber(input, true);
                continue;
            }
            if (input.equals("1")) {
                System.out.print("Please enter the actor last name for which you\n" +
                        "would like to display all actors matching it: ");
                input = scanner.nextLine().trim();
                List<SakilaActor> actors = dataManager.getActorsByLastName(input);
                actors.forEach(System.out::println);
            } else if (input.equals("2")) {
                System.out.print("Please enter the actor id for which you\n" +
                        "would like to display all movies starring that actor: ");
                if (!isNumber(input)) {
                    errorMessageNumber(input, true);
                    continue;
                }
                input = scanner.nextLine().trim();
                List<SakilaFilm> films = dataManager.getFilmsByActorId(Integer.parseInt(input));
                films.forEach(System.out::println);
            } else if (input.equals("0")) {
                displayGoodbye();
                break;
            } else {
                errorMessage(input, "Is An Invalid Menu Option. Only 1,2, or 0 is " +
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
        System.out.println("Welcome To The Sakila Movies Database");
        System.out.println("--------------------------------------");
    }
    public static void displayGoodbye() {
        System.out.println("-----------------------------");
        System.out.println("Thank you! See you next time!");
        System.out.println("-----------------------------");
    }
}
