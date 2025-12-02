package com.pluralsight;
import org.apache.commons.dbcp2.BasicDataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
/**
 * A command line application that performs basic searches on the Sakila (Movies) database
 * in order to retrieve all actors matching a last name, or all movies (title & release year)
 * matching an actor's first and last name.
 *
 * @author Ravi Spigner
 */
public class Exercise5 {
    private static Connection connection;
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
        //1. Open connection
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/sakila");
        dataSource.setUsername(args[0]);
        dataSource.setPassword(args[1]);
        connection = dataSource.getConnection();
        menu();
        //4. Close Connection
        connection.close();
    }
    public static void menu() throws SQLException {
        displayWelcome();
        String input;
        scanner = new Scanner(System.in);
        //TODO: maybe redesign each method to use try with resources block
        while (true) {
            System.out.print("""
                    ------------Main Menu------------
                    What do you want to do?
                    1) Display all actors with matching last name
                    2) Display all movies starring actor matching first and last name
                    0) Exit
                    Select an option:\s""");
            input = scanner.nextLine().trim();
            if (!isNumber(input)) {
                errorMessageNumber(input, true);
                continue;
            }
            if (input.equals("1")) {
                displayAllActorsMatchingLastName();
            } else if (input.equals("2")) {
                displayAllMoviesMatchingFirstAndLastActorName();
            } else if (input.equals("0")) {
                displayGoodbye();
                break;
            } else {
                errorMessage(input, "Is An Invalid Menu Option. Only 1,2, or 0 is " +
                        "acceptable for your menu input");
            }
        }
    }
    public static void displayAllMoviesMatchingFirstAndLastActorName() throws SQLException {
        //Ask User for last name to display all actors who match it
        String firstNameInput;
        String lastNameInput;
        while (true) {
            System.out.println("-----Display all movies starring an actor-----");
            System.out.print("Please enter the first name for which you\n" +
                    "would like to display all movies starring that actor: ");
            firstNameInput = scanner.nextLine().trim();
            System.out.print("Please enter the last name for which you\n" +
                    "would like to display all movies starring that actor: ");
            lastNameInput = scanner.nextLine().trim();
            if (isNumber(firstNameInput)) {
                errorMessageNumber(firstNameInput, false);
            }  else if (isNumber(lastNameInput)) {
                errorMessageNumber(lastNameInput, false);
            }else {
                //2. Perform Query To Show Actors
                String query = "SELECT f.title, f.release_year FROM actor a " +
                        "JOIN film_actor fa ON a.actor_id = fa.actor_id " +
                        "JOIN film f ON fa.film_id = f.film_id WHERE first_name = ? " +
                        "AND last_name = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, firstNameInput);
                statement.setString(2, lastNameInput);
                ResultSet results = statement.executeQuery();
                //3. Display Results
                //-header ('%' = start of delimiter, '-' = left align, '##' = total characters
                //(to pad with spaces), 's' = String Type
                System.out.printf("%-32s %-4s%n",
                        "Film Title", "Release Year");
                //max char lengths of each field:
                //32 chars is the max length of any film name
                //4 chars is the max length of any film release year (even though release_year is
                //of year type, we get rid of the month and date since they are always both 1)
                //using substring()
                System.out.println("-------------------------------- ---------------");
                //-results
                while (results.next()) {
                    String filmTitle = results.getString("title");
                    String releaseYear = results.getString("release_year").substring(0,4);
                    System.out.printf("%-32s %-4s%n",
                            filmTitle, releaseYear);
                    //delimiters ('%' = start of delimiter, '-' = left align, '##' = total characters
                    //(to pad with spaces), 's'/'f'/'d' = String Type/
                    //(decimal number/double/"float")/integer
                }
                break;
            }
        }
    }
    public static void displayAllActorsMatchingLastName() throws SQLException {
        //Ask User for last name to display all actors who match it
        String input;
        while (true) {
            System.out.println("-----Display all actors matching a last name-----");
            System.out.print("Please enter the last name for which you\n" +
                    "would like to display all all actors with matching last names to: ");
            input = scanner.nextLine().trim();
            if (isNumber(input)) {
                errorMessageNumber(input, false);
            } else {
                //2. Perform Query To Show Actors
                String query = "SELECT first_name, last_name FROM actor WHERE last_name = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, input);
                ResultSet results = statement.executeQuery();
                //3. Display Results
                //-header ('%' = start of delimiter, '-' = left align, '##' = total characters
                //(to pad with spaces), 's' = String Type
                System.out.printf("%-15s %-15s%n",
                        "First Name", "Last Name");
                //max char lengths of each field:
                //2 chars (should be in case more are added) the max length of any category id
                //32 chars is the max length of any category name
                System.out.println("--------------- ---------------");
                //-results
                while (results.next()) {
                    String firstName = results.getString("first_name");
                    String lastName = results.getString("last_name");
                    System.out.printf("%-15s %-15s%n",
                            firstName, lastName);
                    //delimiters ('%' = start of delimiter, '-' = left align, '##' = total characters
                    //(to pad with spaces), 's'/'f'/'d' = String Type/
                    //(decimal number/double/"float")/integer
                }
                break;
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
