package com.amazonaws.samples.qdevmovies.movies;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MovieService Search Functionality Tests - Arrr! Testing our treasure hunting methods!")
public class MovieServiceTest {

    private MovieService movieService;

    @BeforeEach
    public void setUp() {
        movieService = new MovieService();
    }

    @Test
    @DisplayName("Should return all movies when no search criteria provided")
    public void testSearchMovies_NoParameters_ReturnsAllMovies() {
        // Arrange & Act
        List<Movie> results = movieService.searchMovies(null, null, null);
        
        // Assert
        assertNotNull(results, "Results should not be null, matey!");
        assertEquals(12, results.size(), "Should return all 12 movies from the treasure chest!");
    }

    @Test
    @DisplayName("Should return all movies when empty string parameters provided")
    public void testSearchMovies_EmptyParameters_ReturnsAllMovies() {
        // Arrange & Act
        List<Movie> results = movieService.searchMovies("", null, "");
        
        // Assert
        assertNotNull(results, "Results should not be null, ye scallywag!");
        assertEquals(12, results.size(), "Should return all 12 movies when parameters be empty!");
    }

    @Test
    @DisplayName("Should find movies by exact name match")
    public void testSearchMovies_ExactNameMatch_ReturnsCorrectMovie() {
        // Arrange & Act
        List<Movie> results = movieService.searchMovies("The Prison Escape", null, null);
        
        // Assert
        assertNotNull(results, "Results should not be null!");
        assertEquals(1, results.size(), "Should find exactly one movie, arrr!");
        assertEquals("The Prison Escape", results.get(0).getMovieName(), "Should find the correct movie!");
        assertEquals(1L, results.get(0).getId(), "Should find movie with ID 1!");
    }

    @Test
    @DisplayName("Should find movies by partial name match (case insensitive)")
    public void testSearchMovies_PartialNameMatch_ReturnsMatchingMovies() {
        // Arrange & Act
        List<Movie> results = movieService.searchMovies("the", null, null);
        
        // Assert
        assertNotNull(results, "Results should not be null, me hearty!");
        assertTrue(results.size() >= 5, "Should find multiple movies with 'the' in the name!");
        
        // Verify all results contain "the" in their names (case insensitive)
        for (Movie movie : results) {
            assertTrue(movie.getMovieName().toLowerCase().contains("the"), 
                      "Movie '" + movie.getMovieName() + "' should contain 'the'!");
        }
    }

    @Test
    @DisplayName("Should find movie by exact ID")
    public void testSearchMovies_ExactIdMatch_ReturnsCorrectMovie() {
        // Arrange & Act
        List<Movie> results = movieService.searchMovies(null, 5L, null);
        
        // Assert
        assertNotNull(results, "Results should not be null!");
        assertEquals(1, results.size(), "Should find exactly one movie with ID 5!");
        assertEquals(5L, results.get(0).getId(), "Should find movie with correct ID!");
        assertEquals("Life Journey", results.get(0).getMovieName(), "Should find the correct movie!");
    }

    @Test
    @DisplayName("Should find movies by genre (case insensitive)")
    public void testSearchMovies_GenreMatch_ReturnsMatchingMovies() {
        // Arrange & Act
        List<Movie> results = movieService.searchMovies(null, null, "drama");
        
        // Assert
        assertNotNull(results, "Results should not be null, matey!");
        assertTrue(results.size() >= 3, "Should find multiple drama movies!");
        
        // Verify all results contain "drama" in their genre (case insensitive)
        for (Movie movie : results) {
            assertTrue(movie.getGenre().toLowerCase().contains("drama"), 
                      "Movie '" + movie.getMovieName() + "' should be in drama genre!");
        }
    }

    @Test
    @DisplayName("Should find movies matching multiple criteria")
    public void testSearchMovies_MultipleCriteria_ReturnsMatchingMovies() {
        // Arrange & Act
        List<Movie> results = movieService.searchMovies("family", null, "crime");
        
        // Assert
        assertNotNull(results, "Results should not be null!");
        assertEquals(1, results.size(), "Should find exactly one movie matching both criteria!");
        assertEquals("The Family Boss", results.get(0).getMovieName(), "Should find 'The Family Boss'!");
        assertTrue(results.get(0).getGenre().toLowerCase().contains("crime"), "Should be in crime genre!");
    }

    @Test
    @DisplayName("Should return empty list when no movies match criteria")
    public void testSearchMovies_NoMatches_ReturnsEmptyList() {
        // Arrange & Act
        List<Movie> results = movieService.searchMovies("NonexistentMovie", null, null);
        
        // Assert
        assertNotNull(results, "Results should not be null even when empty!");
        assertTrue(results.isEmpty(), "Should return empty list when no matches found, arrr!");
    }

    @Test
    @DisplayName("Should return empty list for invalid ID")
    public void testSearchMovies_InvalidId_ReturnsEmptyList() {
        // Arrange & Act
        List<Movie> results = movieService.searchMovies(null, 999L, null);
        
        // Assert
        assertNotNull(results, "Results should not be null!");
        assertTrue(results.isEmpty(), "Should return empty list for non-existent ID!");
    }

