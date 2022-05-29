package com.company.model;

import com.company.db.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.UUID;

public class Person extends Customer {
    private UUID personId;
    private String pesel;
    private String firstName;
    private String lastName;

    public Person(Address address, String phone, String pesel, String firstName, String lastName) {
        super(address, phone);
        this.pesel = pesel;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Person() {
        super();
    }

    public void save(){
        super.save();
        try {
            ResultSet rs = DBConnection.getStatement().executeQuery("SELECT COUNT(*) FROM people WHERE id='"+personId+"'");
            rs.next();
            if(rs.getInt(1)==0) {
                this.personId = UUID.randomUUID();
                DBConnection.getStatement().execute(String.format(Locale.US,
                        "UPDATE clients SET person_id='%s' WHERE id='%s'",
                        personId,
                        getCustomerId().toString()
                ));
                DBConnection.getStatement().execute(String.format(Locale.US,
                        "INSERT INTO people VALUES ('%s', '%s', '%s', '%s')",
                        personId.toString(),
                        pesel,
                        firstName,
                        lastName
                ));
            }
            else{
                DBConnection.getStatement().execute(String.format(Locale.US,
                        "UPDATE people SET pesel='%s', first_name='%s', last_name='%s' WHERE id='%s'",
                        pesel,
                        firstName,
                        lastName,
                        personId
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPersonId(UUID personId) {
        this.personId = personId;
    }

    public String getPesel() {
        return pesel;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public UUID getPersonId() {
        return personId;
    }
}
