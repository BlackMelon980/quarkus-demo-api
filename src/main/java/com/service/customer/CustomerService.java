package com.service.customer;

import com.model.RegexConfig;
import com.model.customer.Customer;
import com.model.customer.CustomerDto;
import com.model.customer.CustomerUpdateDto;
import com.repository.customer.CustomerRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@ApplicationScoped
public class CustomerService {

    CustomerRepository customerRepository;

    CustomerService(CustomerRepository customerRepository) {

        this.customerRepository = customerRepository;
    }

    public Customer save(@Valid CustomerDto customerDto) {

        Customer customer = new Customer(customerDto.getFirstName(), customerDto.getLastName(),
                customerDto.getFiscalCode(), customerDto.getAddress());

        return customerRepository.save(customer);
    }

    public Customer getByFiscalCode(@NotBlank @Pattern(regexp = RegexConfig.FISCAL_CODE_REGEX) String fiscalCode) {

        return customerRepository.getByFiscalCode(fiscalCode);
    }

    public Boolean remove(@NotBlank @Pattern(regexp = RegexConfig.FISCAL_CODE_REGEX) String fiscalCode) {

        return customerRepository.remove(fiscalCode);
    }

    public Customer update(@Valid CustomerUpdateDto customerUpdateDto) {

        Customer customer = customerRepository.getByFiscalCode(customerUpdateDto.getFiscalCode());
        if (customer == null) {
            return null;
        }
        customer.setAddress(customerUpdateDto.getAddress());
        return customerRepository.save(customer);
    }
}