    @Test
    @DisplayName("Should find movies by name only using dedicated method")
    public void testSearchMoviesByName_ValidName_ReturnsMatchingMovies() {
        // Arrange & Act
        List<Movie> results = movieService.searchMoviesByName("Space");
        
        // Assert
        assertNotNull(results, "Results should not be null!");
        assertEquals(1, results.size(), "Should find one movie with 'Space' in name!");
        assertEquals("Space Wars: The Beginning", results.get(0).getMovieName(), "Should find correct movie!");
    }

    @Test
    @DisplayName("Should return empty list for null name in searchMoviesByName")
    public void testSearchMoviesByName_NullName_ReturnsEmptyList() {
        // Arrange & Act
        List<Movie> results = movieService.searchMoviesByName(null);
        
        // Assert
        assertNotNull(results, "Results should not be null!");
        assertTrue(results.isEmpty(), "Should return empty list for null name!");
    }

    @Test
    @DisplayName("Should return empty list for empty name in searchMoviesByName")
    public void testSearchMoviesByName_EmptyName_ReturnsEmptyList() {
        // Arrange & Act
        List<Movie> results = movieService.searchMoviesByName("   ");
        
        // Assert
        assertNotNull(results, "Results should not be null!");
        assertTrue(results.isEmpty(), "Should return empty list for empty/whitespace name!");
    }

    @Test
    @DisplayName("Should find movies by genre using dedicated method")
    public void testSearchMoviesByGenre_ValidGenre_ReturnsMatchingMovies() {
        // Arrange & Act
        List<Movie> results = movieService.searchMoviesByGenre("Action");
        
        // Assert
        assertNotNull(results, "Results should not be null!");
        assertTrue(results.size() >= 2, "Should find multiple action movies!");
        
        for (Movie movie : results) {
            assertTrue(movie.getGenre().toLowerCase().contains("action"), 
                      "Movie should be in action genre!");
        }
    }

    @Test
    @DisplayName("Should return empty list for null genre in searchMoviesByGenre")
    public void testSearchMoviesByGenre_NullGenre_ReturnsEmptyList() {
        // Arrange & Act
        List<Movie> results = movieService.searchMoviesByGenre(null);
        
        // Assert
        assertNotNull(results, "Results should not be null!");
        assertTrue(results.isEmpty(), "Should return empty list for null genre!");
    }

    @Test
    @DisplayName("Should return all unique genres")
    public void testGetAllGenres_ReturnsUniqueGenres() {
        // Arrange & Act
        List<String> genres = movieService.getAllGenres();
        
        // Assert
        assertNotNull(genres, "Genres list should not be null!");
        assertFalse(genres.isEmpty(), "Should return some genres, matey!");
        assertTrue(genres.contains("Drama"), "Should contain Drama genre!");
        assertTrue(genres.contains("Action/Crime"), "Should contain Action/Crime genre!");
        
        // Check for uniqueness
        long uniqueCount = genres.stream().distinct().count();
        assertEquals(uniqueCount, genres.size(), "All genres should be unique!");
    }

    @Test
    @DisplayName("Should handle case insensitive search properly")
    public void testSearchMovies_CaseInsensitive_ReturnsCorrectResults() {
        // Arrange & Act
        List<Movie> upperCaseResults = movieService.searchMovies("PRISON", null, null);
        List<Movie> lowerCaseResults = movieService.searchMovies("prison", null, null);
        List<Movie> mixedCaseResults = movieService.searchMovies("PrIsOn", null, null);
        
        // Assert
        assertEquals(upperCaseResults.size(), lowerCaseResults.size(), 
                    "Case should not matter for search!");
        assertEquals(lowerCaseResults.size(), mixedCaseResults.size(), 
                    "Mixed case should work the same!");
        assertFalse(upperCaseResults.isEmpty(), "Should find movies regardless of case!");
    }

    @Test
    @DisplayName("Should maintain existing functionality - getAllMovies")
    public void testGetAllMovies_ReturnsAllMovies() {
        // Arrange & Act
        List<Movie> allMovies = movieService.getAllMovies();
        
        // Assert
        assertNotNull(allMovies, "All movies list should not be null!");
        assertEquals(12, allMovies.size(), "Should return all 12 movies!");
    }

    @Test
    @DisplayName("Should maintain existing functionality - getMovieById")
    public void testGetMovieById_ValidId_ReturnsMovie() {
        // Arrange & Act
        Optional<Movie> movieOpt = movieService.getMovieById(1L);
        
        // Assert
        assertTrue(movieOpt.isPresent(), "Should find movie with ID 1!");
        assertEquals("The Prison Escape", movieOpt.get().getMovieName(), "Should return correct movie!");
    }

    @Test
    @DisplayName("Should maintain existing functionality - getMovieById with invalid ID")
    public void testGetMovieById_InvalidId_ReturnsEmpty() {
        // Arrange & Act
        Optional<Movie> movieOpt = movieService.getMovieById(999L);
        
        // Assert
        assertFalse(movieOpt.isPresent(), "Should not find movie with invalid ID!");
    }
}