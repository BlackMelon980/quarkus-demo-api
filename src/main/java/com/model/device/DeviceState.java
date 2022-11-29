package com.model.device;

public enum DeviceState {

    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE"),
    LOST("LOST");

    private final String name;

    DeviceState(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return String.valueOf(this.name);
    }
}
