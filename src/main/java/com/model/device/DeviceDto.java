package com.model.device;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class DeviceDto {
    @NotBlank
    private String customerId;
    @NotBlank
    @Pattern(regexp = "ACTIVE|INACTIVE|LOST")
    private String state;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
