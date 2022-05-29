package com.company;

import com.company.controllers.Controller;
import com.company.enums.PolicyTypes;
import com.company.model.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Set;

import static com.company.db.DBConnection.*;

public class Main {

    public static void main(String[] args) throws SQLException {
        connect("jdbc:sqlite:db.sqlite");
        createTables();

        Risk risk = new Risk();
        risk.setPriceFrom(120.20);
        risk.setPriceTo(150.20);
        risk.setDescription("desc");
        risk.setPolicyType(PolicyTypes.LIFE_INSURANCE_POLICY);
        risk.save();


        Address address1 = new Address("Poland", "12-345", "Warsaw", "ul. Wisniowa", "5", "2");
        address1.save();

        Person person = new Person();
        person.setAddress(address1);
        person.setPesel("12345678912");
        person.setPhone("123456789");
        person.setFirstName("John");
        person.setLastName("Kowalski");
        person.save();

        Policy policy1 = new Policy();
        policy1.setPolicyholder(person);
        policy1.setInsured(person);
        policy1.setBeneficiary(person);
        policy1.setDurationFrom(LocalDate.now());
        policy1.setDurationTo(LocalDate.of(2032, Month.MAY, 14));
        policy1.setPrice(1234d);
        policy1.setPolicyType(PolicyTypes.PROPERTY_POLICY);
        policy1.setRisks(List.of(risk));
        policy1.save();

        Damage damage = new Damage();
        damage.setPolicy(policy1);
        damage.setVictim(person);
        damage.save();


        Incident incident = new Incident();
        incident.setIncidentDate(LocalDate.of(2022, Month.APRIL, 2));
        incident.setPlace(address1);
        incident.setDamages(List.of(damage));
        incident.save();

        incident.getPlace().setCity("Cracow");
        incident.getPlace().save();

        Controller controller = new Controller();
        List<Policy> policies = controller.getAllPolicies();
        policies.get(0).getRisks().forEach(System.out::println);

        disconnect();


    }
}
