package edu.movie.whitman.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.movie.whitman.model.Movie;
import edu.movie.whitman.service.MovieService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MovieController.class)
class MovieControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllMovies_returnsList() throws Exception {
        Mockito.when(movieService.getAllMovies()).thenReturn(Arrays.asList(
                new Movie(1L, "A", "DirA", 2000),
                new Movie(2L, "B", "DirB", 2001)
        ));
        mockMvc.perform(get("/api/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("A"));
    }

    @Test
    void getMovieById_found() throws Exception {
        Mockito.when(movieService.getMovieById(1L)).thenReturn(Optional.of(new Movie(1L, "A", "DirA", 2000)));
        mockMvc.perform(get("/api/movies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("A"));
    }

    @Test
    void getMovieById_notFound() throws Exception {
        Mockito.when(movieService.getMovieById(99L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/movies/99"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Movie with ID 99 not found"));
    }

    @Test
    void createMovie_returnsCreated() throws Exception {
        Movie toCreate = new Movie(null, "C", "DirC", 2022);
        Movie created = new Movie(3L, "C", "DirC", 2022);
        Mockito.when(movieService.createMovie(any(Movie.class))).thenReturn(created);
        mockMvc.perform(post("/api/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(toCreate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3L));
    }

    @Test
    void updateMovie_found() throws Exception {
        Movie update = new Movie(null, "D", "DirD", 2023);
        Movie updated = new Movie(4L, "D", "DirD", 2023);
        Mockito.when(movieService.updateMovie(eq(4L), any(Movie.class))).thenReturn(Optional.of(updated));
        mockMvc.perform(put("/api/movies/4")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("D"));
    }

    @Test
    void updateMovie_notFound() throws Exception {
        Mockito.when(movieService.updateMovie(eq(404L), any(Movie.class))).thenReturn(Optional.empty());
        mockMvc.perform(put("/api/movies/404")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Movie(null, "X", "Y", 2000))))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Movie with ID 404 not found"));
    }

    @Test
    void deleteMovie_found() throws Exception {
        Mockito.when(movieService.deleteMovie(5L)).thenReturn(true);
        mockMvc.perform(delete("/api/movies/5"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteMovie_notFound() throws Exception {
        Mockito.when(movieService.deleteMovie(6L)).thenReturn(false);
        mockMvc.perform(delete("/api/movies/6"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Movie with ID 6 not found"));
    }
}
