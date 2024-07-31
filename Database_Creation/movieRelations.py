# from collections import deque, defaultdict

# # Create a graph where each actor is a node and each movie represents an edge between actors
# def build_graph(movies, actors):
#     graph = defaultdict(set)

#     # Map movie_ids to actor names
#     movie_to_actors = defaultdict(set)
#     for actor in actors:
#         for movie_id in actor["movie_ids"]:
#             movie_to_actors[movie_id].add(actor["name"])

#     # Build the graph
#     for movie_id, actor_list in movie_to_actors.items():
#         for actor1 in actor_list:
#             for actor2 in actor_list:
#                 if actor1 != actor2:
#                     graph[actor1].add(actor2)

#     return graph

# # Function to find the shortest path using BFS
# def find_shortest_path(graph, start_actor, target_actor):
#     if start_actor not in graph or target_actor not in graph:
#         return None

#     queue = deque([(start_actor, [start_actor])])
#     visited = set()

#     while queue:
#         current_actor, path = queue.popleft()
#         if current_actor in visited:
#             continue
#         visited.add(current_actor)

#         for neighbor in graph[current_actor]:
#             if neighbor == target_actor:
#                 return path + [neighbor]
#             if neighbor not in visited:
#                 queue.append((neighbor, path + [neighbor]))

#     return None

