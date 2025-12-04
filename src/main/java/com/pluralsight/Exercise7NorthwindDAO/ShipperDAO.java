package com.pluralsight.Exercise7NorthwindDAO;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 * Handles opening & closing the database connection, searching it for information,
 * and returning the information to the main application class (Northwind Application)
 * for the Northwind (products) database.
 *
 * @author Ravi Spigner
 */
public class ShipperDAO {
    private DataSource dataSource;
    public ShipperDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public List<NorthwindShipper> getAllShippers() throws SQLException {
        List<NorthwindShipper> shippers = new ArrayList<>();
        //Perform Query To retrieve all Shippers
        String query = "SELECT * FROM shippers";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);) {
            try (ResultSet results = statement.executeQuery();) {
                //Process Results
                while (results.next()) {
                    int idResult = results.getInt("ShipperID");
                    String companyNameResult = results.getString("CompanyName");
                    String phone = results.getString("Phone");
                    shippers.add(new NorthwindShipper(idResult, companyNameResult, phone));
                }
            }
        }
        //Return Results
        return shippers;
    }
    public NorthwindShipper getShipper(int shipperID) throws SQLException{
        NorthwindShipper shipper;
        //Perform Query To retrieve specific Shipper
        String query = "SELECT * FROM shippers WHERE ShipperID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);) {
            statement.setInt(1, shipperID);
            try (ResultSet results = statement.executeQuery();) {
                //Process Results
                if (results.next()) {
                    int idResult = results.getInt("ShipperID");
                    String companyNameResult = results.getString("CompanyName");
                    String phone = results.getString("Phone");
                    //Return result
                    return new NorthwindShipper(idResult, companyNameResult, phone);
                }
            }
        }
        return null;
    }
    public NorthwindShipper add(NorthwindShipper shipper) throws SQLException {
        //Perform Create operation for new shipper
        String query = "INSERT INTO shippers VALUES(NULL, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query,
                     PreparedStatement.RETURN_GENERATED_KEYS);) {
            statement.setString(1, shipper.getCompanyName());
            statement.setString(2, shipper.getPhone());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys();) {
                if (keys.next()) {
                    //Process New Add
                    int idAdded = keys.getInt(1);
                    String companyNameAdded = shipper.getCompanyName();
                    String phoneAdded = shipper.getPhone();
                    //Return Result
                    return new NorthwindShipper(idAdded, companyNameAdded, phoneAdded);
                }
            }
        }
        return null;
    }
    public NorthwindShipper updatePhoneNumber(NorthwindShipper shipper, String newPhoneNumber) throws SQLException {
        //Perform Update operation for existing shipper
        String query = "UPDATE shippers SET Phone = ? WHERE ShipperID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newPhoneNumber);
            statement.setInt(2, shipper.getShipperID());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                //Return updated shipper object
                return new NorthwindShipper(
                        shipper.getShipperID(),
                        shipper.getCompanyName(),
                        newPhoneNumber
                );
            }
        }
        return null;
    }
    public NorthwindShipper delete(int shipperID) throws SQLException {
        //retrieve the shipper details before deletion
        NorthwindShipper shipperToDelete = getShipper(shipperID);
        //Perform Delete operation for existing shipper
        String query = "DELETE FROM shippers WHERE ShipperID = ?";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, shipperID);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                //Return the deleted shipper details
                return shipperToDelete;
            }
        }
        return null;
    }
}
