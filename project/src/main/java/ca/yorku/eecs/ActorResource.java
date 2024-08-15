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
    	try (Session session = driver.session()) {
	        // Check if actor already exists
	        Result existingActorCheck = session.run(
	                "MATCH (a:Actor {actorId: $actorId}) RETURN a",
	                parameters("actorId", actor.getActorId())
	        );
	
	        if (existingActorCheck.hasNext()) {
	            return Response.status(Status.BAD_REQUEST).entity("Actor with ID " + actor.getActorId() + " already exists.").build();
	        }
	
	        // If not, create the actor node
	        session.run("CREATE (a:Actor {name: $name, actorId: $actorId})",
	                parameters("name", actor.getName(), "actorId", actor.getActorId()));
	        return Response.status(Status.CREATED).build(); // 201 Created for successful creation
    	} 
    
    	catch (Exception e) {
    		return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Error adding actor: " + e.getMessage()).build();
    	}
    }

    @GET
    @Path("/getActor")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActor(@QueryParam("actorId") String actorId) {
	    try (Session session = driver.session()) {
	        Result result = session.run(
	                "MATCH (a:Actor {actorId: $actorId})-[:ACTED_IN]->(m:Movie) " +
	                        "RETURN a.actorId as actorId, a.name as name, collect(m.movieId) as movies",
	                parameters("actorId", actorId)
	        );
	
	        if (result.hasNext()) {
	            Record record = result.next();
	            // If the actor exists but hasn't acted in any movies, 'movies' will be an empty list
	            return Response.ok().entity(record.asMap()).build(); 
	        } else {
	            return Response.status(Status.NOT_FOUND).entity("Actor not found").build();
	        }
	    } catch (Exception e) {
	        return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Error getting actor: " + e.getMessage()).build();
	    }
    }

    @PUT
    @Path("/updateActorName")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateActorName(UpdateActorNameRequest request) {
	    try (Session session = driver.session()) {
	        Result result = session.run(
	                "MATCH (a:Actor {actorId: $actorId}) " +
	                        "SET a.name = $newName " +
	                        "RETURN a.name as name",
	                parameters("actorId", request.getActorId(), "newName", request.getNewName())
	        );
	
	        if (result.hasNext()) {
	            Record record = result.next();
	            return Response.ok().entity(record.asMap()).build();
	        } else {
	            return Response.status(Status.NOT_FOUND).entity("Actor not found").build();
	        }
	    } catch (Exception e) {
	        return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Error updating actor name: " + e.getMessage()).build();
	    }
    }

    // Add endpoint for getActorsTopThreeFilms
    @GET
    @Path("/getActorsTopThreeFilms")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getActorsTopThreeFilms(@QueryParam("actorId") String actorId) {
        // Implementation for getting an actor's top 3 films based on some criteria
    }

}
