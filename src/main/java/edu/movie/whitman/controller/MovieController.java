package edu.movie.whitman.controller;

import edu.movie.whitman.model.Movie;
import edu.movie.whitman.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movies")
public class MovieController {
    
    @Autowired
    private MovieService movieService;

    /**
     * GET /api/movies
     * Retrieve all movies
     * @return 200 OK with list of movies
     */
    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        return ResponseEntity.ok(movies);
    }

    /**
     * GET /api/movies/{id}
     * Retrieve a movie by ID
     * @param id the movie ID
     * @return 200 OK with the movie, or 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable Long id) {
        Optional<Movie> movie = movieService.getMovieById(id);
        if (movie.isPresent()) {
            return ResponseEntity.ok(movie.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Movie with ID " + id + " not found");
        }
    }

    /**
     * POST /api/movies
     * Create a new movie
     * @param movie the movie to create
     * @return 201 Created with the created movie
     */
    @PostMapping
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
        Movie createdMovie = movieService.createMovie(movie);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMovie);
    }

    /**
     * PUT /api/movies/{id}
     * Update an existing movie
     * @param id the movie ID
     * @param movieDetails the updated movie details
     * @return 200 OK with the updated movie, or 404 Not Found
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMovie(@PathVariable Long id, @RequestBody Movie movieDetails) {
        Optional<Movie> updatedMovie = movieService.updateMovie(id, movieDetails);
        if (updatedMovie.isPresent()) {
            return ResponseEntity.ok(updatedMovie.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Movie with ID " + id + " not found");
        }
    }

    /**
     * DELETE /api/movies/{id}
     * Delete a movie by ID
     * @param id the movie ID
     * @return 204 No Content, or 404 Not Found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable Long id) {
        if (movieService.deleteMovie(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Movie with ID " + id + " not found");
        }
    }
}
