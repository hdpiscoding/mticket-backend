package com.mticket.controller;

import com.mticket.base.BaseController;
import com.mticket.entity.Showtime;
import com.mticket.service.interfaces.ShowtimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/showtimes")
@RequiredArgsConstructor
public class ShowtimeController extends BaseController {
    private final ShowtimeService showtimeService;

    @GetMapping("/{movieId}")
    public ResponseEntity<Object> getAllShowtimesByMovie(@PathVariable Long movieId) {
        return buildResponse(showtimeService.getAllShowtimesByMovie(movieId), HttpStatus.OK, "Showtimes retrieved successfully");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("")
    public ResponseEntity<Object> createShowtime(@RequestParam Long movieId, @RequestBody Showtime showtime) {
        return buildResponse(showtimeService.createShowtime(movieId, showtime), HttpStatus.CREATED, "Showtime created successfully");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateShowtime(@PathVariable Long id, @RequestParam Long movieId, @RequestBody Showtime showtime) {
        return buildResponse(showtimeService.updateShowtime(id, movieId, showtime), HttpStatus.OK, "Showtime updated successfully");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteShowtime(@PathVariable Long id) {
        showtimeService.deleteShowtime(id);
        return buildResponse(null, HttpStatus.OK, "Showtime deleted successfully");
    }
}
