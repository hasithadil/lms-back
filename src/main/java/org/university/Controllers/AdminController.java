package org.university.Controllers;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
public class AdminController {
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "admin controller";
    }

}
