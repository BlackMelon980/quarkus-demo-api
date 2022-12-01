package com.service.customer;

import com.model.customer.Customer;
import com.model.customer.CustomerDevicesResponse;
import com.model.customer.CustomerDto;
import com.model.customer.CustomerUpdateDto;
import com.model.device.Device;
import com.model.device.DeviceState;
import com.repository.customer.CustomerRepository;
import com.repository.device.DeviceRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
public class CustomerServiceTest {

    @Inject
    CustomerService customerService;

    @InjectMock
    CustomerRepository customerRepository;
    @InjectMock
    DeviceRepository deviceRepository;
    Map<UUID, Device> deviceMap;
    Map<String, Customer> customerMap;

    @BeforeEach
    void setUp() {

        customerMap = new HashMap<>();
        deviceMap = new HashMap<>();
        Customer firstCustomer = new Customer("Francesca", "Capodanno", "CPDFNC96L42F839M", "Viale Europa");
        customerMap.put("CPDFNC96L42F839M", firstCustomer);

        when(customerRepository.save(any(Customer.class))).thenAnswer(i -> {
            Customer customer = i.getArgument(0);
            customerMap.put(customer.getFiscalCode(), customer);
            return customer;
        });

        when(customerRepository.getByFiscalCode(any(String.class))).thenAnswer(i -> {
            String fiscalCode = i.getArgument(0);
            return customerMap.get(fiscalCode);
        });

        when(customerRepository.remove(any(String.class))).thenAnswer(i -> {
            String fiscalCode = i.getArgument(0);
            Customer customer = customerMap.remove(fiscalCode);
            return customer != null;
        });

        when(deviceRepository.remove(any(UUID.class))).thenAnswer(i -> {
            UUID uuid = i.getArgument(0);
            Device device = deviceMap.remove(uuid);
            return device != null;
        });

        when(deviceRepository.getDevicesByCustomerId(any(String.class))).thenAnswer(i -> {
            String customerId = i.getArgument(0);
            return deviceMap.values().stream().filter(d -> d.getCustomerId().equals(customerId)).collect(Collectors.toList());
        });

    }

    private Device createAndInsertDevice() {
        Device device = new Device("CPDFNC96L42F839M", DeviceState.ACTIVE);
        deviceMap.put(device.getUuid(), device);
        return device;
    }

    @Test
    void shouldSaveCustomerWithSuccess() {

        CustomerDto customerDto = new CustomerDto("Rosaria", "Capodanno", "CPDRSC96L42F839M", "Viale Europa2");
        Customer savedCustomer = customerService.save(customerDto);
        Assertions.assertEquals("CPDRSC96L42F839M", savedCustomer.getFiscalCode());

    }

    @Test
    void shouldFindCustomerByFiscalCode() {

        Customer customer = customerService.getByFiscalCode("CPDFNC96L42F839M");
        Assertions.assertEquals("CPDFNC96L42F839M", customer.getFiscalCode());
        Assertions.assertEquals("Francesca", customer.getFirstName());
        Assertions.assertEquals("Capodanno", customer.getLastName());

    }

    @Test
    void shouldNotFindCustomerWithInvalidFiscalCode() {

        Assertions.assertThrows(Exception.class, () -> {
            customerService.getByFiscalCode("CPDFNC96L42F839M123");
        });

    }

    @Test
    void shouldNotFindCustomerWithWrongFiscalCode() {

        Customer customer = customerService.getByFiscalCode("CPDFNC96L42F839R");
        Assertions.assertNull(customer);
    }

    @Test
    void shouldFindCustomerAndDevices() {

        Device device = createAndInsertDevice();
        CustomerDevicesResponse response = customerService.getCustomerAndDevices("CPDFNC96L42F839M");
        Assertions.assertEquals("CPDFNC96L42F839M", response.getCustomer().getFiscalCode());
        Assertions.assertEquals(device.getUuid(), response.getDevices().get(0).getUuid());

    }

    @Test
    void shouldNotFindCustomerAndDevicesWithWrongFiscalCode() {

        CustomerDevicesResponse response = customerService.getCustomerAndDevices("CPDFNC96L42F839R");
        Assertions.assertNull(response);

    }

    @Test
    void shouldUpdateCustomer() {

        CustomerUpdateDto customerUpdateDto = new CustomerUpdateDto("CPDFNC96L42F839M", "Via Roma");
        Customer updatedCustomer = customerService.update(customerUpdateDto);

        Assertions.assertEquals("CPDFNC96L42F839M", updatedCustomer.getFiscalCode());
        Assertions.assertEquals("Via Roma", updatedCustomer.getAddress());

    }

    @Test
    void cantUpdateCustomerWithWrongFiscalCode() {

        CustomerUpdateDto customerUpdateDto = new CustomerUpdateDto("CPDFNC96L42F839D", "Via Roma");
        Customer updatedCustomer = customerService.update(customerUpdateDto);
        Assertions.assertNull(updatedCustomer);

    }

    @Test
    void shouldRemoveCustomer() {

        Assertions.assertTrue(customerService.remove("CPDFNC96L42F839M"));

    }

    @Test
    void cantRemoveCustomerWithWrongFiscalCode() {

        Assertions.assertFalse(customerService.remove("CPDFNC96L42F839D"));

    }

}
