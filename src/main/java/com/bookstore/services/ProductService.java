package com.bookstore.services;

import com.bookstore.converter.ProductConverter;
import com.bookstore.dto.ProductDto;
import com.bookstore.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductConverter productConverter;

    public ProductService(ProductRepository productRepository, ProductConverter productConverter) {
        this.productRepository = productRepository;
        this.productConverter = productConverter;
    }

    public Optional<ProductDto> findById(Integer id) {
        return productRepository.findById(id)
                .map(productConverter::entityToDto);
    }

    public List<ProductDto> findAllProducts() {
        return productRepository.findAll().stream()
                .map(productConverter::entityToDto)
                .collect(Collectors.toList());
    }

    public ProductDto saveProduct(ProductDto productDto) {
        if (productDto == null) {
            throw new RuntimeException("Product can not be null");
        }
        var createdProduct = productRepository.save(productConverter.dtoToEntity(productDto));
        return productConverter.entityToDto(createdProduct);
    }

    public void deleteById(Integer id) {
        this.productRepository.deleteById(id);
    }
}