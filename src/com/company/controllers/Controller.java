package com.company.controllers;

import com.company.db.DBConnection;
import com.company.enums.PolicyTypes;
import com.company.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Controller {

    public List<Risk> getPolicyRisks(UUID policyId){
        List<Risk> risks = new ArrayList<>();
        Statement st = DBConnection.getNewStatement();
        try {
            ResultSet risksRS = st.executeQuery(String.format(
                    "select risks.* from policies_risks JOIN risks on policies_risks.risk_id=risks.id WHERE policies_risks.policy_id='%s'",
                    policyId));
            while (risksRS.next()){
                Risk risk = new Risk();
                risk.setId(UUID.fromString(risksRS.getString("id")));
                risk.setDescription(risksRS.getString("description"));
                risk.setPriceFrom(risksRS.getDouble("price_from"));
                risk.setPriceTo(risksRS.getDouble("price_to"));
                risk.setPolicyType(PolicyTypes.valueOf(risksRS.getString("policy_type")));
                risks.add(risk);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return risks;
    }

    public List<Policy> getAllPolicies() throws SQLException {
        List<Policy> policies = new ArrayList<>();
        ResultSet policiesRS = DBConnection.getNewStatement().executeQuery("SELECT * FROM policies");
        while (policiesRS.next()){
            Policy policy = new Policy();
            policy.setId(UUID.fromString(policiesRS.getString("id")));
            policy.setPolicyholder(getClientById(UUID.fromString(policiesRS.getString("policyholder_id"))));
            policy.setInsured(getClientById(UUID.fromString(policiesRS.getString("insured_id"))));
            policy.setBeneficiary(getClientById(UUID.fromString(policiesRS.getString("beneficiary_id"))));
            policy.setDurationFrom(LocalDate.parse(policiesRS.getString("duration_from")));
            policy.setDurationTo(LocalDate.parse(policiesRS.getString("duration_to")));
            policy.setPrice(policiesRS.getDouble("price"));
            policy.setPolicyType(PolicyTypes.valueOf(policiesRS.getString("policy_type")));
            policy.setActive(policiesRS.getBoolean("is_active"));
            policy.setRisks(getPolicyRisks(UUID.fromString(policiesRS.getString("id"))));
            policies.add(policy);
        }
        return policies;
    }



    public Person getPersonById(UUID id) throws SQLException {
        Statement st = DBConnection.getNewStatement();
        ResultSet personRS = st.executeQuery(String.format(
                "SELECT * FROM people WHERE id='%s'",
                id));
        personRS.next();
        Person person = new Person();
        person.setPersonId(UUID.fromString(personRS.getString("id")));
        person.setPesel(personRS.getString("pesel"));
        person.setFirstName(personRS.getString("first_name"));
        person.setLastName(personRS.getString("last_name"));
        st.close();
        return person;
    }

    public Company getEnterpriseById(UUID id) throws SQLException {
        Statement st = DBConnection.getNewStatement();
        ResultSet enterpriseRS = st.executeQuery(String.format(
                "SELECT * FROM enterprise WHERE id='%s'",
                id));
        enterpriseRS.next();
        Company enterprise = new Company();
        enterprise.setCompanyId(UUID.fromString(enterpriseRS.getString("id")));
        enterprise.setNip(enterpriseRS.getString("nip"));
        enterprise.setRegon(enterpriseRS.getString("regon"));
        enterprise.setRepresentative(getPersonById(UUID.fromString(enterpriseRS.getString("representative_id"))));
        st.close();
        return enterprise;
    }

    public Customer getClientById(UUID id) throws SQLException {
        Statement st = DBConnection.getNewStatement();
        ResultSet clientRS = st.executeQuery(String.format(
                "SELECT * FROM clients WHERE id='%s'",
                id));
        clientRS.next();
        if(clientRS.getString("person_id")!=null){
            Person person = getPersonById(UUID.fromString(clientRS.getString("person_id")));
            person.setCustomerId(UUID.fromString(clientRS.getString("id")));
            person.setAddress(getAddressById(UUID.fromString(clientRS.getString("address_id"))));
            person.setPhone(clientRS.getString("phone"));
            st.close();
            return person;
        }
        else{
            Company enterprise = getEnterpriseById(UUID.fromString(clientRS.getString("enterprise_id")));
            enterprise.setCustomerId(UUID.fromString(clientRS.getString("id")));
            enterprise.setAddress(getAddressById(UUID.fromString(clientRS.getString("address_id"))));
            enterprise.setPhone(clientRS.getString("phone"));
            st.close();
            return enterprise;
        }
    }

    public Address getAddressById(UUID id) throws SQLException {
        Statement st = DBConnection.getNewStatement();
        ResultSet addressRS = st.executeQuery(String.format(
                "SELECT * FROM addresses WHERE id='%s'",
                id));
        addressRS.next();
        Address address = new Address(
                addressRS.getString("country"),
                addressRS.getString("zip_code"),
                addressRS.getString("city"),
                addressRS.getString("street"),
                addressRS.getString("building_number"),
                addressRS.getString("apartment_number")
        );
        address.setId(UUID.fromString(addressRS.getString("id")));
        st.close();
        return address;
    }
}
