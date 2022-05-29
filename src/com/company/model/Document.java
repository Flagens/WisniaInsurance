package com.company.model;

import com.company.db.DBConnection;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class Document {
    private UUID id;
    private String document;

    public Document(UUID id, String document_url) {
        this.id = id;
        this.document = document_url;
    }

    public Document() {
    }

    public void save(){
        try {
            ResultSet rs = DBConnection.getStatement().executeQuery("SELECT COUNT(*) FROM documents WHERE id='"+id+"'");
            rs.next();
            if(rs.getInt(1)==0) {
                this.id = UUID.randomUUID();
                DBConnection.getStatement().execute(String.format(
                        "INSERT INTO documents VALUES ('%s', '%s')",
                        id,
                        document
                ));
            }
            else{
                DBConnection.getStatement().execute(String.format(
                        "UPDATE documents SET document_url='%s' WHERE id='%s'",
                        document,
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

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDocument_url() {
        return document;
    }

    public void setDocument_url(String document_url) {
        this.document = document_url;
    }
}
