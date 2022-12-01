package com.model.customer;

import com.model.device.Device;

import java.util.List;

public class CustomerDevicesResponse {

    private final Customer customer;
    private final List<Device> devices;

    public CustomerDevicesResponse(Customer customer, List<Device> devices) {
        this.customer = customer;
        this.devices = devices;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<Device> getDevices() {
        return devices;
    }

}
