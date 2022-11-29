package com.model.customer;

import com.model.RegexConfig;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class CustomerUpdateDto {

    @NotBlank
    @Pattern(regexp = RegexConfig.FISCAL_CODE_REGEX)
    private String fiscalCode;
    @NotBlank
    private String address;

    public CustomerUpdateDto() {
    }

    public CustomerUpdateDto(String fiscalCode, String address) {
        this.fiscalCode = fiscalCode;
        this.address = address;
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
