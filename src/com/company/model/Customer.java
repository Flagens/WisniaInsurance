package com.company.model;

import com.company.db.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public abstract class Customer {
    private UUID customerId;
    private Address address;
    private String phone;

    public Customer(Address address, String phone) {
        this.address = address;
        this.phone = phone;
    }

    public Customer() {
        this.customerId = UUID.randomUUID();
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Policy> getPolicies(List<Policy> allPolicies){
        List<Policy> clientPolicies = new ArrayList<>();

        for(Policy p : allPolicies){
            if(p.getPolicyholder() == this || p.getInsured() == this || p.getBeneficiary() == this){
                clientPolicies.add(p);
            }
        }
        return clientPolicies;
    }


    public void save(){
        try {
            ResultSet rs = DBConnection.getStatement().executeQuery("SELECT COUNT(*) FROM clients WHERE id='"+ customerId +"'");
            rs.next();
            if(rs.getInt(1)==0) {
                this.customerId = UUID.randomUUID();
                DBConnection.getStatement().execute(String.format(Locale.US,
                        "INSERT INTO clients (id, address_id, phone) VALUES ('%s', '%s', '%s')",
                        customerId,
                        address.getId().toString(),
                        phone
                ));
            }
            else{
                DBConnection.getStatement().execute(String.format(Locale.US,
                        "UPDATE clients SET address_id='%s', phone='%s' WHERE id='%s'",
                        address.getId(),
                        phone,
                        customerId
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
