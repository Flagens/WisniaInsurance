package com.company.model;

import java.util.UUID;

public class Company extends Customer {
    private String nip;
    private String regon;
    private Person representative;
    private UUID companyId;

    public Company(Address address, String phone, String nip, String regon, Person representative) {
        super(address, phone);
        this.nip = nip;
        this.regon = regon;
        this.representative = representative;
    }

    public Company() {
        super();
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public String getRegon() {
        return regon;
    }

    public void setRegon(String regon) {
        this.regon = regon;
    }

    public Person getRepresentative() {
        return representative;
    }

    public void setRepresentative(Person representative) {
        this.representative = representative;
    }

    public UUID getCompanyId() {
        return companyId;
    }

    public void setCompanyId(UUID companyId) {
        this.companyId = companyId;
    }
}
