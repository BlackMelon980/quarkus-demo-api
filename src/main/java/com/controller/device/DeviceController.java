package com.controller.device;

import com.model.device.Device;
import com.model.device.DeviceDto;
import com.model.device.DeviceUpdateDto;
import com.service.device.DeviceService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/devices")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeviceController {

    DeviceService deviceService;

    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @POST
    public Response saveDevice(DeviceDto deviceDto) {

        Device device = deviceService.save(deviceDto);
        if (device == null) {

            return Response.status(Response.Status.BAD_REQUEST.getStatusCode()).entity("Error saving device.").build();
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
