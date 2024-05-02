package com.su.FlightScheduler.Controller;

import com.su.FlightScheduler.Entity.CabinCrewEntity;
import com.su.FlightScheduler.Service.AttendantService;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/attendants")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AttendantController {

    @Inject
    private AttendantService attendantService;

    @POST
    public Response addAttendant(CabinCrewEntity attendant) {
        attendantService.addAttendant(attendant);
        return Response.status(Response.Status.CREATED).entity(attendant).build();
    }

    @GET
    @Path("/{id}")
    public Response getAttendant(@PathParam("id") int id) {
        CabinCrewEntity attendant = attendantService.findAttendantById(id);
        if (attendant == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(attendant).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateAttendant(@PathParam("id") int id, CabinCrewEntity updatedAttendant) {
        boolean isUpdated = attendantService.updateAttendant(id, updatedAttendant);
        if (isUpdated) {
            return Response.ok().entity(updatedAttendant).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteAttendant(@PathParam("id") int id) {
        boolean isDeleted = attendantService.deleteAttendant(id);
        if (isDeleted) {
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    @Path("/all")
    public Response getAllAttendants() {
        return Response.ok(attendantService.getAllAttendants()).build();
    }
}
