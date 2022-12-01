package com.model.device;

import com.model.RegexConfig;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class DeviceUpdateDto {
    @NotBlank
    @Pattern(regexp = RegexConfig.UUID_REGEX)
    private String uuid;
    @NotBlank
    @Pattern(regexp = "ACTIVE|INACTIVE|LOST")
    private String state;

    public DeviceUpdateDto() {
    }

    public DeviceUpdateDto(String uuid, String state) {
        this.uuid = uuid;
        this.state = state;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
