package com.bookstore.controller;

import com.bookstore.business.ProductService;
import com.bookstore.model.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private final static Integer ID = 1234;
    private final static String PRODUCT_NAME = "Product A";

    @Test
    public void getProductByIdWhenProductExistsShouldReturnOk() {
        // given
        var product = new Product();
        product.setId(ID);
        product.setName(PRODUCT_NAME);
        when(productService.findById(ID)).thenReturn(Optional.of(product));

        // when
        var response = productController.getProductById(ID);

        // then
        verify(productService, times(1)).findById(ID);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(product, response.getBody());
    }

    @Test
    public void getProductByIdWhenProductDoesNotExistShouldReturnNotFound() {
        // given
        when(productService.findById(ID)).thenReturn(Optional.empty());

        // when
        var response = productController.getProductById(ID);

        // then
        verify(productService, times(1)).findById(ID);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testListAllProductsFound() {
        // given
        var products = Arrays.asList(
                new Product(),
                new Product()
        );
        when(productService.findAllProducts()).thenReturn(products);

        // when
        var response = productController.listAllProducts();

        // then
        verify(productService, times(1)).findAllProducts();
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(products, response.getBody());
    }

    @Test
    public void testListAllProductsNotFound() {
        // given
        List<Product> products = Collections.emptyList();
        when(productService.findAllProducts()).thenReturn(products);

        // when
        var response = productController.listAllProducts();

        // then
        verify(productService, times(1)).findAllProducts();
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void createProductWithValidInputShouldReturnCreatedStatus() {
        // given
        var product = new Product();
        // productId is set because valid return from saveProduct() should have productId
        when(productService.saveProduct(product))
                .thenAnswer(invocation -> {
                    Product p = invocation.getArgument(0);
                    p.setId(333);
                    return p;
                });

        // when
        var response = productController.createProduct(product);

        // then
        verify(productService, times(1)).saveProduct(product);
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertNotNull(Objects.requireNonNull(response.getBody()).getId());
        Assertions.assertEquals(product, response.getBody());
    }

    @Test
    public void createProductWithNullInputShouldReturnBadRequest() {
        // when
        var response = productController.createProduct(null);

        // then
        verify(productService, never()).saveProduct(null);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void updateProductWithValidInputShouldReturnUpdatedProduct() {
        // given
        var product = new Product();
        product.setId(ID);
        product.setName(PRODUCT_NAME);

        var newProduct = new Product();
        newProduct.setName("Updated Product name");

        // when
        when(productService.findById(ID)).thenReturn(Optional.of(product));
        when(productService.saveProduct(newProduct)).thenReturn(newProduct);

        // then
        var response = productController.updateProduct(ID, newProduct);
        verify(productService, times(1)).findById(ID);
        verify(productService, times(1)).saveProduct(newProduct);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(newProduct.getName(), Objects.requireNonNull(response.getBody()).getName());
    }

    @Test
    public void updateProductWithNotExistingProductIdShouldReturnNotFound() {
        // given
        when(productService.findById(ID)).thenReturn(Optional.empty());

        // when
        var response = productController.updateProduct(ID, new Product());

        // then
        verify(productService, times(1)).findById(ID);
        verify(productService, never()).saveProduct(any(Product.class));
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void deleteProductWhenProductExistsShouldDeleteProduct() {
        // given
        var product = new Product();
        product.setId(ID);
        when(productService.findById(ID)).thenReturn(Optional.of(product));

        // when
        var response = productController.deleteProduct(ID);

        // then
        verify(productService, times(1)).deleteById(ID);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }

    @Test
    public void deleteProductWhenProductDoesNotExistShouldReturnNotFound() {
        // given
        when(productService.findById(ID)).thenReturn(Optional.empty());

        // when
        var response = productController.deleteProduct(ID);

        // then
        verify(productService, times(0)).deleteById(ID);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertNull(response.getBody());
    }
}