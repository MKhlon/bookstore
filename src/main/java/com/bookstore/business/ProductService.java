package com.bookstore.business;

import com.bookstore.model.Product;
import com.bookstore.model.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Optional<Product> findById(Integer id) {
        return productRepository.findById(id);
    }

    public List<Product> findAllProducts() { return productRepository.findAll();}

    public Product saveProduct(Product product) {
        if(product == null) {
            throw new RuntimeException("Product can not be null");
        }
        return this.productRepository.save(product);
    }

    public void deleteById(Integer id) {
        this.productRepository.deleteById(id);
    }
}