# # Sample data
# # 
# movies = [
#     {"id": 1, "title": "A Few Good Men", "genres": ["Drama", "Thriller"], "rating": 7.7},
#     {"id": 2, "title": "Top Gun", "genres": ["Action", "Drama"], "rating": 6.9},
#     {"id": 3, "title": "Footloose", "genres": ["Drama", "Music"], "rating": 6.6},
#     {"id": 4, "title": "The Big Chill", "genres": ["Comedy", "Drama"], "rating": 7.2},
#     {"id": 5, "title": "The Untouchables", "genres": ["Crime", "Drama"], "rating": 7.9},
#     {"id": 6, "title": "Renaissance Man", "genres": ["Comedy", "Drama"], "rating": 6.0},
#     {"id": 7, "title": "Black Mass", "genres": ["Biography", "Crime"], "rating": 6.9},
#     {"id": 8, "title": "Silver Linings Playbook", "genres": ["Comedy", "Drama"], "rating": 7.7},
#     {"id": 9, "title": "Guardians of the Galaxy", "genres": ["Action", "Adventure"], "rating": 8.0},
#     {"id": 10, "title": "X-Men: First Class", "genres": ["Action", "Adventure"], "rating": 7.7},
#     {"id": 11, "title": "The Godfather", "genres": ["Crime", "Drama"], "rating": 9.2},
#     {"id": 12, "title": "The Shawshank Redemption", "genres": ["Drama"], "rating": 9.3},
#     {"id": 13, "title": "Pulp Fiction", "genres": ["Crime", "Drama"], "rating": 8.9},
#     {"id": 14, "title": "Forrest Gump", "genres": ["Drama", "Romance"], "rating": 8.8},
#     {"id": 15, "title": "The Dark Knight", "genres": ["Action", "Crime", "Drama"], "rating": 9.0},
#     {"id": 16, "title": "Inception", "genres": ["Action", "Adventure", "Sci-Fi"], "rating": 8.8},
#     {"id": 17, "title": "Fight Club", "genres": ["Drama"], "rating": 8.8},
#     {"id": 18, "title": "The Matrix", "genres": ["Action", "Sci-Fi"], "rating": 8.7},
#     {"id": 19, "title": "The Lord of the Rings: The Return of the King", "genres": ["Action", "Adventure", "Drama"], "rating": 8.9},
#     {"id": 20, "title": "The Lord of the Rings: The Fellowship of the Ring", "genres": ["Action", "Adventure", "Drama"], "rating": 8.8},
#     {"id": 21, "title": "Star Wars: Episode IV - A New Hope", "genres": ["Action", "Adventure", "Fantasy"], "rating": 8.6},
#     {"id": 22, "title": "Star Wars: Episode V - The Empire Strikes Back", "genres": ["Action", "Adventure", "Fantasy"], "rating": 8.7},
#     {"id": 23, "title": "Star Wars: Episode VI - Return of the Jedi", "genres": ["Action", "Adventure", "Fantasy"], "rating": 8.3},
#     {"id": 24, "title": "Jurassic Park", "genres": ["Action", "Adventure", "Sci-Fi"], "rating": 8.1},
#     {"id": 25, "title": "Jaws", "genres": ["Adventure", "Thriller"], "rating": 8.0},
#     {"id": 26, "title": "E.T. the Extra-Terrestrial", "genres": ["Adventure", "Family", "Sci-Fi"], "rating": 7.8},
#     {"id": 27, "title": "Back to the Future", "genres": ["Adventure", "Comedy", "Sci-Fi"], "rating": 8.5},
#     {"id": 28, "title": "The Silence of the Lambs", "genres": ["Crime", "Drama", "Thriller"], "rating": 8.6},
#     {"id": 29, "title": "The Usual Suspects", "genres": ["Crime", "Drama", "Mystery"], "rating": 8.5},
#     {"id": 30, "title": "Se7en", "genres": ["Crime", "Drama", "Mystery"], "rating": 8.6},
#     {"id": 31, "title": "Goodfellas", "genres": ["Biography", "Crime", "Drama"], "rating": 8.7},
#     {"id": 32, "title": "Taxi Driver", "genres": ["Crime", "Drama"], "rating": 8.3},
#     {"id": 33, "title": "One Flew Over the Cuckoo's Nest", "genres": ["Drama"], "rating": 8.7},
#     {"id": 34, "title": "The Departed", "genres": ["Crime", "Drama", "Thriller"], "rating": 8.5},
#     {"id": 35, "title": "Gladiator", "genres": ["Action", "Adventure", "Drama"], "rating": 8.5},
#     {"id": 36, "title": "Braveheart", "genres": ["Biography", "Drama", "War"], "rating": 8.3},
#     {"id": 37, "title": "Saving Private Ryan", "genres": ["Drama", "War"], "rating": 8.6},
#     {"id": 38, "title": "The Green Mile", "genres": ["Drama", "Fantasy"], "rating": 8.6},
#     {"id": 39, "title": "American Beauty", "genres": ["Drama"], "rating": 8.4},
#     {"id": 40, "title": "The Prestige", "genres": ["Drama", "Mystery", "Thriller"], "rating": 8.5},
#     {"id": 41, "title": "Memento", "genres": ["Mystery", "Thriller"], "rating": 8.4},
#     {"id": 42, "title": "A Beautiful Mind", "genres": ["Biography", "Drama"], "rating": 8.2},
#     {"id": 43, "title": "The Lion King", "genres": ["Animation", "Adventure", "Drama"], "rating": 8.5},
#     {"id": 44, "title": "The Incredibles", "genres": ["Animation", "Action", "Adventure"], "rating": 8.0},
#     {"id": 45, "title": "Finding Nemo", "genres": ["Animation", "Adventure", "Comedy"], "rating": 8.1},
#     {"id": 46, "title": "Up", "genres": ["Animation", "Adventure", "Comedy"], "rating": 8.2},
#     {"id": 47, "title": "WALL·E", "genres": ["Animation", "Adventure", "Drama"], "rating": 8.4},
#     {"id": 48, "title": "Toy Story", "genres": ["Animation", "Adventure", "Comedy"], "rating": 8.3},
#     {"id": 49, "title": "Toy Story 3", "genres": ["Animation", "Adventure", "Comedy"], "rating": 8.2},
#     {"id": 50, "title": "Ratatouille", "genres": ["Animation", "Adventure", "Comedy"], "rating": 8.0},
#     {"id": 51, "title": "Shrek", "genres": ["Animation", "Adventure", "Comedy"], "rating": 7.9},
#     {"id": 52, "title": "Shrek 2", "genres": ["Animation", "Adventure", "Comedy"], "rating": 7.2},
#     {"id": 53, "title": "Madagascar", "genres": ["Animation", "Adventure", "Comedy"], "rating": 6.9},
#     {"id": 54, "title": "Kung Fu Panda", "genres": ["Animation", "Action", "Adventure"], "rating": 7.6},
#     {"id": 55, "title": "How to Train Your Dragon", "genres": ["Animation", "Action", "Adventure"], "rating": 8.1},
#     {"id": 56, "title": "The Croods", "genres": ["Animation", "Adventure", "Comedy"], "rating": 7.2},
#     {"id": 57, "title": "Despicable Me", "genres": ["Animation", "Adventure", "Comedy"], "rating": 7.6},
#     {"id": 58, "title": "Despicable Me 2", "genres": ["Animation", "Adventure", "Comedy"], "rating": 7.3},
#     {"id": 59, "title": "Minions", "genres": ["Animation", "Adventure", "Comedy"], "rating": 6.4},
#     {"id": 60, "title": "Frozen", "genres": ["Animation", "Adventure", "Comedy"], "rating": 7.4},
# ]

