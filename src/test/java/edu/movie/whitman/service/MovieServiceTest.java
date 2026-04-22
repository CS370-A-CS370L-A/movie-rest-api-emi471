package edu.movie.whitman.service;

import edu.movie.whitman.model.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MovieServiceTest {
    private MovieService movieService;

    @BeforeEach
    void setUp() {
        movieService = new MovieService();
    }

    @Test
    void testCreateAndGetMovie() {
        Movie movie = new Movie(null, "Inception", "Christopher Nolan", 2010);
        Movie created = movieService.createMovie(movie);
        assertNotNull(created.getId());
        assertEquals("Inception", created.getTitle());
        assertEquals("Christopher Nolan", created.getDirector());
        assertEquals(2010, created.getReleaseYear());

        Optional<Movie> found = movieService.getMovieById(created.getId());
        assertTrue(found.isPresent());
        assertEquals(created.getId(), found.get().getId());
    }

    @Test
    void testGetAllMovies() {
        movieService.createMovie(new Movie(null, "Movie1", "Dir1", 2001));
        movieService.createMovie(new Movie(null, "Movie2", "Dir2", 2002));
        List<Movie> movies = movieService.getAllMovies();
        assertEquals(2, movies.size());
    }

    @Test
    void testUpdateMovie() {
        Movie created = movieService.createMovie(new Movie(null, "Old", "OldDir", 1999));
        Movie update = new Movie(null, "New", "NewDir", 2020);
        Optional<Movie> updated = movieService.updateMovie(created.getId(), update);
        assertTrue(updated.isPresent());
        assertEquals("New", updated.get().getTitle());
        assertEquals("NewDir", updated.get().getDirector());
        assertEquals(2020, updated.get().getReleaseYear());
    }

    @Test
    void testUpdateMovieNotFound() {
        Movie update = new Movie(null, "X", "Y", 2000);
        Optional<Movie> updated = movieService.updateMovie(999L, update);
        assertFalse(updated.isPresent());
    }

    @Test
    void testDeleteMovie() {
        Movie created = movieService.createMovie(new Movie(null, "ToDelete", "Dir", 2011));
        boolean deleted = movieService.deleteMovie(created.getId());
        assertTrue(deleted);
        assertFalse(movieService.getMovieById(created.getId()).isPresent());
    }

    @Test
    void testDeleteMovieNotFound() {
        boolean deleted = movieService.deleteMovie(12345L);
        assertFalse(deleted);
    }
}
