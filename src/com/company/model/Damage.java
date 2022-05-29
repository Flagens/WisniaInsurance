package com.company.model;

import com.company.db.DBConnection;
import com.company.enums.DamageTypes;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public class Damage {
    private UUID id;
    private Policy policy;
    private DamageTypes damageType;
    private Customer victim;
    private Set<Document> documents;

    public Damage(Policy policy, DamageTypes damageType, Customer victim, Set<Document> documents) {
        this.policy = policy;
        this.damageType = damageType;
        this.victim = victim;
        this.documents = documents;
    }

    public Damage() {
    }

    public void save(){
        try {
            ResultSet rs = DBConnection.getStatement().executeQuery("SELECT COUNT(*) FROM damages WHERE id='"+id+"'");
            rs.next();
            if(rs.getInt(1)==0) {
                this.id = UUID.randomUUID();
                DBConnection.getStatement().execute(String.format(
                        "INSERT INTO damages VALUES ('%s', '%s', '%s', '%s')",
                        id,
                        policy.getId(),
                        damageType,
                        victim.getCustomerId()
                ));
                for (Document document : documents) {
                    DBConnection.getStatement().execute(String.format(Locale.US,
                            "INSERT INTO damages_documents VALUES ('%s', '%s')",
                            id,
                            document.getId()
                    ));
                }
            }
            else{
                DBConnection.getStatement().execute(String.format(
                        "UPDATE damages SET policy_id='%s', damage_type='%s', victim_id='%s' WHERE id='%s'",
                        policy.getId(),
                        damageType,
                        victim.getCustomerId(),
                        id
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public UUID getId() {
        return id;
    }


    public void setPolicy(Policy policy) {
        this.policy = policy;
    }

    public void setVictim(Customer victim) {
        this.victim = victim;
    }

    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }

}
