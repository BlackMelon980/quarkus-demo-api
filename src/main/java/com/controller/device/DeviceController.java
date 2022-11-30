package com.controller.device;

import com.model.customer.Customer;
import com.model.device.Device;
import com.model.device.DeviceDto;
import com.model.device.DeviceUpdateDto;
import com.service.customer.CustomerService;
import com.service.device.DeviceService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/devices")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeviceController {

    DeviceService deviceService;
    CustomerService customerService;

    public DeviceController(DeviceService deviceService, CustomerService customerService) {
        this.deviceService = deviceService;
        this.customerService = customerService;
    }

    @POST
    public Response saveDevice(DeviceDto deviceDto) {

        Customer customer = customerService.getByFiscalCode(deviceDto.getCustomerId());
        if (customer == null) {

            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity("Customer with fiscal code: " + deviceDto.getCustomerId() + " does not exist.").build();
        }

        Device device = deviceService.save(deviceDto);
        if (device == null) {

            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity("Too many devices for customer with id: " + deviceDto.getCustomerId()).build();
        }

        return Response.ok(device).build();

    }

    @GET
    public Response getDeviceByUUID(@QueryParam("uuid") String uuid) {

        Device device = deviceService.getByUUID(uuid);
        if (device == null) {

            return Response.status(Response.Status.NOT_FOUND).entity("Device with uuid: " + uuid + " does not exist.").build();
        }

        return Response.ok(device).build();

    }

    @GET
    @Path("/getByCustomer")
    public Response getDevicesByCustomer(@QueryParam("customerId") String customerId) {

        List<Device> devices = deviceService.getByCustomerId(customerId);
        if (devices.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("No devices for customer with id: " + customerId).build();
        }

        return Response.ok(devices).build();

    }

    @PUT
    public Response updateDevice(DeviceUpdateDto deviceUpdateDto) {

        Device device = deviceService.update(deviceUpdateDto);
        if (device == null) {

            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity("Error saving device.").build();
        }

        return Response.ok(device).build();

    }

    @DELETE
    public Response removeDevice(@QueryParam("uuid") String uuid) {

        Boolean isDeleted = deviceService.remove(uuid);
        return isDeleted ? Response.status(Response.Status.NO_CONTENT).build() : Response.status(Response.Status.NOT_FOUND).build();

    }
}
