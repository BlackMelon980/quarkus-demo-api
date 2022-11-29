package com.model.customer;

import com.model.RegexConfig;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class CustomerDto {

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    @Pattern(regexp = RegexConfig.FISCAL_CODE_REGEX)
    private String fiscalCode;
    @NotBlank
    private String address;

    public CustomerDto() {
    }

    public CustomerDto(String firstName, String lastName, String fiscalCode, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.fiscalCode = fiscalCode;
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFiscalCode() {
        return fiscalCode;
    }

    public void setFiscalCode(String fiscalCode) {
        this.fiscalCode = fiscalCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
