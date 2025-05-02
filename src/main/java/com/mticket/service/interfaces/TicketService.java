package com.mticket.service.interfaces;

import com.mticket.dto.BookingResponseDTO;
import com.mticket.entity.Booking;
import com.mticket.entity.Seat;

import java.util.List;

public interface TicketService {
    BookingResponseDTO bookSeats(List<Long> seatIds, Long userId, Long showTimeId);
    void cancelBooking(Long bookingId);
    List<Seat> getSeats(Long showTimeId);
    List<Booking> getBookingsByUserId(Long userId);
}
