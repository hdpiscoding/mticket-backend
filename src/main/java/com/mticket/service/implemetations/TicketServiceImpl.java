package com.mticket.service.implemetations;

import com.mticket.dto.BookingResponseDTO;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    public BookingResponseDTO bookSeats(List<Long> seatIds, Long userId, Long showTimeId) {
        Showtime showTime = showTimeRepository.findById(showTimeId)
                .orElseThrow(() -> new IllegalStateException("ShowTime not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("User not found"));

        List<Seat> availableSeats = new ArrayList<>();
        List<BookingResponseDTO.SeatInfo> successfulSeats = new ArrayList<>();
        List<BookingResponseDTO.SeatInfo> failedSeats = new ArrayList<>();

        for (Long seatId : seatIds) {
            Optional<Seat> seatOpt = seatRepository.findByIdForUpdate(seatId, showTimeId);
            if (seatOpt.isPresent()) {
                Seat seat = seatOpt.get();
                availableSeats.add(seat);
                successfulSeats.add(new BookingResponseDTO.SeatInfo() {{
                    setSeatId(seat.getId());
                    setSeatNumber(seat.getRow() + seat.getNumber());
                    setStatus(String.valueOf(SeatStatus.SELECTED));
                }});
            } else {
                Seat seat = seatRepository.findById(seatId).orElse(null);
                failedSeats.add(new BookingResponseDTO.SeatInfo() {{
                    setSeatId(seatId);
                    setSeatNumber(seat != null ? seat.getRow() + seat.getNumber() : "Unknown");
                    setStatus(seat != null && seat.getShowtime().getId().equals(showTimeId) ? "SELECTED" : "INVALID");
                }});
            }
        }

        BookingResponseDTO response = new BookingResponseDTO();
        response.setSuccessfulSeats(successfulSeats);
        response.setFailedSeats(failedSeats);

        if (!availableSeats.isEmpty()) {
            Booking booking = new Booking();
            booking.setSeats(availableSeats);
            booking.setUser(user);
            booking.setShowtime(showTime);
            booking.setTotalPrice(showTime.getPrice() * availableSeats.size());
            bookingRepository.save(booking);

            for (Seat seat : availableSeats) {
                seat.setStatus(SeatStatus.SELECTED);
                seatRepository.save(seat);
            }

            response.setMessage("Reserved " + successfulSeats.size() + " seat(s) successfully. " +
                    (failedSeats.isEmpty() ? "" : failedSeats.size() + " seat(s) could not be reserved."));
        } else {
            response.setMessage("No seats could be reserved.");
        }

        return response;
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
