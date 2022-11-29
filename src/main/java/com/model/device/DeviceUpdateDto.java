package com.model.device;

import java.util.UUID;

public class DeviceUpdateDto {

    private UUID uuid;
    private DeviceState state;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public DeviceState getState() {
        return state;
    }

    public void setState(DeviceState state) {
        this.state = state;
    }
}
