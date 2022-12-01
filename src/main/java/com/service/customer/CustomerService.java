package com.service.customer;

import com.model.RegexConfig;
import com.model.customer.Customer;
import com.model.customer.CustomerDevicesResponse;
import com.model.customer.CustomerDto;
import com.model.customer.CustomerUpdateDto;
import com.model.device.Device;
import com.repository.customer.CustomerRepository;
import com.service.device.DeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@ApplicationScoped
public class CustomerService {

    CustomerRepository customerRepository;
    DeviceService deviceService;
    final Logger LOG = LoggerFactory.getLogger(String.valueOf(getClass()));

    CustomerService(CustomerRepository customerRepository, DeviceService deviceService) {

        this.customerRepository = customerRepository;
        this.deviceService = deviceService;
    }

    public Customer save(CustomerDto customerDto) {

        LOG.info("Called service method to save customer");
        Customer customer = new Customer(customerDto.getFirstName(), customerDto.getLastName(),
                customerDto.getFiscalCode(), customerDto.getAddress());

        return customerRepository.save(customer);
    }

    public Customer getByFiscalCode(@NotBlank @Pattern(regexp = RegexConfig.FISCAL_CODE_REGEX) String fiscalCode) {

        LOG.info("Called service method to get customer by fiscalCode");
        return customerRepository.getByFiscalCode(fiscalCode);
    }

    public Boolean remove(String fiscalCode) {

        LOG.info("Called service method to remove customer by fiscalCode");
        deviceService.removeCustomerDevices(fiscalCode);
        LOG.info("Devices removed successfully.");

        return customerRepository.remove(fiscalCode);
    }

    public Customer update(CustomerUpdateDto customerUpdateDto) {

        LOG.info("Called service method to update customer");
        Customer customer = customerRepository.getByFiscalCode(customerUpdateDto.getFiscalCode());
        if (customer == null) {
            LOG.error("Customer with fiscalCode: " + customerUpdateDto.getFiscalCode() + " does not exist.");
            return null;
        }
        customer.setAddress(customerUpdateDto.getAddress());
        return customerRepository.save(customer);
    }

    public CustomerDevicesResponse getCustomerAndDevices(String fiscalCode) {

        LOG.info("Called service method to get customer with devices");
        Customer customer = customerRepository.getByFiscalCode(fiscalCode);
        if (customer == null) {

            LOG.error("Customer with fiscalCode: " + fiscalCode + " does not exist.");
            return null;
        }

        List<Device> devices = deviceService.getByCustomerId(fiscalCode);
        return new CustomerDevicesResponse(customer, devices);

    }
}
