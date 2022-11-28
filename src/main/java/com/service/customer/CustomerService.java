package com.service.customer;

import com.model.customer.Customer;
import com.model.customer.CustomerDto;
import com.model.customer.CustomerUpdateDto;
import com.repository.customer.CustomerRepository;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@ApplicationScoped
public class CustomerService {

    CustomerRepository customerRepository;

    CustomerService(CustomerRepository customerRepository) {

        this.customerRepository = customerRepository;
    }

    public Customer save(@Valid CustomerDto customerdto) {

        Customer customer = new Customer(customerdto.getFirstName(), customerdto.getLastName(),
                customerdto.getFiscalCode(), customerdto.getAddress());

        return customerRepository.save(customer);
    }

    public Customer getByFiscalCode(@NotBlank(message = "FiscalCode may not be blank") String fiscalCode) {

        return customerRepository.getByFiscalCode(fiscalCode);
    }

    public Boolean remove(@NotBlank(message = "FiscalCode may not be blank") String fiscalCode) {

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
