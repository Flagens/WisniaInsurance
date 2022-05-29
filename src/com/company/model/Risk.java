package com.company.model;

import com.company.db.DBConnection;
import com.company.enums.PolicyTypes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.UUID;

public class Risk {
    private UUID id;
    private Double priceFrom;
    private Double priceTo;
    private String description;
    private PolicyTypes policyType;

    public Risk(Double priceFrom, Double priceTo, String description, PolicyTypes policyType) {
        this.priceFrom = priceFrom;
        this.priceTo = priceTo;
        this.description = description;
        this.policyType = policyType;
    }

    public Risk() {
        this.id = UUID.randomUUID();
    }

    public PolicyTypes getPolicyType() {
        return policyType;
    }


    public void setPriceFrom(Double priceFrom) {
        this.priceFrom = priceFrom;
    }

    public void setPriceTo(Double priceTo) {
        this.priceTo = priceTo;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Double getPriceFrom() {
        return priceFrom;
    }

    public Double getPriceTo() {
        return priceTo;
    }

    public String getDescription() {
        return description;
    }

    public void setPolicyType(PolicyTypes policyType) {
        this.policyType = policyType;
    }

    public UUID getId() {
        return id;
    }

    public void save(){
        try {
            ResultSet rs = DBConnection.getStatement().executeQuery("SELECT COUNT(*) FROM risks WHERE id='"+id+"'");
            rs.next();
            if(rs.getInt(1)==0) {
                this.id = UUID.randomUUID();
                DBConnection.getStatement().execute(String.format(Locale.US,
                        "INSERT INTO risks VALUES ('%s', %f, %f, '%s', '%s')",
                        id,
                        priceFrom,
                        priceTo,
                        description,
                        policyType
                ));
            }
            else{
                DBConnection.getStatement().execute(String.format(Locale.US,
                        "UPDATE risks SET price_from=%f, price_to=%f, description='%s', policy_type='%s' WHERE id='%s'",
                        priceFrom,
                        priceTo,
                        description,
                        policyType,
                        id
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
