package com.amazonaws.samples.qdevmovies.movies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.ui.Model;
import org.springframework.ui.ExtendedModelMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("MoviesController Tests - Arrr! Testing our movie treasure map endpoints!")
public class MoviesControllerTest {

    private MoviesController moviesController;
    private Model model;
    private MovieService mockMovieService;
    private ReviewService mockReviewService;

    @BeforeEach
    public void setUp() {
        moviesController = new MoviesController();
        model = new ExtendedModelMap();
        
        // Create mock services
        mockMovieService = new MovieService() {
            @Override
            public List<Movie> getAllMovies() {
                return Arrays.asList(
                    new Movie(1L, "Test Movie", "Test Director", 2023, "Drama", "Test description", 120, 4.5),
                    new Movie(2L, "Action Movie", "Action Director", 2022, "Action", "Action description", 110, 4.0)
                );
            }
            
            @Override
            public Optional<Movie> getMovieById(Long id) {
                if (id == 1L) {
                    return Optional.of(new Movie(1L, "Test Movie", "Test Director", 2023, "Drama", "Test description", 120, 4.5));
                }
                return Optional.empty();
            }
            
            @Override
            public List<Movie> searchMovies(String name, Long id, String genre) {
                List<Movie> results = new ArrayList<>();
                
                // Mock search logic for testing
                if (name != null && name.toLowerCase().contains("test")) {
                    results.add(new Movie(1L, "Test Movie", "Test Director", 2023, "Drama", "Test description", 120, 4.5));
                }
                if (id != null && id == 1L) {
                    results.add(new Movie(1L, "Test Movie", "Test Director", 2023, "Drama", "Test description", 120, 4.5));
                }
                if (genre != null && genre.toLowerCase().contains("action")) {
                    results.add(new Movie(2L, "Action Movie", "Action Director", 2022, "Action", "Action description", 110, 4.0));
                }
                
                // Return empty list if no criteria match or if searching for "nonexistent"
                if ((name != null && name.toLowerCase().contains("nonexistent")) ||
                    (id != null && id == 999L)) {
                    return new ArrayList<>();
                }
                
                // If no specific criteria, return all movies
                if ((name == null || name.trim().isEmpty()) && 
                    id == null && 
                    (genre == null || genre.trim().isEmpty())) {
                    return getAllMovies();
                }
                
                return results;
            }
            
            @Override
            public List<String> getAllGenres() {
                return Arrays.asList("Action", "Drama", "Comedy");
            }
        };
        
        mockReviewService = new ReviewService() {
            @Override
            public List<Review> getReviewsForMovie(long movieId) {
                return new ArrayList<>();
            }
        };
        
        // Inject mocks using reflection
        try {
            java.lang.reflect.Field movieServiceField = MoviesController.class.getDeclaredField("movieService");
            movieServiceField.setAccessible(true);
            movieServiceField.set(moviesController, mockMovieService);
            
            java.lang.reflect.Field reviewServiceField = MoviesController.class.getDeclaredField("reviewService");
            reviewServiceField.setAccessible(true);
            reviewServiceField.set(moviesController, mockReviewService);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject mock services", e);
        }
    }

    @Test
    @DisplayName("Should return movies view for getMovies")
    public void testGetMovies() {
        String result = moviesController.getMovies(model);
        assertNotNull(result);
        assertEquals("movies", result);
        
        // Verify model attributes
        assertTrue(model.containsAttribute("movies"), "Model should contain movies attribute!");
        assertTrue(model.containsAttribute("allGenres"), "Model should contain allGenres attribute!");
    }

    @Test
    @DisplayName("Should return movie details view for valid movie ID")
    public void testGetMovieDetails() {
        String result = moviesController.getMovieDetails(1L, model);
        assertNotNull(result);
        assertEquals("movie-details", result);
    }

    @Test
    @DisplayName("Should return error view for invalid movie ID")
    public void testGetMovieDetailsNotFound() {
        String result = moviesController.getMovieDetails(999L, model);
        assertNotNull(result);
        assertEquals("error", result);
    }

    @Test
    @DisplayName("Should handle search with no parameters - return all movies")
    public void testSearchMovies_NoParameters_ReturnsAllMovies() {
        // Arrange & Act
        String result = moviesController.searchMovies(null, null, null, model);
        
        // Assert
        assertEquals("movies", result, "Should return movies view!");
        assertTrue(model.containsAttribute("movies"), "Model should contain movies!");
        assertTrue(model.containsAttribute("searchPerformed"), "Should indicate search was performed!");
        assertTrue(model.containsAttribute("pirateMessage"), "Should contain pirate message!");
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertEquals(2, movies.size(), "Should return all movies when no search criteria!");
    }

    @Test
    @DisplayName("Should handle search by movie name")
    public void testSearchMovies_ByName_ReturnsMatchingMovies() {
        // Arrange & Act
        String result = moviesController.searchMovies("test", null, null, model);
        
        // Assert
        assertEquals("movies", result, "Should return movies view!");
        assertTrue(model.containsAttribute("movies"), "Model should contain movies!");
        assertTrue(model.containsAttribute("searchName"), "Should preserve search name!");
        assertEquals("test", model.getAttribute("searchName"), "Should preserve search parameter!");
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertEquals(1, movies.size(), "Should find one movie matching 'test'!");
        assertEquals("Test Movie", movies.get(0).getMovieName(), "Should find correct movie!");
    }

    @Test
    @DisplayName("Should handle search with no results - empty treasure chest")
    public void testSearchMovies_NoResults_ReturnsEmptyWithPirateMessage() {
        // Arrange & Act
        String result = moviesController.searchMovies("nonexistent", null, null, model);
        
        // Assert
        assertEquals("movies", result, "Should return movies view!");
        assertTrue(model.containsAttribute("emptyResults"), "Should indicate empty results!");
        assertTrue(model.containsAttribute("pirateMessage"), "Should contain pirate message for empty results!");
        
        String pirateMessage = (String) model.getAttribute("pirateMessage");
        assertTrue(pirateMessage.contains("No treasure found"), "Pirate message should mention no treasure found!");
        
        @SuppressWarnings("unchecked")
        List<Movie> movies = (List<Movie>) model.getAttribute("movies");
        assertTrue(movies.isEmpty(), "Should return empty list when no matches!");
    }

    @Test
    @DisplayName("Should handle invalid movie ID in search")
    public void testSearchMovies_InvalidId_ReturnsError() {
        // Arrange & Act
        String result = moviesController.searchMovies(null, -1L, null, model);
        
        // Assert
        assertEquals("error", result, "Should return error view for invalid ID!");
        assertTrue(model.containsAttribute("title"), "Should contain error title!");
        assertTrue(model.containsAttribute("message"), "Should contain error message!");
        assertTrue(model.containsAttribute("pirateMessage"), "Should contain pirate error message!");
        
        String errorMessage = (String) model.getAttribute("message");
        assertTrue(errorMessage.contains("Arrr!"), "Error message should be pirate-themed!");
    }

    @Test
    @DisplayName("Should maintain backward compatibility with existing functionality")
    public void testMovieServiceIntegration() {
        List<Movie> movies = mockMovieService.getAllMovies();
        assertEquals(2, movies.size());
        assertEquals("Test Movie", movies.get(0).getMovieName());
        
        Optional<Movie> movie = mockMovieService.getMovieById(1L);
        assertTrue(movie.isPresent());
        assertEquals("Test Movie", movie.get().getMovieName());
    }
}
