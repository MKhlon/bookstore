package com.bookstore.apitests;

import com.bookstore.model.Product;
import com.bookstore.utils.DataFactory;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.bookstore.utils.Messages.IMAGE_PATH_IS_NOT_AS_EXPECTED;
import static com.bookstore.utils.Messages.NUMBER_OF_ALL_PRODUCTS_IS_NOT_AS_EXPECTED;
import static com.bookstore.utils.Messages.PRICE_IS_NOT_AS_EXPECTED;
import static com.bookstore.utils.Messages.PRODUCT_AUTHOR_IS_NOT_AS_EXPECTED;
import static com.bookstore.utils.Messages.PRODUCT_DESCRIPTION_IS_NOT_AS_EXPECTED;
import static com.bookstore.utils.Messages.PRODUCT_NAME_IS_NOT_AS_EXPECTED;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assumptions.assumeTrue;


@ExtendWith(SpringExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductServiceIntegrationTest extends BaseTest {

    private static final String API_URL = "/api/product";

    @Test
    public void testGetProductById() {
        // given
        final var name = "Harry Potter";
        final var description = "Science fiction best seller";
        final var author = "J.K.Rowling";
        final var price = 12.50F;
        final var imagePath = "http://www.testimages.harry_potter.jpg";
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
        assumeTrue(id.equals(idValue), PRODUCT_NAME_IS_NOT_AS_EXPECTED);
        verifyResponse(name, description, author, price, imagePath, response);
    }

    @Test
    @Order(1)
    public void testListAllProducts() {
        // when
        Response response = given()
                .log().all()
                .when()
                .get(API_URL)
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .response();

        // then
        int size = response.path("$.size()");
        Assertions.assertEquals(2, size, NUMBER_OF_ALL_PRODUCTS_IS_NOT_AS_EXPECTED);
    }

    @Test
    public void testCreateProduct() {
        // given
        var product = DataFactory.getProduct();

        // when
        var response = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(product)
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
        verifyResponse(product.getName(), product.getDescription(), product.getAuthor(), product.getPrice(),
                product.getImagePath(), response);
    }

    @Test
    public void testUpdateProduct() {
        // given
        var product = createProduct();

        // when
        var response = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(product)
                .when()
                .put(API_URL + "/" + product.getId())
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .response();

        // then
        verifyResponse(product.getName(), product.getDescription(), product.getAuthor(), product.getPrice(),
                product.getImagePath(), response);
    }

    @Test
    public void deleteProductTest() {
        // given
        var product = createProduct();

        // when
        var response = given()
                .when()
                .log().all()
                .delete("/api/product/{id}", product.getId());

        // then
        response
                .then()
                .log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    private void verifyResponse(String name, String description, String author, Float price, String image, Response response) {
        assertAll(
                () -> Assertions.assertEquals(name, response.path("name"), PRODUCT_NAME_IS_NOT_AS_EXPECTED),
                () -> Assertions.assertEquals(description, response.path("description"), PRODUCT_DESCRIPTION_IS_NOT_AS_EXPECTED),
                () -> Assertions.assertEquals(author, response.path("author"), PRODUCT_AUTHOR_IS_NOT_AS_EXPECTED),
                () -> Assertions.assertEquals(price, response.path("price"), PRICE_IS_NOT_AS_EXPECTED),
                () -> Assertions.assertEquals(image, response.path("imagePath"), IMAGE_PATH_IS_NOT_AS_EXPECTED)
        );
    }

    private Product createProduct() {
        var product = new Product();
        var productId = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(product)
                .when()
                .post(API_URL)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .jsonPath().getInt("id");

        // populate product with values
        product.setName("Updated Test Product");
        product.setDescription("Updated Test Product");
        product.setPrice(20.0F);
        product.setAuthor("Updated Test Author");
        product.setImagePath("http://www.testimages.updateImage.jpg");
        product.setId(productId);
        return product;
    }
}
