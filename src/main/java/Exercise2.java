import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.*;
/**
 * Performs a basic search on a Northwind (product) database to retrieve all product ids,
 * product names, unit prices, and units in stock.
 *
 * @author Ravi Spigner
 */
public class Exercise2 {
    public static void main(String[] args) throws SQLException {
        //A database URL has the following format:
        //jdbc:mysql://[host][:port]/[databaseName]
        //1. Open connection
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL("jdbc:mysql://localhost:3306/northwind");
        dataSource.setUser(args[0]);
        dataSource.setPassword(args[1]);
        Connection connection = dataSource.getConnection();
        //2. Perform Query
        String query = "SELECT * FROM Products";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet results = statement.executeQuery();
        //3. Display Results
        //header ('%' = start of delimiter, '-' = left align, '##' = total characters
        //(to pad with spaces), 's' = String Type
        System.out.printf("%-2s %-32s %-6s %-3s%n",
                "Id", "Name", "Price", "Stock");
        //max char lengths of each field:
        //2 chars is the max length of any product id
        //32 chars is the max length of any product name
        //4 chars is the max length of any product price
        //3 chars is the max length of any product stock
        System.out.println("-- -------------------------------- -----  -----");
        //results
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
        //4. Close Connection
        connection.close();
    }
}
