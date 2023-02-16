package com.bookstore.services;

import com.bookstore.converter.ProductConverter;
import com.bookstore.dto.ProductDto;
import com.bookstore.model.Product;
import com.bookstore.repositories.ProductRepository;
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

    @Mock
    private ProductConverter productConverter;

    @InjectMocks
    private ProductService productService;

    @Test
    void findByIdTest() {
        // given
        var product = new Product();
        var productDto = new ProductDto();
        when(productRepository.findById(ID)).thenReturn(Optional.of(product));
        when(productConverter.entityToDto(product)).thenReturn(productDto);

        // when
        var result = productService.findById(ID);

        // then
        verify(productRepository, times(1)).findById(ID);
        verify(productConverter,times(1)).entityToDto(product);
        assertTrue(result.isPresent());
        assertEquals(productDto, result.get());
    }

    @Test
    void findAllProductsTest() {
        // given
        var product1 = new Product();
        product1.setName("test name");
        var product2 = new Product();
        product2.setName("test name 2");
        var products = List.of(product1, product2);
        when(productRepository.findAll()).thenReturn(products);
        var productDto1 = new ProductDto();
        var productDto2 = new ProductDto();
        var productDtos = List.of(new ProductDto(), new ProductDto());
        when(productConverter.entityToDto(product1)).thenReturn(productDto1);
        when(productConverter.entityToDto(product2)).thenReturn(productDto2);

        // when
        var result = productService.findAllProducts();

        // then
        verify(productRepository, times(1)).findAll();
        verify(productConverter).entityToDto(product1);
        verify(productConverter).entityToDto(product2);
        assertEquals(productDtos, result);
    }

    @Test
    void createProductTest() {
        // given
        var product = new Product();
        var productDto = new ProductDto();
        when(productConverter.dtoToEntity(productDto)).thenReturn(product);
        var createdProduct = new Product();
        when(productRepository.save(product)).thenReturn(createdProduct);
        var createdProductDto = new ProductDto();
        when(productConverter.entityToDto(createdProduct)).thenReturn(createdProductDto);

        // when
        var result = productService.saveProduct(productDto);

        // then
        verify(productConverter).dtoToEntity(productDto);
        verify(productRepository, times(1)).save(product);
        verify(productConverter).entityToDto(createdProduct);
        assertEquals(createdProductDto, result);
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
