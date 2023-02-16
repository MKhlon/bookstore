package com.bookstore.converter;

import com.bookstore.dto.ProductDto;
import com.bookstore.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductConverter extends Converter {

    public ProductDto entityToDto(Product product) {
        ProductDto productDto = new ProductDto();
        super.convert(product, productDto);
        return productDto;
    }

    public Product dtoToEntity(ProductDto productDto) {
        Product product = new Product();
        super.convert(productDto, product);
        return product;
    }
}
