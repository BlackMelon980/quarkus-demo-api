package com.service.device;

import com.model.RegexConfig;
import com.model.device.Device;
import com.model.device.DeviceDto;
import com.model.device.DeviceState;
import com.model.device.DeviceUpdateDto;
import com.repository.device.DeviceRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class DeviceService {

    DeviceRepository deviceRepository;

    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    public Device save(@Valid DeviceDto deviceDto) {

        List<Device> customerDevices = deviceRepository.getDevicesByCustomerId(deviceDto.getCustomerId());
        if (customerDevices.size() >= 2) {
            return null;
        }

        Device device = new Device(deviceDto.getCustomerId(), DeviceState.valueOf(deviceDto.getState()));
        return deviceRepository.save(device);

    }

    public Device getByUUID(@Pattern(regexp = RegexConfig.UUID_REGEX) @NotBlank String uuid) {

        return deviceRepository.getByUUID(UUID.fromString(uuid));

    }

    public List<Device> getByCustomerId(String customerId) {

        return deviceRepository.getDevicesByCustomerId(customerId);

    }

    public Device update(@Valid DeviceUpdateDto deviceUpdateDto) {

        UUID deviceUuid = UUID.fromString(deviceUpdateDto.getUuid());
        Device device = deviceRepository.getByUUID(deviceUuid);
        if (device == null) {
            return null;
        }

        device.setState(DeviceState.valueOf(deviceUpdateDto.getState()));
        return deviceRepository.save(device);

    }

    public Boolean remove(@Pattern(regexp = RegexConfig.UUID_REGEX) @NotBlank String uuid) {

        return deviceRepository.remove(UUID.fromString(uuid));

    }


}
