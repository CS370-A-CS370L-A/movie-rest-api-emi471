package edu.movie.whitman.service;

import edu.movie.whitman.model.Movie;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    private final List<Movie> movies = new ArrayList<>();
    private Long nextId = 1L;

    /**
     * Get all movies
     * @return list of all movies
     */
    public List<Movie> getAllMovies() {
        return new ArrayList<>(movies);
    }

    /**
     * Get a movie by ID
     * @param id the movie ID
     * @return Optional containing the movie if found
     */
    public Optional<Movie> getMovieById(Long id) {
        return movies.stream()
                .filter(movie -> movie.getId().equals(id))
                .findFirst();
    }

    /**
     * Create a new movie
     * @param movie the movie to create (without ID)
     * @return the created movie with assigned ID
     */
    public Movie createMovie(Movie movie) {
        movie.setId(nextId++);
        movies.add(movie);
        return movie;
    }

    /**
     * Update an existing movie
     * @param id the movie ID
     * @param movieDetails the updated movie details
     * @return Optional containing the updated movie if found
     */
    public Optional<Movie> updateMovie(Long id, Movie movieDetails) {
        Optional<Movie> existingMovie = getMovieById(id);
        if (existingMovie.isPresent()) {
            Movie movie = existingMovie.get();
            movie.setTitle(movieDetails.getTitle());
            movie.setDirector(movieDetails.getDirector());
            movie.setReleaseYear(movieDetails.getReleaseYear());
            return Optional.of(movie);
        }
        return Optional.empty();
    }

    /**
     * Delete a movie by ID
     * @param id the movie ID
     * @return true if the movie was deleted, false otherwise
     */
    public boolean deleteMovie(Long id) {
        return movies.removeIf(movie -> movie.getId().equals(id));
    }
}
