package ca.yorku.eecs;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.neo4j.driver.*;
import static org.neo4j.driver.Values.parameters;

@Path("/api/v1") 
public class ActorResource {

    private final Driver driver;

    public ActorResource(Driver driver) {
        this.driver = driver;
    }

    @PUT
    @Path("/addActor")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addActor(Actor actor) {
        // Implementation for adding an actor to the database
    }

    @GET
    @Path("/getActor")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActor(@QueryParam("actorId") String actorId) {
        // Implementation for getting an actor's details and movies
    }

    @PUT
    @Path("/updateActorName")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateActorName(UpdateActorNameRequest request) {
        // Implementation for updating an actor's name
    }

    // Add endpoint for getActorsTopThreeFilms if needed

}

