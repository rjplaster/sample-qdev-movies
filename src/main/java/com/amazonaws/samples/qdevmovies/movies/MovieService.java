package com.amazonaws.samples.qdevmovies.movies;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

@Service
public class MovieService {
    private static final Logger logger = LogManager.getLogger(MovieService.class);
    private final List<Movie> movies;
    private final Map<Long, Movie> movieMap;

    public MovieService() {
        this.movies = loadMoviesFromJson();
        this.movieMap = new HashMap<>();
        for (Movie movie : movies) {
            movieMap.put(movie.getId(), movie);
        }
    }

    private List<Movie> loadMoviesFromJson() {
        List<Movie> movieList = new ArrayList<>();
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("movies.json");
            if (inputStream != null) {
                Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name());
                String jsonContent = scanner.useDelimiter("\\A").next();
                scanner.close();
                
                JSONArray moviesArray = new JSONArray(jsonContent);
                for (int i = 0; i < moviesArray.length(); i++) {
                    JSONObject movieObj = moviesArray.getJSONObject(i);
                    movieList.add(new Movie(
                        movieObj.getLong("id"),
                        movieObj.getString("movieName"),
                        movieObj.getString("director"),
                        movieObj.getInt("year"),
                        movieObj.getString("genre"),
                        movieObj.getString("description"),
                        movieObj.getInt("duration"),
                        movieObj.getDouble("imdbRating")
                    ));
                }
            }
        } catch (Exception e) {
            logger.error("Failed to load movies from JSON: {}", e.getMessage());
        }
        return movieList;
    }

    public List<Movie> getAllMovies() {
        return movies;
    }

    public Optional<Movie> getMovieById(Long id) {
        if (id == null || id <= 0) {
            return Optional.empty();
        }
        return Optional.ofNullable(movieMap.get(id));
    }

    /**
     * Search for movies based on multiple criteria with pirate-themed logging
     * Ahoy! This method be searchin' through our treasure chest of movies!
     * 
     * @param name Movie name to search for (case-insensitive partial match)
     * @param id Specific movie ID to find
     * @param genre Genre to filter by (case-insensitive partial match)
     * @return List of movies matching the search criteria
     */
    public List<Movie> searchMovies(String name, Long id, String genre) {
        logger.info("Ahoy! Searchin' for movies with criteria - name: '{}', id: '{}', genre: '{}'", 
                   name, id, genre);
        
        List<Movie> results = new ArrayList<>();
        
        // If all parameters be null, return all movies (full treasure chest!)
        if (isAllParametersEmpty(name, id, genre)) {
            logger.info("Arrr! No search criteria provided, returnin' all movies from the treasure chest!");
            return new ArrayList<>(movies);
        }
        
        for (Movie movie : movies) {
            if (matchesSearchCriteria(movie, name, id, genre)) {
                results.add(movie);
            }
        }
        
        logger.info("Treasure hunt complete! Found {} movies matchin' yer criteria, matey!", results.size());
        return results;
    }

    /**
     * Search movies by name only (case-insensitive partial match)
     * 
     * @param name Movie name to search for
     * @return List of movies with names containing the search term
     */
    public List<Movie> searchMoviesByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            logger.warn("Arrr! Empty name provided for search, returnin' empty results!");
            return new ArrayList<>();
        }
        
        String searchTerm = name.trim().toLowerCase();
        logger.info("Searchin' for movies with name containin': '{}'", searchTerm);
        
        List<Movie> results = movies.stream()
                .filter(movie -> movie.getMovieName().toLowerCase().contains(searchTerm))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        
        logger.info("Found {} movies with names matchin' '{}', ye savvy!", results.size(), searchTerm);
        return results;
    }

    /**
     * Search movies by genre (case-insensitive partial match)
     * 
     * @param genre Genre to filter by
     * @return List of movies in the specified genre
     */
    public List<Movie> searchMoviesByGenre(String genre) {
        if (genre == null || genre.trim().isEmpty()) {
            logger.warn("Arrr! Empty genre provided for search, returnin' empty results!");
            return new ArrayList<>();
        }
        
        String searchGenre = genre.trim().toLowerCase();
        logger.info("Searchin' for movies in genre: '{}'", searchGenre);
        
        List<Movie> results = movies.stream()
                .filter(movie -> movie.getGenre().toLowerCase().contains(searchGenre))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
        
        logger.info("Found {} movies in genre '{}', me hearty!", results.size(), searchGenre);
        return results;
    }

    /**
     * Get all unique genres from the movie collection
     * 
     * @return List of unique genres available
     */
    public List<String> getAllGenres() {
        return movies.stream()
                .map(Movie::getGenre)
                .distinct()
                .sorted()
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
    }

    private boolean isAllParametersEmpty(String name, Long id, String genre) {
        return (name == null || name.trim().isEmpty()) && 
               id == null && 
               (genre == null || genre.trim().isEmpty());
    }

    private boolean matchesSearchCriteria(Movie movie, String name, Long id, String genre) {
        // Check ID match first (exact match required)
        if (id != null && !movie.getId().equals(id)) {
            return false;
        }
        
        // Check name match (case-insensitive partial match)
        if (name != null && !name.trim().isEmpty()) {
            String searchName = name.trim().toLowerCase();
            if (!movie.getMovieName().toLowerCase().contains(searchName)) {
                return false;
            }
        }
        
        // Check genre match (case-insensitive partial match)
        if (genre != null && !genre.trim().isEmpty()) {
            String searchGenre = genre.trim().toLowerCase();
            if (!movie.getGenre().toLowerCase().contains(searchGenre)) {
                return false;
            }
        }
        
        return true;
    }
}
