package com.model.customer;

public class Customer {

    private final String firstName;
    private final String lastName;
    private final String fiscalCode;
    private String address;

    public Customer(String firstName, String lastName, String fiscalCode, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fiscalCode = fiscalCode;
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFiscalCode() {
        return fiscalCode;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
