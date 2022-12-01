package com.repository.device;

import com.model.device.Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class DeviceRepository {

    private Map<UUID, Device> devices = new HashMap<>();
    final Logger LOG = LoggerFactory.getLogger(String.valueOf(getClass()));

    public Device save(Device device) {

        LOG.info("Saving device into repository");
        devices.put(device.getUuid(), device);
        return devices.get(device.getUuid());

    }

    public Device getByUUID(UUID uuid) {

        LOG.info("Searching device into repository");
        return devices.get(uuid);

    }

    public Boolean remove(UUID uuid) {

        LOG.info("Removing device from repository");
        Device device = devices.remove(uuid);
        return device != null;

    }

    public List<Device> getDevicesByCustomerId(String customerId) {

        LOG.info("Searching devices into repository by customerId");
        return devices.values().stream().filter(d -> d.getCustomerId().equals(customerId)).collect(Collectors.toList());

    }
}
