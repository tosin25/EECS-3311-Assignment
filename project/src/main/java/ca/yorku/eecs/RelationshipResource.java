package ca.yorku.eecs;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.neo4j.driver.*;
import static org.neo4j.driver.Values.parameters;

@Path("/api/v1") 
public class RelationshipResource {

    private final Driver driver;

    public RelationshipResource(Driver driver) {
        this.driver = driver;
    }

    @PUT
    @Path("/addRelationship")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addRelationship(AddRelationshipRequest request) {
        // Implementation for adding a relationship between an actor and a movie
    }

    @POST 
    @Path("/hasRelationship")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response hasRelationship(HasRelationshipRequest request) {
        // Implementation for checking if a relationship exists
    }

}
