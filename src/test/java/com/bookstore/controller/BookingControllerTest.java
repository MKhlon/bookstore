package com.bookstore.controller;

import com.bookstore.dto.BookingDto;
import com.bookstore.services.BookingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Objects;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {
    private final static Integer ID = 25;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @Test
    public void whenGetBookingByIdOfExistingBookingThenReturnOk() {
        // given
        var booking = new BookingDto();
        booking.setId(ID);

        // when
        when(bookingService.findById(ID)).thenReturn(Optional.of(booking));
        var response = bookingController.getBookingById(ID);

        // then
        verify(bookingService, times(1)).findById(ID);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(booking, response.getBody());
    }

    @Test
    public void whenGetBookingByIdOfNotExistingBookingThenReturnNotFound() {
        // given
        when(bookingService.findById(ID)).thenReturn(Optional.empty());

        // when
        var actual = bookingController.getBookingById(ID);

        // then
        verify(bookingService, times(1)).findById(ID);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
    }

    @Test
    public void whenCreateBookingWithNullBookingThenReturnBadRequest() {
        // when
        var response = bookingController.createBooking(null);

        // then
        verify(bookingService, never()).saveBooking(null);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void whenCreateBookingWithValidBookingThenReturnCreated() {
        // given
        var booking = new BookingDto();
        // bookingId is set as saveBooking() set up bookingId
        when(bookingService.saveBooking(booking))
                .thenAnswer(invocation -> {
                    BookingDto b = invocation.getArgument(0);
                    b.setId(555);
                    return b;
                });

        // when
        var response = bookingController.createBooking(booking);

        // then
        verify(bookingService, times(1)).saveBooking(booking);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(Objects.requireNonNull(response.getBody()).getId());
        Assertions.assertEquals(booking, response.getBody());
    }

    @Test
    public void whenUpdateBookingWithExistingBookingThenReturnUpdatedProduct() {
        // given
        var booking = new BookingDto();
        booking.setId(77);
        var bookingId = booking.getId();

        var newBooking = new BookingDto();
        newBooking.setQuantity(9);

        // when
        when(bookingService.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingService.saveBooking(newBooking)).thenReturn(newBooking);

        // then
        var response = bookingController.updateBooking(bookingId, newBooking);

        verify(bookingService, times(1)).findById(bookingId);
        verify(bookingService, times(1)).saveBooking(newBooking);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(newBooking.getQuantity(), Objects.requireNonNull(response.getBody()).getQuantity());
        Assertions.assertEquals(newBooking, response.getBody());
    }

    @Test
    public void whenUpdateBookingWithNotExistingBookingThenReturnNotFound() {
        // given
        when(bookingService.findById(ID)).thenReturn(Optional.empty());

        // when
        var actual = bookingController.updateBooking(ID, new BookingDto());

        // then
        Assertions.assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
        verify(bookingService, times(1)).findById(ID);
        verify(bookingService, never()).saveBooking(any(BookingDto.class));
    }

    @Test
    public void whenDeleteExistingBookingThenReturnNoContent() {
        // given
        var booking = new BookingDto();
        booking.setId(ID);
        when(bookingService.findById(ID)).thenReturn(Optional.of(booking));

        // when
        var response = bookingController.deleteBooking(ID);

        // then
        verify(bookingService, times(1)).deleteById(ID);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    public void whenDeleteNotExistingBookingThenReturnNotFound() {
        // given
        when(bookingService.findById(ID)).thenReturn(Optional.empty());

        // when
        var response = bookingController.deleteBooking(ID);

        // then
        verify(bookingService, times(0)).deleteById(ID);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }
}
