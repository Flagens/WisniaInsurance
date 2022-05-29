package com.company.model;

import com.company.db.DBConnection;
import com.company.enums.PolicyTypes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class Policy {
    private UUID id;
    private Customer policyholder;
    private Customer insured;
    private Customer beneficiary;
    private List<Risk> risks;
    private LocalDate durationFrom;
    private LocalDate durationTo;
    private Double price;
    private PolicyTypes policyType;
    private Boolean isActive;

    public Policy() {
    }

    public Policy(Customer policyholder, Customer insured, Customer beneficiary, List<Risk> risks, LocalDate durationFrom, LocalDate durationTo, Double price, PolicyTypes policyType) {
        this.policyholder = policyholder;
        this.insured = insured;
        this.beneficiary = beneficiary;
        this.risks = risks;
        this.durationFrom = durationFrom;
        this.durationTo = durationTo;
        this.price = price;
        this.policyType = policyType;
        this.isActive = true;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setPolicyholder(Customer policyholder) {
        this.policyholder = policyholder;
    }

    public void setInsured(Customer insured) {
        this.insured = insured;
    }

    public void setBeneficiary(Customer beneficiary) {
        this.beneficiary = beneficiary;
    }

    public void setRisks(List<Risk> risks) {
        this.risks = risks;
    }

    public void setDurationFrom(LocalDate durationFrom) {
        this.durationFrom = durationFrom;
    }

    public void setDurationTo(LocalDate durationTo) {
        this.durationTo = durationTo;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setPolicyType(PolicyTypes policyType) {
        this.policyType = policyType;
    }

    public Customer getPolicyholder() {
        return policyholder;
    }

    public Customer getInsured() {
        return insured;
    }

    public UUID getId() {
        return id;
    }

    public List<Risk> getRisks() {
        return risks;
    }

    public LocalDate getDurationFrom() {
        return durationFrom;
    }

    public LocalDate getDurationTo() {
        return durationTo;
    }

    public Double getPrice() {
        return price;
    }

    public Boolean getActive() {
        return isActive;
    }

    public Customer getBeneficiary() {
        return beneficiary;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public PolicyTypes getPolicyType() {
        return policyType;
    }

    public List<Risk> getAvailableRisks(List<Risk> risks){
        List<Risk> availableRisks = new ArrayList<>();
        for(Risk r : risks){
            if(r.getPolicyType() == this.getPolicyType()){
                availableRisks.add(r);
            }
        }
        return availableRisks;
    }

    public void save(){
        try {
            ResultSet rs = DBConnection.getStatement().executeQuery("SELECT COUNT(*) FROM policies WHERE id='"+id+"'");
            rs.next();
            if(rs.getInt(1)==0) {
                this.id = UUID.randomUUID();
                DBConnection.getStatement().execute(String.format(Locale.US,
                        "INSERT INTO policies(id, policyholder_id, insured_id, beneficiary_id, duration_from, duration_to, price, policy_type) VALUES ('%s', '%s', '%s', '%s', '%s', '%s', %f, '%s')",
                        id,
                        policyholder.getCustomerId(),
                        insured.getCustomerId(),
                        beneficiary.getCustomerId(),
                        durationFrom,
                        durationTo,
                        price,
                        policyType
                ));
                for (Risk risk : risks) {
                    DBConnection.getStatement().execute(String.format(Locale.US,
                            "INSERT INTO policies_risks VALUES ('%s', '%s')",
                            id,
                            risk.getId()
                    ));
                }
            }
            else{
                DBConnection.getStatement().execute(String.format(
                        "UPDATE policies SET policyholder_id='%s', insured_id='%s', beneficiary_id='%s', duration_from='%s', duration_to='%s', price=%f, policy_type='%s', is_active='%s' WHERE id='%s'",
                        policyholder.getCustomerId(),
                        insured.getCustomerId(),
                        beneficiary.getCustomerId(),
                        durationFrom,
                        durationTo,
                        price,
                        policyType,
                        isActive ? "TRUE" : "FALSE",
                        id
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Policy{" +
                "id=" + id +
                ", policyholder=" + policyholder +
                ", insured=" + insured +
                ", beneficiary=" + beneficiary +
                ", risks=" + risks +
                ", durationFrom=" + durationFrom +
                ", durationTo=" + durationTo +
                ", price=" + price +
                ", policyType=" + policyType +
                ", isActive=" + isActive +
                '}';
    }
}
