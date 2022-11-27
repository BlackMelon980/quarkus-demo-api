package com.service.customer;

import com.model.customer.Customer;
import com.repository.customer.CustomerRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CustomerService {

    CustomerRepository customerRepository;

    CustomerService(CustomerRepository customerRepository) {

        this.customerRepository = customerRepository;
    }

    public boolean save(Customer customer) {

        return customerRepository.save(customer);
    }

    public Customer getByFiscalCode(String fiscalCode) {

        return customerRepository.getByFiscalCode(fiscalCode);
    }

    public boolean remove(String fiscalCode) {

        return customerRepository.remove(fiscalCode);
    }
}
