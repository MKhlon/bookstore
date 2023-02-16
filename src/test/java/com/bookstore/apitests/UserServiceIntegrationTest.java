package com.bookstore.apitests;

import com.bookstore.dto.UserDto;
import com.bookstore.model.enums.RoleType;
import com.bookstore.utils.DataFactory;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.bookstore.utils.Messages.ADDRESS_IS_NOT_AS_EXPECTED;
import static com.bookstore.utils.Messages.EMAIL_IS_NOT_AS_EXPECTED;
import static com.bookstore.utils.Messages.LOGIN_IS_NOT_AS_EXPECTED;
import static com.bookstore.utils.Messages.PASSWORD_IS_NOT_AS_EXPECTED;
import static com.bookstore.utils.Messages.PHONE_IS_NOT_AS_EXPECTED;
import static com.bookstore.utils.Messages.ROLE_ID_IS_NOT_AS_EXPECTED;
import static com.bookstore.utils.Messages.ROLE_NAME_IS_NOT_AS_EXPECTED;
import static com.bookstore.utils.Messages.USER_ID_IS_NOT_AS_EXPECTED;
import static com.bookstore.utils.Messages.USER_NAME_IS_NOT_AS_EXPECTED;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@ExtendWith(SpringExtension.class)
public class UserServiceIntegrationTest extends BaseTest {

    private static final String API_URL = "/api/user";

    @Test
    public void testGetUserById() {
        // given
        final var name = "Maria Khlon";
        final var address = "Krakow, Dobrego Pasterza";
        final var email = "maria_khlon@test.com";
        final var phone = "+48111222333";
        final var login = "testLogin";
        final var password = "testPassword";
        final var roleName = RoleType.ADMIN.name();
        final Integer roleId = 1;
        final Integer id = 1;

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
        assumeTrue(id.equals(idValue), USER_ID_IS_NOT_AS_EXPECTED);
        verifyResponse(name, roleId, roleName, email, phone, address, login, password, response);
    }

    @Test
    public void testCreateUser() {
        // given
        var user = DataFactory.getUser();

        // when
        var response = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(API_URL)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .response();

        // then
        assumeTrue(response.path("id") != null);
        verifyResponse(user.getUserName(), user.getRoleId(), user.getRoleName(), user.getEmail(),
                user.getPhone(), user.getAddress(), user.getLogin(), user.getPassword(), response);
    }

    @Test
    public void testUpdateUser() {
        // given
        // create new user
        var userDto = DataFactory.getUser();
        var userId = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(userDto)
                .when()
                .post(API_URL)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .jsonPath().getInt("id");
        // fill with updated values
        updateUser(userDto);
        userDto.setId(userId);

        // when
        var response = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(userDto)
                .when()
                .put(API_URL + "/" + userDto.getId())
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .response();

        // then
        verifyResponse(userDto.getUserName(), userDto.getRoleId(), userDto.getRoleName(), userDto.getEmail(),
                userDto.getPhone(), userDto.getAddress(), userDto.getLogin(), userDto.getPassword(), response);
    }

    @Test
    public void testDeleteUser() {
        // given
        var userDto = DataFactory.getUser();
        var userId = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(userDto)
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
                .delete("/api/user/{id}", userId);

        // then
        response
                .then()
                .log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    private void verifyResponse(String name, Integer roleId, String roleName, String email, String phone, String address,
                                String login, String password, Response response) {
        assertAll(
                () -> Assertions.assertEquals(name, response.path("userName"), USER_NAME_IS_NOT_AS_EXPECTED),
                () -> Assertions.assertEquals(roleId, response.path("roleId"), ROLE_ID_IS_NOT_AS_EXPECTED),
                () -> Assertions.assertEquals(roleName, response.path("roleName"), ROLE_NAME_IS_NOT_AS_EXPECTED),
                () -> Assertions.assertEquals(email, response.path("email"), EMAIL_IS_NOT_AS_EXPECTED),
                () -> Assertions.assertEquals(phone, response.path("phone"), PHONE_IS_NOT_AS_EXPECTED),
                () -> Assertions.assertEquals(address, response.path("address"), ADDRESS_IS_NOT_AS_EXPECTED),
                () -> Assertions.assertEquals(login, response.path("login"), LOGIN_IS_NOT_AS_EXPECTED),
                () -> Assertions.assertEquals(password, response.path("password"), PASSWORD_IS_NOT_AS_EXPECTED)
        );
    }

    private UserDto updateUser(UserDto userDto) {
        userDto.setUserName("Test user name");
        userDto.setAddress("Poland, Krakow");
        userDto.setEmail("test@gmail.com");
        userDto.setPhone("+44111225535");
        userDto.setLogin("test login value");
        userDto.setPassword("strong test password");
        userDto.setRoleId(1);
        userDto.setRoleName(RoleType.ADMIN.name());
        return userDto;
    }
}
