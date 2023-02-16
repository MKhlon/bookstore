package com.bookstore.converter;

import com.bookstore.dto.BookingDto;
import com.bookstore.model.Booking;
import com.bookstore.model.BookingStatus;
import com.bookstore.model.enums.BookingStatusName;
import com.bookstore.repositories.ProductRepository;
import com.bookstore.repositories.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class BookingConverter extends Converter {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public BookingConverter(ProductRepository productRepository,
                            UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public BookingDto entityToDto(Booking booking) {
        var bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setProductId(booking.getProduct().getId());
        bookingDto.setProductName(booking.getProduct().getName());
        bookingDto.setUserId(booking.getUser().getId());
        bookingDto.setUserName(booking.getUser().getName());
        bookingDto.setDeliveryAddress(booking.getDeliveryAddress());
        bookingDto.setDate(booking.getDate());
        bookingDto.setTime(booking.getTime());
        bookingDto.setStatusId(booking.getStatus().getId());
        bookingDto.setStatusName(booking.getStatus().getName().name());
        bookingDto.setQuantity(booking.getQuantity());
        return bookingDto;
    }

    public Booking dtoToEntity(BookingDto bookingDto) {
        var booking = new Booking();
        var product = productRepository.findById(bookingDto.getProductId()).orElse(null);
        var user = userRepository.findById(bookingDto.getUserId()).orElse(null);
        if(bookingDto.getId() != null) {
            booking.setId(bookingDto.getId());
        }
        booking.setProduct(product);
        booking.setUser(user);
        booking.setDeliveryAddress(bookingDto.getDeliveryAddress());
        booking.setDate(bookingDto.getDate());
        booking.setTime(bookingDto.getTime());
        booking.setQuantity(bookingDto.getQuantity());
        var status = new BookingStatus();
        status.setName(BookingStatusName.valueOf(bookingDto.getStatusName()));
        status.setId(bookingDto.getStatusId());
        booking.setStatus(status);
        return booking;
    }
}
