package com.mticket.service.implemetations;

import com.mticket.entity.Booking;
import com.mticket.entity.Seat;
import com.mticket.entity.Showtime;
import com.mticket.entity.User;
import com.mticket.enums.SeatStatus;
import com.mticket.repository.BookingRepository;
import com.mticket.repository.SeatRepository;
import com.mticket.repository.ShowtimeRepository;
import com.mticket.repository.UserRepository;
import com.mticket.service.interfaces.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;
    private final ShowtimeRepository showTimeRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public String bookSeats(List<Long> seatIds, Long userId, Long showTimeId) {
        Showtime showTime = showTimeRepository.findById(showTimeId)
                .orElseThrow(() -> new IllegalStateException("ShowTime not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("User not found"));

        // Lock the chosen seats
        List<Seat> seats = seatRepository.findByIdsForUpdate(seatIds, showTimeId);
        if (seats.size() != seatIds.size()) {
            throw new IllegalStateException("One or more seats are not available");
        }

        // Create booking
        Booking booking = new Booking();
        booking.setSeats(seats);
        booking.setUser(user);
        booking.setShowtime(showTime);
        booking.setTotalPrice(seats.size() * showTime.getPrice());
        bookingRepository.save(booking);

        // Update seat status
        for (Seat seat : seats) {
            seat.setStatus(SeatStatus.SELECTED);
            seatRepository.save(seat);
        }

        return "Seats " + seats.stream().map(seat -> seat.getRow() + seat.getNumber()).collect(Collectors.joining(", ")) + " reserved successfully";
    }

    @Override
    @Transactional
    public void cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalStateException("Booking not found"));
        for (Seat seat : booking.getSeats()) {
            seat.setStatus(SeatStatus.AVAILABLE);
            seatRepository.save(seat);
        }
        bookingRepository.delete(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Seat> getSeats(Long showTimeId) {
        return seatRepository.findByShowtimeId(showTimeId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }
}
