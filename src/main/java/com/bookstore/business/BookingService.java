package com.bookstore.business;

import com.bookstore.model.Booking;
import com.bookstore.model.repositories.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookingService {
    private final BookingRepository bookingRepository;

    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public Optional<Booking> findById(Integer id) {
        return bookingRepository.findById(id);
    }

    public Booking saveBooking(Booking booking) {
        if (booking == null) {
            throw new RuntimeException("Booking can not be null");
        }
        return this.bookingRepository.save(booking);
    }

    public void deleteById(Integer id) {
        this.bookingRepository.deleteById(id);
    }
}