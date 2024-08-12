package ca.yorku.eecs;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Map;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import org.json.JSONException;
import org.json.JSONObject;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;
import org.neo4j.driver.v1.exceptions.Neo4jException;

public class App {
	
    static int PORT = 7687; 
    private static Driver driver;

    public static void main(String[] args) throws IOException {
     
        driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "12345678"));
        System.out.println("Connected to Neo4j");
        
        HttpServer server = HttpServer.create(new InetSocketAddress("0.0.0.0", PORT), 0);
        server.createContext("/api/v1/addActor", App::addActor);
        server.createContext("/api/v1/addMovie", App::addMovie);
        server.createContext("/api/v1/addGenre", App::addGenre);
        server.createContext("/api/v1/addRelationship", App::addRelationship);
        server.createContext("/api/v1/getActor", App::getActor);
        server.createContext("/api/v1/getMovie", App::getMovie);
        server.createContext("/api/v1/hasRelationship", App::hasRelationship);
        server.createContext("/api/v1/computeBaconNumber", App::computeBaconNumber);
        server.createContext("/api/v1/addGenreToMovie", App::addGenreToMovie);
        server.createContext("/api/v1/getMoviesByGenre", App::getMoviesByGenre);
        server.createContext("/api/v1/getActorTop3Films", App::getActorTop3Films);
        server.createContext("/api/v1/deleteMovie", App::deleteMovie);
     

        server.start();
        System.out.printf("Server started on port %d...\n", PORT);
        
        //TESTED WITH POSTMAN

        //close driver
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (driver != null) {
                driver.close();
            }
        }));
    }
    
    
    //PUT 
    public static void addActor(HttpExchange exchange) throws IOException {
        if ("PUT".equals(exchange.getRequestMethod())) {
            try {
                String requestBody = new String(exchange.getRequestBody().readAllBytes());
                JSONObject json = new JSONObject(requestBody);

                String actorId = json.getString("actorId");
                String name = json.getString("name");

                // Add actor to the Neo4j database
                try (Session session = driver.session()) {
                    session.writeTransaction((TransactionWork<Void>) tx -> {
                        tx.run("MERGE (a:Actor {id: $id, name: $name})",
                                org.neo4j.driver.v1.Values.parameters("id", actorId, "name", name));
                        return null;
                    });
                }

                String response = "Actor added successfully";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();

            } catch (Exception e) {
                String response = "Bad Request";
                exchange.sendResponseHeaders(400, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        } else {
            exchange.sendResponseHeaders(405, -1);}
    }

    
    public static void addMovie(HttpExchange exchange) throws IOException {
        if ("PUT".equals(exchange.getRequestMethod())) {
            try {
                String requestBody = new String(exchange.getRequestBody().readAllBytes());
                JSONObject json = new JSONObject(requestBody);

                String movieId = json.getString("movieId");
                String name = json.getString("name");

                // Add movie to the Neo4j database
                try (Session session = driver.session()) {
                    session.writeTransaction((TransactionWork<Void>) tx -> {
                        tx.run("MERGE (m:Movie {id: $id, name: $name})",
                                org.neo4j.driver.v1.Values.parameters("id", movieId, "name", name));
                        return null;
                    });
                }

                String response = "Movie added successfully";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();

            } catch (Exception e) {
                String response = "Bad Request";
                exchange.sendResponseHeaders(400, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        } else {
            exchange.sendResponseHeaders(405, -1); // Method Not Allowed
        }
    }

  
    public static void addRelationship(HttpExchange exchange) throws IOException {
        if ("PUT".equals(exchange.getRequestMethod())) {
            try {
                String requestBody = new String(exchange.getRequestBody().readAllBytes());
                JSONObject json = new JSONObject(requestBody);

                String actorId = json.getString("actorId");
                String movieId = json.getString("movieId");

                // Add relationship to the Neo4j database
                try (Session session = driver.session()) {
                    session.writeTransaction((TransactionWork<Void>) tx -> {
                        tx.run("MATCH (a:Actor {id: $actorId}), (m:Movie {id: $movieId}) " +
                                "MERGE (a)-[:ACTED_IN]->(m)",
                                org.neo4j.driver.v1.Values.parameters("actorId", actorId, "movieId", movieId));
                        return null;
                    });
                }

                String response = "Relationship added successfully";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();

            } catch (Exception e) {
                String response = "Bad Request or Not Found";
                exchange.sendResponseHeaders(400, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        } else {
            exchange.sendResponseHeaders(405, -1); 
        }
    }
    
    
    //GET METHODS
    public static void getActor(HttpExchange exchange) throws IOException {
        String actorId = exchange.getRequestURI().getQuery().split("=")[1];
        try (Session session = driver.session()) {
            StatementResult result = session.run("MATCH (a:Actor {actorId: $actorId}) RETURN a.name AS name", 
                                        Map.of("actorId", actorId));
            if (result.hasNext()) {
                String name = result.single().get("name").asString();
                String response = String.format("{\"actorId\": \"%s\", \"name\": \"%s\"}", actorId, name);
                exchange.sendResponseHeaders(200, response.length());
                exchange.getResponseBody().write(response.getBytes());
            } else {
                exchange.sendResponseHeaders(404, -1);
            }
        } catch (Neo4jException e) {
            exchange.sendResponseHeaders(500, -1);
        } finally {
            exchange.close();
        }
    }

    public static void getMovie(HttpExchange exchange) throws IOException {
        String movieId = exchange.getRequestURI().getQuery().split("=")[1];
        try (Session session = driver.session()) {
            StatementResult result = session.run("MATCH (m:Movie {movieId: $movieId}) RETURN m.name AS name", 
                                        Map.of("movieId", movieId));
            if (result.hasNext()) {
                String name = result.single().get("name").asString();
                String response = String.format("{\"movieId\": \"%s\", \"name\": \"%s\"}", movieId, name);
                exchange.sendResponseHeaders(200, response.length());
                exchange.getResponseBody().write(response.getBytes());
            } else {
                exchange.sendResponseHeaders(404, -1);
            }
        } catch (Neo4jException e) {
            exchange.sendResponseHeaders(500, -1);
        } finally {
            exchange.close();
        }
    }

    public static void hasRelationship(HttpExchange exchange) throws IOException {
        String[] params = exchange.getRequestURI().getQuery().split("&");
        String actorId = params[0].split("=")[1];
        String movieId = params[1].split("=")[1];

        try (Session session = driver.session()) {
            StatementResult result = session.run("MATCH (a:Actor {actorId: $actorId})-[r:ACTED_IN]->(m:Movie {movieId: $movieId}) RETURN r", 
                                        Map.of("actorId", actorId, "movieId", movieId));
            if (result.hasNext()) {
                exchange.sendResponseHeaders(200, -1);
            } else {
                exchange.sendResponseHeaders(404, -1);
            }
        } catch (Neo4jException e) {
            exchange.sendResponseHeaders(500, -1);
        } finally {
            exchange.close();
        }
    }
    

    //BACON NUMBER
    public static void computeBaconNumber(HttpExchange exchange) throws IOException {
        String actorId = exchange.getRequestURI().getQuery().split("=")[1];
        
        try (Session session = driver.session()) {
            StatementResult result = session.run(
                "MATCH (bacon:Actor {name: 'Kevin Bacon'}), (actor:Actor {actorId: $actorId}), " +
                "p = shortestPath((bacon)-[:ACTED_IN*]-(actor)) " +
                "RETURN length(p) AS baconNumber",
                Map.of("actorId", actorId)
            );

            if (result.hasNext()) {
                int baconNumber = result.single().get("baconNumber").asInt();
                String response = String.format("{\"actorId\": \"%s\", \"baconNumber\": %d}", actorId, baconNumber);
                exchange.sendResponseHeaders(200, response.length());
                exchange.getResponseBody().write(response.getBytes());
            } else {
                exchange.sendResponseHeaders(404, -1);
            }
        } catch (Neo4jException e) {
            exchange.sendResponseHeaders(500, -1);
        } finally {
            exchange.close();
        }
    }
    
    
    //ADDED FEATURES
    public static void addGenre(HttpExchange exchange) throws IOException {
        if ("PUT".equals(exchange.getRequestMethod())) {
            try {
                // Read the request body
                String requestBody = new String(exchange.getRequestBody().readAllBytes());
                JSONObject json = new JSONObject(requestBody);

                // Extract genreId and name from the JSON object
                String genreId = json.getString("genreId");
                String name = json.getString("name");

                // Add genre to the Neo4j database
                try (Session session = driver.session()) {
                    session.writeTransaction((TransactionWork<Void>) tx -> {
                        tx.run("MERGE (g:Genre {id: $id, name: $name})",
                                org.neo4j.driver.v1.Values.parameters("id", genreId, "name", name));
                        return null;
                    });
                }

                // Send a success response
                String response = "Genre added successfully";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();

            } catch (Exception e) {
                // Handle exceptions and send a bad request response
                String response = "Bad Request";
                exchange.sendResponseHeaders(400, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        } else {
            // Send a method not allowed response if the request method is not PUT
            exchange.sendResponseHeaders(405, -1);
        }
    }

    public static void addGenreToMovie(HttpExchange exchange) throws IOException {
        if ("PUT".equals(exchange.getRequestMethod())) {
            try {
                String requestBody = new String(exchange.getRequestBody().readAllBytes());
                JSONObject json = new JSONObject(requestBody);

                String movieId = json.getString("movieId");
                String genre = json.getString("genre");

                // Add genre to the movie in the Neo4j database
                try (Session session = driver.session()) {
                    session.writeTransaction((TransactionWork<Void>) tx -> {
                        tx.run("MATCH (m:Movie {id: $id}) SET m.genre = $genre",
                                org.neo4j.driver.v1.Values.parameters("id", movieId, "genre", genre));
                        return null;
                    });
                }

                String response = "Genre added to movie successfully";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                exchange.getResponseBody().write(response.getBytes());

            } catch (Exception e) {
                exchange.sendResponseHeaders(400, "Bad Request".getBytes().length);
                exchange.getResponseBody().write("Bad Request".getBytes());
            } finally {
                exchange.getResponseBody().close();
            }
        } else {
            exchange.sendResponseHeaders(405, -1); 
        }
    }
    
    

    public static void getMoviesByGenre(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            String genre = exchange.getRequestURI().getQuery().split("=")[1];

            try (Session session = driver.session()) {
                StatementResult result = session.run(
                        "MATCH (m:Movie {genre: $genre}) RETURN m ORDER BY m.rating DESC LIMIT 10",
                        org.neo4j.driver.v1.Values.parameters("genre", genre));

                StringBuilder response = new StringBuilder("[");
                while (result.hasNext()) {
                    response.append(result.next().get("m").asNode().asMap().toString()).append(",");
                }
                if (response.length() > 1) {
                    response.setLength(response.length() - 1);  // remove trailing comma
                }
                response.append("]");

                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, response.length());
                exchange.getResponseBody().write(response.toString().getBytes());

            } catch (Exception e) {
                exchange.sendResponseHeaders(500, "Internal Server Error".getBytes().length);
                exchange.getResponseBody().write("Internal Server Error".getBytes());
            } finally {
                exchange.getResponseBody().close();
            }
        } else {
            exchange.sendResponseHeaders(405, -1); 
        }
    }
    
    
    public static void getActorTop3Films(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            String actorName = exchange.getRequestURI().getQuery().split("=")[1];

            try (Session session = driver.session()) {
                StatementResult result = session.run(
                        "MATCH (a:Actor {name: $actorName})-[:ACTED_IN]->(m:Movie) " +
                        "RETURN m ORDER BY m.rating DESC LIMIT 3",
                        org.neo4j.driver.v1.Values.parameters("actorName", actorName));

                StringBuilder response = new StringBuilder("[");
                while (result.hasNext()) {
                    response.append(result.next().get("m").asNode().asMap().toString()).append(",");
                }
                if (response.length() > 1) {
                    response.setLength(response.length() - 1);  // remove trailing comma
                }
                response.append("]");

                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, response.length());
                exchange.getResponseBody().write(response.toString().getBytes());

            } catch (Exception e) {
                exchange.sendResponseHeaders(500, "Internal Server Error".getBytes().length);
                exchange.getResponseBody().write("Internal Server Error".getBytes());
            } finally {
                exchange.getResponseBody().close();
            }
        } else {
            exchange.sendResponseHeaders(405, -1); 
        }
    }
    

    public static void deleteMovie(HttpExchange exchange) throws IOException {
        if ("DELETE".equals(exchange.getRequestMethod())) {
            try {
                String requestBody = new String(exchange.getRequestBody().readAllBytes());
                JSONObject json = new JSONObject(requestBody);

                String movieId = json.getString("movieId");

                try (Session session = driver.session()) {
                    session.writeTransaction((TransactionWork<Void>) tx -> {
                        tx.run("MATCH (m:Movie {id: $id}) DETACH DELETE m",
                                org.neo4j.driver.v1.Values.parameters("id", movieId));
                        return null;
                    });
                }

                String response = "Movie deleted successfully";
                exchange.sendResponseHeaders(200, response.getBytes().length);
                exchange.getResponseBody().write(response.getBytes());

            } catch (Exception e) {
                exchange.sendResponseHeaders(400, "Bad Request".getBytes().length);
                exchange.getResponseBody().write("Bad Request".getBytes());
            } finally {
                exchange.getResponseBody().close();
            }
        } else {
            exchange.sendResponseHeaders(405, -1); 
        }
    }


    
    

}

