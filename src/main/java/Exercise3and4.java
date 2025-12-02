import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.*;
import java.util.Scanner;
/**
 * A command line application that performs basic searches on a Northwind (product) database
 * in order to retrieve all products or all customers.
 *
 * @author Ravi Spigner
 */
public class Exercise3and4 {
    private static Connection connection;
    private static Scanner scanner;
    public static void main(String[] args) throws SQLException {
        //A database URL has the following format:
        //jdbc:mysql://[host][:port]/[databaseName]
        //1. Open connection
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL("jdbc:mysql://localhost:3306/northwind");
        dataSource.setUser(args[0]);
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
        while (true) {
            System.out.print("""
                    ------------Main Menu------------
                    What do you want to do?
                    1) Display all products
                    2) Display all customers
                    3) Display all categories
                    0) Exit
                    Select an option:\s""");
            input = scanner.nextLine().trim();
            if (!isNumber(input)) {
                errorMessageNumber(input, true);
                continue;
            }
            if (input.equals("1")) {
                displayAllProducts();
            } else if (input.equals("2")) {
                displayAllCustomers();
            } else if (input.equals("3")) {
                displayAllCategories();
                //TODO: redesign each method to use try with resources block
            } else if (input.equals("0")) {
                displayGoodbye();
                break;
            } else {
                errorMessage(input, "Is An Invalid Menu Option. Only 1,2,3, or 0 is " +
                        "acceptable for your menu input");
            }
        }
    }
    public static void displayAllProducts() throws SQLException {
        //2. Perform Query
        String query = "SELECT * FROM Products";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet results = statement.executeQuery();
        //3. Display Results
        //-header ('%' = start of delimiter, '-' = left align, '##' = total characters
        //(to pad with spaces), 's' = String Type
        System.out.printf("%-2s %-32s %-6s %-3s%n",
                "Id", "Name", "Price", "Stock");
        //max char lengths of each field:
        //2 chars is the max length of any product id
        //32 chars is the max length of any product name
        //4 chars is the max length of any product price
        //3 chars is the max length of any product stock
        System.out.println("-- -------------------------------- -----  -----");
        //-results
        while (results.next()) {
            int id = results.getInt("ProductID");
            String name = results.getString("ProductName");
            double price = results.getDouble("UnitPrice");
            int stock = results.getInt("UnitsInStock");
            System.out.printf("%-2d %-32s %-6.2f %-3d%n",
                    id, name, price, stock);
            //delimiters ('%' = start of delimiter, '-' = left align, '##' = total characters
            //(to pad with spaces), 's'/'f'/'d' = String Type/
            //(decimal number/double/"float")/integer
        }
    }
    public static void displayAllCustomers() throws SQLException {
        //2. Perform Query
        String query = "SELECT * FROM Customers";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet results = statement.executeQuery();
        //3. Display Results
        //-header ('%' = start of delimiter, '-' = left align, '##' = total characters
        //(to pad with spaces), 's' = String Type
        System.out.printf("%-7s %-40s %-17s %-14s %-13s %-16s%n",
                "Id", "Company Name", "City", "Region", "Country", "Phone Number");
        //max char lengths of each field:
        //5 chars is the max length of any company id
        //38 chars is the max length of any company name
        //15 chars is the max length of any city
        //12 chars is the max length of any region
        //11 chars is the max length of any country
        //14 chars is the max length of any phone (number)
        System.out.println("-----   ---------------------------------------- -----------------  " +
                "--------------  -----------  ----------------");
        //-results
        while (results.next()) {
            String id = results.getString("CustomerID");
            String companyName = results.getString("CompanyName");
            String city = results.getString("City");
            String region = results.getString("Region");
            String country = results.getString("Country");
            String phone = results.getString("Phone");
            System.out.printf("%-7s %-40s %-17s %-14s %-13s %-16s%n",
                    id, companyName, city, region, country, phone);

            //delimiters ('%' = start of delimiter, '-' = left align, '##' = total characters
            //(to pad with spaces), 's'/'f'/'d' = String Type/
            //(decimal number/double/"float")/integer
        }
    }
    public static void displayAllCategories() throws SQLException {
        //2. Perform Query To Show Categories
        String query = "SELECT CategoryID, CategoryName FROM Categories";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet results = statement.executeQuery();
        //3. Display Results
        //-header ('%' = start of delimiter, '-' = left align, '##' = total characters
        //(to pad with spaces), 's' = String Type
        System.out.printf("%-2s %-20s%n",
                "Id", "Name");
        //max char lengths of each field:
        //2 chars (should be in case more are added) the max length of any category id
        //32 chars is the max length of any category name
        System.out.println("-- --------------------");
        //-results
        while (results.next()) {
            int id = results.getInt("CategoryID");
            String name = results.getString("CategoryName");
            System.out.printf("%-2s %-20s%n",
                    id, name);
            //delimiters ('%' = start of delimiter, '-' = left align, '##' = total characters
            //(to pad with spaces), 's'/'f'/'d' = String Type/
            //(decimal number/double/"float")/integer
        }
        //2.5/3.5. Ask User for category id to display all products from the category
        String input;
        while (true) {
            System.out.print("Please enter the CategoryID of a category for which you " +
                    "would like to display all products of: ");
            input = scanner.nextLine().trim();
            if (!isNumber(input)) {
                errorMessageNumber(input, true);
            } else {
                //2. Perform Query To Show Categories
                query = "SELECT p.* FROM Products p JOIN Categories c ON p.CategoryID = " +
                        "c.CategoryID WHERE p.CategoryID = ?";
                statement = connection.prepareStatement(query);
                statement.setInt(1, Integer.parseInt(input));
                results = statement.executeQuery();
                //3. Display Results
                //-header ('%' = start of delimiter, '-' = left align, '##' = total characters
                //(to pad with spaces), 's' = String Type
                System.out.printf("%-2s %-32s %-6s %-3s%n",
                        "Id", "Name", "Price", "Stock");
                //max char lengths of each field:
                //2 chars is the max length of any product id
                //32 chars is the max length of any product name
                //4 chars is the max length of any product price
                //3 chars is the max length of any product stock
                System.out.println("-- -------------------------------- -----  -----");
                //-results
                while (results.next()) {
                    int id = results.getInt("ProductID");
                    String name = results.getString("ProductName");
                    double price = results.getDouble("UnitPrice");
                    int stock = results.getInt("UnitsInStock");
                    System.out.printf("%-2d %-32s %-6.2f %-3d%n",
                            id, name, price, stock);
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
        System.out.println("-------------------");
        System.out.println("ERROR: " + input + " " + errorMessage);
        System.out.println("-------------------");
    }
    public static void displayWelcome() {
        System.out.println("-------------------");
        System.out.println("Welcome To The Northwind Products Database");
        System.out.println("-------------------");
    }
    public static void displayGoodbye() {
        System.out.println("-------------------");
        System.out.println("Thank you! See you next time!");
        System.out.println("-------------------");
    }
}
