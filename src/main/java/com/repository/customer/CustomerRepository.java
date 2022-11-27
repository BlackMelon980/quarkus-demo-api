package com.repository.customer;

import com.model.customer.Customer;

import javax.enterprise.context.ApplicationScoped;
import java.util.LinkedHashMap;
import java.util.Map;

@ApplicationScoped
public class CustomerRepository {

    Map<String, Customer> customers = new LinkedHashMap<>();

    public Customer save(Customer customer) {

        customers.put(customer.getFiscalCode(), customer);
        return customers.get(customer.getFiscalCode());

    }

    public Customer getByFiscalCode(String fiscalCode) {

        return customers.get(fiscalCode);
    }

    public Boolean remove(String fiscalCode) {

        Customer customer = customers.remove(fiscalCode);
        return customer != null;

    }

}
