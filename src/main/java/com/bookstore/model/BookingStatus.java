package com.bookstore.model;

import com.bookstore.model.enums.BookingStatusType;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "booking_status")
public class BookingStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", insertable = false, updatable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private BookingStatusType name;
}