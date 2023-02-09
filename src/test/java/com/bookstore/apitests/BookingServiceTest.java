package com.bookstore.apitests;

import com.bookstore.model.Booking;
import com.bookstore.model.BookingStatus;
import com.bookstore.model.User;
import com.bookstore.model.enums.BookingStatusType;
import com.bookstore.utils.Messages;
import com.bookstore.utils.Utils;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalTime;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookingServiceTest extends BaseTest {

    private static final String API_URL = "/api/booking";

    @Test
    public void testGetBookingById() {
        // given
        final var deliveryAddress = "Poland Krakow";
        final var date = "2022-05-15";
        final var time = "02:00:00";
        final Integer id = 1;
        final Integer productId = 1;

        // when
        var response = given().
                log().all()
                .when()
                .get(API_URL + "/" + id)
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .response();
        Integer idValue = response.path("id");

        // then
        assumeTrue(id.equals(idValue), Messages.PRODUCT_ID_IS_NOT_AS_EXPECTED);
        verifyResponse(productId, 1, deliveryAddress, date, time, 1, 1, response);
    }

    @Test
    public void testCreateBooking() {
        // given
        var booking = createBooking();

        // when
        var response = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(booking)
                .when()
                .post(API_URL)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .response();
        Integer idValue = response.path("id");

        // then
        assumeTrue(response.path("id") != null);
        verifyResponse(idValue, booking.getUser().getId(), booking.getDeliveryAddress(), booking.getDate().toString(),
                booking.getTime().toString(), booking.getStatus().getId(), booking.getQuantity(), response);
    }

    @Test
    public void testUpdateBooking() {
        // given
        // create initial booking
        var booking = new Booking();
        var response = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(booking)
                .when()
                .post(API_URL)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .response();
        Integer idValue = response.path("id");

        // create updated booking object
        var updatedBooking = createBooking();
        updatedBooking.setId(idValue);

        // when
        var updatedResponse = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(booking)
                .when()
                .put(API_URL + "/" + idValue)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .response();

        // then
        assumeTrue(updatedResponse.path("id") != null);
        verifyResponse(idValue, updatedBooking.getUser().getId(), updatedBooking.getDeliveryAddress(),
                updatedBooking.getDate().toString(), updatedBooking.getTime().toString(),
                updatedBooking.getStatus().getId(), updatedBooking.getQuantity(), response);
    }

    @Test
    public void testDeleteBooking() {
        // given
        var booking = new Booking();
        var bookingId = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(booking)
                .when()
                .post(API_URL)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .jsonPath().getInt("id");

        // when
        var response = given()
                .when()
                .log().all()
                .delete("/api/booking/{id}", bookingId);

        // then
        response
                .then()
                .log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    private void verifyResponse(Integer productId, Integer userId, String deliveryAddress, String date, String time,
                                Integer statusId, Integer quantity, Response response) {
        assertAll(
                () -> Assertions.assertEquals(productId, response.path("product.id"),
                        Messages.PRODUCT_ID_IS_NOT_AS_EXPECTED),
                () -> Assertions.assertEquals(userId, response.path("user.id"),
                        Messages.USER_ID_IS_NOT_AS_EXPECTED),
                () -> Assertions.assertEquals(deliveryAddress, response.path("deliveryAddress"),
                        Messages.DELIVERY_ADDRESS_IS_NOT_AS_EXPECTED),
                () -> Assertions.assertEquals(date, response.path("date"),
                        Messages.DATE_IS_NOT_AS_EXPECTED),
                () -> Assertions.assertEquals(time, response.path("time"),
                        Messages.TIME_IS_NOT_AS_EXPECTED),
                () -> Assertions.assertEquals(statusId, response.path("status.id"),
                        Messages.STATUS_ID_IS_NOT_AS_EXPECTED),
                () -> Assertions.assertEquals(quantity, response.path("quantity"),
                        Messages.QUANTITY_IS_NOT_AS_EXPECTED)
        );
    }

    private Booking createBooking() {

        var booking = new Booking();

        //create Product
        var product = Utils.createDefaultProduct();
        var productId = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(product)
                .when()
                .post("/api/product")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .jsonPath().getInt("id");
        product.setId(productId);

        //create User
        var user = new User();
        var userId = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(product)
                .when()
                .post("/api/user")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .jsonPath().getInt("id");
        user.setId(userId);

        // create Status object
        var status = new BookingStatus();
        status.setName(BookingStatusType.SUBMITTED);
        status.setId(1);

        // fulfill Booking object
        booking.setUser(user);
        booking.setProduct(product);
        booking.setStatus(status);
        booking.setDeliveryAddress("UK, Cambridge");
        booking.setDate(LocalDate.now());
        booking.setTime(LocalTime.now());
        booking.setQuantity(15);
        return booking;
    }
}