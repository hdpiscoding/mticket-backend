package com.mticket.service.interfaces;

import com.mticket.entity.Showtime;

import java.util.List;

public interface ShowtimeService {
    Showtime createShowtime(Long movieId, Showtime showtime);
    Showtime updateShowtime(Long id, Long movieId, Showtime showtime);
    void deleteShowtime(Long id);
    List<Showtime> getAllShowtimesByMovie(Long movieId);
}
