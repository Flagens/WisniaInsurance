package com.company.model;

import com.company.db.DBConnection;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class Incident {
    private UUID incidentId;
    private LocalDate incidentDate;
    private List<Damage> damages;
    private Address place;

    public Incident(Integer id, LocalDate date, List<Damage> damages, Address place) {
        this.incidentDate = date;
        this.damages = damages;
        this.place = place;
    }

    public Incident() {
    }

    public void save(){
        this.incidentId = UUID.randomUUID();
        try {
            DBConnection.getStatement().execute(String.format(
                    "INSERT INTO incidents VALUES ('%s', '%s', '%s')",
                    incidentId,
                    incidentDate,
                    place.getId()
            ));
            for(Damage damage : damages){
                DBConnection.getStatement().execute(String.format(Locale.US,
                        "INSERT INTO incidents_damages VALUES ('%s', '%s')",
                        incidentId,
                        damage.getId()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public UUID getIncidentId() {
        return incidentId;
    }

    public LocalDate getIncidentDate() {
        return incidentDate;
    }

    public void setIncidentDate(LocalDate incidentDate) {
        this.incidentDate = incidentDate;
    }

    public List<Damage> getDamages() {
        return damages;
    }

    public void setDamages(List<Damage> damages) {
        this.damages = damages;
    }

    public Address getPlace() {
        return place;
    }

    public void setPlace(Address place) {
        this.place = place;
    }
}