# actors = actors = [
#     {"id": 1, "name": "Kevin Bacon", "movie_ids": [1, 2, 3, 4, 5]},
#     {"id": 2, "name": "Tom Hanks", "movie_ids": [12, 14, 38]}, # Fixed movie_ids
#     {"id": 3, "name": "Johnny Depp", "movie_ids": [7]}, # Fixed movie_ids
#     {"id": 4, "name": "Meryl Streep", "movie_ids": []}, # Fixed movie_ids
#     {"id": 5, "name": "Denzel Washington", "movie_ids": [6]}, # Fixed movie_ids
#     {"id": 6, "name": "Julia Roberts", "movie_ids": []}, # Fixed movie_ids
#     {"id": 7, "name": "Brad Pitt", "movie_ids": [28, 30]}, # Fixed movie_ids
#     {"id": 8, "name": "Nicole Kidman", "movie_ids": []}, # Fixed movie_ids
#     {"id": 9, "name": "Jodie Foster", "movie_ids": [28]}, # Fixed movie_ids
#     {"id": 10, "name": "Morgan Freeman", "movie_ids": [12, 38]}, # Fixed movie_ids
#     {"id": 11, "name": "Matt Damon", "movie_ids": [35]}, # Fixed movie_ids
#     {"id": 12, "name": "Angelina Jolie", "movie_ids": []}, # Fixed movie_ids
#     {"id": 13, "name": "Robert De Niro", "movie_ids": [32]}, # Fixed movie_ids
#     {"id": 14, "name": "Jack Nicholson", "movie_ids": [33]}, # Fixed movie_ids
#     {"id": 15, "name": "Al Pacino", "movie_ids": [11]}, # Fixed movie_ids
#     {"id": 16, "name": "Edward Norton", "movie_ids": [17]}, # Fixed movie_ids
#     {"id": 17, "name": "Mark Ruffalo", "movie_ids": [19]}, # Fixed movie_ids
#     {"id": 18, "name": "Naomi Watts", "movie_ids": []}, # Fixed movie_ids
#     {"id": 19, "name": "Christian Bale", "movie_ids": [15]}, # Fixed movie_ids
#     {"id": 20, "name": "Hugh Jackman", "movie_ids": [10]}, # Fixed movie_ids
#     {"id": 21, "name": "Kate Winslet", "movie_ids": []}, # Fixed movie_ids
#     {"id": 22, "name": "Sandra Bullock", "movie_ids": []}, # Fixed movie_ids
#     {"id": 23, "name": "Will Smith", "movie_ids": []}, # Fixed movie_ids
#     {"id": 24, "name": "Ryan Reynolds", "movie_ids": []}, # Fixed movie_ids
#     {"id": 25, "name": "Ben Affleck", "movie_ids": [34]}, # Fixed movie_ids
#     {"id": 26, "name": "Chris Evans", "movie_ids": [9, 10]}, # Fixed movie_ids
#     {"id": 27, "name": "Harrison Ford", "movie_ids": []}, # Fixed movie_ids
#     {"id": 28, "name": "Sean Penn", "movie_ids": []}, # Fixed movie_ids
#     {"id": 29, "name": "Jeremy Renner", "movie_ids": []}, # Fixed movie_ids
#     {"id": 30, "name": "Jennifer Lawrence", "movie_ids": [35]}, # Fixed movie_ids
#     {"id": 41, "name": "Tilda Swinton", "movie_ids": []}, # Fixed movie_ids
#     {"id": 47, "name": "James Franco", "movie_ids": []}, # Fixed movie_ids
#     {"id": 48, "name": "Zoe Saldana", "movie_ids": [9]}, # Fixed movie_ids
#     {"id": 53, "name": "Mark Ruffalo", "movie_ids": [10]}, # Fixed movie_ids
#     {"id": 54, "name": "Ryan Reynolds", "movie_ids": []}, # Fixed movie_ids
#     {"id": 59, "name": "Lupita Nyong'o", "movie_ids": []}, # Fixed movie_ids
#     {"id": 60, "name": "Emily Blunt", "movie_ids": []}, # Fixed movie_ids
#     {"id": 65, "name": "Jessica Alba", "movie_ids": []}, # Fixed movie_ids
#     {"id": 71, "name": "Naomi Watts", "movie_ids": []}, # Fixed movie_ids
#     {"id": 72, "name": "Emily Blunt", "movie_ids": []}, # Fixed movie_ids
#     {"id": 77, "name": "Emily Mortimer", "movie_ids": []}, # Fixed movie_ids
#     {"id": 83, "name": "Chris Pratt", "movie_ids": [9]}, # Fixed movie_ids
#     {"id": 163, "name": "Chris Evans", "movie_ids": [9, 10]}, # Fixed movie_ids
# ]



