package com.bookstore.controller;

import com.bookstore.business.ProductService;
import com.bookstore.model.Product;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product")
@Api(value = "Product management service")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ApiOperation(value = "Get a product by id", response = Product.class)
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(
            @ApiParam(value = "Product ID", required = true) @PathVariable("id") Integer id) {
        return this.productService.findById(id).map(p ->
                        new ResponseEntity<>(p, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @ApiOperation(value = "View list of available products", response = List.class)
    @GetMapping()
    public ResponseEntity<List<Product>> listAllProducts() {
        List<Product> products = productService.findAllProducts();
        if (products.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @ApiOperation(value = "Add a product")
    @PostMapping
    public ResponseEntity<Product> createProduct(
            @ApiParam(value = "Product store in database", required = true) @RequestBody Product product) {
        if (product == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        this.productService.addProduct(product);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Update a product")
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @ApiParam(value = "Product ID", required = true) @PathVariable("id") Integer id,
            @ApiParam(value = "Update product object", required = true) @RequestBody Product product) {
        Optional<Product> currentProduct = productService.findById(id);
        if (currentProduct.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Product productToUpdate = currentProduct.get();
        productToUpdate.setName(product.getName());
        productToUpdate.setDescription(product.getDescription());
        productToUpdate.setAuthor(product.getAuthor());
        productToUpdate.setPrice(product.getPrice());
        productToUpdate.setImagePath(product.getImagePath());
        productService.addProduct(productToUpdate);
        return new ResponseEntity<>(productToUpdate, HttpStatus.OK);
    }

    @ApiOperation(value = "Delete a product")
    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(
            @ApiParam(value = "Product ID", required = true) @PathVariable("id") Integer id) {
        Optional<Product> product = productService.findById(id);
        if (product.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        productService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}