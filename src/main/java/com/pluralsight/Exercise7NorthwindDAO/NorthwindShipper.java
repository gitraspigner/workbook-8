package com.pluralsight.Exercise7NorthwindDAO;
/**
 * Represents a Shipper (entry within the shippers table of the Northwind (products) database).
 *
 * @author Ravi Spigner
 */
public class NorthwindShipper {
    private int ShipperID;
    private String CompanyName;
    private String Phone;
    public NorthwindShipper(int shipperID, String companyName, String phone) {
        this.ShipperID = shipperID;
        this.CompanyName = companyName;
        this.Phone = phone;
    }
    public NorthwindShipper(String companyName, String phone) {
        this.CompanyName = companyName;
        this.Phone = phone;
    }
    @Override
    public String toString() {
        return "ShipperID: " + ShipperID +
                ", CompanyName: " + CompanyName +
                ", Phone: " + Phone;
    }
    public int getShipperID() {
        return ShipperID;
    }
    public void setShipperID(int shipperID) {
        ShipperID = shipperID;
    }
    public String getCompanyName() {
        return CompanyName;
    }
    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }
    public String getPhone() {
        return Phone;
    }
    public void setPhone(String phone) {
        Phone = phone;
    }
}
