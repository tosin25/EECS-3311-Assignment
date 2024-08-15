package ca.yorku.eecs;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.neo4j.driver.*;
import static org.neo4j.driver.Values.parameters;

@Path("/api/v1") 
public class BaconNumberResource {

    private final Driver driver;

    public BaconNumberResource(Driver driver) {
        this.driver = driver;
    }

    @GET
    @Path("/computeBaconNumber")
    @Produces(MediaType.APPLICATION_JSON)
    public Response computeBaconNumber(@QueryParam("actorId") String actorId) {
        // Implementation for computing Bacon Number
    }

    @GET
    @Path("/computeBaconPath")
    @Produces(MediaType.APPLICATION_JSON)
    public Response computeBaconPath(@QueryParam("actorId") String actorId) {
        // Implementation for computing Bacon Path
    }

}
