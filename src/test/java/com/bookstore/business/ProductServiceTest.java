package com.bookstore.business;

import com.bookstore.model.Product;
import com.bookstore.model.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    private final static Integer ID = 1;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void findByIdTest() {
        // given
        var product = new Product();
        var productOptional = Optional.of(product);
        when(productRepository.findById(ID)).thenReturn(productOptional);

        // when
        var result = productService.findById(ID);

        // then
        verify(productRepository, times(1)).findById(ID);
        assertTrue(result.isPresent());
        assertEquals(product, result.get());
    }

    @Test
    void findAllProductsTest() {
        // given
        var products = List.of(new Product(), new Product());
        when(productRepository.findAll()).thenReturn(products);

        // when
        var result = productService.findAllProducts();

        // then
        verify(productRepository, times(1)).findAll();
        assertEquals(products, result);
    }

    @Test
    void saveProductTest() {
        // given
        var product = new Product();
        when(productRepository.save(product)).thenReturn(product);

        // when
        var result = productService.saveProduct(product);

        // then
        verify(productRepository, times(1)).save(product);
        assertEquals(product, result);
    }

    @Test
    void saveProductTestWhenProductIsNull() {
        // when and then
        assertThrows(RuntimeException.class, () -> productService.saveProduct(null));
    }

    @Test
    void deleteByIdTest() {
        // given
        doNothing().when(productRepository).deleteById(ID);

        // when
        productService.deleteById(ID);

        // then
        verify(productRepository, times(1)).deleteById(ID);
    }
}
