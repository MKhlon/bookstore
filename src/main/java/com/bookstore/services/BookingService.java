package com.bookstore.services;

import com.bookstore.converter.BookingConverter;
import com.bookstore.dto.BookingDto;
import com.bookstore.repositories.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final BookingConverter bookingConverter;

    public BookingService(BookingRepository bookingRepository, BookingConverter bookingConverter) {
        this.bookingRepository = bookingRepository;
        this.bookingConverter = bookingConverter;
    }

    public Optional<BookingDto> findById(Integer id) {
        return bookingRepository.findById(id)
                .map(bookingConverter::entityToDto);
    }

    public BookingDto saveBooking(BookingDto dto) {
        if (dto == null) {
            throw new RuntimeException("Booking can not be null");
        }
        var createdBooking = bookingRepository.save(bookingConverter.dtoToEntity(dto));
        return bookingConverter.entityToDto(createdBooking);
    }

    public void deleteById(Integer id) {
        this.bookingRepository.deleteById(id);
    }
}
