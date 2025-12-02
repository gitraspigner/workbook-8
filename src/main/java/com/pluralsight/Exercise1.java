package com.pluralsight;

import com.mysql.cj.jdbc.MysqlDataSource;
import java.sql.*;
/**
 * Performs a basic search on a Northwind (product) database to retrieve all product names
 * (and no other info of products in the database).
 *
 * @author Ravi Spigner
 */
public class Exercise1 {
    public static void main(String[] args) throws SQLException {
        //A database URL has the following format:
        //jdbc:mysql://[host][:port]/[databaseName]
        //1. Open connection
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL("jdbc:mysql://localhost:3306/northwind");
        dataSource.setUser("root");
        dataSource.setPassword("yearup");
        Connection connection = dataSource.getConnection();
        //2. Perform Query
        String query = "SELECT ProductName FROM Products";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet results = statement.executeQuery();
        //3. Display Results
        while (results.next()) {
            String product = results.getString("ProductName");
            System.out.println(product);
        }
        //4. Close Connection
        connection.close();
    }
}
