package com.controller.customer;

import com.model.customer.Customer;
import com.model.customer.CustomerDto;
import com.model.customer.CustomerUpdateDto;
import com.service.customer.CustomerService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerController {

    CustomerService customerService;

    CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @POST
    public Response saveCustomer(CustomerDto customerDto) {

        Customer customer = customerService.save(customerDto);

        if (customer == null) {

            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity("Error saving customer.").build();
        }

        return Response.ok(customer).build();

    }

    @GET
    public Response getCustomer(@QueryParam("fiscalcode") String fiscalCode) {

        Customer customer = customerService.getByFiscalCode(fiscalCode);
        if (customer == null) {

            return Response.status(Response.Status.NOT_FOUND).entity("Customer with fiscalcode: " + fiscalCode + " does not exist.").build();
        }

        return Response.ok(customer).build();
    }

    @PUT
    public Response updateCustomer(CustomerUpdateDto customerUpdateDto) {

        Customer customer = customerService.update(customerUpdateDto);
        if (customer == null) {

            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity("Error saving customer.").build();
        }

        return Response.ok(customer).build();
    }

    @DELETE
    public Response deleteCustomer(@QueryParam("fiscalcode") String fiscalCode) {

        Boolean isDeleted = customerService.remove(fiscalCode);
        return isDeleted ? Response.status(Response.Status.NO_CONTENT).build() : Response.status(Response.Status.NOT_FOUND).build();
    }
}
