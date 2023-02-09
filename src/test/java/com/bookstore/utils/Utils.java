package com.bookstore.utils;

import com.bookstore.model.Product;
import lombok.Data;


@Data
public class Utils {

    public static Product createDefaultProduct() {
        var product = new Product();
        product.setName("Test Product");
        product.setDescription("This is a test description product");
        product.setAuthor("Test author");
        product.setPrice(19.99F);
        product.setImagePath("http://www.test.image.jpg");
        return product;
    }
}