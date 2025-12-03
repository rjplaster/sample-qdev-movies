package com.amazonaws.samples.qdevmovies.movies;

import com.amazonaws.samples.qdevmovies.utils.MovieIconUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

@Controller
public class MoviesController {
    private static final Logger logger = LogManager.getLogger(MoviesController.class);

    @Autowired
    private MovieService movieService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/movies")
    public String getMovies(org.springframework.ui.Model model) {
        logger.info("Fetching movies");
        model.addAttribute("movies", movieService.getAllMovies());
        model.addAttribute("allGenres", movieService.getAllGenres());
        return "movies";
    }

    @GetMapping("/movies/search")
    public String searchMovies(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "id", required = false) Long id,
            @RequestParam(value = "genre", required = false) String genre,
            org.springframework.ui.Model model) {
        
        logger.info("Ahoy! Search request received - name: '{}', id: '{}', genre: '{}'", name, id, genre);
        
        try {
            // Validate ID parameter if provided
            if (id != null && id <= 0) {
                logger.warn("Arrr! Invalid movie ID provided: {}", id);
                model.addAttribute("title", "Invalid Search Parameters");
                model.addAttribute("message", "Arrr! That ID be not a valid treasure map number, matey! Please provide a positive number.");
                model.addAttribute("pirateMessage", "🏴‍☠️ Batten down the hatches and try again with proper coordinates!");
                return "error";
            }
            
            // Perform the search
            List<Movie> searchResults = movieService.searchMovies(name, id, genre);
            
            // Add search results and parameters to model
            model.addAttribute("movies", searchResults);
            model.addAttribute("allGenres", movieService.getAllGenres());
            model.addAttribute("searchPerformed", true);
            model.addAttribute("searchName", name);
            model.addAttribute("searchId", id);
            model.addAttribute("searchGenre", genre);
            
            // Add pirate-themed messages based on results
            if (searchResults.isEmpty()) {
                model.addAttribute("emptyResults", true);
                model.addAttribute("pirateMessage", "🏴‍☠️ Arrr! No treasure found with those search terms, me hearty! Try different coordinates for yer treasure hunt!");
                logger.info("No movies found matching search criteria - returnin' empty treasure chest message");
            } else {
                model.addAttribute("pirateMessage", String.format("🏴‍☠️ Ahoy! Found %d pieces of treasure matching yer search, matey!", searchResults.size()));
                logger.info("Search successful! Found {} movies for the crew", searchResults.size());
            }
            
            return "movies";
            
        } catch (IllegalArgumentException e) {
            logger.error("Arrr! Invalid search parameters provided: {}", e.getMessage(), e);
            model.addAttribute("title", "Invalid Search Parameters");
            model.addAttribute("message", "Arrr! Invalid search parameters provided, matey! Please check yer input and try again.");
            model.addAttribute("pirateMessage", "🏴‍☠️ Ye've provided invalid coordinates for the treasure hunt! Check yer map and try again!");
            return "error";
        } catch (RuntimeException e) {
            logger.error("Arrr! Search encountered rough seas: {}", e.getMessage(), e);
            model.addAttribute("title", "Search Error");
            model.addAttribute("message", "Arrr! The search encountered rough seas, matey! Please try again.");
            model.addAttribute("pirateMessage", "🏴‍☠️ The kraken has interfered with yer treasure hunt! Chart a new course and try again!");
            return "error";
        }
    }

    @GetMapping("/movies/{id}/details")
    public String getMovieDetails(@PathVariable("id") Long movieId, org.springframework.ui.Model model) {
        logger.info("Fetching details for movie ID: {}", movieId);
        
        Optional<Movie> movieOpt = movieService.getMovieById(movieId);
        if (!movieOpt.isPresent()) {
            logger.warn("Movie with ID {} not found", movieId);
            model.addAttribute("title", "Movie Not Found");
            model.addAttribute("message", "Movie with ID " + movieId + " was not found.");
            return "error";
        }
        
        Movie movie = movieOpt.get();
        model.addAttribute("movie", movie);
        model.addAttribute("movieIcon", MovieIconUtils.getMovieIcon(movie.getMovieName()));
        model.addAttribute("allReviews", reviewService.getReviewsForMovie(movie.getId()));
        
        return "movie-details";
    }
}