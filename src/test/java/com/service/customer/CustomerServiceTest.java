package com.service.customer;

import com.model.customer.Customer;
import com.model.customer.CustomerDto;
import com.model.customer.CustomerUpdateDto;
import com.repository.customer.CustomerRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
public class CustomerServiceTest {

    @Inject
    CustomerService customerService;

    @InjectMock
    CustomerRepository customerRepository;

    Map<String, Customer> customerMap;

    @BeforeEach
    void setUp() {

        customerMap = new HashMap<>();

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

    }

    private void insertFirstCustomerInMap() {

        Customer firstCustomer = new Customer("Francesca", "Capodanno", "CPDFNC96L42F839M", "Viale Europa");
        customerMap.put("CPDFNC96L42F839M", firstCustomer);
    }

    @Test
    void saveCustomerWithSuccess() {

        CustomerDto customerDto = new CustomerDto("Rosaria", "Capodanno", "CPDRSC96L42F839M", "Viale Europa2");
        Customer savedCustomer = customerService.save(customerDto);
        Assertions.assertEquals("CPDRSC96L42F839M", savedCustomer.getFiscalCode());

    }

    @Test
    void cantSaveCustomerWithoutAllParams() {

        CustomerDto customerDto = new CustomerDto("Rosaria", "Capodanno", null, "Viale Europa");
        Assertions.assertThrows(Exception.class, () -> {
            customerService.save(customerDto);
        });

    }

    @Test
    void cantSaveCustomerWithWrongFiscalcode() {

        CustomerDto customerDto = new CustomerDto("Rosaria", "Capodanno", "CPDFNC96L42F839Msdasd", "Viale Europa");
        Assertions.assertThrows(Exception.class, () -> {
            customerService.save(customerDto);
        });

    }

    @Test
    void getCustomerByFiscalCode() {

        insertFirstCustomerInMap();

        Customer customer = customerService.getByFiscalCode("CPDFNC96L42F839M");
        Assertions.assertEquals("CPDFNC96L42F839M", customer.getFiscalCode());
        Assertions.assertEquals("Francesca", customer.getFirstName());
        Assertions.assertEquals("Capodanno", customer.getLastName());

    }

    @Test
    void cantGetCustomerWithWrongFiscalCode() {

        Customer customer = customerService.getByFiscalCode("CPDFNC96L42F839M123");
        Assertions.assertNull(customer);

    }

    @Test
    void updateCustomer() {

        insertFirstCustomerInMap();
        CustomerUpdateDto customerUpdateDto = new CustomerUpdateDto("CPDFNC96L42F839M", "Via Roma");
        Customer updatedCustomer = customerService.update(customerUpdateDto);

        Assertions.assertEquals("CPDFNC96L42F839M", updatedCustomer.getFiscalCode());
        Assertions.assertEquals("Via Roma", updatedCustomer.getAddress());

    }

    @Test
    void cantUpdateCustomerWithoutAllParams() {

        CustomerUpdateDto customerUpdateDto = new CustomerUpdateDto(null, "Via Roma");
        Assertions.assertThrows(Exception.class, () -> {
            customerService.update(customerUpdateDto);
        });

    }

    @Test
    void cantUpdateCustomerWithWrongFiscalCode() {

        CustomerUpdateDto customerUpdateDto = new CustomerUpdateDto("CPDFNC96L42F839D", "Via Roma");
        Customer updatedCustomer = customerService.update(customerUpdateDto);
        Assertions.assertNull(updatedCustomer);

    }

    @Test
    void cantUpdateCustomerWithInvalidFiscalCode() {

        CustomerUpdateDto customerUpdateDto = new CustomerUpdateDto("CPDFNC96L42F839M123", "Via Roma");
        Assertions.assertThrows(Exception.class, () -> {
            customerService.update(customerUpdateDto);
        });

    }

    @Test
    void removeCustomer() {

        insertFirstCustomerInMap();
        Assertions.assertTrue(customerService.remove("CPDFNC96L42F839M"));

    }

    @Test
    void cantRemoveCustomerWithWrongFiscalCode() {

        Assertions.assertFalse(customerService.remove("CPDFNC96L42F839D"));

    }

}
