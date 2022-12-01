package com.repository.customer;

import com.model.customer.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.LinkedHashMap;
import java.util.Map;

@ApplicationScoped
public class CustomerRepository {

    Map<String, Customer> customers = new LinkedHashMap<>();
    final Logger LOG = LoggerFactory.getLogger(String.valueOf(getClass()));

    public Customer save(Customer customer) {

        LOG.info("Saving customer into repository");
        customers.put(customer.getFiscalCode(), customer);
        return customers.get(customer.getFiscalCode());

    }

    public Customer getByFiscalCode(String fiscalCode) {

        LOG.info("Searching customer into repository");
        return customers.get(fiscalCode);
    }

    public Boolean remove(String fiscalCode) {

        Customer customer = customers.remove(fiscalCode);
        return customer != null;

    }

}
