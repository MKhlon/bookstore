package com.bookstore.integration;

import com.bookstore.apitests.BaseTest;
import com.bookstore.dto.ProductDto;
import com.bookstore.repositories.ProductRepository;
import com.bookstore.services.ProductService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.bookstore.utils.Messages.PRICE_IS_NOT_AS_EXPECTED;
import static com.bookstore.utils.Messages.PRODUCT_NAME_IS_NOT_AS_EXPECTED;

@ExtendWith(SpringExtension.class)
public class ProductServiceIntegrationDbTests extends BaseTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void whenProductWithOnlyMandatoryFieldsProvidedThenOneProductIsCreated() {
        // given
        var productId = 999;
        var product = ProductDto.builder()
                .id(productId)
                .name("Integration DB test product name")
                .price(100F)
                .build();

        // when
        var createdProduct = productService.saveProduct(product);

        // then
        var createdProductId = createdProduct.getId();
        var actualProduct = productRepository.findById(createdProductId);

        Assertions.assertEquals(product.getPrice(), actualProduct.get().getPrice(), PRICE_IS_NOT_AS_EXPECTED);
        Assertions.assertEquals(product.getName(), actualProduct.get().getName(), PRODUCT_NAME_IS_NOT_AS_EXPECTED);
    }
}
