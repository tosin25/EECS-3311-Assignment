package ca.yorku.eecs;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import org.neo4j.driver.*;
import static org.neo4j.driver.Values.parameters;

@Path("/api/v1") 
public class MovieResource {

    private final Driver driver;

    public MovieResource(Driver driver) {
        this.driver = driver;
    }

    @PUT
    @Path("/addMovie")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addMovie(Movie movie) {
        // Implementation for adding a movie to the database
    }

    @GET
    @Path("/getMovie")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovie(@QueryParam("movieId") String movieId) {
        // Implementation for getting a movie's details and actors
    }

    @DELETE
    @Path("/deleteMovie")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMovie(@QueryParam("movieId") String movieId) {
        // Implementation for deleting a movie
    }

    // Add endpoint for getMoviesByGenre if needed

}
