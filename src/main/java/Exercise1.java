import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * *******Add program description here******
 *
 * @author Ravi Spigner
 */
public class Exercise1 {
    public static void main(String[] args) throws SQLException {
        //    A database URL has the following format:
        //    jdbc:mysql://[host][:port]/[database][?propertyName1=propertyValue1]
        //1. Open connection
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL("jdbc:mysql://localhost:3306/northwind");
        dataSource.setUser("root");
        dataSource.setPassword("yearup");
        Connection connection = dataSource.getConnection();
        //2. Perform Query
        Statement statement = connection.createStatement();
        String query = "SELECT ProductName FROM Products;";
        ResultSet results = statement.executeQuery(query);
        //3. Display Results
        while (results.next()) {
            String product = results.getString("ProductName");
            System.out.println(product);
        }
        //4. Close Connection
        connection.close();
    }
}
