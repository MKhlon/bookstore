package com.bookstore.apitests;

import com.bookstore.model.Role;
import com.bookstore.model.User;
import com.bookstore.model.enums.RoleType;
import com.bookstore.utils.Messages;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceTest extends BaseTest {

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
        assumeTrue(id.equals(idValue), Messages.USER_ID_IS_NOT_AS_EXPECTED);
        verifyResponse(name, roleId, roleName, email, phone, address, login, password, response);
    }

    @Test
    public void testCreateUser() {
        // given
        var user = updateUser(new User());

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
        verifyResponse(user.getName(), user.getRole().getId(), user.getRole().getName().name(), user.getEmail(),
                user.getPhone(), user.getAddress(), user.getLogin(), user.getPassword(), response);
    }

    @Test
    public void testUpdateUser() {
        // given
        // create new user
        var user = new User();
        var userId = given()
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
                .jsonPath().getInt("id");
        // fill with updated values
        updateUser(user);
        user.setId(userId);

        // when
        var response = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .put(API_URL + "/" + user.getId())
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .response();

        // then
        verifyResponse(user.getName(), user.getRole().getId(), user.getRole().getName().name(), user.getEmail(),
                user.getPhone(), user.getAddress(), user.getLogin(), user.getPassword(), response);
    }

    @Test
    public void testDeleteUser() {
        // given
        var user = new User();
        var userId = given()
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
                () -> Assertions.assertEquals(name, response.path("name"),
                        Messages.USER_NAME_IS_NOT_AS_EXPECTED),
                () -> Assertions.assertEquals(roleId, response.path("role.id"),
                        Messages.ROLE_ID_IS_NOT_AS_EXPECTED),
                () -> Assertions.assertEquals(roleName, response.path("role.name"),
                        Messages.ROLE_NAME_IS_NOT_AS_EXPECTED),
                () -> Assertions.assertEquals(email, response.path("email"),
                        Messages.EMAIL_IS_NOT_AS_EXPECTED),
                () -> Assertions.assertEquals(phone, response.path("phone"),
                        Messages.PHONE_IS_NOT_AS_EXPECTED),
                () -> Assertions.assertEquals(address, response.path("address"),
                        Messages.ADDRESS_IS_NOT_AS_EXPECTED),
                () -> Assertions.assertEquals(login, response.path("login"),
                        Messages.LOGIN_IS_NOT_AS_EXPECTED),
                () -> Assertions.assertEquals(password, response.path("password"),
                        Messages.PASSWORD_IS_NOT_AS_EXPECTED)
        );
    }

    private User updateUser(User user) {
        user.setName("Test user name");
        user.setAddress("Poland, Krakow");
        user.setEmail("test@gmail.com");
        user.setPhone("+44111225535");
        user.setLogin("test login value");
        user.setPassword("strong test password");
        var role = new Role();
        role.setId(1);
        role.setName(RoleType.ADMIN);
        user.setRole(role);
        return user;
    }
}
