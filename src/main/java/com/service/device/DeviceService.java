package com.service.device;

import com.model.customer.Customer;
import com.model.device.Device;
import com.model.device.DeviceDto;
import com.model.device.DeviceState;
import com.model.device.DeviceUpdateDto;
import com.repository.device.DeviceRepository;
import com.service.customer.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class DeviceService {

    DeviceRepository deviceRepository;
    CustomerService customerService;
    final Logger LOG = LoggerFactory.getLogger(String.valueOf(getClass()));

    public DeviceService(DeviceRepository deviceRepository, CustomerService customerService) {
        this.deviceRepository = deviceRepository;
        this.customerService = customerService;
    }

    public Device save(DeviceDto deviceDto) {

        LOG.info("Called service method to save device");

        Customer customer = customerService.getByFiscalCode(deviceDto.getCustomerId());
        if (customer == null) {

            LOG.error("Customer with id: " + deviceDto.getCustomerId() + " does not exist.");
            return null;
        }

        List<Device> customerDevices = deviceRepository.getDevicesByCustomerId(deviceDto.getCustomerId());
        if (customerDevices.size() >= 2) {

            LOG.error("This customer already has 2 devices.");
            return null;
        }

        Device device = new Device(deviceDto.getCustomerId(), DeviceState.valueOf(deviceDto.getState()));
        return deviceRepository.save(device);

    }

    public Device getByUUID(String uuid) {

        LOG.info("Called service method to get device by uuid");
        return deviceRepository.getByUUID(UUID.fromString(uuid));

    }

    public List<Device> getByCustomerId(String customerId) {

        LOG.info("Called service method to get devices by customerId");
        return deviceRepository.getDevicesByCustomerId(customerId);

    }

    public Device update(DeviceUpdateDto deviceUpdateDto) {

        LOG.info("Called service method to update device");
        UUID deviceUuid = UUID.fromString(deviceUpdateDto.getUuid());
        Device device = deviceRepository.getByUUID(deviceUuid);

        if (device == null) {
            LOG.error("Device with uuid: " + deviceUpdateDto.getUuid() + " does not exist.");
            return null;
        }

        device.setState(DeviceState.valueOf(deviceUpdateDto.getState()));
        return deviceRepository.save(device);

    }

    public Boolean remove(String uuid) {

        LOG.info("Called service method to remove device by uuid");
        return deviceRepository.remove(UUID.fromString(uuid));

    }


    public Boolean removeCustomerDevices(String customerId) {

        LOG.info("Called service method to remove customer devices ");
        Boolean isDeleted;
        List<Device> devices = deviceRepository.getDevicesByCustomerId(customerId);
        if (devices.isEmpty()) {

            LOG.error("Customer with id: " + customerId + " hasn't devices");
            return false;
        }

        for (Device device : devices) {

            isDeleted = deviceRepository.remove(device.getUuid());

            if (!isDeleted) {

                LOG.error("Error removing device with uuid: " + device.getUuid());
                return false;
            }

        }
        return true;

    }
}
