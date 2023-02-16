package com.bookstore.services;

import com.bookstore.converter.BookingConverter;
import com.bookstore.dto.BookingDto;
import com.bookstore.model.Booking;
import com.bookstore.repositories.BookingRepository;
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

    @Mock
    private BookingConverter bookingConverter;

    @InjectMocks
    private BookingService bookingService;

    @Test
    public void findByIdTest() {
        // given
        var booking = new Booking();
        var expected = new BookingDto();
        when(bookingRepository.findById(ID)).thenReturn(Optional.of(booking));
        when(bookingConverter.entityToDto(booking)).thenReturn(expected);

        // when
        var result = bookingService.findById(ID);

        // then
        verify(bookingRepository, times(1)).findById(ID);
        verify(bookingConverter).entityToDto(booking);
        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
    }

    @Test
    public void createBookingTest() {
        // given
        var booking = new Booking();
        var dto = new BookingDto();
        when(bookingConverter.dtoToEntity(dto)).thenReturn(booking);
        var savedBooking = new Booking();
        when(bookingRepository.save(booking)).thenReturn(savedBooking);
        var savedDto = new BookingDto();
        when(bookingConverter.entityToDto(savedBooking)).thenReturn(savedDto);

        // when
        var result = bookingService.saveBooking(dto);

        // then
        verify(bookingConverter).dtoToEntity(dto);
        verify(bookingRepository, times(1)).save(booking);
        verify(bookingConverter).entityToDto(savedBooking);
        assertEquals(savedDto, result);
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
