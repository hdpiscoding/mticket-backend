package com.mticket.repository;

import com.mticket.entity.Seat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Seat s WHERE s.id = :seatId AND s.status = 'AVAILABLE' AND s.showtime.id = :showtimeId")
    Optional<Seat> findByIdForUpdate(@Param("seatId") Long seatId, @Param("showtimeId") Long showtimeId);

    List<Seat> findByShowtimeId(Long showTimeId);
}
