package com.bookstore.business;

import com.bookstore.model.Booking;
import com.bookstore.model.repositories.BookingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {
    private final static Integer ID = 1;

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingService bookingService;

    @Test
    public void findByIdTest() {
        // given
        var booking = new Booking();
        var bookingOptional = Optional.of(booking);
        when(bookingRepository.findById(ID)).thenReturn(bookingOptional);

        // when
        var result = bookingService.findById(ID);

        // then
        verify(bookingRepository, times(1)).findById(ID);
        assertTrue(result.isPresent());
        assertEquals(booking, result.get());
    }

    @Test
    public void saveBookingTest() {
        // given
        var booking = new Booking();
        when(bookingRepository.save(booking)).thenReturn(booking);

        // when
        var result = bookingService.saveBooking(booking);

        // then
        verify(bookingRepository, times(1)).save(booking);
        assertEquals(booking, result);
    }

    @Test
    public void saveBookingTestWhenBookingIsNull() {
        // when and then
        assertThrows(RuntimeException.class, () -> bookingService.saveBooking(null));
    }

    @Test
    public void deleteByIdTest() {
        // given
        doNothing().when(bookingRepository).deleteById(ID);

        // when
        bookingService.deleteById(ID);

        // then
        verify(bookingRepository, times(1)).deleteById(ID);
    }
}
