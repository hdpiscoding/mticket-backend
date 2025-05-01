package com.mticket.controller;

import com.mticket.base.BaseController;
import com.mticket.dto.BookingRequestDTO;
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

    @PostMapping("/book-seats")
    public ResponseEntity<Object> bookSeats(@RequestHeader("Authorization") String authHeader, @RequestBody BookingRequestDTO request) {
        try{
            Long userId = jwtService.extractUserId(authHeader.substring(7));
            return buildResponse(null, HttpStatus.CREATED, ticketService.bookSeats(request.getSeatIds(), userId, request.getShowTimeId()));
        }
        catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error");
        }
    }

    @PostMapping("/cancel-booking")
    public ResponseEntity<String> cancelBooking(@RequestParam Long bookingId) {
        try {
            ticketService.cancelBooking(bookingId);
            return ResponseEntity.ok("Booking cancelled");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/seats")
    public ResponseEntity<Object> getSeats(@RequestParam Long ShowTimeId) {
        return buildResponse(ticketService.getSeats(ShowTimeId), HttpStatus.OK, "Seats retrieved successfully");
    }

    @GetMapping("/bookings/me")
    public ResponseEntity<Object> getMyBookings(@RequestHeader("Authorization") String authHeader) {
        Long userId = jwtService.extractUserId(authHeader.substring(7));
        return buildResponse(ticketService.getBookingsByUserId(userId), HttpStatus.OK, "Bookings retrieved successfully");
    }
}
