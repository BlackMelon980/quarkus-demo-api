package com.controller.customer;

import com.model.RegexConfig;
import com.model.customer.Customer;
import com.model.customer.CustomerDevicesResponse;
import com.model.customer.CustomerDto;
import com.model.customer.CustomerUpdateDto;
import com.service.customer.CustomerService;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerController {

    CustomerService customerService;
    final Logger LOG = LoggerFactory.getLogger(String.valueOf(getClass()));

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @POST
    @Operation(summary = "Save customer")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class))),
            @APIResponse(responseCode = "400", description = "Error saving customer"),
            @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public Response saveCustomer(@Valid CustomerDto customerDto) {

        LOG.info("Called save customer with fiscalCode: " + customerDto.getFiscalCode());
        Customer customer = customerService.save(customerDto);

        if (customer == null) {
            LOG.error("Error saving customer with fiscalCode: " + customerDto.getFiscalCode());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity("Error saving customer.").build();
        }

        LOG.info("Customer has been saved with success.");
        return Response.ok(customer).build();

    }

    @GET
    @Operation(summary = "Retrieve customer", description = "Retrieve a customer by fiscal code.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class))),
            @APIResponse(responseCode = "404", description = "Customer does not exist"),
            @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public Response getCustomer(@QueryParam("fiscalcode") @NotBlank @Pattern(regexp = RegexConfig.FISCAL_CODE_REGEX) String fiscalCode) {

        LOG.info("Called get customer by fiscalCode: " + fiscalCode);
        Customer customer = customerService.getByFiscalCode(fiscalCode);

        if (customer == null) {
            LOG.error("Customer with fiscalcode: " + fiscalCode + " does not exist.");
            return Response.status(Response.Status.NOT_FOUND).entity("Customer with fiscalcode: " + fiscalCode + " does not exist.").build();
        }

        return Response.ok(customer).build();
    }

    @GET
    @Path("/devices")
    @Operation(summary = "Retrieve customer and devices", description = "Retrieve a customer by fiscal code and all associated devices.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = CustomerDevicesResponse.class))),
            @APIResponse(responseCode = "404", description = "Customer does not exist"),
            @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public Response getCustomerAndDevices(@QueryParam("fiscalcode") @NotBlank @Pattern(regexp = RegexConfig.FISCAL_CODE_REGEX) String fiscalCode) {

        LOG.info("Called get customer and devices by fiscalCode: " + fiscalCode);
        CustomerDevicesResponse response = customerService.getCustomerAndDevices(fiscalCode);
        if (response == null) {

            LOG.error("Customer with fiscalcode: " + fiscalCode + " does not exist.");
            return Response.status(Response.Status.NOT_FOUND).entity("Customer with fiscalcode: " + fiscalCode + " does not exist.").build();
        }

        return Response.ok(response).build();
    }

    @PUT
    @Operation(summary = "Update customer")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Customer.class))),
            @APIResponse(responseCode = "400", description = "Error saving customer"),
            @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public Response updateCustomer(@Valid CustomerUpdateDto customerUpdateDto) {

        LOG.info("Called update customer with fiscalCode: " + customerUpdateDto.getFiscalCode());
        Customer customer = customerService.update(customerUpdateDto);
        if (customer == null) {

            LOG.error("Error saving customer with fiscalcode: " + customerUpdateDto.getFiscalCode());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity("Error saving customer.").build();
        }

        LOG.info("Customer has been saved with success.");
        return Response.ok(customer).build();
    }

    @DELETE
    @Operation(summary = "Deletes customer", description = "Delete a customer and all associated devices.")
    @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "Success"),
            @APIResponse(responseCode = "404", description = "Customer does not exist"),
            @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public Response deleteCustomer(@QueryParam("fiscalcode") @NotBlank @Pattern(regexp = RegexConfig.FISCAL_CODE_REGEX) String fiscalCode) {

        LOG.info("Called delete customer with fiscalCode: " + fiscalCode);
        Boolean isDeleted = customerService.remove(fiscalCode);
        if (!isDeleted) {

            LOG.error("Customer with fiscal code: " + fiscalCode + " does not exist.");
            return Response.status(Response.Status.NOT_FOUND).entity("Customer with fiscal code: " + fiscalCode + " does not exist.").build();
        }

        LOG.info("Customer removed successfully.");
        return Response.status(Response.Status.NO_CONTENT).build();

    }
}
