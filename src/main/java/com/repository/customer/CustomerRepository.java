package com.repository.customer;

import com.model.customer.Customer;

import javax.enterprise.context.ApplicationScoped;
import java.util.LinkedHashMap;
import java.util.Map;

@ApplicationScoped
public class CustomerRepository {

    Map<String, Customer> customers = new LinkedHashMap<>();

    public boolean save(Customer customer) {

        customers.put(customer.getFiscalCode(), customer);
        return customers.containsKey(customer.getFiscalCode());

    }

    public Customer getByFiscalCode(String fiscalCode) {

        return customers.get(fiscalCode);
    }

    public boolean remove(String fiscalCode) {

        customers.remove(fiscalCode);
        return !customers.containsKey(fiscalCode);

    }

}
