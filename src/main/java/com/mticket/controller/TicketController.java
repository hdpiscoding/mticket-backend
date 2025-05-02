package com.mticket.controller;

import com.mticket.base.BaseController;
import com.mticket.dto.BookingRequestDTO;
import com.mticket.dto.BookingResponseDTO;
import com.mticket.service.interfaces.JwtService;
import com.mticket.service.interfaces.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class TicketController extends BaseController {
    private final TicketService ticketService;
    private final JwtService jwtService;

    @PostMapping("/bookings/book")
    public ResponseEntity<Object> bookSeats(@RequestHeader("Authorization") String authHeader, @RequestBody BookingRequestDTO request) {
        try{
            Long userId = jwtService.extractUserId(authHeader.substring(7));
            BookingResponseDTO response = ticketService.bookSeats(request.getSeatIds(), userId, request.getShowTimeId());
            if (response.getSuccessfulSeats().isEmpty()) {
                return buildResponse(null, HttpStatus.BAD_REQUEST, response.getMessage());
            }
            else if (response.getFailedSeats().isEmpty()) {
                return buildResponse(response, HttpStatus.CREATED, "Booking created successfully");
            }
            else {
                return buildResponse(response, HttpStatus.CREATED, response.getMessage());
            }
        }
        catch (IllegalStateException e) {
            BookingResponseDTO errorResponse = new BookingResponseDTO();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            BookingResponseDTO errorResponse = new BookingResponseDTO();
            errorResponse.setMessage("Server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/bookings/cancel")
    public ResponseEntity<Object> cancelBooking(@RequestParam Long bookingId) {
        try {
            ticketService.cancelBooking(bookingId);
            return buildResponse(null, HttpStatus.OK, "Booking cancelled successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/seats")
    public ResponseEntity<Object> getSeats(@RequestParam Long showtimeId) {
        return buildResponse(ticketService.getSeats(showtimeId), HttpStatus.OK, "Seats retrieved successfully");
    }

    @GetMapping("/bookings/me")
    public ResponseEntity<Object> getMyBookings(@RequestHeader("Authorization") String authHeader) {
        Long userId = jwtService.extractUserId(authHeader.substring(7));
        return buildResponse(ticketService.getBookingsByUserId(userId), HttpStatus.OK, "Bookings retrieved successfully");
    }
}
