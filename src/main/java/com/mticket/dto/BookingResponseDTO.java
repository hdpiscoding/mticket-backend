package com.mticket.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookingResponseDTO {
    private String message;
    private List<SeatInfo> successfulSeats;
    private List<SeatInfo> failedSeats;

    @Data
    public static class SeatInfo {
        private Long seatId;
        private String seatNumber;
        private String status;
    }
}