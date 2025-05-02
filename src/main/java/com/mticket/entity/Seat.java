package com.mticket.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mticket.enums.SeatStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "seat")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "showtime_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Showtime showtime;

    @Column(name = "`row`")
    private String row;

    @Column(name = "number")
    private int number;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