# # Build the graph and find the shortest path
# graph = build_graph(movies, actors)
# start_actor = "Kevin Bacon"
# size= 0

# # Display the movies and actors
# for actor in actors:
#     target_actor = actor["name"]
#     if target_actor != start_actor:
#         path = find_shortest_path(graph, start_actor, target_actor)
#         if path:
#             if len(path) <=6:
#                 path = path[::-1]
#                 # print(actor)
#                 # print(path)
#                 print(f"Shortest path from {start_actor} to {target_actor}: {' -> '.join(path)}")
#                 size=size+1



# print(size)

# # def get_top_rated_movies(movies, genre, top_n=3):
# #     # Filter movies by the specified genre
# #     filtered_movies = [movie for movie in movies if genre in movie["genres"]]
    
# #     if not filtered_movies:
# #         return []  # No movies found in the specified genre

# #     # Sort the filtered movies by rating in descending order
# #     sorted_movies = sorted(filtered_movies, key=lambda movie: movie["rating"], reverse=True)
    
# #     # Get the top_n movies
# #     top_movies = sorted_movies[:top_n]
    
# #     return top_movies
# # # Example usage

# # genre = "Action"
# # top_n = 3
# # top_movies = get_top_rated_movies(movies, genre, top_n)

# # if top_movies:
# #     print(f"Top {top_n} rated '{genre}' movies:")
# #     for movie in top_movies:
# #         print(f"Title: {movie['title']}, Rating: {movie['rating']},")
# # else:
# #     print(f"No movies found in the genre '{genre}'")


# # def get_movie_rating(movie_id, movies):
# #     # Find movie by ID
# #     for movie in movies:
# #         if movie["id"] == movie_id:
# #             return movie["rating"]
# #     return None

# # def get_movie_by_id(movie_id, movies):
# #     for movie in movies:
# #         if movie["id"] == movie_id:
# #             return movie
# #     return None

# # def get_top_rated_movies(actor_name, actors, movies):
# #     top_3 = []

# #     # Find the actor
# #     for actor in actors:
# #         if actor["name"] == actor_name:
# #             movie_ids = actor["movie_ids"]
            
# #             # Get movie ratings
# #             movie_ratings = [(movie_id, get_movie_by_id(movie_id, movies)["rating"]) for movie_id in movie_ids if get_movie_by_id(movie_id, movies) is not None]
            
# #             # Sort movies by rating (highest first)
# #             sorted_movies = sorted(movie_ratings, key=lambda x: x[1], reverse=True)
            
# #             # Get top 3 movies
# #             top_3 = sorted_movies[:3]
            
# #             break
    
# #     return top_3

# # # Example usage
# # actor_name = "Chris Evans"
# # top_rated_movies = get_top_rated_movies(actor_name, actors, movies)

# # print()
# # print(actor_name+" Rop 3")

# # for movie_id, rating in top_rated_movies:
# #     movie = get_movie_by_id(movie_id, movies)
# #     print(f"Title: {movie['title']}, Rating: {rating}")

  
