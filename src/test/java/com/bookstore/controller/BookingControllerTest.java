package com.bookstore.controller;

import com.bookstore.business.BookingService;
import com.bookstore.model.Booking;
import com.bookstore.utils.Utils;
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
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookingControllerTest {
    private final static Integer ID = 1;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private BookingController bookingController;

    @Test
    public void getBookingByIdWhenBookingExistsShouldReturnOk() {
        // given
        var expected = new Booking();

        // when
        when(bookingService.findById(ID)).thenReturn(Optional.of(expected));
        var actual = bookingController.getBookingById(ID);

        // then
        Assertions.assertEquals(HttpStatus.OK, actual.getStatusCode());
        Assertions.assertEquals(expected, actual.getBody());
    }

    @Test
    public void getBookingByIdWhenBookingDoesNotExistShouldReturnNotFound() {
        // given
        when(bookingService.findById(ID)).thenReturn(Optional.empty());

        // when
        var actual = bookingController.getBookingById(ID);

        // then
        Assertions.assertEquals(HttpStatus.NOT_FOUND, actual.getStatusCode());
        verify(bookingService, times(1)).findById(ID);
    }

    @Test
    public void createBookingWithBookingIsNullShouldReturnBadRequest() {
        // when
        var actual = bookingController.createBooking(null);

        // then
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, actual.getStatusCode());
        verify(bookingService, never()).addBooking(null);
    }

    @Test
    public void createBookingWithValidBookingShouldReturnCreated() {
        // given
        var booking = new Booking();

        // when
        var actual = bookingController.createBooking(booking);

        // then
        Assertions.assertEquals(HttpStatus.CREATED, actual.getStatusCode());
        verify(bookingService, times(1)).addBooking(booking);
    }

    @Test
    public void updateBookingWithExistingBookingShouldReturnUpdatedProduct() {
        // given
        var booking = Utils.createDefaultBooking();
        Optional<Booking> currentBooking = Optional.of(booking);
        var bookingId = currentBooking.get().getId();
        when(bookingService.findById(bookingId)).thenReturn(currentBooking);
        doNothing().when(bookingService).addBooking(currentBooking.get());
        var bookingToUpdate = new Booking();
        bookingToUpdate.setQuantity(9);

        // when
        var response = bookingController.updateBooking(bookingId, bookingToUpdate);

        //then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(bookingToUpdate.getQuantity(), Objects.requireNonNull(response.getBody()).getQuantity());
        Assertions.assertEquals(booking, response.getBody());
        verify(bookingService, times(1)).findById(bookingId);
        verify(bookingService, times(1)).addBooking(booking);
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
        verify(bookingService, never()).addBooking(any(Booking.class));
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