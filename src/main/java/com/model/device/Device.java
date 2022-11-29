package com.model.device;

import java.util.UUID;

public class Device {

    private UUID uuid = UUID.randomUUID();
    private final String customerId;
    private DeviceState state;

    public Device(String customerId, DeviceState state) {
        this.customerId = customerId;
        this.state = state;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setState(DeviceState state) {
        this.state = state;
    }

    public DeviceState getState() {
        return state;
    }
}
