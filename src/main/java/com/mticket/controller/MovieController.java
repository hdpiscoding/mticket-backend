package com.mticket.controller;

import com.mticket.base.BaseController;
import com.mticket.entity.Movie;
import com.mticket.service.interfaces.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/movies")
@RequiredArgsConstructor
public class MovieController extends BaseController {
    private final MovieService movieService;

    @GetMapping("")
    public ResponseEntity<Object> getAllMovies() {
        return buildResponse(movieService.getAllMovies(), HttpStatus.OK, "Movies fetched successfully");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("")
    public ResponseEntity<Object> createMovie(@RequestBody Movie movie) {
        return buildResponse(movieService.createMovie(movie), HttpStatus.CREATED, "Movie created successfully");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMovie(@PathVariable Long id, @RequestBody Movie movie) {
        return buildResponse(movieService.updateMovie(id, movie), HttpStatus.OK, "Movie updated successfully");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return buildResponse(null, HttpStatus.OK, "Movie deleted successfully");
    }
}
