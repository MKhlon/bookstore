package com.bookstore.controller;

import com.bookstore.business.BookingService;
import com.bookstore.model.Booking;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/booking")
@Api(value = "Booking management service")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @ApiOperation(value = "Get booking by id", response = Booking.class)
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(
            @ApiParam(value = "Booking ID", required = true) @PathVariable("id") Integer id) {
        return this.bookingService.findById(id).map(b ->
                        new ResponseEntity<>(b, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @ApiOperation(value = "Add a booking")
    @PostMapping
    public ResponseEntity<Booking> createBooking(
            @ApiParam(value = "Booking store in database", required = true) @RequestBody Booking booking) {
        if (booking == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        this.bookingService.addBooking(booking);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update a booking")
    @PutMapping("/{id}")
    public ResponseEntity<Booking> updateBooking(
            @ApiParam(value = "Booking ID", required = true) @PathVariable("id") Integer id,
            @ApiParam(value = "Update booking object", required = true) @RequestBody Booking booking) {
        Optional<Booking> currentBooking = bookingService.findById(id);

        if (currentBooking.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Booking bookingToUpdate = currentBooking.get();

        bookingToUpdate.setUser(booking.getUser());
        bookingToUpdate.setDeliveryAddress(booking.getDeliveryAddress());
        bookingToUpdate.setDate(booking.getDate());
        bookingToUpdate.setTime(booking.getTime());
        bookingToUpdate.setStatus(booking.getStatus());
        bookingToUpdate.setQuantity(booking.getQuantity());

        bookingService.addBooking(bookingToUpdate);
        return new ResponseEntity<>(bookingToUpdate, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete a booking")
    @DeleteMapping("/{id}")
    public ResponseEntity<Booking> deleteBooking(@ApiParam(value = "Booking ID", required = true)
                                                 @PathVariable("id") Integer id) {
        Optional<Booking> booking = bookingService.findById(id);
        if (booking.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        bookingService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}