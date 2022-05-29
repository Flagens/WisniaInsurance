package com.company.model;

import com.company.db.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.UUID;

public class Address {

    private UUID id;
    private String country;
    private String zipCode;
    private String city;
    private String street;
    private String buildingNumber;
    private String apartmentNumber;

    public Address(String country, String zipCode, String city, String street, String buildingNumber, String apartmentNumber) {
        this.country = country;
        this.zipCode = zipCode;
        this.city = city;
        this.street = street;
        this.buildingNumber = buildingNumber;
        this.apartmentNumber = apartmentNumber;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public void setCity(String city) {
        this.city = city;
    }



    public void save(){
        try {
            ResultSet rs = DBConnection.getStatement().executeQuery("SELECT COUNT(*) FROM addresses WHERE id='"+id+"'");
            rs.next();
            if(rs.getInt(1)==0) {
                this.id = UUID.randomUUID();
                DBConnection.getStatement().execute(String.format(Locale.US,
                        "INSERT INTO addresses VALUES ('%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                        id,
                        country,
                        zipCode,
                        city,
                        street,
                        buildingNumber,
                        apartmentNumber
                ));
            }
            else{
                DBConnection.getStatement().execute(String.format(Locale.US,
                        "UPDATE addresses SET country='%s', zip_code='%s', city='%s', street='%s', building_number='%s', apartment_number='%s' WHERE id='%s'",
                        country,
                        zipCode,
                        city,
                        street,
                        buildingNumber,
                        apartmentNumber,
                        id
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
