package com.controller.customer;

import com.model.customer.Customer;
import com.model.customer.CustomerDto;
import com.model.customer.CustomerUpdateDto;
import com.repository.customer.CustomerRepository;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
@TestHTTPEndpoint(CustomerController.class)
public class CustomerControllerTest {

    @InjectMock
    CustomerRepository customerRepository;


    @BeforeEach
    void setUp() {

        Map<String, Customer> customerMap = new HashMap<>();
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

    }

    @Test
    void shouldSaveCustomer() {

        CustomerDto customerDto = new CustomerDto("Rosaria", "Capodanno", "CPDRSC96L42F839M", "Viale Europa2");

        given()
                .body(customerDto)
                .contentType(ContentType.JSON)
                .when().post()
                .then()
                .statusCode(200);

    }

    @Test
    void shouldNotSaveCustomerWithoutAllParams() {

        CustomerDto customerDto = new CustomerDto(null, "Capodanno", "CPDRSC96L42F839M", "Viale Europa2");

        given()
                .body(customerDto)
                .contentType(ContentType.JSON)
                .when().post()
                .then()
                .statusCode(400);

    }

    @Test
    void shouldNotSaveCustomerWithInvalidFiscalCode() {

        CustomerDto customerDto = new CustomerDto("Rosaria", "Capodanno", "CPDRSC96L42F839M123", "Viale Europa2");

        given()
                .body(customerDto)
                .contentType(ContentType.JSON)
                .when().post()
                .then()
                .statusCode(400);

    }

    @Test
    void whenGetCustomerByFiscalCode_TheCustomerShouldBeFound() {

        given()
                .param("fiscalCode", "CPDFNC96L42F839M")
                .contentType(ContentType.JSON)
                .when().get()
                .then()
                .statusCode(200);
    }

    @Test
    void whenGetCustomerByWrongFiscalCode_ShouldReturn404() {

        given()
                .param("fiscalCode", "CPDFNC96L42F839R")
                .contentType(ContentType.JSON)
                .when().get()
                .then()
                .statusCode(404);
    }

    @Test
    void shouldNotFindCustomerWithInvalidFiscalCode() {

        given()
                .param("fiscalCode", "1234567")
                .contentType(ContentType.JSON)
                .when().get()
                .then()
                .statusCode(400);
    }

    @Test
    void shouldFindCustomerAndDevices() {

        given()
                .param("fiscalCode", "CPDFNC96L42F839M")
                .contentType(ContentType.JSON)
                .when().get("/devices")
                .then()
                .statusCode(200);

    }

    @Test
    void shouldNotFindCustomerAndDevicesWithWrongFiscalCode() {

        given()
                .param("fiscalCode", "CPDFNC96L42F839R")
                .contentType(ContentType.JSON)
                .when().get("/devices")
                .then()
                .statusCode(404);

    }

    @Test
    void shouldNotFindCustomerAndDevicesWithInvalidFiscalCode() {

        given()
                .param("fiscalCode", "1234567")
                .contentType(ContentType.JSON)
                .when().get("/devices")
                .then()
                .statusCode(400);

    }

    @Test
    void shouldUpdateCustomer() {

        CustomerUpdateDto customerUpdateDto = new CustomerUpdateDto("CPDFNC96L42F839M", "Via Roma");

        given()
                .body(customerUpdateDto)
                .contentType(ContentType.JSON)
                .when().put()
                .then()
                .statusCode(200);

    }

    @Test
    void shouldNotUpdateCustomerWithWrongFiscalCode() {

        CustomerUpdateDto customerUpdateDto = new CustomerUpdateDto("CPDFNC96L42F839R", "Via Roma");

        given()
                .body(customerUpdateDto)
                .contentType(ContentType.JSON)
                .when().put()
                .then()
                .statusCode(400);
    }

    @Test
    void shouldNotUpdateCustomerWithoutAllParams() {

        CustomerUpdateDto customerUpdateDto = new CustomerUpdateDto("CPDFNC96L42F839M", null);

        given()
                .body(customerUpdateDto)
                .contentType(ContentType.JSON)
                .when().put()
                .then()
                .statusCode(400);

    }

    @Test
    void shouldNotUpdateCustomerWithInvalidFiscalCode() {

        CustomerUpdateDto customerUpdateDto = new CustomerUpdateDto("1234567", "Via Roma");
        given()
                .body(customerUpdateDto)
                .contentType(ContentType.JSON)
                .when().put()
                .then()
                .statusCode(400);
    }

    @Test
    void shouldRemoveCustomer() {

        given()
                .param("fiscalCode", "CPDFNC96L42F839M")
                .contentType(ContentType.JSON)
                .when().delete()
                .then()
                .statusCode(204);

    }

    @Test
    void shouldNotRemoveCustomerWithWrongFiscalCode() {

        given()
                .param("fiscalCode", "CPDFNC96L42F839R")
                .contentType(ContentType.JSON)
                .when().delete()
                .then()
                .statusCode(404);

    }

    @Test
    void shouldNotRemoveCustomerWithInvalidFiscalCode() {

        given()
                .param("fiscalCode", "123456765")
                .contentType(ContentType.JSON)
                .when().delete()
                .then()
                .statusCode(400);
    }
}
