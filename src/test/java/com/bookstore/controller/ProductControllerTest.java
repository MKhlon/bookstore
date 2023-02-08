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
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
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
    private final static Integer ID = 1;
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
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(productService, times(1)).findById(ID);
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
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void createProductWithValidInputShouldReturnCreatedStatus() {
        // given
        var product = new Product();
        var description = "This is a test product";
        product.setName(PRODUCT_NAME);
        product.setDescription(description);
        doNothing().when(productService).addProduct(product);

        // when
        var response = productController.createProduct(product);

        // then
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(product, response.getBody());
        verify(productService, times(1)).addProduct(product);
    }

    @Test
    public void createProductWithNullInputShouldReturnBadRequest() {
        // when
        var response = productController.createProduct(null);

        // then
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(productService, never()).addProduct(null);
    }

    @Test
    public void updateProductWithValidInputShouldReturnUpdatedProduct() {
        // given
        var initialProduct = new Product();
        var updatedProductName = "Updated Product name";
        initialProduct.setId(ID);
        initialProduct.setName(PRODUCT_NAME);
        var productToUpdate = new Product();
        productToUpdate.setId(2);
        productToUpdate.setName(updatedProductName);

        // when
        when(productService.findById(ID)).thenReturn(Optional.of(initialProduct));
        doNothing().when(productService).addProduct(initialProduct);

        // then
        var response = productController.updateProduct(ID, productToUpdate);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(initialProduct.getName(), Objects.requireNonNull(response.getBody()).getName());
        verify(productService, times(1)).findById(ID);
        verify(productService, times(1)).addProduct(initialProduct);
    }

    @Test
    public void updateProductWithNotExistingProductIdShouldReturnNotFound() {
        // given
        when(productService.findById(ID)).thenReturn(Optional.empty());

        // when
        ResponseEntity<Product> response = productController.updateProduct(ID, new Product());

        // then
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(productService, times(1)).findById(ID);
        verify(productService, never()).addProduct(any(Product.class));
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
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        Assertions.assertNull(response.getBody());
        verify(productService, times(1)).deleteById(ID);
    }

    @Test
    public void deleteProductWhenProductDoesNotExistShouldReturnNotFound() {
        // given
        when(productService.findById(ID)).thenReturn(Optional.empty());

        // when
        var response = productController.deleteProduct(ID);

        // then
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Assertions.assertNull(response.getBody());
        verify(productService, times(0)).deleteById(ID);
    }
}