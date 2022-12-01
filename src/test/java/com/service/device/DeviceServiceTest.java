package com.service.device;

import com.model.device.Device;
import com.model.device.DeviceDto;
import com.model.device.DeviceState;
import com.model.device.DeviceUpdateDto;
import com.repository.device.DeviceRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
public class DeviceServiceTest {

    @Inject
    DeviceService deviceService;
    @InjectMock
    DeviceRepository deviceRepository;
    Map<UUID, Device> deviceMap;

    @BeforeEach
    void setUp() {

        deviceMap = new HashMap<>();

        when(deviceRepository.save(any(Device.class))).thenAnswer(i -> {
            Device device = i.getArgument(0);
            deviceMap.put(device.getUuid(), device);
            return device;
        });

        when(deviceRepository.getDevicesByCustomerId(any(String.class))).thenAnswer(i -> {
            String customerId = i.getArgument(0);
            return deviceMap.values().stream().filter(d -> d.getCustomerId().equals(customerId)).collect(Collectors.toList());
        });

        when(deviceRepository.getByUUID(any(UUID.class))).thenAnswer(i -> {
            UUID uuid = i.getArgument(0);
            return deviceMap.get(uuid);
        });

        when(deviceRepository.remove(any(UUID.class))).thenAnswer(i -> {
            UUID uuid = i.getArgument(0);
            Device device = deviceMap.remove(uuid);
            return device != null;
        });

    }

    private Device createAndInsertDevice() {
        Device device = new Device("CPDFNC96L42F839M", DeviceState.ACTIVE);
        deviceMap.put(device.getUuid(), device);
        return device;
    }

    @Test
    void shouldSaveDeviceWithSuccess() {

        DeviceDto deviceDto = new DeviceDto("CPDFNC96L42F839M", "ACTIVE");
        Device device = deviceService.save(deviceDto);
        Assertions.assertEquals("CPDFNC96L42F839M", device.getCustomerId());

    }

    @Test
    void shouldNotSaveDeviceWithoutAllParams() {

        DeviceDto deviceDto = new DeviceDto(null, "ACTIVE");
        Assertions.assertThrows(Exception.class, () -> {
            deviceService.save(deviceDto);
        });

    }

    @Test
    void shouldSaveTwoDevices() {

        Device firstDevice = createAndInsertDevice();
        DeviceDto deviceDto = new DeviceDto("CPDFNC96L42F839M", "ACTIVE");

        Device device = deviceService.save(deviceDto);
        Assertions.assertEquals("CPDFNC96L42F839M", device.getCustomerId());
        Assertions.assertNotEquals(firstDevice.getUuid(), device.getUuid());

    }

    @Test
    void shouldNotSaveMoreThanTwoDevices() {

        createAndInsertDevice();
        createAndInsertDevice();
        DeviceDto deviceDto = new DeviceDto("CPDFNC96L42F839M", "ACTIVE");

        Device device = deviceService.save(deviceDto);
        Assertions.assertNull(device);

    }

    @Test
    void shouldGetDeviceByUUID() {

        Device firstDevice = createAndInsertDevice();
        Device savedDevice = deviceService.getByUUID(firstDevice.getUuid().toString());
        Assertions.assertEquals(firstDevice.getUuid(), savedDevice.getUuid());

    }

    @Test
    void shouldNotFindDeviceWithInvalidUUID() {

        Assertions.assertThrows(Exception.class, () -> {
            deviceService.getByUUID("123456");
        });

    }

    @Test
    void shouldNotFindDeviceWithUUIDNull() {

        Assertions.assertThrows(Exception.class, () -> {
            deviceService.getByUUID(null);
        });
    }

    @Test
    void shouldNotFindDeviceWithWrongUUID() {

        createAndInsertDevice();
        Device device = deviceService.getByUUID("1345cf63-0a6c-4f75-8422-80c38a76b79c");
        Assertions.assertNull(device);

    }

    @Test
    void shouldFindDevicesByCustomerId() {

        createAndInsertDevice();
        List<Device> devices = deviceService.getByCustomerId("CPDFNC96L42F839M");
        Assertions.assertEquals(1, devices.size());

    }

    @Test
    void shouldNotFindDevicesWithWrongCustomerId() {

        createAndInsertDevice();
        List<Device> devices = deviceService.getByCustomerId("CPDFNC96L42F839R");
        Assertions.assertEquals(0, devices.size());

    }

    @Test
    void shouldUpdateDevice() {

        Device device = createAndInsertDevice();
        DeviceUpdateDto deviceUpdateDto = new DeviceUpdateDto(device.getUuid().toString(), "INACTIVE");
        Device savedDevice = deviceService.update(deviceUpdateDto);

        Assertions.assertEquals(device.getUuid(), savedDevice.getUuid());
        Assertions.assertEquals(DeviceState.INACTIVE, savedDevice.getState());

    }

    @Test
    void shouldNotUpdateDeviceWithInvalidUUID() {

        DeviceUpdateDto deviceUpdateDto = new DeviceUpdateDto("12345", "INACTIVE");
        Assertions.assertThrows(Exception.class, () -> {
            deviceService.update(deviceUpdateDto);
        });

    }

    @Test
    void shouldNotUpdateDeviceWithoutAllParams() {

        DeviceUpdateDto deviceUpdateDto = new DeviceUpdateDto(null, "INACTIVE");
        Assertions.assertThrows(Exception.class, () -> {
            deviceService.update(deviceUpdateDto);
        });

    }

    @Test
    void shouldNotUpdateDeviceWithInvalidState() {

        Device device = createAndInsertDevice();
        DeviceUpdateDto deviceUpdateDto = new DeviceUpdateDto(device.getUuid().toString(), "DELETE");
        Assertions.assertThrows(Exception.class, () -> {
            deviceService.update(deviceUpdateDto);
        });

    }

    @Test
    void shouldNotUpdateDeviceWithWrongUUID() {

        DeviceUpdateDto deviceUpdateDto = new DeviceUpdateDto("1345cf63-0a6c-4f75-8422-80c38a76b79c", "INACTIVE");
        Device device = deviceService.update(deviceUpdateDto);
        Assertions.assertNull(device);

    }

    @Test
    void shouldRemoveDeviceByUUID() {

        Device device = createAndInsertDevice();
        Boolean isDeleted = deviceService.remove(device.getUuid().toString());
        Assertions.assertTrue(isDeleted);

    }

    @Test
    void cantRemoveDeviceWithInvalidUUID() {

        Assertions.assertThrows(Exception.class, () -> {
            deviceService.remove("123456");
        });

    }

    @Test
    void cantRemoveDeviceWithWrongUUID() {

        createAndInsertDevice();
        Boolean isDeleted = deviceService.remove("1345cf63-0a6c-4f75-8422-80c38a76b79c");
        Assertions.assertFalse(isDeleted);

    }

    @Test
    void shouldRemoveCustomerDevices() {

        Device device = createAndInsertDevice();
        Boolean isDeleted = deviceService.removeCustomerDevices(device.getCustomerId());
        Assertions.assertTrue(isDeleted);

    }

    @Test
    void shouldNotRemoveCustomerDevicesIfThereAreNotDevices() {

        Boolean isDeleted = deviceService.removeCustomerDevices("CPDFNC96L42F839M");
        Assertions.assertFalse(isDeleted);

    }
}
