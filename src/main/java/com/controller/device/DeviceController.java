package com.controller.device;

import com.model.RegexConfig;
import com.model.device.Device;
import com.model.device.DeviceDto;
import com.model.device.DeviceUpdateDto;
import com.service.device.DeviceService;
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
import java.util.List;

@Path("/devices")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeviceController {

    DeviceService deviceService;
    final Logger LOG = LoggerFactory.getLogger(String.valueOf(getClass()));

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @POST
    @Operation(summary = "Save device", description = "Save a device. An existing customer's fiscal code is required.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Device.class))),
            @APIResponse(responseCode = "400", description = "Error saving device"),
            @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public Response saveDevice(@Valid DeviceDto deviceDto) {

        LOG.info("Called save device with customerId: " + deviceDto.getCustomerId());
        Device device = deviceService.save(deviceDto);
        if (device == null) {

            LOG.info("Error saving device with customerId: " + deviceDto.getCustomerId());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity("Error saving device with customerId: " + deviceDto.getCustomerId()).build();
        }

        LOG.info("Device has been saved with success.");
        return Response.ok(device).build();

    }

    @GET
    @Operation(summary = "Retrieve device", description = "Retrieve a device by UUID.")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Device.class))),
            @APIResponse(responseCode = "404", description = "device does not exist"),
            @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public Response getDeviceByUUID(@QueryParam("uuid") @Pattern(regexp = RegexConfig.UUID_REGEX) @NotBlank String uuid) {

        LOG.info("Called get device by uuid: " + uuid);
        Device device = deviceService.getByUUID(uuid);
        if (device == null) {

            LOG.error("Device with uuid: " + uuid + " does not exist.");
            return Response.status(Response.Status.NOT_FOUND).entity("Device with uuid: " + uuid + " does not exist.").build();
        }

        return Response.ok(device).build();

    }

    @GET
    @Path("/getByCustomer")
    @Operation(summary = "Retrieve device by customer fiscal code")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Device.class))),
            @APIResponse(responseCode = "404", description = "No devices for customer"),
            @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public Response getDevicesByCustomer(@QueryParam("customerId") @NotBlank String customerId) {

        LOG.info("Called get device by customerId: " + customerId);
        List<Device> devices = deviceService.getByCustomerId(customerId);
        if (devices.isEmpty()) {

            LOG.error("No devices for customer with id: " + customerId);
            return Response.status(Response.Status.NOT_FOUND).entity("No devices for customer with id: " + customerId).build();
        }

        return Response.ok(devices).build();

    }

    @PUT
    @Operation(summary = "Update device")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Success",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Device.class))),
            @APIResponse(responseCode = "400", description = "Error saving device"),
            @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public Response updateDevice(@Valid DeviceUpdateDto deviceUpdateDto) {

        LOG.info("Called update device with uuid: " + deviceUpdateDto.getUuid());
        Device device = deviceService.update(deviceUpdateDto);
        if (device == null) {

            LOG.error("Error saving device with uuid: " + deviceUpdateDto.getUuid());
            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity("Error saving device with uuid: " + deviceUpdateDto.getUuid()).build();
        }

        LOG.info("Device has been saved with success.");
        return Response.ok(device).build();

    }

    @DELETE
    @Operation(summary = "Delete device")
    @APIResponses(value = {
            @APIResponse(responseCode = "204", description = "Success"),
            @APIResponse(responseCode = "404", description = "device does not exist"),
            @APIResponse(responseCode = "500", description = "Internal server error")
    })
    public Response removeDevice(@QueryParam("uuid") @Pattern(regexp = RegexConfig.UUID_REGEX) @NotBlank String uuid) {

        LOG.info("Called delete device with uuid: " + uuid);
        Boolean isDeleted = deviceService.remove(uuid);
        if (!isDeleted) {

            LOG.error("Error deleting device");
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        LOG.info("Device removed successfully.");
        return Response.status(Response.Status.NO_CONTENT).build();

    }
}
