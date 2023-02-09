package com.bookstore.controller;

import com.bookstore.business.BookingService;
import com.bookstore.model.Booking;
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
    public void getBookingByIdWhenBookingExistsShouldReturnOk() {
        // given
        var booking = new Booking();
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
    public void getBookingByIdWhenBookingDoesNotExistShouldReturnNotFound() {
        // given
        when(bookingService.findById(ID)).thenReturn(Optional.empty());

        // when
        var actual = bookingController.getBookingById(ID);

        // then
        verify(bookingService, times(1)).findById(ID);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
    }

    @Test
    public void createBookingWithBookingIsNullShouldReturnBadRequest() {
        // when
        var response = bookingController.createBooking(null);

        // then
        verify(bookingService, never()).saveBooking(null);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void createBookingWithValidBookingShouldReturnCreated() {
        // given
        var booking = new Booking();
        // bookingId is set as saveBooking() set up bookingId
        when(bookingService.saveBooking(booking))
                .thenAnswer(invocation -> {
                    Booking b = invocation.getArgument(0);
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
    public void updateBookingWithExistingBookingShouldReturnUpdatedProduct() {
        // given
        var booking = new Booking();
        booking.setId(77);
        var bookingId = booking.getId();

        var newBooking = new Booking();
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
    public void updateBookingWithNotExistingBookingShouldReturnNotFound() {
        // given
        when(bookingService.findById(ID)).thenReturn(Optional.empty());
        var booking = new Booking();

        // when
        var actual = bookingController.updateBooking(ID, booking);

        // then
        Assertions.assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
        verify(bookingService, times(1)).findById(ID);
        verify(bookingService, never()).saveBooking(any(Booking.class));
    }

    @Test
    public void deleteBookingWhenBookingExistsShouldDeleteBooking() {
        // given
        var booking = new Booking();
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
    public void deleteBookingWhenBookingDoesNotExistShouldReturnNotFound() {
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