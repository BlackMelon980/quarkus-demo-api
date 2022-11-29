package com.service.device;

import com.model.device.Device;
import com.model.device.DeviceDto;
import com.model.device.DeviceUpdateDto;
import com.repository.device.DeviceRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

@ApplicationScoped
public class DeviceService {

    DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public Device save(DeviceDto deviceDto) {

        Device device = new Device(deviceDto.getCustomerId(), deviceDto.getState());
        return deviceRepository.save(device);

    }

    public Device getByUUID(UUID uuid) {

        return deviceRepository.getByUUID(uuid);

    }

    public Device update(DeviceUpdateDto deviceUpdateDto) {

        Device device = deviceRepository.getByUUID(deviceUpdateDto.getUuid());
        if (device == null) {
            return null;
        }

        device.setState(deviceUpdateDto.getState());
        return deviceRepository.save(device);

    }

    public Boolean remove(UUID uuid) {

        return deviceRepository.remove(uuid);

    }
}
