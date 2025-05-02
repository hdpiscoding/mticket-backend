package com.mticket.service.implemetations;

import com.mticket.entity.Movie;
import com.mticket.entity.Seat;
import com.mticket.entity.Showtime;
import com.mticket.enums.SeatStatus;
import com.mticket.repository.MovieRepository;
import com.mticket.repository.SeatRepository;
import com.mticket.repository.ShowtimeRepository;
import com.mticket.service.interfaces.ShowtimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShowtimeServiceImpl implements ShowtimeService {
    private final MovieRepository movieRepository;
    private final ShowtimeRepository showTimeRepository;
    private final SeatRepository seatRepository;

    @Override
    @Transactional
    public Showtime createShowtime(Long movieId, Showtime showtime) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new IllegalStateException("Movie not found"));
        showtime.setMovie(movie);
        Showtime savedShowTime = showTimeRepository.save(showtime);
        for (int i = 1; i <= showtime.getTotalSeats(); i++) {
            Seat seat = new Seat();
            seat.setRow(String.valueOf((char) ('A' + (i - 1) / 10)));
            seat.setNumber(i % 10);
            seat.setStatus(SeatStatus.AVAILABLE);
            seat.setShowtime(savedShowTime);
            seatRepository.save(seat);
        }
        return savedShowTime;
    }

    @Override
    @Transactional
    public Showtime updateShowtime(Long id, Showtime showtime) {
        Showtime s = showTimeRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("ShowTime not found"));
        s.setRoom(showtime.getRoom());
        s.setPrice(showtime.getPrice());
        s.setDate(showtime.getDate());
        s.setTime(showtime.getTime());
        return showTimeRepository.save(s);
    }

    @Override
    @Transactional
    public void deleteShowtime(Long id) {
        showTimeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Showtime> getAllShowtimesByMovie(Long movieId) {
        return showTimeRepository.findByMovieId(movieId);
    }
}
