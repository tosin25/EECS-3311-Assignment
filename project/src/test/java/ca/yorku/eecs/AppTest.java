package ca.yorku.eecs;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.junit.*;
import static org.junit.Assert.*;

import javax.ws.rs.client.*;
import javax.ws.rs.core.*;

import org.json.*;

import org.neo4j.driver.*; 


/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase 
{
	//Driver driver = GraphDatabase.driver("bolt://localhost:8080", AuthTokens.basic("neo4j", "123456")); 

	// modifications
    private static final String BASE_URL = "http://localhost:8080/api/v1/"; // personal port
    private static Client client;

    @BeforeClass
    public static void setUp() {
        client = ClientBuilder.newClient();
    }

    @AfterClass
    public static void tearDown() {
        client.close();
    }

    // PUT /api/v1/addActor

    @Test
    public void addActorPass() {
        String actorJson = "{\"name\": \"Test Actor\", \"actorId\": \"nm0000001\"}";
        Response response = client.target(BASE_URL + "addActor")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(actorJson));

        assertEquals(200, response.getStatus());
        response.close();
    }

    @Test
    public void addActorFail() {
        // Missing actorId
        String actorJson = "{\"name\": \"Test Actor\"}"; 
        Response response = client.target(BASE_URL + "addActor")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(actorJson));

        assertEquals(400, response.getStatus()); 
        response.close();
    }

    // tests for other PUT endpoints: addMovie, addRelationship
    
    // PUT /api/v1/addMovie

    @Test
    public void addMoviePass() {
        String movieJson = "{\"name\": \"Test Movie\", \"movieId\": \"tt0000001\"}";
        Response response = client.target(BASE_URL + "addMovie")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(movieJson));

        assertEquals(200, response.getStatus());
        response.close();
    }

    @Test
    public void addMovieFail() {
        // Missing movieId
        String movieJson = "{\"name\": \"Test Movie\"}"; 
        Response response = client.target(BASE_URL + "addMovie")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(movieJson));

        assertEquals(400, response.getStatus()); 
        response.close();
    }

    // PUT /api/v1/addRelationship

    @Test
    public void addRelationshipPass() {
        // Assuming you've added an actor and a movie in previous tests
        String relationshipJson = "{\"actorId\": \"nm0000001\", \"movieId\": \"tt0000001\"}";
        Response response = client.target(BASE_URL + "addRelationship")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(relationshipJson));

        assertEquals(200, response.getStatus());
        response.close();
    }

    @Test
    public void addRelationshipFail_MissingData() {
        // Missing actorId
        String relationshipJson = "{\"movieId\": \"tt0000001\"}"; 
        Response response = client.target(BASE_URL + "addRelationship")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(relationshipJson));

        assertEquals(400, response.getStatus()); 
        response.close();
    }

    @Test
    public void addRelationshipFail_NonexistentActor() {
        // Non-existent actorId
        String relationshipJson = "{\"actorId\": \"nonexistentActor\", \"movieId\": \"tt0000001\"}";
        Response response = client.target(BASE_URL + "addRelationship")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(relationshipJson));

        assertEquals(404, response.getStatus()); 
        response.close();
    }


    // GET /api/v1/getActor

    @Test
    public void getActorPass() {
        // Assuming you've added an actor with actorId "nm0000001" in a previous test
        Response response = client.target(BASE_URL + "getActor")
                .queryParam("actorId", "nm0000001")
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertEquals(200, response.getStatus());
        String responseJson = response.readEntity(String.class);
        JSONObject json = new JSONObject(responseJson);
        assertEquals("nm0000001", json.getString("actorId")); 
        assertEquals("Test Actor", json.getString("name")); 
        response.close();
    }

    @Test
    public void getActorFail() {
        Response response = client.target(BASE_URL + "getActor")
                .queryParam("actorId", "nonexistentActorId") 
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertEquals(404, response.getStatus()); 
        response.close();
    }

    // tests for other GET endpoints: getMovie, hasRelationship, computeBaconNumber, computeBaconPath, and your new features
    
    // GET /api/v1/getMovie

    @Test
    public void getMoviePass() {
        // Assuming you've added a movie with movieId "tt0000001" in a previous test
        Response response = client.target(BASE_URL + "getMovie")
                .queryParam("movieId", "tt0000001")
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertEquals(200, response.getStatus());
        String responseJson = response.readEntity(String.class);
        JSONObject json = new JSONObject(responseJson);
        assertEquals("tt0000001", json.getString("movieId")); 
        assertEquals("Test Movie", json.getString("name")); 
        response.close();
    }

    @Test
    public void getMovieFail() {
        Response response = client.target(BASE_URL + "getMovie")
                .queryParam("movieId", "nonexistentMovieId") 
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertEquals(404, response.getStatus()); 
        response.close();
    }

    // GET /api/v1/hasRelationship

    @Test
    public void hasRelationshipPass() {
        // Assuming you've added a relationship between actor "nm0000001" and movie "tt0000001"
        String relationshipJson = "{\"actorId\": \"nm0000001\", \"movieId\": \"tt0000001\"}";
        Response response = client.target(BASE_URL + "hasRelationship")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(relationshipJson)); // Note: The handout specifies POST for this endpoint

        assertEquals(200, response.getStatus());
        String responseJson = response.readEntity(String.class);
        JSONObject json = new JSONObject(responseJson);
        assertTrue(json.getBoolean("hasRelationship")); 
        response.close();
    }

    @Test
    public void hasRelationshipFail() {
        // Non-existent relationship
        String relationshipJson = "{\"actorId\": \"nm0000001\", \"movieId\": \"nonexistentMovieId\"}";
        Response response = client.target(BASE_URL + "hasRelationship")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.json(relationshipJson));

        assertEquals(404, response.getStatus()); 
        response.close();
    }

    // GET /api/v1/computeBaconNumber

    @Test
    public void computeBaconNumberPass() {
        // Assuming you have a path from "nm0000001" to Kevin Bacon
        Response response = client.target(BASE_URL + "computeBaconNumber")
                .queryParam("actorId", "nm0000001")
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertEquals(200, response.getStatus());
        String responseJson = response.readEntity(String.class);
        JSONObject json = new JSONObject(responseJson);
        assertTrue(json.getInt("baconNumber") >= 0); 
        response.close();
    }

    @Test
    public void computeBaconNumberFail() {
        // Non-existent actor or no path to Kevin Bacon
        Response response = client.target(BASE_URL + "computeBaconNumber")
                .queryParam("actorId", "nonexistentActorId") 
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertEquals(404, response.getStatus()); 
        response.close();
    }

    // GET /api/v1/computeBaconPath

    @Test
    public void computeBaconPathPass() {
        // Assuming you have a path from "nm0000001" to Kevin Bacon
        Response response = client.target(BASE_URL + "computeBaconPath")
                .queryParam("actorId", "nm0000001")
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertEquals(200, response.getStatus());
        String responseJson = response.readEntity(String.class);
        JSONObject json = new JSONObject(responseJson);
        JSONArray baconPath = json.getJSONArray("baconPath");
        assertTrue(baconPath.length() > 0); 
        assertEquals("nm0000102", baconPath.getString(baconPath.length() - 1)); // Last element should be Kevin Bacon
        response.close();
    }

    @Test
    public void computeBaconPathFail() {
        // Non-existent actor or no path to Kevin Bacon
        Response response = client.target(BASE_URL + "computeBaconPath")
                .queryParam("actorId", "nonexistentActorId") 
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertEquals(404, response.getStatus()); 
        response.close();
    }

    // New Features

    // GET /api/v1/getMoviesByGenre

    @Test
    public void getMoviesByGenrePass() {
        // Assuming you've added movies with genre "Test Genre"
        Response response = client.target(BASE_URL + "getMoviesByGenre")
                .queryParam("genre", "Test Genre")
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertEquals(200, response.getStatus());
        // Further assertions on the response content can be added here
        response.close();
    }

    @Test
    public void getMoviesByGenreFail() {
        // Non-existent genre
        Response response = client.target(BASE_URL + "getMoviesByGenre")
                .queryParam("genre", "Nonexistent Genre")
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertEquals(404, response.getStatus()); // Or 400 if you consider it a bad request
        response.close();
    }

    // PUT /api/v1/updateActorName 

    @Test
    public void updateActorNamePass() {
        // Assuming you've added an actor with actorId "nm0000001"
        String updateJson = "{\"actorId\": \"nm0000001\", \"newName\": \"Updated Actor Name\"}";
        Response response = client.target(BASE_URL + "updateActorName")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(updateJson));

        assertEquals(200, response.getStatus());
        response.close();

        // Verify the update
        response = client.target(BASE_URL + "getActor")
                .queryParam("actorId", "nm0000001")
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertEquals(200, response.getStatus());
        String responseJson = response.readEntity(String.class);
        JSONObject json = new JSONObject(responseJson);
        assertEquals("Updated Actor Name", json.getString("name")); 
        response.close();
    }

    @Test
    public void updateActorNameFail() {
        // Non-existent actorId
        String updateJson = "{\"actorId\": \"nonexistentActor\", \"newName\": \"Updated Actor Name\"}";
        Response response = client.target(BASE_URL + "updateActorName")
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(updateJson));

        assertEquals(404, response.getStatus()); 
        response.close();
    }

    // DELETE /api/v1/deleteMovie

    @Test
    public void deleteMoviePass() {
        // Assuming you've added a movie with movieId "tt0000001"
        Response response = client.target(BASE_URL + "deleteMovie")
                .queryParam("movieId", "tt0000001")
                .request(MediaType.APPLICATION_JSON)
                .delete(); 

        assertEquals(200, response.getStatus());
        response.close();

        // Verify the deletion
        response = client.target(BASE_URL + "getMovie")
                .queryParam("movieId", "tt0000001")
                .request(MediaType.APPLICATION_JSON)
                .get();

        assertEquals(404, response.getStatus()); 
        response.close();
    }

    @Test
    public void deleteMovieFail() {
        // Non-existent movieId
        Response response = client.target(BASE_URL + "deleteMovie")
                .queryParam("movieId", "nonexistentMovieId")
                .request(MediaType.APPLICATION_JSON)
                .delete();

        assertEquals(404, response.getStatus()); 
        response.close();
    }

}

