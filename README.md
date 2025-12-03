# Movie Service - Spring Boot Demo Application

A simple movie catalog web application built with Spring Boot, demonstrating Java application development best practices with a fun pirate-themed search feature!

## Features

- **Movie Catalog**: Browse 12 classic movies with detailed information
- **Movie Details**: View comprehensive information including director, year, genre, duration, and description
- **🏴‍☠️ Pirate-Themed Movie Search**: Ahoy! Search for treasure (movies) by name, ID, or genre with our swashbuckling search interface
- **Advanced Filtering**: Filter movies using multiple criteria simultaneously
- **Customer Reviews**: Each movie includes authentic customer reviews with ratings and avatars
- **Responsive Design**: Mobile-first design that works on all devices
- **Modern UI**: Dark theme with gradient backgrounds and smooth animations
- **Pirate Language**: Fun pirate-themed messages and error handling throughout the search experience

## Technology Stack

- **Java 8**
- **Spring Boot 2.0.5**
- **Maven** for dependency management
- **Thymeleaf** for templating
- **Log4j 2.20.0**
- **JUnit 5.8.2**

## Quick Start

### Prerequisites

- Java 8 or higher
- Maven 3.6+

### Run the Application

```bash
git clone https://github.com/<youruser>/sample-qdev-movies.git
cd sample-qdev-movies
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Access the Application

- **Movie List**: http://localhost:8080/movies
- **Movie Search**: http://localhost:8080/movies/search (with query parameters)
- **Movie Details**: http://localhost:8080/movies/{id}/details (where {id} is 1-12)

## Building for Production

```bash
mvn clean package
java -jar target/sample-qdev-movies-0.1.0.jar
```

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/amazonaws/samples/qdevmovies/
│   │       ├── MoviesApplication.java    # Main Spring Boot application
│   │       ├── MoviesController.java     # REST controller for movie endpoints
│   │       ├── MovieService.java         # Business logic with search functionality
│   │       ├── Movie.java                # Movie data model
│   │       ├── Review.java               # Review data model
│   │       └── utils/
│   │           ├── MovieIconUtils.java   # Movie icon utilities
│   │           └── MovieUtils.java       # Movie validation utilities
│   └── resources/
│       ├── application.yml               # Application configuration
│       ├── movies.json                   # Movie data (12 sample movies)
│       ├── mock-reviews.json             # Mock review data
│       ├── templates/
│       │   ├── movies.html               # Main movies page with search form
│       │   └── movie-details.html        # Movie details page
│       └── log4j2.xml                    # Logging configuration
└── test/                                 # Unit tests including search functionality
```

## API Endpoints

### Get All Movies
```
GET /movies
```
Returns an HTML page displaying all movies with ratings, basic information, and a pirate-themed search form.

### 🏴‍☠️ Search Movies (New Feature!)
```
GET /movies/search
```
Ahoy matey! Search through our treasure chest of movies using various criteria.

**Query Parameters:**
- `name` (optional): Movie name to search for (case-insensitive partial match)
- `id` (optional): Specific movie ID to find (exact match, must be positive number)
- `genre` (optional): Genre to filter by (case-insensitive partial match)

**Examples:**
```bash
# Search by movie name (partial match)
http://localhost:8080/movies/search?name=prison

# Search by specific movie ID
http://localhost:8080/movies/search?id=5

# Search by genre
http://localhost:8080/movies/search?genre=drama

# Search with multiple criteria
http://localhost:8080/movies/search?name=the&genre=action

# Search with no parameters (returns all movies)
http://localhost:8080/movies/search
```

**Response Features:**
- 🏴‍☠️ Pirate-themed success and error messages
- Preserves search parameters in the form after submission
- Displays "No treasure found" message for empty results
- Shows search results in the same grid format as the main movies page
- Includes genre dropdown populated with all available genres

### Get Movie Details
```
GET /movies/{id}/details
```
Returns an HTML page with detailed movie information and customer reviews.

**Parameters:**
- `id` (path parameter): Movie ID (1-12)

**Example:**
```
http://localhost:8080/movies/1/details
```

## Search Functionality Details

### 🏴‍☠️ Pirate-Themed Search Interface

The search functionality includes a fun pirate theme with:

- **Treasure Hunt Form**: Styled search form with pirate-themed labels and placeholders
- **Pirate Messages**: Success and error messages using pirate language
- **Responsive Design**: Works on all devices with mobile-friendly layout
- **Genre Dropdown**: Dynamically populated with all available genres from the movie collection

### Search Capabilities

1. **Name Search**: Case-insensitive partial matching
   - Example: Searching "prison" finds "The Prison Escape"
   
2. **ID Search**: Exact match for specific movie lookup
   - Example: Searching ID "5" finds "Life Journey"
   
3. **Genre Search**: Case-insensitive partial matching
   - Example: Searching "drama" finds all movies with "Drama" in their genre
   
4. **Combined Search**: Use multiple criteria simultaneously
   - Example: Name "the" + Genre "action" finds action movies with "the" in the title

### Error Handling

- **Invalid ID**: Negative or zero IDs return pirate-themed error messages
- **No Results**: Empty search results display encouraging pirate messages
- **Server Errors**: Graceful error handling with pirate-themed error pages

## Available Movies

The application includes 12 sample movies across various genres:

1. **The Prison Escape** (Drama) - ID: 1
2. **The Family Boss** (Crime/Drama) - ID: 2  
3. **The Masked Hero** (Action/Crime) - ID: 3
4. **Urban Stories** (Crime/Drama) - ID: 4
5. **Life Journey** (Drama/Romance) - ID: 5
6. **Dream Heist** (Action/Sci-Fi) - ID: 6
7. **The Virtual World** (Action/Sci-Fi) - ID: 7
8. **The Wise Guys** (Crime/Drama) - ID: 8
9. **The Quest for the Ring** (Adventure/Fantasy) - ID: 9
10. **Space Wars: The Beginning** (Adventure/Sci-Fi) - ID: 10
11. **The Factory Owner** (Drama/History) - ID: 11
12. **Underground Club** (Drama/Thriller) - ID: 12

## Testing

The application includes comprehensive unit tests for all functionality:

```bash
# Run all tests
mvn test

# Run specific test classes
mvn test -Dtest=MovieServiceTest
mvn test -Dtest=MoviesControllerTest
```

**Test Coverage:**
- MovieService search methods (all search scenarios)
- MoviesController search endpoint (parameter handling, error cases)
- Edge cases (empty results, invalid parameters, null values)
- Pirate-themed message validation
- Backward compatibility with existing functionality

## Troubleshooting

### Port 8080 already in use

Run on a different port:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

### Build failures

Clean and rebuild:
```bash
mvn clean compile
```

### Search not working

1. Verify the application is running on the correct port
2. Check that movie data is loaded properly (should see 12 movies)
3. Ensure search parameters are properly URL-encoded
4. Check application logs for pirate-themed error messages

## Contributing

This project is designed as a demonstration application. Feel free to:
- Add more movies to the catalog (update `movies.json`)
- Enhance the UI/UX with more pirate themes
- Add new search features (year range, rating filters, etc.)
- Improve the responsive design
- Add more pirate language elements
- Implement pagination for large result sets

## License

This sample code is licensed under the MIT-0 License. See the LICENSE file.

---

🏴‍☠️ **Ahoy matey!** Enjoy searching through our treasure chest of movies! May ye find the perfect film for yer viewing pleasure! 🏴‍☠️
