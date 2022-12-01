package com.controller.device;

import com.model.customer.Customer;
import com.model.device.Device;
import com.model.device.DeviceDto;
import com.model.device.DeviceState;
import com.model.device.DeviceUpdateDto;
import com.repository.customer.CustomerRepository;
import com.repository.device.DeviceRepository;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
@TestHTTPEndpoint(DeviceController.class)
public class DeviceControllerTest {

    @InjectMock
    DeviceRepository deviceRepository;
    @InjectMock
    CustomerRepository customerRepository;
    Map<UUID, Device> deviceMap;
    Map<String, Customer> customerMap;

    @BeforeEach
    void setUp() {

        deviceMap = new HashMap<>();
        customerMap = new HashMap<>();

        when(customerRepository.getByFiscalCode(any(String.class))).thenAnswer(i -> {
            String fiscalCode = i.getArgument(0);
            return customerMap.get(fiscalCode);
        });

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

    public void insertCustomer() {

        Customer firstCustomer = new Customer("Francesca", "Capodanno", "CPDFNC96L42F839M", "Viale Europa");
        customerMap.put("CPDFNC96L42F839M", firstCustomer);
    }

    private Device createAndInsertDevice() {
        Device device = new Device("CPDFNC96L42F839M", DeviceState.ACTIVE);
        deviceMap.put(device.getUuid(), device);
        return device;
    }

    @Test
    void shouldSaveDevice() {

        insertCustomer();

        DeviceDto deviceDto = new DeviceDto("CPDFNC96L42F839M", "ACTIVE");
        given()
                .body(deviceDto)
                .contentType(ContentType.JSON)
                .when().post()
                .then()
                .statusCode(200);

    }

    @Test
    void shouldNotSaveDeviceWithoutAllParams() {

        insertCustomer();

        DeviceDto deviceDto = new DeviceDto(null, "ACTIVE");
        given()
                .body(deviceDto)
                .contentType(ContentType.JSON)
                .when().post()
                .then()
                .statusCode(400);

    }

    @Test
    void shouldNotSaveDeviceWithoutAnExistingCustomer() {

        DeviceDto deviceDto = new DeviceDto("CPDFNC96L42F839M", "ACTIVE");
        given()
                .body(deviceDto)
                .contentType(ContentType.JSON)
                .when().post()
                .then()
                .statusCode(400);

    }

    @Test
    void shouldNotSaveDeviceWithInvalidCustomerId() {

        insertCustomer();
        DeviceDto deviceDto = new DeviceDto("123456", "ACTIVE");
        given()
                .body(deviceDto)
                .contentType(ContentType.JSON)
                .when().post()
                .then()
                .statusCode(400);
    }

    @Test
    void shouldNotSaveDeviceWithInvalidDeviceState() {

        insertCustomer();
        DeviceDto deviceDto = new DeviceDto("CPDFNC96L42F839M", "DELETED");
        given()
                .body(deviceDto)
                .contentType(ContentType.JSON)
                .when().post()
                .then()
                .statusCode(400);

    }

    @Test
    void shouldNotSaveMoreThanTwoDevices() {

        insertCustomer();
        createAndInsertDevice();
        createAndInsertDevice();
        DeviceDto deviceDto = new DeviceDto("CPDFNC96L42F839M", "ACTIVE");
        given()
                .body(deviceDto)
                .contentType(ContentType.JSON)
                .when().post()
                .then()
                .statusCode(400);

    }

    @Test
    void shouldFindDeviceByUUID() {

        Device device = createAndInsertDevice();
        given()
                .param("uuid", device.getUuid().toString())
                .contentType(ContentType.JSON)
                .when().get()
                .then()
                .statusCode(200);

    }

    @Test
    void shouldNotFindDeviceWithInvalidUUID() {

        createAndInsertDevice();
        given()
                .param("uuid", "123456")
                .contentType(ContentType.JSON)
                .when().get()
                .then()
                .statusCode(400);

    }

    @Test
    void shouldNotFindDeviceWithWrongUUID() {

        createAndInsertDevice();
        given()
                .param("uuid", "1345cf63-0a6c-4f75-8422-80c38a76b79c")
                .contentType(ContentType.JSON)
                .when().get()
                .then()
                .statusCode(404);
    }

    @Test
    void shouldFindDevicesByCustomerId() {

        createAndInsertDevice();
        given()
                .param("customerId", "CPDFNC96L42F839M")
                .contentType(ContentType.JSON)
                .when().get("/getByCustomer")
                .then()
                .statusCode(200);
    }

    @Test
    void shouldNotFindDevicesWithWrongCustomerId() {

        createAndInsertDevice();
        given()
                .param("customerId", "CPDFNC96L42F839R")
                .contentType(ContentType.JSON)
                .when().get("/getByCustomer")
                .then()
                .statusCode(404);
    }

    @Test
    void shouldUpdateDevice() {

        Device device = createAndInsertDevice();
        DeviceUpdateDto deviceUpdateDto = new DeviceUpdateDto(device.getUuid().toString(), "INACTIVE");
        given()
                .body(deviceUpdateDto)
                .contentType(ContentType.JSON)
                .when().put()
                .then()
                .statusCode(200);

    }

    @Test
    void shouldNotUpdateDeviceWithoutAllParams() {

        DeviceUpdateDto deviceUpdateDto = new DeviceUpdateDto(null, "INACTIVE");
        given()
                .body(deviceUpdateDto)
                .contentType(ContentType.JSON)
                .when().put()
                .then()
                .statusCode(400);
    }

    @Test
    void shouldNotUpdateDeviceWithInvalidUUID() {

        Device device = createAndInsertDevice();
        DeviceUpdateDto deviceUpdateDto = new DeviceUpdateDto("12345", "INACTIVE");
        given()
                .body(deviceUpdateDto)
                .contentType(ContentType.JSON)
                .when().put()
                .then()
                .statusCode(400);

    }

    @Test
    void shouldNotUpdateDeviceWithInvalidDeviceState() {

        Device device = createAndInsertDevice();
        DeviceUpdateDto deviceUpdateDto = new DeviceUpdateDto(device.getUuid().toString(), "DELETED");
        given()
                .body(deviceUpdateDto)
                .contentType(ContentType.JSON)
                .when().put()
                .then()
                .statusCode(500);

    }

    @Test
    void shouldNotUpdateDeviceIfDoesNotExist() {

        createAndInsertDevice();
        DeviceUpdateDto deviceUpdateDto = new DeviceUpdateDto("1345cf63-0a6c-4f75-8422-80c38a76b79c", "INACTIVE");
        given()
                .body(deviceUpdateDto)
                .contentType(ContentType.JSON)
                .when().put()
                .then()
                .statusCode(400);

    }

    @Test
    void shouldRemoveDevice() {

        Device device = createAndInsertDevice();
        given()
                .param("uuid", device.getUuid().toString())
                .contentType(ContentType.JSON)
                .when().delete()
                .then()
                .statusCode(204);

    }

    @Test
    void shouldNotRemoveDeviceWithInvalidUUID() {

        given()
                .param("uuid", "123456")
                .contentType(ContentType.JSON)
                .when().delete()
                .then()
                .statusCode(400);

    }

    @Test
    void shouldNotRemoveDeviceIfDoesNotExist() {

        createAndInsertDevice();
        given()
                .param("uuid", "1345cf63-0a6c-4f75-8422-80c38a76b79c")
                .contentType(ContentType.JSON)
                .when().delete()
                .then()
                .statusCode(404);

    }
}
