package com.bookstore.utils;

import com.bookstore.model.Booking;
import com.bookstore.model.BookingStatus;
import com.bookstore.model.Product;
import com.bookstore.model.Role;
import com.bookstore.model.User;
import com.bookstore.model.enums.BookingStatusType;
import com.bookstore.model.enums.RoleType;
import lombok.Data;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class Utils {

    public static Product createDefaultProduct() {
        var product = new Product();
        product.setName("Test Product");
        product.setDescription("This is a test description product");
        product.setAuthor("Test author");
        product.setPrice(19.99F);
        product.setImagePath("http://www.test.image.jpg");
        return product;
    }

    public static Booking createDefaultBooking() {
        var booking = new Booking();
        var status = new BookingStatus();
        status.setId(5);
        status.setName(BookingStatusType.IN_DELIVERY);
        Date date;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2023-01-28 05:00:00");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        Time time;
        try {
            time = new Time(new SimpleDateFormat("HH:mm:ss").parse("05:00:00").getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        booking.setId(10);
        booking.setUser(createDefaultUser());
        booking.setProduct(createDefaultProduct());
        booking.setDeliveryAddress("USA, San Francisco");
        booking.setDate(date);
        booking.setTime(time);
        booking.setStatus(status);
        booking.setQuantity(2);
        return booking;
    }

    public static User createDefaultUser() {
        var role = new Role();
        role.setName(RoleType.ADMIN);
        role.setId(1);
        var user = new User();
        user.setId(10);
        user.setName("Test user");
        user.setRole(role);
        user.setEmail("testemail@yahoo.com");
        user.setAddress("UK, Westminster");
        user.setLogin("test login");
        user.setPassword("test password");
        user.setPhone("+48111555666");
        return user;
    }
}