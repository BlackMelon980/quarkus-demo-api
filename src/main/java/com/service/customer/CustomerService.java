package com.service.customer;

import com.model.customer.Customer;
import com.model.customer.CustomerDto;
import com.repository.customer.CustomerRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CustomerService {

    CustomerRepository customerRepository;

    CustomerService(CustomerRepository customerRepository) {

        this.customerRepository = customerRepository;
    }

    public Customer save(CustomerDto customerdto) {

        Customer customer = new Customer(customerdto.getFirstName(), customerdto.getLastName(),
                customerdto.getFiscalCode(), customerdto.getAddress());

        return customerRepository.save(customer);
    }

    public Customer getByFiscalCode(String fiscalCode) {

        return customerRepository.getByFiscalCode(fiscalCode);
    }

    public Boolean remove(String fiscalCode) {

        return customerRepository.remove(fiscalCode);
    }
}
