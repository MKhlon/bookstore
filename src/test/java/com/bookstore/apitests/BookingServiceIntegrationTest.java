package com.bookstore.apitests;

import com.bookstore.dto.BookingDto;
import com.bookstore.model.BookingStatus;
import com.bookstore.model.enums.BookingStatusName;
import com.bookstore.utils.DataFactory;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.bookstore.utils.Messages.BOOKING_ID_CAN_NOT_BE_NULL;
import static com.bookstore.utils.Messages.BOOKING_ID_IS_NOT_AS_EXPECTED;
import static com.bookstore.utils.Messages.DATE_IS_NOT_AS_EXPECTED;
import static com.bookstore.utils.Messages.DELIVERY_ADDRESS_IS_NOT_AS_EXPECTED;
import static com.bookstore.utils.Messages.PRODUCT_ID_IS_NOT_AS_EXPECTED;
import static com.bookstore.utils.Messages.PRODUCT_NAME_IS_NOT_AS_EXPECTED;
import static com.bookstore.utils.Messages.QUANTITY_IS_NOT_AS_EXPECTED;
import static com.bookstore.utils.Messages.STATUS_ID_IS_NOT_AS_EXPECTED;
import static com.bookstore.utils.Messages.STATUS_NAME_IS_NOT_AS_EXPECTED;
import static com.bookstore.utils.Messages.TIME_IS_NOT_AS_EXPECTED;
import static com.bookstore.utils.Messages.USER_ID_IS_NOT_AS_EXPECTED;
import static com.bookstore.utils.Messages.USER_NAME_IS_NOT_AS_EXPECTED;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@ExtendWith(SpringExtension.class)
public class BookingServiceIntegrationTest extends BaseTest {

    private static final String API_URL = "/api/booking";

    @Test
    public void testGetBookingById() {
        // given
        final var deliveryAddress = "Poland Krakow";
        final var date = "2022-05-15";
        final var time = "02:00:00.000000000";
        final var userName = "Maria Khlon";
        final Integer id = 1;
        final String productName = "Harry Potter";

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
        assumeTrue(id.equals(idValue), BOOKING_ID_IS_NOT_AS_EXPECTED);
        verifyResponse(productName, userName, deliveryAddress, date, time, BookingStatusName.SUBMITTED.name(), 1, response);
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
        assumeTrue(idValue != null);
        verifyResponse(booking.getProductName(), booking.getUserName(), booking.getDeliveryAddress(),
                booking.getDate().toString(), booking.getTime().toString(), booking.getStatusName(),
                booking.getQuantity(), response);
    }

    @Test
    public void testUpdateBooking() {
        // given
        // create initial booking
        var booking = createBooking();
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
        var updatedBooking = new BookingDto();
        updatedBooking.setId(idValue);
        updatedBooking.setUserName("Test User");
        updatedBooking.setUserId(2);
        updatedBooking.setProductId(2);
        updatedBooking.setProductName("Baby Shark");
        updatedBooking.setStatusId(3);
        updatedBooking.setStatusName(BookingStatusName.APPROVED.name());
        updatedBooking.setDeliveryAddress("Poland, Warsaw");
        updatedBooking.setDate(LocalDate.now());
        updatedBooking.setTime(LocalTime.now());
        updatedBooking.setQuantity(10);

        // when
        var updatedResponse = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(updatedBooking)
                .when()
                .put(API_URL + "/" + idValue)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .response();

        // then
        assumeTrue(updatedResponse.path("id") != null, BOOKING_ID_CAN_NOT_BE_NULL);
        assumeTrue(updatedResponse.path("id").equals(idValue), BOOKING_ID_IS_NOT_AS_EXPECTED);
        verifyResponse(updatedBooking.getProductName(), updatedBooking.getUserName(), updatedBooking.getDeliveryAddress(),
                updatedBooking.getDate().toString(), updatedBooking.getTime().toString(), updatedBooking.getStatusName(),
                updatedBooking.getQuantity(), updatedResponse);
    }

    @Test
    public void testDeleteBooking() {
        // given
        var booking = createBooking();
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

    private void verifyResponse(String productName, String userName, String deliveryAddress, String date, String time,
                                String statusName, Integer quantity, Response response) {
        assertAll(
                () -> Assertions.assertEquals(productName, response.path("productName"), PRODUCT_NAME_IS_NOT_AS_EXPECTED),
                () -> Assertions.assertNotNull(response.path("productId"), PRODUCT_ID_IS_NOT_AS_EXPECTED),
                () -> Assertions.assertEquals(userName, response.path("userName"), USER_NAME_IS_NOT_AS_EXPECTED),
                () -> Assertions.assertNotNull(response.path("userId"), USER_ID_IS_NOT_AS_EXPECTED),
                () -> Assertions.assertEquals(deliveryAddress, response.path("deliveryAddress"), DELIVERY_ADDRESS_IS_NOT_AS_EXPECTED),
                () -> Assertions.assertEquals(date, response.path("date"), DATE_IS_NOT_AS_EXPECTED),
                () -> Assertions.assertEquals(time, response.path("time"), TIME_IS_NOT_AS_EXPECTED),
                () -> Assertions.assertEquals(statusName, response.path("statusName"), STATUS_NAME_IS_NOT_AS_EXPECTED),
                () -> Assertions.assertNotNull(response.path("statusId"), STATUS_ID_IS_NOT_AS_EXPECTED),
                () -> Assertions.assertEquals(quantity, response.path("quantity"), QUANTITY_IS_NOT_AS_EXPECTED)
        );
    }

    private BookingDto createBooking() {

        var booking = new BookingDto();

        //create Product
        var productDto = DataFactory.getProduct();
        var productId = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(productDto)
                .when()
                .post("/api/product")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .jsonPath().getInt("id");
        productDto.setId(productId);

        //create User
        var user = DataFactory.getUser();
        var userId = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(user)
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
        status.setName(BookingStatusName.SUBMITTED);
        status.setId(1);

        // fulfill Booking
        booking.setUserName(user.getUserName());
        booking.setUserId(userId);
        booking.setProductName(productDto.getName());
        booking.setProductId(productDto.getId());
        booking.setStatusName(status.getName().name());
        booking.setStatusId(status.getId());
        booking.setDeliveryAddress("UK, Cambridge");
        booking.setDate(LocalDate.now());
        booking.setTime(LocalTime.now());
        booking.setQuantity(15);
        return booking;
    }
}
