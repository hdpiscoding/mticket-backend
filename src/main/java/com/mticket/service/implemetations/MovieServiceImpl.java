package com.mticket.service.implemetations;

import com.mticket.entity.Movie;
import com.mticket.repository.MovieRepository;
import com.mticket.service.interfaces.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
    private final MovieRepository movieRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @Override
    @Transactional
    public Movie createMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    @Override
    @Transactional
    public Movie updateMovie(Long id, Movie movie) {
        Movie m = movieRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Movie not found"));
        m.setTitle(movie.getTitle());
        m.setGenre(movie.getGenre());
        m.setDescription(movie.getDescription());
        m.setReleaseDate(movie.getReleaseDate());
        m.setPosterUrl(movie.getPosterUrl());
        m.setDuration(movie.getDuration());
        return movieRepository.save(m);
    }

    @Override
    @Transactional
    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    @Override
    public Movie getMovieById(Long id) {
        return movieRepository.findById(id).orElseThrow(() -> new IllegalStateException("Movie not found"));
    }
}
