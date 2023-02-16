package com.bookstore.controller;

import com.bookstore.dto.ProductDto;
import com.bookstore.services.ProductService;
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
    public void whenGetProductByIdForExistingProductThenReturnOk() {
        // given
        var product = new ProductDto();
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
    public void whenGetNotExistingProductByIdThenReturnNotFound() {
        // given
        when(productService.findById(ID)).thenReturn(Optional.empty());

        // when
        var response = productController.getProductById(ID);

        // then
        verify(productService, times(1)).findById(ID);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void whenGetListAllExistingProductsThenReturnFound() {
        // given
        var products = Arrays.asList(
                new ProductDto(),
                new ProductDto()
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
    public void whenGetListAllNotExistingProductsThenReturnNotFound() {
        // given
        List<ProductDto> productDtos = Collections.emptyList();
        when(productService.findAllProducts()).thenReturn(productDtos);

        // when
        var response = productController.listAllProducts();

        // then
        verify(productService, times(1)).findAllProducts();
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void whenCreateProductWithValidInputThenReturnCreated() {
        // given
        var product = new ProductDto();
        // productId is set because valid return from saveProduct() should have productId
        when(productService.saveProduct(product))
                .thenAnswer(invocation -> {
                    ProductDto p = invocation.getArgument(0);
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
    public void whenCreateProductWithNullInputThenReturnBadRequest() {
        // when
        var response = productController.createProduct(null);

        // then
        verify(productService, never()).saveProduct(null);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void whenUpdateProductWithValidInputThenReturnUpdatedProduct() {
        // given
        var productDto = new ProductDto();
        productDto.setId(ID);
        productDto.setName(PRODUCT_NAME);

        var newProductDto = new ProductDto();
        newProductDto.setName("Updated Product name");

        // when
        when(productService.findById(ID)).thenReturn(Optional.of(productDto));
        when(productService.saveProduct(newProductDto)).thenReturn(newProductDto);

        // then
        var response = productController.updateProduct(ID, newProductDto);
        verify(productService, times(1)).findById(ID);
        verify(productService, times(1)).saveProduct(newProductDto);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(newProductDto.getName(), Objects.requireNonNull(response.getBody()).getName());
    }

    @Test
    public void whenUpdateProductWithNotExistingProductIdThenReturnNotFound() {
        // given
        when(productService.findById(ID)).thenReturn(Optional.empty());

        // when
        var response = productController.updateProduct(ID, new ProductDto());

        // then
        verify(productService, times(1)).findById(ID);
        verify(productService, never()).saveProduct(any(ProductDto.class));
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void whenDeleteExisingProductThenReturnNoContent() {
        // given
        var product = new ProductDto();
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
    public void whenDeleteNotExistingProductThenReturnNotFound() {
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
