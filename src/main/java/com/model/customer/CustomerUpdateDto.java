package com.model.customer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class CustomerUpdateDto {

    @NotBlank
    @Pattern(regexp = "^[A-Za-z]{6}[0-9]{2}[A-Za-z]{1}[0-9]{2}[A-Za-z]{1}[0-9]{3}[A-Za-z]{1}$")
    private String fiscalCode;
    @NotBlank
    private String address;

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
