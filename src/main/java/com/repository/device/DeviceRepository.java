package com.repository.device;

import com.model.device.Device;

import javax.enterprise.context.ApplicationScoped;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class DeviceRepository {

    private Map<UUID, Device> devices = new HashMap<>();

    public Device save(Device device) {

        devices.put(device.getUuid(), device);
        return devices.get(device.getUuid());

    }

    public Device getByUUID(UUID uuid) {

        return devices.get(uuid);

    }

    public Boolean remove(UUID uuid) {

        Device device = devices.remove(uuid);
        return device != null;

    }

}
